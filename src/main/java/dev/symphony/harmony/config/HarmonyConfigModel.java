package dev.symphony.harmony.config;

import dev.symphony.harmony.Harmony;
import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.RangeConstraint;
import io.wispforest.owo.config.annotation.SectionHeader;


import java.util.concurrent.TimeUnit;


@Modmenu(modId = Harmony.MOD_ID)
@Config(name = "harmony", wrapperName = "HarmonyConfig")
public class HarmonyConfigModel {

    // Transportation 🏳️‍⚧️
    @SectionHeader("Transportation")
    public boolean liquidsDeactivateElytra = true;
    public boolean exitVehicleOnDamage = true;
    @HarmonyConfigCondition.ResourceConfigName(config_name = "recipe/saddle")  public boolean saddleRecipe = true;
    public boolean horseArmorPreventsBucking = true;
    public boolean enderPearlsTeleportVehicles = true;
    public boolean enderPearlsDamageVehicles = true;
    public float furnaceMinecartSpeed = 1f;
    public float furnaceMinecartSpeedInWater = 1f;
    @RangeConstraint(min = 0f, max = 1f)
    public float riptideAccelerationOnWater = 0.1f;
    public boolean riptideCooldown = true;
    public int riptideTimeMultiplier = 5;
    public boolean reduceRiptideWaterDrag = true;

    // Food
    @SectionHeader("Food")
    public int stewStackSize = 16;
    public int glowBerryEffect = 10;

    // Building
    @SectionHeader("Building")
    public boolean armorStandStickArms = true;
    public int armorStandSticks = 1;

    // Redstone
    @SectionHeader("Redstone")
    public boolean oneTickCopperBulbDelay = true;
    public boolean removeRedstoneLampDelay = true;

    // Potions
    @SectionHeader("Potions")
    public boolean beaconsAffectTamedMobs = true;

    // Combat
    @SectionHeader("Combat")
    public boolean tridentsReturnFromVoid = true;
    public boolean changeItemDespawnTime = true;
    private static final int ONE_HOUR = 60 * 60;

    @RangeConstraint(min = 0, max = ONE_HOUR)
    public int
        itemDespawnTimeEasy = (int) TimeUnit.MINUTES.toSeconds(20),
        itemDespawnTimeNormal = (int) TimeUnit.MINUTES.toSeconds(10),
        itemDespawnTimeHard = (int) TimeUnit.MINUTES.toSeconds(5);


    // Mobs
    @SectionHeader("Mobs")
    public boolean wolvesGrowlAtMonsters = true;
    public boolean mismatchedMobArmor = true;
    public boolean permissiveParrotPerching = true;
    public boolean husksDropSandOnConvert = true;
}

