package dev.symphony.harmony.mixin.mob.peacefulMonsterSpawning;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.symphony.harmony.config.HarmonyConfig;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.MinecraftServer;
import net.minecraft.village.raid.Raid;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static dev.symphony.harmony.util.PeacefulMonsterSpawningUtils.*;

public class World {

    @Mixin(LocalDifficulty.class)
    public static class LocalDifficultyMixin {
        
        @ModifyExpressionValue(method = "setLocalDifficulty", at = @At(value = "FIELD", target = "Lnet/minecraft/world/Difficulty;PEACEFUL:Lnet/minecraft/world/Difficulty;"))
        private Difficulty bypassPeacefulCheck(Difficulty original) {
            return getEasyIfOptionEnabled(original);
        }
        
        @ModifyExpressionValue(method = "setLocalDifficulty", at = @At(value = "FIELD", target = "Lnet/minecraft/world/Difficulty;EASY:Lnet/minecraft/world/Difficulty;"))
        private Difficulty modifyReassignment(Difficulty original, @Local(argsOnly = true) Difficulty difficulty) {
            return HarmonyConfig.peacefulMonsterSpawning && (difficulty == Difficulty.PEACEFUL || difficulty == Difficulty.EASY) ?
                difficulty : original;
        }

        @WrapOperation(method = "setLocalDifficulty", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Difficulty;getId()I"))
        private int capId(Difficulty instance, Operation<Integer> original) {
            return capAbovePeacefulId(original.call(instance));
        }

    }

    @Mixin(MinecraftServer.class)
    public static class MinecraftServerMixin {

        @ModifyReturnValue(method = "isMonsterSpawningEnabled", at = @At("RETURN"))
        private boolean monsterSpawningEnabledInPeaceful(boolean original) {
            return original || HarmonyConfig.peacefulMonsterSpawning;
        }

    }

    @Mixin(Raid.class)
    public static class RaidMixin {

        @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;getDifficulty()Lnet/minecraft/world/Difficulty;"))
        private Difficulty bypassPeacefulInvalidationCheck(Difficulty original) {
            return getEasyIfOptionEnabled(original);
        }

    }

    @Mixin(SpawnGroup.class)
    public static class SpawnGroupMixin {

        @ModifyReturnValue(method = "isPeaceful", at = @At("RETURN"))
        private boolean alwaysIsPeaceful(boolean original) {
            return original || HarmonyConfig.peacefulMonsterSpawning;
        }

    }
    
}
