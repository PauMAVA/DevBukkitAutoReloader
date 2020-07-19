package me.PauMAVA.DBAR.common.protocol;

import static me.PauMAVA.DBAR.common.util.ConversionUtils.subArray;

public class PacketConnectionApprove extends Packet {

    private boolean isAccepted;

    public PacketConnectionApprove() {
        super(PacketType.CONNECTION_ACCEPT, PacketParameter.NO_PARAM);
    }

    public PacketConnectionApprove(PacketParameter parameter) {
        super(PacketType.CONNECTION_ACCEPT, parameter);
        isAccepted = parameter == PacketParameter.CONNECTION_ACCEPTED;
    }

    @Override
    public byte[] serialize() {
        return toByteArray();
    }

    @Override
    public void deserialize(byte[] data) throws ProtocolException {
        validate(data, PacketType.CONNECTION_ACCEPT, PacketParameter.CONNECTION_ACCEPTED, PacketParameter.CONNECTION_DENIED);
        PacketParameter packetParameter = PacketParameter.getByData(subArray(data, 4, 7));
        if (packetParameter != null) {
            super.setParameter(packetParameter);
        }
        super.setPacketData(new byte[0]);
        isAccepted = packetParameter == PacketParameter.CONNECTION_ACCEPTED;
    }

    public boolean isAccepted() {
        return isAccepted;
    }
}
