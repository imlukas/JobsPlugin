package me.imlukas.jobsplugin.jobs.menu;

import me.imlukas.jobsplugin.JobsPlugin;
import me.imlukas.jobsplugin.utils.menu.base.ConfigurableMenu;
import me.imlukas.jobsplugin.utils.menu.configuration.ConfigurationApplicator;
import me.imlukas.jobsplugin.utils.menu.layer.BaseLayer;
import me.imlukas.jobsplugin.utils.menu.layer.PaginableLayer;
import me.imlukas.jobsplugin.utils.menu.mask.PatternMask;
import me.imlukas.jobsplugin.utils.menu.pagination.PaginableArea;
import org.bukkit.entity.Player;

public class JobList {

    private final JobsPlugin plugin;

    public JobList(JobsPlugin plugin) {
        this.plugin = plugin;
    }

    public void open(Player viewer) {
        ConfigurableMenu menu = (ConfigurableMenu) plugin.getMenuRegistry().create("jobs-list", viewer);
        BaseLayer layer = new BaseLayer(menu);

        PaginableLayer paginableLayer = new PaginableLayer(menu);
        ConfigurationApplicator applicator = menu.getApplicator();

        PatternMask mask = applicator.getMask();

        menu.addRenderable(layer, paginableLayer);

        PaginableArea area = new PaginableArea(mask.selection("."));
        paginableLayer.addArea(area);
    }
}
