package me.PauMAVA.DBAR.network;

import me.PauMAVA.DBAR.DBAR;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
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
                new DBARConnection(socket);
            }
        }
    }

    @Override
    public void run() {
        startListening();
    }

    public Socket waitForConnection() {
        try {
            Socket socket = listeningSocket.accept();
            return isSocketAuthorized(socket) ? socket : null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isSocketAuthorized(Socket socket) {
        List<Inet4Address> authorizedHosts = dbar.getConfigHandler().getAuthorizedAddresses();
        if (authorizedHosts.contains((Inet4Address) socket.getInetAddress())) {
            return true;
        } else {
            dbar.logWarning("Unauthorized request from " + socket.getInetAddress().toString() + ". Request blocked!");
            return false;
        }
    }
}
