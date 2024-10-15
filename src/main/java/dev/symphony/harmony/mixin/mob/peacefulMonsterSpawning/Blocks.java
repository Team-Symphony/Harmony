package dev.symphony.harmony.mixin.mob.peacefulMonsterSpawning;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.block.WitherSkullBlock;
import net.minecraft.block.entity.SculkShriekerBlockEntity;
import net.minecraft.block.spawner.TrialSpawnerLogic;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static dev.symphony.harmony.util.PeacefulMonsterSpawningUtils.*;

public class Blocks {

    @Mixin(NetherPortalBlock.class)
    public static class NetherPortalBlockMixin {

        @ModifyExpressionValue(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Difficulty;getId()I"))
        private int capId(int original) {
            return capAbovePeacefulId(original);
        }

    }

    @Mixin(SculkShriekerBlockEntity.class)
    public static class SculkShriekerBlockEntityMixin {

        @WrapOperation(method = "canWarn", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;getDifficulty()Lnet/minecraft/world/Difficulty;"))
        private Difficulty allowPeacefulSpawns(ServerWorld instance, Operation<Difficulty> original) {
            return getEasyIfOptionEnabled(instance, original);
        }

    }

    @Mixin(TrialSpawnerLogic.class)
    public static class TrialSpawnerLogicMixin {

        @ModifyExpressionValue(method = "canActivate", at = @At(value = "FIELD", target = "Lnet/minecraft/world/Difficulty;PEACEFUL:Lnet/minecraft/world/Difficulty;"))
        private Difficulty allowPeacefulSpawns(Difficulty original) {
            return getEasyIfOptionEnabled(original);
        }

    }

    @Mixin(WitherSkullBlock.class)
    public static class WitherSkullBlockMixin {

        @ModifyExpressionValue(method = { "onPlaced(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/SkullBlockEntity;)V", "canDispense" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getDifficulty()Lnet/minecraft/world/Difficulty;"))
        private static Difficulty bypassPeacefulDespawnCheck(Difficulty original) {
            return getEasyIfOptionEnabled(original);
        }

    }

}
