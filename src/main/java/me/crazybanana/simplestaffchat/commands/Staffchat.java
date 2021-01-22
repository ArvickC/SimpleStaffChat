package me.crazybanana.simplestaffchat.commands;

import me.crazybanana.simplestaffchat.SimpleStaffChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Staffchat implements CommandExecutor {
    // Constructor
    SimpleStaffChat pl;
    public Staffchat(Plugin plugin) {
        pl = (SimpleStaffChat) plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Permission
        if(sender.hasPermission("simplestaffchat.use")) {
            // Usage
            if(args.length == 0) {
                Player p = (Player) sender;
                if(!SimpleStaffChat.hasEnabled.get(p)) {
                    // Enable
                    SimpleStaffChat.hasEnabled.replace(p, true);
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
                    sendPlayerTitle(pl, p);
                } else {
                    // Disable
                    SimpleStaffChat.hasEnabled.replace(p, false);
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 0.5f);
                }

            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Incorrect Usage&c. /staffchat"));
            }
        }

        return false;
    }
    // Send constant title
    private void sendPlayerTitle(SimpleStaffChat plugin, Player p) {
        // Timer
        new BukkitRunnable() {
            @Override
            public void run() {
                // End check
                if (!SimpleStaffChat.hasEnabled.get(p)) {
                    cancel();
                }
                // Title
                p.sendTitle("", "§eStaff Chat", 0, 8, 0);
            }
        }.runTaskTimer(plugin, 5L, 5L); // Runs 4x second
    }
}
