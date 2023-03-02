package me.imlukas.jobsplugin;

import lombok.Getter;
import me.imlukas.jobsplugin.commands.JobDisplayCommand;
import me.imlukas.jobsplugin.commands.JobJoinCommand;
import me.imlukas.jobsplugin.commands.JobLeaveCommand;
import me.imlukas.jobsplugin.data.JobData;
import me.imlukas.jobsplugin.data.storage.JobStorage;
import me.imlukas.jobsplugin.data.storage.PlayerStorage;
import me.imlukas.jobsplugin.data.file.PlayerFileStorage;
import me.imlukas.jobsplugin.data.file.JobFileStorage;
import me.imlukas.jobsplugin.data.sql.SQLHandler;
import me.imlukas.jobsplugin.data.sql.SQLSetup;
import me.imlukas.jobsplugin.jobs.objectives.impl.BlockBreakObjective;
import me.imlukas.jobsplugin.jobs.objectives.impl.BlockPlaceObjective;
import me.imlukas.jobsplugin.jobs.objectives.impl.KillMobObjective;
import me.imlukas.jobsplugin.jobs.objectives.registry.JobObjectiveRegistry;
import me.imlukas.jobsplugin.listeners.PlayerJoinListener;
import me.imlukas.jobsplugin.utils.TextUtil;
import me.imlukas.jobsplugin.utils.command.impl.CommandManager;
import me.imlukas.jobsplugin.utils.menu.registry.MenuRegistry;
import me.imlukas.jobsplugin.utils.storage.MessagesFile;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class JobsPlugin extends JavaPlugin {

    private TextUtil textUtil;
    private MessagesFile messages;
    private MenuRegistry menuRegistry;
    private CommandManager commandManager;
    private JobObjectiveRegistry objectiveRegistry;

    private PlayerStorage playerStorage;
    private JobStorage jobStorage;
    private JobData jobData;

    private PlayerFileStorage playerFileStorage;
    private JobFileStorage jobFileStorage;

    private SQLHandler sqlHandler;
    private SQLSetup sqlSetup;

    @Override
    public void onEnable() {
        textUtil = new TextUtil(this);
        messages = new MessagesFile(this);

        menuRegistry = new MenuRegistry(this);
        commandManager = new CommandManager(this);
        objectiveRegistry = new JobObjectiveRegistry(this);
        registerObjectives();

        saveResource("menu/jobs-list.yml", false);
        sqlSetup = new SQLSetup(this);
        sqlHandler = new SQLHandler(this);

        playerStorage = new PlayerStorage(this);
        jobStorage = new JobStorage();
        jobData = new JobData(this);

        jobFileStorage = new JobFileStorage(this);
        jobFileStorage.load();

        playerFileStorage = new PlayerFileStorage(this);



        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);

        commandManager.register(new JobLeaveCommand(this));
        commandManager.register(new JobJoinCommand(this));
        commandManager.register(new JobDisplayCommand(this));
    }

    public void registerObjectives() {
        objectiveRegistry.register(new BlockBreakObjective(this));
        objectiveRegistry.register(new BlockPlaceObjective(this));
        objectiveRegistry.register(new KillMobObjective(this));


        System.out.println("Registered " + objectiveRegistry.getObjectives().size() + " objectives");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
