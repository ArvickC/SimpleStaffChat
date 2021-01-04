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
    public static HashMap<Player, Boolean> hasEnabled = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        Bukkit.getPluginManager().registerEvents(this, this);
        getCommand("staffchat").setExecutor(new Staffchat(this));
        getCommand("staffchatreload").setExecutor(new StaffchatReload(this));

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&eSimpleStaffChat&7] &ePlugin&a Activated&e."));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&eSimpleStaffChat&7] &ePlugin&c Deactivated&e."));
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        String message = e.getMessage();
        String start = getConfig().getString("Character");
        Player p = e.getPlayer();

        if(message.startsWith(start)) {
            String finalMessage = message.replaceFirst("# ", "");

            if(p.hasPermission("simplestaffchat.use")) {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    if(player.hasPermission("simplestaffchat.use")) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eStaff Chat&8 >> &7" + p.getName() + "&8 >> &7" + finalMessage));
                    }
                }
                e.setCancelled(true);
            }
        } else if(hasEnabled.containsKey(p) && hasEnabled.get(p) && p.hasPermission("simplestaffchat.use")) {
            for(Player player : Bukkit.getOnlinePlayers()) {
                if(player.hasPermission("simplestaffchat.use")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eStaff Chat&8 >> &7" + p.getName() + "&8 >> &7" + message));
                }
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if(p.hasPermission("simplestaffchat.use")) {
            hasEnabled.put(p, false);
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        if(hasEnabled.containsKey(p)) {
            hasEnabled.remove(p);
        }
    }
}
