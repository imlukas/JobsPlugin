package me.imlukas.jobsplugin.data;

import lombok.Getter;
import me.imlukas.jobsplugin.JobsPlugin;
import me.imlukas.jobsplugin.jobs.Job;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public class PlayerData {

    private final JobsPlugin plugin;
    private final JobData jobData;

    private final UUID playerUUID;
    private final Player player;
    private final Map<Job, Double> selectedJobs;

    public PlayerData(JobsPlugin plugin, UUID playerUUID) {
        this.plugin = plugin;
        this.jobData = plugin.getJobData();

        this.playerUUID = playerUUID;
        this.player = Bukkit.getPlayer(playerUUID);
        this.selectedJobs = new HashMap<>();
    }

    public double getSelectedJobXp(Job job) {
        if (!selectedJobs.containsKey(job)) {
            return 0;
        }

        return selectedJobs.get(job);
    }

    public double getJobXp(Job job) {
        return jobData.getJobXp(player, job);
    }

    public int getJobLevel(Job job) {
        return jobData.getJobLevel(player, job);
    }

    public Set<Job> getJobs() {
        return selectedJobs.keySet();
    }

    public void addJob(Job job, double jobXp) {
        if (selectedJobs.containsKey(job)) {
            return;
        }

        if (selectedJobs.size() >= 3) {
            return;
        }

        selectedJobs.put(job, jobXp);
    }

    public void removeJob(Job job) {
        if (!selectedJobs.containsKey(job)) {
            return;
        }

        selectedJobs.remove(job);
    }

    public void addXp(Job job, double xp) {
        if (!selectedJobs.containsKey(job)) {
            System.out.println("Player does not have this job");
            return;
        }

        double newProgress = selectedJobs.get(job) + xp;

        selectedJobs.put(job, newProgress);
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }



}
