package me.imlukas.jobsplugin.jobs.objectives.registry;

import me.imlukas.jobsplugin.JobsPlugin;
import me.imlukas.jobsplugin.data.PlayerData;
import me.imlukas.jobsplugin.jobs.Job;
import me.imlukas.jobsplugin.jobs.objectives.JobObjective;
import me.imlukas.jobsplugin.jobs.objectives.JobObjectiveType;
import me.imlukas.jobsplugin.utils.EventWrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JobObjectiveRegistry {

    private final JobsPlugin plugin;

    public JobObjectiveRegistry(JobsPlugin plugin) {
        this.plugin = plugin;
    }

    private final Map<Class<? extends Event>, List<JobObjective<?>>> objectives = new ConcurrentHashMap<>();

    /**
     * Registers an event listener and it's related objectives.
     * @param objective The objective to register
     * @param <T> The event type
     */
    public <T extends Event> void register(JobObjective<T> objective) {
        Class<T> jobEvent = objective.getEventClass();

        // Adds a new objective to the list if the event isn't null
        objectives.computeIfPresent(jobEvent, (registeredEvent, jobObjectives) -> {
            List<JobObjective<?>> existingObjectives = objectives.get(jobEvent);

            for (JobObjective<?> existingObjective : existingObjectives) {
                if (existingObjective.getType() == objective.getType()) {
                    System.out.println("Objective already registered, skipping");
                }
            }

            existingObjectives.add(objective);
            return existingObjectives;
        });

        // If the event is not registered, register it
        objectives.computeIfAbsent(jobEvent, registeredEvent -> {
            Bukkit.getPluginManager().registerEvent(jobEvent, new Listener() {}, EventPriority.NORMAL, (listener, event) -> {
                Player player = EventWrapper.getPlayer(event);

                if (player == null) {
                    return;
                }

                UUID playerUUID = player.getUniqueId();

                PlayerData playerData = plugin.getPlayerStorage().getOrCreatePlayerData(playerUUID);

                for (Job job : playerData.getSelectedJobs().keySet()) {
                    List<JobObjective> jobObjectives = job.getObjectives();

                    for (JobObjective jobObjective : jobObjectives) {
                        JobObjectiveType type = jobObjective.getType();

                        if (type != objective.getType()) {
                            continue;
                        }

                        jobObjective.handle(job).accept(event);
                    }
                }

            }, plugin);

            return List.of(objective);
        });
    }

    public Map<Class<? extends Event>, List<JobObjective<?>>> getObjectives() {
        return objectives;
    }
}
