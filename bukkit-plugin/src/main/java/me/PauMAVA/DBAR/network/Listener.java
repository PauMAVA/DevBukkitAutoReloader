package me.PauMAVA.DBAR.network;

import me.PauMAVA.DBAR.DBAR;
import me.PauMAVA.DBAR.common.network.DBARConnection;
import me.PauMAVA.DBAR.common.protocol.Packet;
import me.PauMAVA.DBAR.common.protocol.PacketCloseConnection;
import me.PauMAVA.DBAR.common.protocol.PacketConnectionApprove;
import me.PauMAVA.DBAR.common.protocol.PacketNewConnection;
import me.PauMAVA.DBAR.common.protocol.PacketParameter;
import me.PauMAVA.DBAR.common.protocol.PacketReloadServer;
import me.PauMAVA.DBAR.common.protocol.PacketRequestPassword;
import me.PauMAVA.DBAR.common.protocol.PacketSendPassword;
import me.PauMAVA.DBAR.common.protocol.ProtocolException;
import org.sqlite.core.DB;

import java.awt.font.TextHitInfo;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class Listener implements Runnable {

    private final DBAR dbar;

    private ServerSocket listeningSocket;

    private volatile boolean listen = true;

    public Listener(DBAR dbar) {
        this.dbar = dbar;
        try {
            listeningSocket = new ServerSocket(25678);
        } catch (IOException e) {
            listeningSocket = null;
            e.printStackTrace();
        }
    }

    public void startListening() {
        listen = true;
        listen();
    }

    public void stopListening() {
        listen = false;
    }

    public void closeAll() {
        try {
            listeningSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listen() {
        while(listen) {
            Socket socket = waitForConnection();
            if (socket != null) {
                onSuccessfulConnection(socket);
            }
        }
    }

    @Override
    public void run() {
        startListening();
    }

    public Socket waitForConnection() {
        try {
            dbar.logInfo("Listening for connections...");
            Socket socket = listeningSocket.accept();
            dbar.logInfo("Incoming connection...");
            return isSocketAuthorized(socket) ? socket : null;
        } catch (SocketException e) {
            dbar.logInfo("Killed listening socket...");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isSocketAuthorized(Socket socket) {
        List<Inet4Address> authorizedHosts = dbar.getConfigHandler().getAuthorizedAddresses();
        if (authorizedHosts.contains((Inet4Address) socket.getInetAddress())) {
            dbar.logWarning("Authorized connection request from " + socket.getInetAddress().toString() + ". Requesting password...!");
            return true;
        } else {
            dbar.logWarning("Unauthorized request from " + socket.getInetAddress().toString() + ". Request blocked!");
            return false;
        }
    }

    private void onSuccessfulConnection(Socket socket) {
        try (DBARConnection connection = new DBARConnection(socket)) {
            Packet read = connection.readPacket();
            if (!(read instanceof PacketNewConnection)) {
                throw new ProtocolException("Expected new connection packet. Received: " + read);
            } else {
                connection.writePacket(new PacketRequestPassword());
            }
            read = connection.readPacket();
            if (!(read instanceof PacketSendPassword)) {
                throw new ProtocolException("Expected password send packet. Received: " + read);
            }
            if (!authenticate((PacketSendPassword) read)) {
                connection.writePacket(new PacketConnectionApprove(PacketParameter.CONNECTION_DENIED));
                throw new ProtocolException("Invalid authentication from remote!");
            } else {
                connection.writePacket(new PacketConnectionApprove(PacketParameter.CONNECTION_ACCEPTED));
            }
            read = connection.readPacket();
            if (!(read instanceof PacketReloadServer)) {
                connection.writePacket(new PacketCloseConnection(PacketParameter.FAILURE));
                throw new ProtocolException("Expected server reload packet. Received: " + read);
            } else {
                connection.writePacket(new PacketCloseConnection(PacketParameter.SUCCESS));
                connection.close();
                dbar.reloadServer();
            }
        } catch (IOException | ProtocolException e) {
            e.printStackTrace();
        }
    }

    private boolean authenticate(PacketSendPassword packet) {
        return dbar.getPasswordManager().authorize(packet.getPasswordHash().toLowerCase());
    }

}
