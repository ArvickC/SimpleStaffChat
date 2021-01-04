package me.crazybanana.simplestaffchat.commands;

import me.crazybanana.simplestaffchat.SimpleStaffChat;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StaffchatReload implements CommandExecutor {
    // Constructor
    SimpleStaffChat pl;
    public StaffchatReload(SimpleStaffChat plugin) {
        pl = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender.hasPermission("simplestaffchat.reload")) {
            // Reload
            pl.reloadConfig();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&eSimpleStaffChat&7] &ePlugin&a Reloaded&e."));
        }

        return false;
    }
}
