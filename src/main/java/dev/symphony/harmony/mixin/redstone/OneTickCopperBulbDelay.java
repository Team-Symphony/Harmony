package dev.symphony.harmony.mixin.redstone;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.symphony.harmony.config.HarmonyConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BulbBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

// FEATURE: Added back 1 tick Copper Bulb delay
// AUTHORS: Randomvideos, axialeaa
@Mixin(BulbBlock.class)
public class OneTickCopperBulbDelay extends Block {
    @Shadow
    public void update(BlockState state, ServerWorld world, BlockPos pos) {}

    public OneTickCopperBulbDelay(Settings settings) {super(settings);}

    //Replaced calling update with calling scheduleBlockTick
    @WrapOperation(method = "neighborUpdate",at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BulbBlock;update(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;)V"))
    protected void addDelay(BulbBlock instance, BlockState state, ServerWorld world, BlockPos pos, Operation<Void> original) {
        if(!HarmonyConfig.oneTickCopperBulbDelay) {
            original.call(instance, state, world, pos);
            return;
        }

        world.scheduleBlockTick(pos, this, 1);
    }

    //Calling update
    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        this.update(state, world, pos);
    }

}
