package me.PauMAVA.DBAR.common.protocol;

import java.util.Arrays;

public enum PacketParameter {

    CONNECTION_ACCEPTED(new byte[]{0,0,1,1}),
    CONNECTION_DENIED(new byte[]{0,0,1,0}),
    SUCCESS(new byte[]{0,1,0,1}),
    FAILURE(new byte[]{0,1,0,0}),
    NO_PARAM(new byte[]{0,0,0,0});

    private final byte[] data;

    PacketParameter(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public static PacketParameter getByData(byte[] data) {
        if (data.length != 4) {
            return null;
        }
        for (PacketParameter value: values()) {
            if (Arrays.equals(value.getData(), data)) {
                return value;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return Arrays.toString(data);
    }
}
