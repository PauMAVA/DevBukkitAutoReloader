package me.PauMAVA.DBAR.common.network;

import me.PauMAVA.DBAR.common.protocol.Packet;
import me.PauMAVA.DBAR.common.protocol.PacketFactory;
import me.PauMAVA.DBAR.common.protocol.ProtocolException;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static me.PauMAVA.DBAR.common.util.ConversionUtils.bytesToPrettyBinaryString;

public class DBARConnection implements Closeable {

    private final Socket socket;

    private final DataInputStream din;
    private final DataOutputStream dout;

    public DBARConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.din = new DataInputStream(socket.getInputStream());
        this.dout = new DataOutputStream(socket.getOutputStream());
    }

    public Packet readPacket() throws IOException, ProtocolException {
        int packetLength = din.readInt();
        byte[] buffer = new byte[packetLength];
        din.read(buffer);
        return PacketFactory.buildPacket(buffer);
    }

    public void writePacket(Packet packet) throws IOException {
        dout.writeInt(packet.length());
        byte[] buffer = packet.serialize();
        dout.write(buffer);
    }

    @Override
    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
