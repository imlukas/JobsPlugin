package me.imlukas.jobsplugin.data.sql.data;

import me.imlukas.jobsplugin.data.sql.SQLQueries;

public enum FetchQuery {

    GENERAL(SQLQueries.GET_GENERAL),
    JOB_XP(SQLQueries.GET_JOB_XP),
    SELECTED_JOBS(SQLQueries.GET_SELECTED_JOBS);

    public String query;

    FetchQuery(String name) {
        this.query = name;
    }
}
