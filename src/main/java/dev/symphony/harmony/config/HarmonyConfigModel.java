package dev.symphony.harmony.config;

import dev.symphony.harmony.Harmony;
import io.wispforest.owo.config.annotation.*;


import java.util.concurrent.TimeUnit;


@Modmenu(modId = Harmony.MOD_ID)
@Config(name = "harmony", wrapperName = "HarmonyConfig")
public class HarmonyConfigModel {

    // Transportation 🏳️‍⚧️
    @SectionHeader("Trans")
    public boolean exitVehicleOnDamage = true;

    @Nest @Expanded public TransElytraCat transElytraCat = new TransElytraCat();
    public static class TransElytraCat {
        public boolean liquidsDeactivateElytra = true;
    }

    @Nest @Expanded public TransMinecartCat transMinecartCat = new TransMinecartCat();
    public static class TransMinecartCat {
        public float furnaceMinecartSpeed = 1f;
        public float furnaceMinecartSpeedInWater = 1f;
    }

    @Nest @Expanded public TransSaddledCat transSaddledCat = new TransSaddledCat();
    public static class TransSaddledCat {
        @HarmonyConfigCondition.ResourceConfigName(config_name = "recipe/saddle")  public boolean saddleRecipe = true;
        public boolean horseArmorPreventsBucking = true;
        public boolean enderPearlsTeleportVehicles = true;
        public boolean enderPearlsDamageVehicles = true;
    }

    @Nest @Expanded public TransRiptideCat transRiptideCat = new TransRiptideCat();
    public static class TransRiptideCat {
        @RangeConstraint(min = 0f, max = 1f) public float riptideAccelerationOnWater = 0.1f;
        public boolean riptideCooldown = true;
        public int riptideTimeMultiplier = 5;
        public boolean reduceRiptideWaterDrag = true;
    }




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

    @Nest public ItemDespawnTimeCat itemDespawnTimeCat = new ItemDespawnTimeCat();
    public static class ItemDespawnTimeCat {
        private static final int ONE_HOUR = 60 * 60;
        @RangeConstraint(min = 0, max = ONE_HOUR) public int
                itemDespawnTimeEasy = (int) TimeUnit.MINUTES.toSeconds(20),
                itemDespawnTimeNormal = (int) TimeUnit.MINUTES.toSeconds(10),
                itemDespawnTimeHard = (int) TimeUnit.MINUTES.toSeconds(5);
    }




    // Mobs
    @SectionHeader("Mobs")
    public boolean husksDropSandOnConvert = true;

    @Nest @Expanded public MobsHostileCat mobsHostileCat = new MobsHostileCat();
    public static class MobsHostileCat {
        public boolean mismatchedMobArmor = true;
    }

    @Nest @Expanded public MobsPetsCat mobsPetsCat = new MobsPetsCat();
    public static class MobsPetsCat {
        public boolean permissiveParrotPerching = true;
        public boolean wolvesGrowlAtMonsters = true;
    }
}

