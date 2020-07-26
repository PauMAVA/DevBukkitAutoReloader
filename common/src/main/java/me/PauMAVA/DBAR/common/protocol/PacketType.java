package me.PauMAVA.DBAR.common.protocol;

public enum PacketType {

    REQUEST_CONNECTION((byte) 0),
    REQUEST_PASSWORD((byte) 1),
    SEND_PASSWORD((byte) 2),
    CONNECTION_ACCEPT((byte) 3),
    CONNECTION_CLOSE((byte) 4),
    SERVER_RELOAD((byte) 5);

    private final byte code;

    PacketType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static PacketType getByCode(byte code) {
        for (PacketType type: values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }
}
