package dev.symphony.harmony.loot;

import dev.symphony.harmony.Harmony;
import dev.symphony.harmony.mixin.accessor.LootContextTypesAccessor;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.Identifier;
import net.minecraft.util.context.ContextParameter;
import net.minecraft.util.context.ContextType;

public class HarmonyLootContextTypes {

    public static final ContextType CONVERSION = register("conversion",
            LootContextParameters.ORIGIN,
            LootContextParameters.THIS_ENTITY
    );

    @SuppressWarnings("UnreachableCode")
    private static ContextType register(String conversion, ContextParameter<?> require, ContextParameter<?> allow) {

        ContextType type = new ContextType.Builder().require(require).allow(allow).build();
        LootContextTypesAccessor.getMap().put(Harmony.id(conversion), type);

        return type;
    }

    public static void noop() {}

}
