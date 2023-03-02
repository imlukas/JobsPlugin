package me.imlukas.jobsplugin.jobs.objectives;

import me.imlukas.jobsplugin.jobs.Job;
import me.imlukas.jobsplugin.jobs.filters.FilterType;
import me.imlukas.jobsplugin.jobs.filters.JobFilter;
import org.bukkit.event.Event;

import java.util.List;
import java.util.function.Consumer;

public interface JobObjective<T extends Event> {

    JobObjectiveType getType();
    String getDisplayName();
    String getConfigName();
    String getDescription();
    FilterType getFilterType();
    JobFilter getFilter();
    Class<T> getEventClass();
    Consumer<T> handle(Job job);

    default void setFilter(List<Object> filter) {
        getFilter().setFilter(filter);
    }
}
