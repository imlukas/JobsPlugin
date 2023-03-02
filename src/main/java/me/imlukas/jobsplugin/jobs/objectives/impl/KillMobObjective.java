package me.imlukas.jobsplugin.jobs.objectives.impl;

import me.imlukas.jobsplugin.JobsPlugin;
import me.imlukas.jobsplugin.data.PlayerData;
import me.imlukas.jobsplugin.data.storage.JobStorage;
import me.imlukas.jobsplugin.data.storage.PlayerStorage;
import me.imlukas.jobsplugin.jobs.Job;
import me.imlukas.jobsplugin.jobs.filters.FilterType;
import me.imlukas.jobsplugin.jobs.filters.JobFilter;
import me.imlukas.jobsplugin.jobs.objectives.JobObjective;
import me.imlukas.jobsplugin.jobs.objectives.JobObjectiveType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class KillMobObjective implements JobObjective<EntityDamageByEntityEvent> {

    private final PlayerStorage playerStorage;
    private final JobFilter filter;

    public KillMobObjective(JobsPlugin plugin) {
        this.playerStorage = plugin.getPlayerStorage();
        this.filter = new JobFilter();
    }

    @Override
    public JobObjectiveType getType() {
        return JobObjectiveType.KILL_MOB;
    }

    @Override
    public String getDisplayName() {
        return "Kill mobs";
    }

    @Override
    public String getConfigName() {
        return "kill_mob";
    }

    @Override
    public String getDescription() {
        return "Kill mobs to earn some cash";
    }

    @Override
    public FilterType getFilterType() {
        return FilterType.ENTITY;
    }

    @Override
    public JobFilter getFilter() {
        return filter;
    }

    @Override
    public Class<EntityDamageByEntityEvent> getEventClass() {
        return EntityDamageByEntityEvent.class;
    }


    // TODO: Not listening, fix
    @Override
    public Consumer<EntityDamageByEntityEvent> handle(Job job) {
        return (event) -> {
            Entity entity = event.getEntity();
            Entity damager = event.getDamager();

            if (!(entity instanceof LivingEntity livingEntity && damager instanceof Player player)) {
                return;
            }

            UUID playerUUID = player.getUniqueId();

            double damage = event.getDamage();
            double health = livingEntity.getHealth();

            List<Object> toFilter = getFilter().getToFilter();

            if (!(damage >= health)) {
                return;
            }

            PlayerData playerData = playerStorage.getPlayerData(playerUUID);

            if (toFilter.isEmpty()) {
                playerData.addXp(job, 1);
                return;
            }

            if(getFilter().contains(livingEntity.getType())) {
                playerData.addXp(job, 1);
            }
        };
    }


}
