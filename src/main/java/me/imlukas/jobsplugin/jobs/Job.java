package me.imlukas.jobsplugin.jobs;

import lombok.Builder;
import lombok.Getter;
import me.imlukas.jobsplugin.jobs.objectives.JobObjective;

import java.util.List;
import java.util.UUID;

@Getter
public class Job {

    private String name, description, prefix;
    private List<JobObjective> objectives;
    private int maxLevel;
    private final UUID id;

    public Job() {
        this.id = UUID.randomUUID();
    }

    public Job(String name, String description, String prefix, List<JobObjective> objectives, int maxLevel) {
        this.name = name;
        this.description = description;
        this.prefix = prefix;
        this.objectives = objectives;
        this.maxLevel = maxLevel;
        this.id = UUID.randomUUID();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String name, description, prefix;
        private List<JobObjective> objectives;
        private int maxLevel;

        private Builder() {}

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public Builder objectives(List<JobObjective> objectives) {
            this.objectives = objectives;
            return this;
        }

        public Builder maxLevel(int maxLevel) {
            this.maxLevel = maxLevel;
            return this;
        }

        public Job build() {
            return new Job(name, description, prefix, objectives, maxLevel);
        }

    }
}
