package me.PauMAVA.DBAR.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private InetAddress toInetAddress(String s) throws UnknownHostException {
        return InetAddress.getByName(s);
    }
}
