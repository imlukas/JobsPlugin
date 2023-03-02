package me.imlukas.jobsplugin.commands;

import me.imlukas.jobsplugin.JobsPlugin;
import me.imlukas.jobsplugin.data.PlayerData;
import me.imlukas.jobsplugin.data.storage.JobStorage;
import me.imlukas.jobsplugin.data.storage.PlayerStorage;
import me.imlukas.jobsplugin.jobs.Job;
import me.imlukas.jobsplugin.utils.TextUtil;
import me.imlukas.jobsplugin.utils.command.SimpleCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class JobDisplayCommand implements SimpleCommand {
    private final JobsPlugin plugin;
    private final PlayerStorage playerStorage;
    private final JobStorage jobStorage;

    public JobDisplayCommand(JobsPlugin main) {
        this.plugin = main;
        this.playerStorage = main.getPlayerStorage();
        this.jobStorage = main.getJobStorage();
    }

    @Override
    public String getIdentifier() {
        return "jobs.display.*";
    }

    @Override
    public void execute(CommandSender sender, String... args) {
        if (!(sender instanceof Player player)){
            return;
        }

        PlayerData playerData = playerStorage.getOrCreatePlayerData(player.getUniqueId());

        List<Job> jobs = jobStorage.getAvailableJobs();

        player.sendMessage(TextUtil.color("&7|| &eJobs |&7| )"));

        for (Job job : jobs) {
            player.sendMessage(TextUtil.color("&7|| &e" + job.getName() +
                    " &7| &eLevel: &7" + playerData.getJobLevel(job) + " &7| &eXP: &7"
                    + playerData.getSelectedJobXp(job) + " &7| )"));
        }
    }
}
