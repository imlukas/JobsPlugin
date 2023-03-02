package me.imlukas.jobsplugin.data;

import me.imlukas.jobsplugin.JobsPlugin;
import me.imlukas.jobsplugin.jobs.Job;
import me.imlukas.jobsplugin.utils.XpUtil;
import org.bukkit.entity.Player;

public class JobData {

    private final JobsPlugin plugin;

    public JobData(JobsPlugin plugin) {
        this.plugin = plugin;
    }

    public double getJobXp(Player player, Job job) {
        PlayerData playerData = plugin.getPlayerStorage().getOrCreatePlayerData(player.getUniqueId());

        return playerData.getSelectedJobXp(job);
    }

    public double getXpForNextLevel(Player player, Job job) {
        return XpUtil.getXpNeededForNextLevel(getJobXp(player, job));
    }

    public double getProgress(Player player, Job job) {
        int currentLevel = getJobLevel(player, job);
        double levelXpNeeded = XpUtil.getXpToLevel(currentLevel);

        return levelXpNeeded / getXpForNextLevel(player, job);
    }

    public int getJobLevel(Player player, Job job) {
        return XpUtil.getLevelFromXp(getJobXp(player, job));
    }
}
