package me.imlukas.jobsplugin.jobs.filters;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;


/**
 * This class is used to filter jobs by materials or entities
 * @author Lukas
 */
public class JobFilter extends ArrayList<Object> {

    private List<Object> toFilter = new ArrayList<>();

    public JobFilter() {}

    public JobFilter(List<Object> filter) {
        this.toFilter.addAll(filter);
    }

    public JobFilter(Object... filter) {
        this.toFilter.addAll(List.of(filter));
    }

    public void setFilter(List<Object> filter) {
        this.toFilter = filter;
    }

    public void add(Object... filter) {
        this.toFilter.add(filter);
    }

    public void add(List<Object> filter) {
        this.toFilter.addAll(filter);
    }


    /**
     * Returns true if this list contains the specified element.
     * @param o element whose presence in this list is to be tested
     * @return true if this list contains the specified element
     */
    @Override
    public boolean contains(Object o) {
        for (Object object : toFilter) {
            if ((object instanceof Material material)) {
                if (material.equals(o)) {
                    return true;
                }
            }

            if ((object instanceof EntityType entityType)) {
                if (entityType.equals(o)) {
                    return true;
                }
            }

        }
        return false;
    }


    public boolean check(Object object) {
        if (toFilter.isEmpty()) {
            return true;
        }

        return contains(object);
    }

    public List<Object> getToFilter() {
        return toFilter;
    }
}
