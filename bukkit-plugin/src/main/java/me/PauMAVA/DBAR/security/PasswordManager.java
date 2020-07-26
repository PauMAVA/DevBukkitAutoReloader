package me.PauMAVA.DBAR.security;

import me.PauMAVA.DBAR.DBAR;
import me.PauMAVA.DBAR.config.ConfigHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import static me.PauMAVA.DBAR.common.util.CryptUtils.sha256;
import static me.PauMAVA.DBAR.util.RandomUtils.randomLengthString;

public class PasswordManager {

    private final DBAR dbar;
    private final ConfigHandler configHandler;

    private final int secLevel;

    private final File passwordFile;

    public PasswordManager(DBAR dbar, ConfigHandler configHandler) {
        this.dbar = dbar;
        this.configHandler = configHandler;
        secLevel = checkSecLevels();
        File resultFile;
        try {
            resultFile = init();
        } catch (IOException e) {
            dbar.logSevere("Could not initialize security manager! All reload requests will be denied!");
            e.printStackTrace();
            resultFile = null;
        }
        passwordFile = resultFile;
    }

    private File init() throws IOException {
        if (secLevel == 0) {
            dbar.logSevere("      ^       ");
            dbar.logSevere("     / \\      All security methods are disabled!");
            dbar.logSevere("    / _ \\     This is dangerous as anyone could request server reloads without ");
            dbar.logSevere("   / / \\ \\    any kind of authentication.");
            dbar.logSevere("  /  \\_/  \\   Is extremely advised that you enable ip filtering or password protection");
            dbar.logSevere(" /   (_)   \\  on the config file!");
            dbar.logSevere(" ----------- ");
        }
        if (configHandler.requirePassword() && !checkForPassword()) {
            dbar.logInfo("Registering new password! To change password use: /dbarpass <newPass> <newPass>");
            return registerPassword(randomLengthString(12, 12));
        }
        return new File(dbar.getDataFolder() + File.separator + "password.sec");
    }

    private boolean checkForPassword() {
        return new File(dbar.getDataFolder() + File.separator + "password.sec").exists();
    }

    public File registerPassword(String plainPassword) throws IOException {
        if (!dbar.getDataFolder().exists()) {
            dbar.getDataFolder().mkdirs();
        }
        File passwordFile = new File(dbar.getDataFolder() + File.separator + "password.sec");
        if (!passwordFile.exists()) {
            passwordFile.createNewFile();
        }
        FileConfiguration asConfig = YamlConfiguration.loadConfiguration(passwordFile);
        asConfig.set("hash", sha256(plainPassword));
        asConfig.save(passwordFile);
        return passwordFile;
    }

    private int checkSecLevels() {
        int i = 0;
        i = configHandler.filterByIp() ? i+1 : i;
        i = configHandler.requirePassword() ? i+1 : i;
        return i;
    }

    public boolean authorize(String hash) {
        return getRealHash() != null && getRealHash().equals(hash);
    }

    private String getRealHash() {
        if (passwordFile == null) {
            return null;
        }
        return YamlConfiguration.loadConfiguration(passwordFile).getString("hash");
    }

}
