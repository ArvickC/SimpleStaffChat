package me.crazybanana.simplestaffchat;

import me.crazybanana.simplestaffchat.commands.Staffchat;
import me.crazybanana.simplestaffchat.commands.StaffchatReload;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class SimpleStaffChat extends JavaPlugin implements Listener {
    // Var
    public static HashMap<Player, Boolean> hasEnabled = new HashMap<>();
    public boolean update = false;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        // Reg.
        Bukkit.getPluginManager().registerEvents(this, this);
        getCommand("staffchat").setExecutor(new Staffchat(this));
        getCommand("staffchatreload").setExecutor(new StaffchatReload(this));

        new UpdateChecker(this, 87460).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                //logger.info("There is not a new update available.");
                update = false;
            } else {
                //logger.info("There is a new update available.");
                update = true;
            }
        });

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&eSimpleStaffChat&7] &ePlugin&a Activated&e."));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&eSimpleStaffChat&7] &ePlugin&c Deactivated&e."));
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        // Var
        String message = e.getMessage();
        String start = getConfig().getString("Character");
        Player p = e.getPlayer();

        // Check start
        if(message.startsWith(start)) {
            String finalMessage = message.replaceFirst(getConfig().getString("Character"), "");

            // Permission
            if(p.hasPermission("simplestaffchat.use")) {
                // Send message
                for(Player player : Bukkit.getOnlinePlayers()) {
                    if(player.hasPermission("simplestaffchat.use")) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eStaff Chat&8 >> &7" + p.getName() + "&8 >> &7" + finalMessage));
                        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&eStaff Chat&8 >> &7" + p.getName() + "&8 >> &7" + finalMessage));
                    }
                }
                e.setCancelled(true);
            }
            // Check /staffchat
        } else if(hasEnabled.containsKey(p) && hasEnabled.get(p) && p.hasPermission("simplestaffchat.use")) {
            // Send message
            for(Player player : Bukkit.getOnlinePlayers()) {
                if(player.hasPermission("simplestaffchat.use")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eStaff Chat&8 >> &7" + p.getName() + "&8 >> &7" + message));
                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&eStaff Chat&8 >> &7" + p.getName() + "&8 >> &7" + message));
                }
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        // Add to HashMap
        if(p.hasPermission("simplestaffchat.update")) {
            hasEnabled.put(p, false);
            if(update) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&eSimpleStaffChat&7] &eHey user, there is an &aupdate&e to Simple Staff Chat!"));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&eSimpleStaffChat&7] &eYou can &cdownload &eit here:"));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&eSimpleStaffChat&7] &chttps://www.spigotmc.org/resources/simple-staff-chat.87460/"));
            }
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        // Remove from HashMap
        if(hasEnabled.containsKey(p)) {
            hasEnabled.remove(p);
        }
    }
}
