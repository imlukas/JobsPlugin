package me.imlukas.jobsplugin.listeners;

import me.imlukas.jobsplugin.JobsPlugin;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class BlockPlaceListener implements Listener {


    private final JobsPlugin plugin;

    public BlockPlaceListener(JobsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();

        List<MetadataValue> metadataList = block.getMetadata("placedByPlayer");

        if (metadataList.get(0).asBoolean()) {
            return;
        }

        block.setMetadata("placedByPlayer", new FixedMetadataValue(plugin, true));
    }
}
