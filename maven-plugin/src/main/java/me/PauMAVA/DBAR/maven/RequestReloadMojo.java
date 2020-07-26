package me.PauMAVA.DBAR.maven;

import me.PauMAVA.DBAR.common.network.DBARConnection;
import me.PauMAVA.DBAR.common.protocol.Packet;
import me.PauMAVA.DBAR.common.protocol.PacketCloseConnection;
import me.PauMAVA.DBAR.common.protocol.PacketConnectionApprove;
import me.PauMAVA.DBAR.common.protocol.PacketNewConnection;
import me.PauMAVA.DBAR.common.protocol.PacketReloadServer;
import me.PauMAVA.DBAR.common.protocol.PacketRequestPassword;
import me.PauMAVA.DBAR.common.protocol.PacketSendPassword;
import me.PauMAVA.DBAR.common.protocol.ProtocolException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import static me.PauMAVA.DBAR.common.util.CryptUtils.sha256;

@Mojo(name = "request-reload")
public class RequestReloadMojo extends AbstractMojo {

    private Socket socket;

    @Parameter(property = "request-upload.address", defaultValue = "localhost")
    private String address;

    @Parameter(property = "request-upload.port", defaultValue = "25678")
    private int port;

    @Parameter(property = "request-upload.password")
    private String password;

    public void execute() throws MojoExecutionException, MojoFailureException {
        InetAddress address;
        try {
            address = toInetAddress(this.address);
        } catch (UnknownHostException e) {
            throw new MojoFailureException(this.address, "Invalid IP address.", "The provided address couldn't be identified" +
                    " as a valid IPv4 or IPv6 IP address.");
        }
        try {
            socket = new Socket(address, port);
            requestReload(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private InetAddress toInetAddress(String s) throws UnknownHostException {
        return InetAddress.getByName(s);
    }

    private void requestReload(Socket socket) {
        try (DBARConnection connection = new DBARConnection(socket)) {
            connection.writePacket(new PacketNewConnection());
            Packet read = connection.readPacket();
            if (!(read instanceof PacketRequestPassword)) {
                throw new ProtocolException("Expected packet request password. Received: " + read);
            }
            connection.writePacket(new PacketSendPassword(sha256(password)));
            read = connection.readPacket();
            if (!(read instanceof PacketConnectionApprove)) {
                throw new ProtocolException("Expected packet connection approve. Received: " + read);
            } else if (!((PacketConnectionApprove) read).isAccepted()) {
                throw new ProtocolException("Remote host refused the connection! Is the password correct?");
            }
            connection.writePacket(new PacketReloadServer());
            read = connection.readPacket();
            if (!(read instanceof PacketCloseConnection)) {
                throw new ProtocolException("Expected packet close connection. Received: " + read);
            } else {
                getLog().info((((PacketCloseConnection) read).isSuccess() ? "Server reload successful!" : "Server reload failed!"));
            }
        } catch (IOException | ProtocolException e) {
            e.printStackTrace();
        }
    }
}
