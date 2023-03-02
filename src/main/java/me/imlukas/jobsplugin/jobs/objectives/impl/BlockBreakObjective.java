package me.imlukas.jobsplugin.jobs.objectives.impl;

import me.imlukas.jobsplugin.JobsPlugin;
import me.imlukas.jobsplugin.data.PlayerData;
import me.imlukas.jobsplugin.data.storage.PlayerStorage;
import me.imlukas.jobsplugin.jobs.Job;
import me.imlukas.jobsplugin.jobs.filters.FilterType;
import me.imlukas.jobsplugin.jobs.filters.JobFilter;
import me.imlukas.jobsplugin.jobs.objectives.JobObjective;
import me.imlukas.jobsplugin.jobs.objectives.JobObjectiveType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.metadata.MetadataValue;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class BlockBreakObjective implements JobObjective<BlockBreakEvent> {

    private final PlayerStorage playerStorage;
    private final JobFilter filter;


    public BlockBreakObjective(JobsPlugin plugin) {
        this.filter = new JobFilter();
        this.playerStorage = plugin.getPlayerStorage();
    }

    @Override
    public Class<BlockBreakEvent> getEventClass() {
        return BlockBreakEvent.class;
    }

    @Override
    public JobObjectiveType getType() {
        return JobObjectiveType.BLOCK_BREAK;
    }

    @Override
    public String getDisplayName() {
        return "Break Blocks";
    }

    @Override
    public String getConfigName() {
        return "break_blocks";
    }

    @Override
    public String getDescription() {
        return "Break certain blocks.";
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
    public Consumer<BlockBreakEvent> handle(Job job) {
        return (event) -> {
            Player player = event.getPlayer();
            UUID playerUUID = player.getUniqueId();

            Block block = event.getBlock();

            Material material = block.getType();

            List<MetadataValue> metadataList = block.getMetadata("placedByPlayer");

            boolean isPlacedByPlayer = false;
            if (!metadataList.isEmpty()) {
                isPlacedByPlayer = metadataList.get(0).asBoolean();
            }

            if (isPlacedByPlayer) {
                return;
            }
            PlayerData playerData = playerStorage.getOrCreatePlayerData(playerUUID);

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
