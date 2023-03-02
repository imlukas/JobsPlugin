package me.imlukas.jobsplugin.utils.concurrent;

import me.imlukas.jobsplugin.utils.collection.TypedMap;

import java.util.Map;

public class ConcurrentHelper {

    public static FancyFuture<TypedMap<String>> getMap(Map<String, FancyFuture<?>> futures) {
        TypedMap<String> map = new TypedMap<>();

        for(Map.Entry<String, FancyFuture<?>> entry : futures.entrySet()) {
            String key = entry.getKey();
            FancyFuture<?> future = entry.getValue();

            future.thenAccept(value -> map.put(key, value));
        }

        return FancyFuture.all(futures.values().toArray(new FancyFuture[0])).thenApply(v -> map);
    }

}
