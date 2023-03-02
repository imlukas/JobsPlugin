package me.imlukas.jobsplugin.commands;

import me.imlukas.jobsplugin.JobsPlugin;
import me.imlukas.jobsplugin.data.PlayerData;
import me.imlukas.jobsplugin.data.storage.JobStorage;
import me.imlukas.jobsplugin.jobs.Job;
import me.imlukas.jobsplugin.utils.command.SimpleCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class JobJoinCommand implements SimpleCommand {

    private final JobsPlugin plugin;
    private final JobStorage jobStorage;

    public JobJoinCommand(JobsPlugin plugin) {
        this.plugin = plugin;
        this.jobStorage = plugin.getJobStorage();
    }

    @Override
    public String getIdentifier() {
        return "jobs.join.*";
    }

    @Override
    public void execute(CommandSender sender, String... args) {

        String jobName = args[0];

        if (!(sender instanceof Player player)){
            return;
        }

        for (Job job : jobStorage.getAvailableJobs()) {
            if (!job.getName().equalsIgnoreCase(jobName)) {
                continue;
            }

            PlayerData playerData = plugin.getPlayerStorage().getOrCreatePlayerData(player.getUniqueId());

            double xp = playerData.getJobXp(job);

            playerData.addJob(job, xp);
            break;
        }
    }


    @Override
    public Map<Integer, List<String>> tabCompleteWildcards() {
        return Map.of(1, jobStorage.getJobNames());
    }
}
