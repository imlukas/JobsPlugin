package me.imlukas.jobsplugin.utils.collection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class WatchableMap<K, V> implements Map<K, V> {

    private final Map<K, V> internalMap;
    private final BiConsumer<K, V> updateTask;

    public WatchableMap(Map<K, V> internalMap, BiConsumer<K, V> updateTask) {
        this.internalMap = internalMap;
        this.updateTask = updateTask;
    }


    @Override
    public int size() {
        return internalMap.size();
    }

    @Override
    public boolean isEmpty() {
        return internalMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object o) {
        return internalMap.containsKey(o);
    }

    @Override
    public boolean containsValue(Object o) {
        return internalMap.containsValue(o);
    }

    @Override
    public V get(Object o) {
        return internalMap.get(o);
    }

    @Nullable
    @Override
    public V put(K k, V v) {
        updateTask.accept(k, v);
        return internalMap.put(k, v);
    }

    @Override
    public V remove(Object o) {
        updateTask.accept((K) o, null);
        return internalMap.remove(o);
    }

    @Override
    public void putAll(@NotNull Map<? extends K, ? extends V> map) {
        map.forEach(updateTask);
        internalMap.putAll(map);
    }

    @Override
    public void clear() {
        internalMap.forEach((k, v) -> updateTask.accept(k, null));
        internalMap.clear();
    }

    @NotNull
    @Override
    public Set<K> keySet() {
        return internalMap.keySet();
    }

    @NotNull
    @Override
    public Collection<V> values() {
        return internalMap.values();
    }

    @NotNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        return internalMap.entrySet();
    }
}
