package me.imlukas.jobsplugin.data.sql;

public class SQLQueries {

    public static final String CREATE_JOB_TABLE = "CREATE TABLE IF NOT EXISTS jobs(id int AUTO_INCREMENT," +
            " userid int, jobName text, xp double, jobid int , PRIMARY KEY(id), FOREIGN KEY (userid) REFERENCES users(id), FOREIGN KEY (jobid) REFERENCES jobNames(id))";
    public static final String CREATE_JOB_NAMES_TABLE = "CREATE TABLE IF NOT EXISTS jobNames(id int AUTO_INCREMENT, jobName text, PRIMARY KEY(id))";
    public static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS users(id INT AUTO_INCREMENT, player_uuid VARCHAR(36) UNIQUE NOT NULL, username TEXT, PRIMARY KEY (id))";

    public static final String GET_GENERAL = "SELECT ? FROM ? WHERE ? = ?;";
    public static final String GET_JOB_XP = "SELECT xp FROM jobs WHERE userid = ? AND jobid = ?;";
    public static final String GET_JOB = "SELECT jobName FROM jobs WHERE userid = ? AND jobid = ?;";
    public static final String GET_SELECTED_JOBS = "SELECT jobs.jobName, jobs.xp, jobs.jobid FROM jobs INNER JOIN users ON jobs.userid = users.id WHERE users.player_uuid = ?;";

    public static final String INSERT_JOB = "INSERT INTO jobs(userid, jobname, xp, jobid) values (?, (SELECT jobNames.name FROM jobNames where jobNames.id = ?), ?, ?);";
    public static final String INSERT_JOB_NAME = "INSERT INTO jobNames(jobName) values (?);";
    public static final String INSERT_USER = "INSERT INTO users(player_uuid, username) values (?, ?);";

}
