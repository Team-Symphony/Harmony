package dev.symphony.harmony.mixin.accessor;

import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LootTables.class)
public interface LootTablesAccessor {

    @Invoker("registerLootTable")
    static RegistryKey<LootTable> invokeRegisterLootTable(RegistryKey<LootTable> key) {
        throw new AssertionError();
    }

}
