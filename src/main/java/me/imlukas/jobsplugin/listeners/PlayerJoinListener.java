package me.imlukas.jobsplugin.listeners;

import me.imlukas.jobsplugin.JobsPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {


    private final JobsPlugin plugin;

    public PlayerJoinListener(JobsPlugin plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        plugin.getPlayerFileStorage().load(player);
    }

}
