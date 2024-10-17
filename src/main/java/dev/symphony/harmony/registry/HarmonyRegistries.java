package dev.symphony.harmony.registry;

import dev.symphony.harmony.Harmony;
import dev.symphony.harmony.entity.equipment.Equipment;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class HarmonyRegistries {

//    public static final DefaultedRegistry<Equipment> EQUIPMENT =
//        FabricRegistryBuilder.createDefaulted(
//            HarmonyRegistryKeys.EQUIPMENT,
//            Harmony.id("none")
//        ).buildAndRegister();
    public static final RegistryKey<Registry<Equipment>> EQUIPMENT_KEY = RegistryKey.ofRegistry(Harmony.id("equipment"));

    public static void init() {
        DynamicRegistries.registerSynced(EQUIPMENT_KEY, Equipment.CODEC);
    }

}
