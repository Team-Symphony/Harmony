package dev.symphony.harmony.config;

import eu.midnightdust.lib.config.MidnightConfig;


public class HarmonyConfig extends MidnightConfig {
    @Comment(category = "harmony", centered = true) public static Comment reloadWarning;

    // Transportation
    public static final String TRANS = "transportation";
    @Entry(category = TRANS) public static boolean exitVehicleOnDamage = true;
    @Entry(category = TRANS) public static boolean vehiclesMoveThroughLeaves = true;
    @Entry(category = TRANS, isSlider = true, min = 0f, max = 1f) public static float leafSpeedFactor = 0.85f;

    // Food
    public static final String FOOD = "food";
    @Entry(category = FOOD) public static int stewStackSize = 16;

    // Recipe
    public static final String RECIPE = "recipe";
    @HarmonyConfigCondition.ResourceConfigName(config_name = "stick")
    @Entry(category = RECIPE) public static boolean enableStickRecipe = false;
    @HarmonyConfigCondition.ResourceConfigName(config_name = "stone_stick")
    @Entry(category = RECIPE) public static boolean enableStoneStickRecipe = true;
}
