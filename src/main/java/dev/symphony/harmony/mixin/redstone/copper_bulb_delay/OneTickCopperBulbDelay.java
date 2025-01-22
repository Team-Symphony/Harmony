package dev.symphony.harmony.mixin.redstone.copper_bulb_delay;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.symphony.harmony.Harmony;
import dev.symphony.harmony.mixin.redstone.AbstractBlockImplMixin;
import net.minecraft.block.BlockState;
import net.minecraft.block.BulbBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BulbBlock.class)
public class OneTickCopperBulbDelay extends AbstractBlockImplMixin {

    @Shadow public void update(BlockState state, ServerWorld world, BlockPos pos) {}

    //Replaced calling update with calling scheduleBlockTick
    @WrapOperation(method = "neighborUpdate",at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BulbBlock;update(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;)V"))
    protected void addDelay(BulbBlock instance, BlockState state, ServerWorld world, BlockPos pos, Operation<Void> original) {
        if(!Harmony.CONFIG.oneTickCopperBulbDelay()) {
            original.call(instance, state, world, pos);
            return;
        }

        world.scheduleBlockTick(pos, instance, 1);
    }

    //Calling update
    @Override
    public void scheduledTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, Operation<Void> original) {
        this.update(state, world, pos);
        super.scheduledTickImpl(state, world, pos, random, original);
    }

}
