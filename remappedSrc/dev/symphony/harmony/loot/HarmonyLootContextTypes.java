package dev.symphony.harmony.loot;

import dev.symphony.harmony.Harmony;
import dev.symphony.harmony.mixin.accessor.LootContextTypesAccessor;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.Identifier;
import net.minecraft.util.context.ContextType;

public class HarmonyLootContextTypes {

    public static final ContextType CONVERSION = register("conversion", ContextType.create()
        .require(LootContextParameters.ORIGIN)
        .allow(LootContextParameters.THIS_ENTITY)
        .build()
    );

    @SuppressWarnings("UnreachableCode")
    private static ContextType register(String name, ContextType type) {
        Identifier id = Harmony.id(name);
        LootContextTypesAccessor.getMap().put(id, type);

        return type;
    }

    public static void noop() {}

}
