package me.imlukas.jobsplugin.utils.collection;

import java.util.concurrent.ConcurrentHashMap;

public class TypedMap<K> extends ConcurrentHashMap<K, Object> {

    public <T> T getTyped(K key) {
        return (T) get(key);
    }
}

