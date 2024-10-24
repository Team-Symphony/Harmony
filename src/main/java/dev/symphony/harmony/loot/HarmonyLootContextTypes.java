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

    @SuppressWarnings("UnreacahbleCode")
    private static ContextType register(String name, ContextParameter<?> required, ContextParameter<?> allowed) {
        Identifier id = Harmony.id(name);

        ContextType type = new ContextType.Builder().require(required).allow(allowed).build();
        LootContextTypesAccessor.getMap().put(id, type);

        return type;
    }

    public static void noop() {}

}
