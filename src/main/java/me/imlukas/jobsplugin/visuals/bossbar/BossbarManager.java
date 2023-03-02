package me.imlukas.jobsplugin.visuals.bossbar;

import me.imlukas.jobsplugin.JobsPlugin;
import me.imlukas.jobsplugin.data.JobData;
import me.imlukas.jobsplugin.data.PlayerData;
import me.imlukas.jobsplugin.jobs.Job;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class BossbarManager {

    private final JobsPlugin plugin;
    private final JobData jobData;

    public BossbarManager(JobsPlugin plugin) {
        this.plugin = plugin;
        this.jobData = plugin.getJobData();
    }

    public BossBar createBossbar(Job job, Player player) {
        BossBar bossBar = Bukkit.createBossBar(job.getName(), BarColor.BLUE, BarStyle.SOLID);
        bossBar.setProgress(jobData.getProgress(player, job));
        bossBar.addPlayer(player);
        return bossBar;
    }

}
