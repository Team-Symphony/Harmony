package dev.symphony.harmony.mixin.redstone;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BulbBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BulbBlock.class)
public class AddCopperBulbDelay extends Block {
    @Shadow
    public void update(BlockState state, ServerWorld world, BlockPos pos) {
    }

    public AddCopperBulbDelay(Settings settings) {
        super(settings);
    }

    @Inject(method = "neighborUpdate",at = @At("HEAD"), cancellable = true)
    protected void addDelayOnNeightborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify, CallbackInfo ci) {
        if (!world.isClient) {
            world.scheduleBlockTick(pos, this, 1);
        }
        ci.cancel();
    }

    @Inject(method = "onBlockAdded",at = @At("HEAD"), cancellable = true)
    protected void addDelayOnBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify, CallbackInfo ci) {
        if (oldState.getBlock() != state.getBlock() && world instanceof ServerWorld) {
            world.scheduleBlockTick(pos, this, 1);
        }
        ci.cancel();
    }


    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world instanceof ServerWorld) {
            this.update(state, world, pos);
        }
    }

}
