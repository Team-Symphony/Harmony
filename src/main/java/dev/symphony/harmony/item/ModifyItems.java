package dev.symphony.harmony.item;

import com.mojang.datafixers.util.Either;
import dev.symphony.harmony.Harmony;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.RepairableComponent;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.entry.RegistryEntryOwner;
import net.minecraft.registry.tag.TagKey;


public class ModifyItems {

    public static void init() {
        /**
         * FEATURE: Change stew/soup stack sizes.
         * @author Flatkat
         * @author Trigam
         */

        if (Harmony.CONFIG.stewStackSize() != 1) {
            DefaultItemComponentEvents.MODIFY.register(context -> {
                // Ok so, can't use an item tag since tags only exist
                // on world load, while this is run on mod init
                context.modify(Items.MUSHROOM_STEW, builder ->
                    builder.add(DataComponentTypes.MAX_STACK_SIZE, Harmony.CONFIG.stewStackSize())
                );
                context.modify(Items.RABBIT_STEW, builder ->
                    builder.add(DataComponentTypes.MAX_STACK_SIZE, Harmony.CONFIG.stewStackSize())
                );
                context.modify(Items.BEETROOT_SOUP, builder ->
                    builder.add(DataComponentTypes.MAX_STACK_SIZE, Harmony.CONFIG.stewStackSize())
                );
                context.modify(Items.SUSPICIOUS_STEW, builder ->
                    builder.add(DataComponentTypes.MAX_STACK_SIZE, Harmony.CONFIG.stewStackSize())
                );
            });
        }

        /**
         * FEATURE: Allow tridents to be repaired using prismarine shards
         * @author RandomVideos
         */

        if(Harmony.CONFIG.repairableTridents()) {
            DefaultItemComponentEvents.MODIFY.register(context -> {
                context.modify(Items.TRIDENT, builder ->
                        builder.add(DataComponentTypes.REPAIRABLE, new RepairableComponent(RegistryEntryList.of(Registries.ITEM.getEntry(Items.PRISMARINE_SHARD))))
                );
            });
        }
    }
}


