package dev.symphony.harmony.loot;

import dev.symphony.harmony.Harmony;
import dev.symphony.harmony.mixin.accessor.LootContextTypesAccessor;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.util.Identifier;

public class HarmonyLootContextTypes {

    public static final LootContextType CONVERSION = register("conversion", LootContextType.create()
        .require(LootContextParameters.ORIGIN)
        .allow(LootContextParameters.THIS_ENTITY)
        .build()
    );

    @SuppressWarnings("UnreachableCode")
    private static LootContextType register(String name, LootContextType type) {
        Identifier id = Harmony.id(name);
        LootContextTypesAccessor.getMap().put(id, type);

        return type;
    }

    public static void noop() {}

}
