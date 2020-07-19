package me.PauMAVA.DBAR.common.protocol;

import java.util.Arrays;

import static me.PauMAVA.DBAR.common.util.ConversionUtils.subArray;

public class PacketRequestPassword extends Packet {

    public PacketRequestPassword() {
        super(PacketType.REQUEST_PASSWORD, PacketParameter.NO_PARAM);
    }

    @Override
    public byte[] serialize() {
        return super.toByteArray();
    }

    @Override
    public void deserialize(byte[] data) throws ProtocolException {
        validate(data, PacketType.REQUEST_PASSWORD, PacketParameter.NO_PARAM);
        PacketParameter packetParameter = PacketParameter.getByData(subArray(data, 4, 7));
        if (packetParameter != null) {
            super.setParameter(packetParameter);
        }
        super.setPacketData(new byte[0]);
    }

}
