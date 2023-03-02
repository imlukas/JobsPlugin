package me.imlukas.jobsplugin.data.file;

import me.imlukas.jobsplugin.JobsPlugin;
import me.imlukas.jobsplugin.data.PlayerData;
import me.imlukas.jobsplugin.data.storage.JobStorage;
import me.imlukas.jobsplugin.data.storage.PlayerStorage;
import me.imlukas.jobsplugin.jobs.Job;
import me.imlukas.jobsplugin.utils.storage.YMLBase;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerFileStorage extends YMLBase {

    private final JobsPlugin plugin;
    private final PlayerStorage playerStorage;
    private final JobStorage jobStorage;

    public PlayerFileStorage(JobsPlugin plugin) {
        super(plugin, "data/database.yml");
        this.plugin = plugin;
        this.playerStorage = plugin.getPlayerStorage();
        this.jobStorage = plugin.getJobStorage();
    }

    public void save(Player player) {
        FileConfiguration config = getConfiguration();
        String playerUUID = player.getUniqueId().toString();

        PlayerData playerData = playerStorage.getOrCreatePlayerData(player.getUniqueId());

        CompletableFuture.runAsync(() -> {
            ConfigurationSection playerSection = config.getConfigurationSection(playerUUID);

            List<String> selectedJobs = new ArrayList<>();

            for (Job job : playerData.getSelectedJobs().keySet()) {
                selectedJobs.add(job.getName());
            }

            playerSection.set("selected", selectedJobs);

            ConfigurationSection jobsSection = playerSection.getConfigurationSection("jobs");

            for (Job job : jobStorage.getAvailableJobs()) {
                jobsSection.set(job.getName(), playerData.getSelectedJobXp(job));
            }
        }).thenRun(this::save);
    }

    /**
     * Loads the player data from the database.yml file
     * If the player does not exist in the database, it will create a new entry for the player
     * @param player The player to load
     */
    public void load(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (!getConfiguration().contains(playerUUID.toString())) {
            createPlayer(player);
            return;
        }

        List<String> selectedJobs = getSelectedJobs(player);

        PlayerData playerData = plugin.getPlayerStorage().getOrCreatePlayerData(playerUUID);

        for (String jobName : selectedJobs) {
            Job job = jobStorage.getJobByName(jobName);

            if (job == null) {
                continue;
            }

            int xp = getJobXp(player, job);

            playerData.addJob(job, xp);
        }
    }

    /**
     * Creates a new entry for the player in the database.yml file
     * @param player The player to create
     */
    public void createPlayer(Player player) {
        FileConfiguration config = getConfiguration();
        String playerUUID = player.getUniqueId().toString();

        CompletableFuture.runAsync(() -> {
            ConfigurationSection playerSection = config.createSection(playerUUID);
            playerSection.set("name", player.getName());
            playerSection.set("selected", new ArrayList<>());

            ConfigurationSection jobsSection = playerSection.createSection("jobs");

            for (Job job : jobStorage.getAvailableJobs()) {
                jobsSection.set(job.getName(), 0);
            }
        }).thenRun(this::save);
    }

    /**
     * Gets the xp of the job of the player
     * @param player The player to get the xp from
     * @param job The job to get the xp from
     */
    public void addSelectedJob(Player player, Job job) {
        FileConfiguration config = getConfiguration();
        UUID playerUUID = player.getUniqueId();

        List<String> selectedJobs = config.getStringList(playerUUID + ".selected");

        if (selectedJobs.size() == 3) {
            return;
        }

        CompletableFuture.runAsync(() -> {
            selectedJobs.add(job.getName());

            config.set(playerUUID + ".selected", selectedJobs);
        });
    }

    /**
     * Removes the job from the selected jobs of the player
     * @param player The player to remove the job from
     * @param job The job to remove
     */
    public void removeSelectedJob(Player player, Job job) {
        FileConfiguration config = getConfiguration();
        UUID playerUUID = player.getUniqueId();

        List<String> selectedJobs = config.getStringList(playerUUID + ".selected");

        if (selectedJobs.size() == 0) {
            return;
        }

        if (!selectedJobs.contains(job.getName())) {
            return;
        }

        CompletableFuture.runAsync(() -> {
            selectedJobs.remove(job.getName());

            config.set(playerUUID + ".selected", selectedJobs);
        });
    }

    /**
     * Gets the selected jobs of the player
     * @param player The player to get the selected jobs from
     * @return The selected jobs of the player
     */
    public List<String> getSelectedJobs(Player player) {
        FileConfiguration config = getConfiguration();
        UUID playerUUID = player.getUniqueId();

        return config.getStringList(playerUUID + ".selected");
    }

    /**
     * Gets the xp of the job of the player
     * @param player The player to get the xp from
     * @param job The job to get the xp from
     * @return The xp of the job of the player
     */
    public int getJobXp(Player player, Job job) {
        FileConfiguration config = getConfiguration();
        UUID playerUUID = player.getUniqueId();

        return config.getInt(playerUUID + ".jobs." + job.getName());
    }

    /**
     * Updates the xp of the job of the player
     * @param player The player to update the xp of
     * @param job The job to update the xp of
     * @param xp The new xp
     */
    public void setXp(Player player, Job job, int xp) {
        FileConfiguration config = getConfiguration();
        UUID playerUUID = player.getUniqueId();

        CompletableFuture.runAsync(() -> {
            config.set(playerUUID + ".jobs." + job.getName(), xp);
        });
    }


}
