package me.imlukas.jobsplugin.data.sql;

import me.imlukas.jobsplugin.JobsPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;


public class SQLSetup {
    private static final String[] TABLES = {
            SQLQueries.CREATE_JOB_TABLE
    };

    private final JobsPlugin plugin;

    private final Logger log = Logger.getLogger("SQL");
    private Connection connection;

    public SQLSetup(JobsPlugin plugin) {
        this.plugin = plugin;

        // openConnection();
    }

    public boolean openConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                return true;
            }
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:" + plugin.getDataFolder() +  "/jobs.db";
            connection = DriverManager.getConnection(url);

            connection.setAutoCommit(true);
            System.out.println("[Jobs] Connected to SQLite.");
        } catch (Exception e) {
            log.info(e.toString());
            System.out.println("[Jobs] Failed to connect to SQLite.");
            return false;
        }
        return true;
    }

    public Connection get() {
        if (!isValid(connection)) {
            openConnection();
        }
        return connection;
    }

    private boolean isValid(Connection connection) {
        try {
            connection.prepareStatement("SELECT 1").executeQuery();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public void createTables() {
        CompletableFuture.runAsync(() -> {
            try {
                for (String query : TABLES) {
                    connection.createStatement().execute(query);
                }
            } catch (Exception e) {
                log.info(e.toString());
            }
        });
    }


}
