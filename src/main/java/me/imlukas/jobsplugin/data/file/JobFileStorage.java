package me.imlukas.jobsplugin.data.file;

import lombok.Getter;
import me.imlukas.jobsplugin.JobsPlugin;
import me.imlukas.jobsplugin.data.storage.JobStorage;
import me.imlukas.jobsplugin.jobs.Job;
import me.imlukas.jobsplugin.jobs.filters.FilterType;
import me.imlukas.jobsplugin.jobs.objectives.JobObjective;
import me.imlukas.jobsplugin.jobs.objectives.impl.BlockBreakObjective;
import me.imlukas.jobsplugin.jobs.objectives.impl.BlockPlaceObjective;
import me.imlukas.jobsplugin.jobs.objectives.impl.KillMobObjective;
import me.imlukas.jobsplugin.utils.storage.YMLBase;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

@Getter

public class JobFileStorage extends YMLBase {


    private final JobsPlugin plugin;
    private final JobStorage jobStorage;

    public JobFileStorage(JobsPlugin plugin) {
        super(plugin, "jobs.yml");
        this.plugin = plugin;
        this.jobStorage = plugin.getJobStorage();
    }

    /**
     * Loads the jobs from the jobs.yml file
     */
    public void load() {
        FileConfiguration config = getConfiguration();
        ConfigurationSection jobsSection = config.getConfigurationSection("jobs");


        for (String key : jobsSection.getKeys(false)) {
            ConfigurationSection objectivesSection = jobsSection.getConfigurationSection(key + ".objectives");
            Job job = Job.builder()
                    .name(key)
                    .description(jobsSection.getString(key + ".description"))
                    .prefix(jobsSection.getString(key + ".prefix"))
                    .maxLevel(jobsSection.getInt(key + ".max-level"))
                    .objectives(parseObjectives(objectivesSection))
                    .build();

            jobStorage.addJob(job);
        }

    }
    /**
     * Parses the objectives and the filters of the respective objectives
     * @param objectivesSection The configuration section of the objectives
     * @return A list of job objectives with all the filters
     */
    public List<JobObjective> parseObjectives(ConfigurationSection objectivesSection) {
        List<JobObjective> jobObjectives = new ArrayList<>();

        for (String objectiveType : objectivesSection.getKeys(false)) { // break_blocks, block_place, mob_kill
            JobObjective jobObjective = getObjectiveByConfigName(objectiveType); // BlockBreakObjective

            if (jobObjective == null) {
                continue;
            }

            List<String> filters = objectivesSection.getStringList(objectiveType); // [STONE, DIAMOND_ORE]

            if (!filters.isEmpty()) {
                FilterType filterType = jobObjective.getFilterType(); // Material
                FilterType configFilterType = getFilterType(filters.get(0)); // Material

                if (filterType.equals(configFilterType)) {
                    List<Object> toFilter = handleFilters(filters, filterType);

                    if (toFilter.isEmpty()) {
                        continue;
                    }

                    jobObjective.getFilter().setFilter(toFilter);
                }
            }
            jobObjectives.add(jobObjective);
        }

        return jobObjectives;
    }

    /**
     * Handles the filters and returns a list of the respective objects to filter
     * @param filters The list of filters
     * @param type the filter type of the objective
     * @return A list of the respective objects to filter
     */
    public List<Object> handleFilters(List<String> filters, FilterType type) {
        List<Object> filterList = new ArrayList<>();

        for (String filter : filters) {
            if (type.equals(FilterType.MATERIAL)) {
                filterList.add(Material.getMaterial(filter));
            } else if (type.equals(FilterType.ENTITY)) {
                filterList.add(EntityType.valueOf(filter));
            }
        }

        return filterList;
    }

    /**
     * Gets the filter type based on a string
     * this method is used to check if the filter type of the objective is the same as the one in the config
     * @param firstFilter The first filter of the objective
     * @return The filter type of the objects written in the config
     */

    public FilterType getFilterType(String firstFilter) {
        if (Material.getMaterial(firstFilter) != null) {
            return FilterType.MATERIAL;
        }

        try {
            EntityType.valueOf(firstFilter);
            return FilterType.ENTITY;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public JobObjective getObjectiveByConfigName(String name) {
        return switch (name) {
            case "break_blocks" -> new BlockBreakObjective(plugin);
            case "place_blocks" -> new BlockPlaceObjective(plugin);
            case "kill_mobs" -> new KillMobObjective(plugin);
            default -> null;
        };
    }


}
