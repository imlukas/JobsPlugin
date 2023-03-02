package me.imlukas.jobsplugin.listeners;

import me.imlukas.jobsplugin.JobsPlugin;
import me.imlukas.jobsplugin.data.PlayerData;
import me.imlukas.jobsplugin.data.file.JobFileStorage;
import me.imlukas.jobsplugin.data.file.PlayerFileStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {

    private final PlayerFileStorage playerFileStorage;

    PlayerLeaveListener(JobsPlugin plugin) {
        this.playerFileStorage = plugin.getPlayerFileStorage();
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        playerFileStorage.save(player);
    }


}
