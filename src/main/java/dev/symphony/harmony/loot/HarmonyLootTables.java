package dev.symphony.harmony.loot;

import dev.symphony.harmony.Harmony;
import dev.symphony.harmony.mixin.accessor.LootTablesAccessor;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class HarmonyLootTables {

    public static final RegistryKey<LootTable> HUSK_CONVERSION = register();

    private static RegistryKey<LootTable> register() {
        Identifier id = Harmony.id("conversion/husk");
        RegistryKey<LootTable> registryKey = RegistryKey.of(RegistryKeys.LOOT_TABLE, id);

        return LootTablesAccessor.invokeRegisterLootTable(registryKey);
    }

    public static void noop() {}

}
