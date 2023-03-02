package me.imlukas.jobsplugin.jobs.objectives.impl;

import me.imlukas.jobsplugin.JobsPlugin;
import me.imlukas.jobsplugin.data.PlayerData;
import me.imlukas.jobsplugin.data.storage.PlayerStorage;
import me.imlukas.jobsplugin.jobs.Job;
import me.imlukas.jobsplugin.jobs.filters.FilterType;
import me.imlukas.jobsplugin.jobs.filters.JobFilter;
import me.imlukas.jobsplugin.jobs.objectives.JobObjective;
import me.imlukas.jobsplugin.jobs.objectives.JobObjectiveType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.MetadataValue;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class BlockPlaceObjective implements JobObjective<BlockPlaceEvent> {

    private final PlayerStorage playerStorage;
    private final JobFilter filter;

    public BlockPlaceObjective(JobsPlugin plugin) {
        this.playerStorage = plugin.getPlayerStorage();
        this.filter = new JobFilter();
    }

    @Override
    public JobObjectiveType getType() {
        return JobObjectiveType.BLOCK_PLACE;
    }

    @Override
    public String getDisplayName() {
        return "Block Place";
    }

    @Override
    public String getConfigName() {
        return "place_blocks";
    }

    @Override
    public String getDescription() {
        return "Place Blocks to get xp";
    }

    @Override
    public FilterType getFilterType() {
        return FilterType.MATERIAL;
    }

    @Override
    public JobFilter getFilter() {
        return filter;
    }

    @Override
    public Class<BlockPlaceEvent> getEventClass() {
        return BlockPlaceEvent.class;
    }

    @Override
    public Consumer<BlockPlaceEvent> handle(Job job) {
        return (event) -> {
            Player player = event.getPlayer();
            UUID playerUUID = player.getUniqueId();

            Block block = event.getBlock();

            List<MetadataValue> metadataList = block.getMetadata("placedByPlayer");

            boolean isPlacedByPlayer = false;
            if (!metadataList.isEmpty()) {
                isPlacedByPlayer = metadataList.get(0).asBoolean();
            }

            if (isPlacedByPlayer) {
                return;
            }

            PlayerData playerData = playerStorage.getPlayerData(playerUUID);

            if (getFilter().isEmpty()){
                playerData.addXp(job, 1);
                return;
            }

            if (getFilter().contains(block.getType())) {
                playerData.addXp(job, 1);
            }

        };
    }
}
