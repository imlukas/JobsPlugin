package me.imlukas.jobsplugin.data.storage;

import me.imlukas.jobsplugin.jobs.Job;

import java.util.ArrayList;
import java.util.List;


public class JobStorage {

    private final List<Job> availableJobs = new ArrayList<>();


    public List<Job> getAvailableJobs() {
        return availableJobs;
    }

    public void addJob(Job job) {
        availableJobs.add(job);
    }

    public List<String> getJobNames() {
        List<String> jobNames = new ArrayList<>();

        for (Job job : availableJobs) {
            jobNames.add(job.getName());
        }
        return jobNames;
    }

    public Job getJobByName(String jobName) {

        for (Job job : availableJobs) {
            if (job.getName().equals(jobName)) {
                return job;
            }
        }
        return null;
    }
}
