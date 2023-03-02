package me.imlukas.jobsplugin.data.sql;

import me.imlukas.jobsplugin.JobsPlugin;
import me.imlukas.jobsplugin.data.sql.data.FetchQuery;
import me.imlukas.jobsplugin.data.sql.data.QueryData;
import me.imlukas.jobsplugin.data.sql.data.InsertQuery;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SQLHandler {

    private final Connection connection;

    public SQLHandler(JobsPlugin plugin) {
        SQLSetup sqlSetup = plugin.getSqlSetup();
        this.connection = sqlSetup.get();
    }


    public void store(QueryData queryData, InsertQuery query) {
        String generalValue = queryData.getTyped("value"); // used when there's only one value to be stored
        CompletableFuture.runAsync(() -> {
            try {
                PreparedStatement statement = connection.prepareStatement(query.query);

                if (query == InsertQuery.USERS) {
                    String uuid = queryData.getTyped("uuid");
                    String username = queryData.getTyped("username");

                    statement.setString(1, uuid);
                    statement.setString(2, username);

                } else if (query == InsertQuery.JOBS) {
                    int userId = queryData.getTyped("userId");
                    int jobId = queryData.getTyped("jobId");
                    int xp = queryData.getTyped("xp");

                    statement.setInt(1, userId);
                    statement.setInt(2, jobId);
                    statement.setInt(3, xp);
                    statement.setInt(4, jobId);

                } else if (query == InsertQuery.JOB_NAME) {
                    statement.setString(1, generalValue);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).thenRun(() -> {
            System.out.println("Stored " + queryData.getName() + " in the database.");
        });
    }


    public CompletableFuture<QueryData> fetch(QueryData data, FetchQuery query) {
        QueryData fetchedData = new QueryData("fetchedData");

        return CompletableFuture.supplyAsync(() -> {
            try {
                PreparedStatement statement = connection.prepareStatement(query.query);

                if (query == FetchQuery.GENERAL) {

                } else if (query == FetchQuery.JOB_XP) {
                    int userid = data.getTyped("userid");
                    int jobid = data.getTyped("jobid");

                    statement.setInt(1, userid);
                    statement.setInt(2, jobid);

                } else if (query == FetchQuery.SELECTED_JOBS) {
                    int userid = data.getTyped("userid");

                    statement.setInt(1, userid);
                }

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                        fetchedData.put(resultSet.getMetaData().getColumnName(i), resultSet.getObject(i));
                    }
                }

                resultSet.close();

                return fetchedData;

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
