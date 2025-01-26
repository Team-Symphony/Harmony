package dev.symphony.harmony.mixin.mobs.husks_drop_sand;

import dev.symphony.harmony.Harmony;
import dev.symphony.harmony.loot.HarmonyLootContextTypes;
import dev.symphony.harmony.loot.HarmonyLootTables;
import net.minecraft.entity.mob.HuskEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.registry.ReloadableRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HuskEntity.class)
public class HusksDropSandOnConversion extends ZombieEntity {

    public HusksDropSandOnConversion(World world) {
        super(world);
    }

    @Inject(method = "convertInWater", at = @At("HEAD"))
    private void dropLootOnConvert(CallbackInfo info) {
        if (!Harmony.CONFIG.husksDropSandOnConvert() || !(this.getWorld() instanceof ServerWorld serverWorld))
            return;

        GameRules gameRules = serverWorld.getGameRules();

        if (gameRules.getBoolean(GameRules.DO_MOB_LOOT))
            this.dropLoot(serverWorld);
    }

    @Unique
    private void dropLoot(ServerWorld world) {
        MinecraftServer server = world.getServer();
        ReloadableRegistries.Lookup registries = server.getReloadableRegistries();

        LootTable lootTable = registries.getLootTable(HarmonyLootTables.HUSK_CONVERSION);

        LootWorldContext lootContextParameterSet = new LootWorldContext.Builder(world)
            .add(LootContextParameters.ORIGIN, this.getPos())
            .add(LootContextParameters.THIS_ENTITY, this)
            .build(HarmonyLootContextTypes.CONVERSION);

        for (ItemStack itemStack : lootTable.generateLoot(lootContextParameterSet))
            this.dropStack(world, itemStack);
    }

}