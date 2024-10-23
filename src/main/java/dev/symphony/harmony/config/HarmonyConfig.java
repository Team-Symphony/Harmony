package dev.symphony.harmony.config;

import eu.midnightdust.lib.config.MidnightConfig;

import java.util.concurrent.TimeUnit;


public class HarmonyConfig extends MidnightConfig {
    @Comment(category = "harmony", centered = true) public static Comment reloadWarning;

    // Transportation 🏳️‍⚧️
    public static final String TRANS = "transportation";
    @Entry(category = TRANS) public static boolean exitVehicleOnDamage = true;
    @HarmonyConfigCondition.ResourceConfigName(config_name = "recipe/saddle") @Entry(category = TRANS) public static boolean saddleRecipe = true;
    @Entry(category = TRANS) public static boolean horseArmorPreventsBucking = true;

    // Food
    public static final String FOOD = "food";
    @Entry(category = FOOD) public static int stewStackSize = 16;
    @Entry(category = FOOD) public static int glowBerryEffect = 10;

    // Building
    public static final String BUILD = "building";
    @Entry(category = BUILD) public static boolean armorStandStickArms = true;
    @Entry(category = BUILD) public static int armorStandSticks = 1;

    // Redstone
    public static final String REDSTONE = "redstone";
    @Entry(category = REDSTONE) public static boolean oneTickCopperBulbDelay = true;

    // Potions
    public static final String POTIONS = "potions";
    @Entry(category = POTIONS) public static boolean beaconsAffectTamedMobs = true;

    // Combat
    public static final String COMBAT = "combat";
    @Entry(category = COMBAT) public static boolean changeItemDespawnTime = true;

    private static final int ONE_HOUR = 60 * 60;

    @Entry(category = COMBAT, isSlider = true, min = 0, max = ONE_HOUR) public static int
        itemDespawnTimeEasy = (int) TimeUnit.MINUTES.toSeconds(20),
        itemDespawnTimeNormal = (int) TimeUnit.MINUTES.toSeconds(10),
        itemDespawnTimeHard = (int) TimeUnit.MINUTES.toSeconds(5);

    // Mobs
    public static final String MOBS = "mobs";
    @Entry(category = MOBS) public static boolean wolvesGrowlAtMonsters = true;
    @Entry(category = MOBS) public static boolean mismatchedMobArmor = true;
    @Entry(category = MOBS) public static boolean permissiveParrotPerching = true;
    @Entry(category = MOBS) public static boolean husksDropSandOnConvert = true;
}
