package dev.symphony.harmony.config;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.*;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;


public class HarmonyConfig {

    // Transportation
    public static final String TRANSPORTATION = "transportation";
    @SerialEntry @TickBox @CustomImage("textures/images/exit_vehicle_on_damage.webp") @AutoGen(category = TRANSPORTATION)
    public static boolean exitVehicleOnDamage = true;
    @SerialEntry @TickBox @AutoGen(category = TRANSPORTATION, group = "vehicleleaves")
    public static boolean vehiclesMoveThroughLeaves = true;
    @SerialEntry @FloatSlider(min = 0f, max = 1f, step = 0.1f) @AutoGen(category = TRANSPORTATION, group = "vehicleleaves")
    public static float leafSpeedFactor = 0.85f;
    @SerialEntry @TickBox @CustomImage("textures/images/saddle_recipe.webp") @AutoGen(category = TRANSPORTATION)
    @HarmonyConfigCondition.ResourceConfigName(config_name = "recipe/saddle") public static boolean saddleRecipe = true;

    // Food
    public static final String FOOD = "food";
    @SerialEntry @IntSlider(min = 1, max = 64, step = 1) @CustomImage("textures/images/stew_stack_size.webp") @AutoGen(category = FOOD)
    public static int stewStackSize = 16;

    // Building
    public static final String BUILDING = "building";
    @SerialEntry @MasterTickBox(value = {"armorStandSticks"}) @CustomImage(value = "textures/images/armor_stand_arms.webp") @AutoGen(category = BUILDING, group = "armorstand")
    public static boolean armorStandStickArms = true;
    @SerialEntry @IntSlider(min = 0, max = 64, step = 1) @CustomImage(value = "textures/images/armor_stand_arms.webp") @AutoGen(category = BUILDING,  group = "armorstand")
    public static int armorStandSticks = 1;



    public static void initialize() {
        HANDLER.load();
    }
    public static YetAnotherConfigLib getConfig() {
        return HANDLER.generateGui();
    }

    public static ConfigClassHandler<HarmonyConfig> HANDLER = ConfigClassHandler.createBuilder(HarmonyConfig.class)
            .id(Identifier.of("harmony", "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("harmony.json5"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(true)
                    .build())
            .build();


}
