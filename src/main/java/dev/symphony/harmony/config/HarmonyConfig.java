package dev.symphony.harmony.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class HarmonyConfig extends MidnightConfig{

    public static final String FOOD = "Food";

    @MidnightConfig.Entry(category = FOOD) public static boolean StewStackBool = true;
    @MidnightConfig.Entry(category = FOOD) public static int StewStackSize = 16;

}
