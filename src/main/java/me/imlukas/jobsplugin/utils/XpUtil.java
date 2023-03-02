package me.imlukas.jobsplugin.utils;

public class XpUtil {
    
    private static final float EXPONENT = 0.2f;

    /**
     * Gets the level based on the xp.
     * @param xp the xp to get the level from.
     * @return current level of the user, based on their XP.
     */
    public static int getLevelFromXp(double xp) {

        return (int) (EXPONENT * Math.sqrt(xp));
    }

    /**
     * Calculates the amount of xp needed to reach the next level based on your current xp.
     *
     * @param xp the xp that the user has.
     * @return the amount of xp needed to reach the next level.
     */
    public static double getXpNeededForNextLevel(double xp) {
        return getXpToLevel(getLevelFromXp(xp) + 1) - (int) xp;
    }

    /**
     * Calculates the amount of xp needed to reach a specific level based on the xp the user has.
     *
     * @param xp the xp that the user has.
     * @param level the specified level to know the xp for.
     * @return the amount of xp needed to reach specified level.
     */
    public static double getXpNeededForNextLevel(double xp, int level) {
        return getXpToLevel(level) - (int) xp;
    }


    /**
     * Get the xp needed to reach a certain level.
     *
     * @param level the level you want to know the xp for.
     * @return the xp needed to reach the level.
     */
    public static double getXpToLevel(double level) {
        return Math.floor(Math.pow((level / EXPONENT ), 2.35));
    }


}
