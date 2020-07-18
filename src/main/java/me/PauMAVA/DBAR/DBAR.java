package me.PauMAVA.DBAR;

import me.PauMAVA.DBAR.commands.ChangePasswordCommand;
import me.PauMAVA.DBAR.config.ConfigHandler;
import me.PauMAVA.DBAR.events.EventListener;
import me.PauMAVA.DBAR.network.Listener;
import me.PauMAVA.DBAR.security.PasswordManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Level;

public class DBAR extends JavaPlugin {

    public static final String prefix = "" + ChatColor.BOLD + ChatColor.GRAY + "[" + ChatColor.RESET
            + ChatColor.BOLD + ChatColor.RED + "DBAR" + ChatColor.RESET
            + ChatColor.BOLD + ChatColor.GRAY + "]" + ChatColor.RESET + " ";

    private ConfigHandler configHandler;

    private EventListener eventListener;

    private PasswordManager passwordManager;

    private Listener listener;

    @Override
    public void onEnable() {
        try {
            this.configHandler = new ConfigHandler(this, getConfig());
        } catch (IOException e) {
            logSevere("Couldn't create the config handler. Does this plugin have permissions to access its config file?");
            logSevere("Plugin won't listen for requests!");
            e.printStackTrace();
            return;
        }
        this.eventListener = new EventListener(this);
        getServer().getPluginManager().registerEvents(eventListener, this);
        this.passwordManager = new PasswordManager(this, configHandler);
        this.listener = new Listener(this);
        new Thread(listener).start();
        getServer().getPluginCommand("dbarpass").setExecutor(new ChangePasswordCommand(this));
    }

    @Override
    public void onDisable() {
        listener.stopListening();
        listener.closeAll();
    }

    public void reloadServer() {
        getServer().reload();
    }

    public void logInfo(String message) {
        log(message, Level.INFO);
    }

    public void logWarning(String message) {
        log(message, Level.WARNING);
    }

    public void logSevere(String message) {
        log(message, Level.SEVERE);
    }

    public void log(String message, Level level) {
        getLogger().log(level, message);
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public PasswordManager getPasswordManager() {
        return passwordManager;
    }
}
