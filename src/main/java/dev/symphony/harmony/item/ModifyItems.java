package dev.symphony.harmony.item;

import dev.symphony.harmony.Harmony;
import dev.symphony.harmony.config.HarmonyConfig;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ModifyItems {

    public static void init() {
        // Change stack sizes
        if (HarmonyConfig.stewStackSize != 1) {
            DefaultItemComponentEvents.MODIFY.register(context -> {
                // Ok so, can't use an item tag since tags only exist
                // on world load, while this is run on mod init
                context.modify(Items.MUSHROOM_STEW, builder ->
                    builder.add(DataComponentTypes.MAX_STACK_SIZE, HarmonyConfig.stewStackSize)
                );
                context.modify(Items.RABBIT_STEW, builder ->
                        builder.add(DataComponentTypes.MAX_STACK_SIZE, HarmonyConfig.stewStackSize)
                );
                context.modify(Items.BEETROOT_SOUP, builder ->
                        builder.add(DataComponentTypes.MAX_STACK_SIZE, HarmonyConfig.stewStackSize)
                );
                context.modify(Items.SUSPICIOUS_STEW, builder ->
                        builder.add(DataComponentTypes.MAX_STACK_SIZE, HarmonyConfig.stewStackSize)
                );
            });
        }
    }

}
