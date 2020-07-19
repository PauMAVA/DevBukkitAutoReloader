package me.PauMAVA.DBAR.common.protocol;

import static me.PauMAVA.DBAR.common.util.ConversionUtils.subArray;

public class PacketCloseConnection extends Packet {

    private boolean isSuccess;

    public PacketCloseConnection() {
        super(PacketType.CONNECTION_CLOSE, PacketParameter.NO_PARAM);
    }

    public PacketCloseConnection(PacketParameter parameter) {
        super(PacketType.CONNECTION_CLOSE, parameter);
        isSuccess = parameter == PacketParameter.SUCCESS;
    }

    @Override
    public byte[] serialize() {
        return toByteArray();
    }

    @Override
    public void deserialize(byte[] data) throws ProtocolException {
        validate(data, PacketType.CONNECTION_CLOSE, PacketParameter.SUCCESS, PacketParameter.FAILURE);
        PacketParameter packetParameter = PacketParameter.getByData(subArray(data, 4, 7));
        if (packetParameter != null) {
            super.setParameter(packetParameter);
        }
        super.setPacketData(new byte[0]);
        isSuccess = packetParameter == PacketParameter.CONNECTION_ACCEPTED;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

}
