package me.imlukas.jobsplugin.commands;

import me.imlukas.jobsplugin.JobsPlugin;
import me.imlukas.jobsplugin.data.PlayerData;
import me.imlukas.jobsplugin.data.file.JobFileStorage;
import me.imlukas.jobsplugin.data.storage.JobStorage;
import me.imlukas.jobsplugin.jobs.Job;
import me.imlukas.jobsplugin.utils.command.SimpleCommand;
import me.imlukas.jobsplugin.utils.storage.MessagesFile;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class JobLeaveCommand implements SimpleCommand {

    private final JobsPlugin plugin;
    private final JobStorage jobStorage;
    private final MessagesFile messages;

    public JobLeaveCommand(JobsPlugin plugin) {
        this.plugin = plugin;
        this.jobStorage = plugin.getJobStorage();
        this.messages = plugin.getMessages();
    }

    @Override
    public String getIdentifier() {
        return "jobs.leave.*";
    }

    @Override
    public void execute(CommandSender sender, String... args) {
        String jobName = args[0];

        if (!(sender instanceof Player player)){
            return;
        }

        PlayerData playerData = plugin.getPlayerStorage().getOrCreatePlayerData(player.getUniqueId());

        for (Job job : jobStorage.getAvailableJobs()) {
            if (!job.getName().equalsIgnoreCase(jobName)) {
                continue;
            }
            playerData.removeJob(job);
            break;
        }
    }

    @Override
    public Map<Integer, List<String>> tabCompleteWildcards() {
        return Map.of(1, jobStorage.getJobNames());
    }
}
