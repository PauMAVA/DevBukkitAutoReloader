package me.PauMAVA.DBAR.common.protocol;

public interface Serializable {

    byte[] serialize();

    void deserialize(byte[] data) throws ProtocolException;

}
