package dev.symphony.harmony.food;

import dev.symphony.harmony.config.HarmonyConfig;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Items;

public class StewStack {

    // Would be nice if this was applied to all items with a specific custom item tag to facilitate mod compat.
    public static void StewStackMethod(){
        if(HarmonyConfig.StewStackBool){

            DefaultItemComponentEvents.MODIFY.register(context -> {
                context.modify(Items.MUSHROOM_STEW, builder -> {
                    builder.add(DataComponentTypes.MAX_STACK_SIZE, HarmonyConfig.StewStackSize);
                });
            });

            DefaultItemComponentEvents.MODIFY.register(context -> {
                context.modify(Items.BEETROOT_SOUP, builder -> {
                    builder.add(DataComponentTypes.MAX_STACK_SIZE, HarmonyConfig.StewStackSize);
                });
            });

            DefaultItemComponentEvents.MODIFY.register(context -> {
                context.modify(Items.RABBIT_STEW, builder -> {
                    builder.add(DataComponentTypes.MAX_STACK_SIZE, HarmonyConfig.StewStackSize);
                });
            });

            DefaultItemComponentEvents.MODIFY.register(context -> {
                context.modify(Items.SUSPICIOUS_STEW, builder -> {
                    builder.add(DataComponentTypes.MAX_STACK_SIZE, HarmonyConfig.StewStackSize);
                });
            });
        }
    }
}