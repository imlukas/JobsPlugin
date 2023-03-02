package me.imlukas.jobsplugin.data.storage;

import me.imlukas.jobsplugin.JobsPlugin;
import me.imlukas.jobsplugin.data.PlayerData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerStorage {

    private final JobsPlugin plugin;
    public PlayerStorage(JobsPlugin plugin) {
        this.plugin = plugin;
    }

    private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();

    public PlayerData getOrCreatePlayerData(UUID uuid) {
        if (!playerDataMap.containsKey(uuid)) {
            playerDataMap.put(uuid, new PlayerData(plugin, uuid));
        }
        return playerDataMap.get(uuid);
    }

    public PlayerData getPlayerData(UUID uuid) {
        return playerDataMap.get(uuid);
    }
}
