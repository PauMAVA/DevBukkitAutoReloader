package me.PauMAVA.DBAR.common.protocol;

import static me.PauMAVA.DBAR.common.util.ConversionUtils.subArray;

public class PacketNewConnection extends Packet {

    public PacketNewConnection() {
        super(PacketType.REQUEST_CONNECTION, PacketParameter.NO_PARAM);
    }

    @Override
    public byte[] serialize() {
        return super.toByteArray();
    }

    @Override
    public void deserialize(byte[] data) throws ProtocolException {
        validate(data, PacketType.REQUEST_CONNECTION, PacketParameter.NO_PARAM);
        PacketParameter packetParameter = PacketParameter.getByData(subArray(data, 3, 6));
        if (packetParameter != null) {
            super.setParameter(packetParameter);
        }
        super.setPacketData(new byte[0]);
    }
}
