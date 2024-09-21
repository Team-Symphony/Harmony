package dev.symphony.harmony.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class HarmonyConfig extends MidnightConfig{

    public static final String FOOD = "Food";

    @MidnightConfig.Entry(category = FOOD) public static boolean StewStack = true;

}
