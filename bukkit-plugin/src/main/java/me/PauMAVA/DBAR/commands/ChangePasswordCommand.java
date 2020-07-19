package me.PauMAVA.DBAR.commands;

import me.PauMAVA.DBAR.DBAR;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.io.IOException;

public class ChangePasswordCommand implements CommandExecutor {

    private final DBAR dbar;

    public ChangePasswordCommand(DBAR dbar) {
        this.dbar = dbar;
    }

    @Override
    public boolean onCommand(CommandSender theSender, Command cmd, String label, String[] args) {
        if (!(theSender instanceof ConsoleCommandSender)) {
            theSender.sendMessage(DBAR.prefix + ChatColor.RED + "This command can only be executed via a privileged console!");
            return false;
        }
        if (args.length != 2) {
            theSender.sendMessage(DBAR.prefix + ChatColor.RED + "Command usage: /dbarpass <newPass> <newPass>");
            return false;
        }
        if (!args[0].equals(args[1])) {
            theSender.sendMessage(DBAR.prefix + ChatColor.RED + "Passwords do not match! No changes were made");
            return false;
        }
        try {
            dbar.getPasswordManager().registerPassword(args[0]);
            theSender.sendMessage(DBAR.prefix + ChatColor.GREEN + "The password has been updated successfully!");
            return true;
        } catch (IOException e) {
            theSender.sendMessage(DBAR.prefix + ChatColor.RED + "An error happened when trying to save the password!");
            e.printStackTrace();
            return false;
        }
    }

}
