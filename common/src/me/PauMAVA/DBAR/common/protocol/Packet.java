package me.PauMAVA.DBAR.common.protocol;

import java.util.Arrays;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import static me.PauMAVA.DBAR.common.util.ConversionUtils.*;

public abstract class Packet implements Serializable {

    public static final byte[] DEFAULT_HEADER = new byte[]{(byte) 0xAB, (byte) 0xBA};

    private final byte[] header;

    private final byte packetType;

    private byte[] packetParameter;

    private byte[] packetData;

    private byte[] packetCheckSum;

    Packet(PacketType type, PacketParameter parameter) {
        this(type, parameter, new byte[0]);
    }

    Packet(PacketType type, PacketParameter parameter, byte[] data) {
        this.header = DEFAULT_HEADER;
        this.packetType = type.getCode();
        this.packetParameter = parameter.getData();
        this.packetData = data;
        recalculateChecksum();
    }

    public byte[] getParameterBytes() {
        return packetParameter;
    }

    public PacketParameter getPacketParameter() {
        return PacketParameter.getByData(packetParameter);
    }

    public byte[] getPacketData() {
        return packetData;
    }

    public void setParameter(PacketParameter parameter) {
        this.packetParameter = parameter.getData();
        recalculateChecksum();
    }

    public void setPacketData(byte[] packetData) {
        this.packetData = packetData;
        recalculateChecksum();
    }

    public void recalculateChecksum() {
        Checksum checksum = new CRC32();
        checksum.update(header);
        checksum.update(packetParameter);
        checksum.update(packetType);
        if (packetData.length > 0) {
            checksum.update(packetData);
        }
        this.packetCheckSum = longToBytes(checksum.getValue());
    }

    public byte[] toByteArray() {
        return concatArrays(header, new byte[]{packetType}, packetParameter, packetData, packetCheckSum);
    }

    void validate(byte[] data, PacketType expectedType, PacketParameter... expectedParam) throws ProtocolException {
        if (!Arrays.equals(subArray(data, 0, 1), DEFAULT_HEADER)) {
              throw new ProtocolException("Invalid packet header! Expected: " + Arrays.toString(DEFAULT_HEADER) +
                      ", Received: " + Arrays.toString(subArray(data, 0, 1)));
        }
        PacketType currentType = PacketType.getByCode(data[3]);
        PacketParameter currentParameter = PacketParameter.getByData(subArray(data, 4, 7));
        if (currentType != expectedType) {
            throw new ProtocolException("Invalid packet type! Expected: " + expectedType.getCode() +
                    ", Received: " + (currentType == null ? null : currentType.getCode()));
        }
        if (!Arrays.asList(expectedParam).contains(currentParameter)) {
            throw new ProtocolException("Invalid packet parameter! Expected: " + Arrays.deepToString(expectedParam) +
                    ", Received: " + (currentParameter == null ? null : Arrays.toString(currentParameter.getData())));
        }
        long expectedChecksum = crc32(subArray(data, 0, data.length - 9));
        byte[] currentChecksum = subArray(data, data.length - 8, data.length - 1);
        if (expectedChecksum != bytesToLong(currentChecksum)) {
            throw new ProtocolException("Invalid packet checksum! Expected: " + expectedChecksum + ", Received: " +
                    bytesToLong(currentChecksum));
        }
    }

    public int length() {
        return toByteArray().length;
    }

}
