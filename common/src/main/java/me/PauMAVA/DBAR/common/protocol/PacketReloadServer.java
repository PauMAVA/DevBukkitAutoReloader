package me.PauMAVA.DBAR.common.protocol;

import static me.PauMAVA.DBAR.common.util.ConversionUtils.subArray;

public class PacketReloadServer extends Packet {

    public PacketReloadServer() {
        super(PacketType.SERVER_RELOAD, PacketParameter.NO_PARAM);
    }

    @Override
    public byte[] serialize() {
        return toByteArray();
    }

    @Override
    public void deserialize(byte[] data) throws ProtocolException {
        validate(data, PacketType.SERVER_RELOAD, PacketParameter.NO_PARAM);
        PacketParameter packetParameter = PacketParameter.getByData(subArray(data, 3, 6));
        if (packetParameter != null) {
            super.setParameter(packetParameter);
        }
        super.setPacketData(new byte[0]);
    }
}
