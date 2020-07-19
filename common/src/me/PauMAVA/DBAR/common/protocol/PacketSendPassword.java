package me.PauMAVA.DBAR.common.protocol;

import java.nio.charset.StandardCharsets;

import static me.PauMAVA.DBAR.common.util.ConversionUtils.subArray;

public class PacketSendPassword extends Packet {

    private String passwordHash;

    public PacketSendPassword() {
        super(PacketType.SEND_PASSWORD, PacketParameter.NO_PARAM);
    }

    public PacketSendPassword(String passwordHash) {
        super(PacketType.SEND_PASSWORD, PacketParameter.NO_PARAM);
        super.setPacketData(passwordHash.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public byte[] serialize() {
        return toByteArray();
    }

    @Override
    public void deserialize(byte[] data) throws ProtocolException {
        validate(data, PacketType.SEND_PASSWORD, PacketParameter.NO_PARAM);
        PacketParameter packetParameter = PacketParameter.getByData(subArray(data, 4, 7));
        if (packetParameter != null) {
            super.setParameter(packetParameter);
        }
        byte[] passwordHash = subArray(data, 8, data.length - 9);
        super.setPacketData(passwordHash);
        this.passwordHash = new String(passwordHash, StandardCharsets.UTF_8);
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}
