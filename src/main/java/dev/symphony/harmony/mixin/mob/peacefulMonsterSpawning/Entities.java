package dev.symphony.harmony.mixin.mob.peacefulMonsterSpawning;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.symphony.harmony.config.HarmonyConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static dev.symphony.harmony.util.PeacefulMonsterSpawningUtils.*;

public class Entities {

    @Mixin(targets = "net.minecraft.entity.effect.BadOmenStatusEffect")
    public static class BadOmenStatusEffectMixin {

        @WrapOperation(method = "applyUpdateEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;getDifficulty()Lnet/minecraft/world/Difficulty;"))
        private Difficulty allowPeacefulEffect(ServerWorld instance, Operation<Difficulty> original) {
            return getEasyIfOptionEnabled(instance, original);
        }

    }

    @Mixin(DrownedEntity.class)
    public static class DrownedEntityMixin {
        @WrapOperation(method = "canSpawn(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/random/Random;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/ServerWorldAccess;getDifficulty()Lnet/minecraft/world/Difficulty;"))
        private static Difficulty allowPeacefulSpawns(ServerWorldAccess instance, Operation<Difficulty> original) {
            return getEasyIfOptionEnabled(instance, original);
        }

    }

    @Mixin(GhastEntity.class)
    public static class GhastEntityMixin {

        @WrapOperation(method = "canSpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldAccess;getDifficulty()Lnet/minecraft/world/Difficulty;"))
        private static Difficulty allowPeacefulSpawns(WorldAccess instance, Operation<Difficulty> original) {
            return getEasyIfOptionEnabled(instance, original);
        }

    }

    @Mixin(GuardianEntity.class)
    public static class GuardianEntityMixin {

        @WrapOperation(method = "canSpawn(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/WorldAccess;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/random/Random;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldAccess;getDifficulty()Lnet/minecraft/world/Difficulty;"))
        private static Difficulty allowPeacefulSpawns(WorldAccess instance, Operation<Difficulty> original) {
            return getEasyIfOptionEnabled(instance, original);
        }

    }

    @Mixin(HostileEntity.class)
    public static class HostileEntityMixin {

        @WrapOperation(method = "canSpawnInDark", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/ServerWorldAccess;getDifficulty()Lnet/minecraft/world/Difficulty;"))
        private static Difficulty addSpawnConditionInDark(ServerWorldAccess instance, Operation<Difficulty> original) {
            return getEasyIfOptionEnabled(instance, original);
        }

        @WrapOperation(method = "canSpawnIgnoreLightLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldAccess;getDifficulty()Lnet/minecraft/world/Difficulty;"))
        private static Difficulty addSpawnConditionIgnoreLightLevel(WorldAccess instance, Operation<Difficulty> original) {
            return getEasyIfOptionEnabled(instance, original);
        }

        @ModifyReturnValue(method = "isAngryAt", at = @At("RETURN"))
        private boolean pacifyInPeaceful(boolean original, @Local(argsOnly = true) PlayerEntity player) {
            return HarmonyConfig.peacefulMonsterSpawning ? player.getWorld().getDifficulty() != Difficulty.PEACEFUL : original;
        }

    }

    @Mixin(MagmaCubeEntity.class)
    public static class MagmaCubeEntityMixin {

        @ModifyReturnValue(method = "canMagmaCubeSpawn", at = @At("RETURN"))
        private static boolean addSpawnCondition(boolean original, EntityType<MagmaCubeEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
            return original || HarmonyConfig.peacefulMonsterSpawning;
        }

    }

    @Mixin(MobEntity.class)
    public static class MobEntityMixin {

        @ModifyExpressionValue(method = "checkDespawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobEntity;isDisallowedInPeaceful()Z"))
        private boolean bypassPeacefulDespawnCheck(boolean original) {
            return !HarmonyConfig.peacefulMonsterSpawning;
        }

    }

    @Mixin({ PigEntity.class, VillagerEntity.class })
    public static class Pig_VillagerEntityMixin {

        @ModifyExpressionValue(method = "onStruckByLightning", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;getDifficulty()Lnet/minecraft/world/Difficulty;"))
        private Difficulty bypassPeacefulConversionCheck(Difficulty original) {
            return getEasyIfOptionEnabled(original);
        }

    }

    @Mixin(ShulkerBulletEntity.class)
    public static class ShulkerBulletEntityMixin {

        @ModifyExpressionValue(method = "checkDespawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getDifficulty()Lnet/minecraft/world/Difficulty;"))
        private Difficulty allowPeacefulSpawning(Difficulty original) {
            return getEasyIfOptionEnabled(original);
        }

    }

    @Mixin(SlimeEntity.class)
    public static class SlimeEntityMixin {

        @ModifyExpressionValue(method = "canSpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldAccess;getDifficulty()Lnet/minecraft/world/Difficulty;"))
        private static Difficulty allowPeacefulSpawning(Difficulty original) {
            return getEasyIfOptionEnabled(original);
        }

    }

    @Mixin(WitherEntity.class)
    public static class WitherEntityMixin {

        @ModifyExpressionValue(method = "checkDespawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/boss/WitherEntity;isDisallowedInPeaceful()Z"))
        private boolean bypassPeacefulDespawnCheck(boolean original) {
            return !HarmonyConfig.peacefulMonsterSpawning;
        }

    }

    @Mixin(ZombifiedPiglinEntity.class)
    public static class ZombifiedPiglinEntityMixin {

        @WrapOperation(method = "canSpawn(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/WorldAccess;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/random/Random;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldAccess;getDifficulty()Lnet/minecraft/world/Difficulty;"))
        private static Difficulty addSpawnCondition(WorldAccess instance, Operation<Difficulty> original) {
            return getEasyIfOptionEnabled(instance, original);
        }

    }
    
}
