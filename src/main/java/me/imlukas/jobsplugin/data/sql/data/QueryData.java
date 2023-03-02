package me.imlukas.jobsplugin.data.sql.data;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class QueryData extends HashMap<String, Object> {

    private final String name;

    public QueryData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public <T> T getTyped(String key) {
        return (T) get(key);
    }
}
