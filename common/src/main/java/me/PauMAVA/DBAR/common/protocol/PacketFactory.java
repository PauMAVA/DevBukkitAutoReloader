package me.PauMAVA.DBAR.common.protocol;

public class PacketFactory {

    public static Packet buildPacket(byte[] data) throws ProtocolException {
        Packet packet = buildPacketFromClass(PacketType.getByCode(data[2]));
        if (packet == null) {
            throw new ProtocolException("Unable to determine packet type!");
        }
        packet.deserialize(data);
        return packet;
    }

    private static Packet buildPacketFromClass(PacketType type) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case REQUEST_CONNECTION: return new PacketNewConnection();
            case CONNECTION_ACCEPT: return new PacketConnectionApprove();
            case REQUEST_PASSWORD: return new PacketRequestPassword();
            case SEND_PASSWORD: return new PacketSendPassword();
            case SERVER_RELOAD: return new PacketReloadServer();
            case CONNECTION_CLOSE: return new PacketCloseConnection();
        }
        return null;
    }

}
