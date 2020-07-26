package me.PauMAVA.DBAR.common.protocol;

public class ProtocolException extends Exception {

    private final String message;

    public ProtocolException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
