package dev.symphony.harmony.config;

import eu.midnightdust.lib.config.MidnightConfig;


public class HarmonyConfig extends MidnightConfig {
    @Comment(category = "harmony", centered = true) public static Comment reloadWarning;

    // Transportation
    public static final String TRANS = "transportation";
    @Entry(category = TRANS) public static boolean exitVehicleOnDamage = true;
    @Entry(category = TRANS) public static boolean vehiclesMoveThroughLeaves = true;
    @Entry(category = TRANS, isSlider = true, min = 0f, max = 1f) public static float leafSpeedFactor = 0.85f;
    @HarmonyConfigCondition.ResourceConfigName(config_name = "recipe/saddle") @Entry(category = TRANS) public static boolean saddleRecipe = true;

    // Food
    public static final String FOOD = "food";
    @Entry(category = FOOD) public static int stewStackSize = 16;

    // Building
    public static final String BUILD = "building";
    @Entry(category = BUILD) public static boolean armorStandStickArms = true;
    @Entry(category = BUILD) public static int armorStandSticks = 1;

    // Redstone
    public static final String REDSTONE = "redstone";
    @Entry(category = REDSTONE) public static boolean oneTickCopperBulbDelay = true;
    @Entry(category = REDSTONE) public static int redstoneBulbDelay = 0;
}
