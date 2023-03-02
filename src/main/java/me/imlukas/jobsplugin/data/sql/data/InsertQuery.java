package me.imlukas.jobsplugin.data.sql.data;

import me.imlukas.jobsplugin.data.sql.SQLQueries;

public enum InsertQuery {

    USERS(SQLQueries.INSERT_USER),
    JOBS(SQLQueries.INSERT_JOB),
    JOB_NAME(SQLQueries.INSERT_JOB_NAME);

    public String query;

    InsertQuery(String name) {
        this.query = name;
    }
}
