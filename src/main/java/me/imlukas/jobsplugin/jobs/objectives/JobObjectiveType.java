package me.imlukas.jobsplugin.jobs.objectives;

import me.imlukas.jobsplugin.jobs.Job;
import me.imlukas.jobsplugin.jobs.objectives.impl.BlockBreakObjective;
import me.imlukas.jobsplugin.jobs.objectives.impl.BlockPlaceObjective;
import me.imlukas.jobsplugin.jobs.objectives.impl.KillMobObjective;

public enum JobObjectiveType {

    BLOCK_BREAK("break_blocks"),
    BLOCK_PLACE("place_blocks"),
    KILL_MOB("kill_mobs"),
    CRAFT_ITEM("craft_items"),
    SMELT_ITEM("smelt_items"),
    BREW_POTION("brew_potions"),
    ENCHANT_ITEM("enchant_items"),
    FISH("fish"),
    TAME_ANIMAL("tame_animals"),
    WALK_BLOCKS("walk_blocks");
    
    public final String configName;
    
    JobObjectiveType(String configName) {
        this.configName = configName;
    }
}
