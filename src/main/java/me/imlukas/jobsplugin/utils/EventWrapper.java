package me.imlukas.jobsplugin.utils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerEvent;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class EventWrapper {

    private static final Set<Class<?>> nonGettableClasses = new HashSet<>();
    private static final Map<Class<?>, Method> eventMethods = new ConcurrentHashMap<>();

    public static Player getPlayer(Event event) {
        if(event instanceof PlayerEvent playerEvent)
            return playerEvent.getPlayer();

        if(event instanceof EntityEvent entityEvent) {
            Entity entity = entityEvent.getEntity();

            if(entity instanceof Player)
                return (Player) entity;
        }

        if(event instanceof InventoryInteractEvent inventoryInteractEvent) {
            Entity entity = inventoryInteractEvent.getWhoClicked();

            if(entity instanceof Player)
                return (Player) entity;
        }

        // REFLECTIONS
        Class<?> eventClass = event.getClass();

        if(nonGettableClasses.contains(eventClass))
            return null;

        Method method = eventMethods.get(eventClass);

        if(method == null) {
            try {
                method = eventClass.getMethod("getPlayer");
                eventMethods.put(eventClass, method);
            } catch(NoSuchMethodException e) {
                nonGettableClasses.add(eventClass);
            }
        }

        if(method == null)
            return null;

        try {
            return (Player) method.invoke(event);
        } catch(Exception e) {
            return null;
        }
    }
}