package me.PauMAVA.DBAR.config;

import me.PauMAVA.DBAR.DBAR;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ConfigHandler {

    private final DBAR dbar;

    private final File file;

    private final FileConfiguration configuration;

    public ConfigHandler(DBAR dbar, FileConfiguration configuration) throws IOException {
        this.dbar = dbar;
        this.configuration = configuration;
        file = checkFile();
        replaceMissingWithDefaults();
    }

    private File checkFile() throws IOException {
        File file = new File(dbar.getDataFolder() + File.separator + "config.yml");
        if (!dbar.getDataFolder().exists()) {
            dbar.getDataFolder().mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    private void replaceMissingWithDefaults() throws IOException {
        if (!configuration.contains("port")) {
          configuration.set("port", 25678);
        }
        if (!configuration.contains("authorized")) {
            configuration.set("authorized", Collections.singletonList("127.0.0.1"));
        }
        if (!configuration.contains("filterByIp")) {
            configuration.set("filterByIp", true);
        }
        if (!configuration.contains("requirePassword")) {
            configuration.set("requirePassword", true);
        }
        configuration.save(file);
    }

    public int getPort() {
        return configuration.getInt("port");
    }

    public List<Inet4Address> getAuthorizedAddresses() {
        List<?> authorized = configuration.getList("authorized");
        List<Inet4Address> authorizedAddresses = new ArrayList<>();
        if (authorized != null) {
            for (Object s: authorized) {
                if (s instanceof String) {
                    Inet4Address addr = toInetAddress((String) s);
                    if (addr != null) {
                        authorizedAddresses.add(addr);
                    } else {
                        dbar.logWarning("The authorized host " + s + " is not a valid IPv4 address. All requests from " +
                                "the intended IPv4 will be denied.");
                    }
                } else {
                    dbar.logWarning("An authorized host must be a valid IPv4 address as a String. Found: " +
                            s.getClass() + "All requests from the intended IPv4 will be denied.");
                }
            }
        }
        return authorizedAddresses;
    }

    private Inet4Address toInetAddress(String s) {
        try {
            return (Inet4Address) InetAddress.getByName(s);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean filterByIp() {
        return configuration.getBoolean("filterByIp");
    }

    public boolean requirePassword() {
        return configuration.getBoolean("requirePassword");
    }

}
