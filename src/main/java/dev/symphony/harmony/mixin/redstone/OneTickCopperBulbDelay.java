package dev.symphony.harmony.mixin.redstone;

import dev.symphony.harmony.config.HarmonyConfig;
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

// FEATURE: Added back 1 tick Copper Bulb delay
// AUTHORS: Randomvideos
@Mixin(BulbBlock.class)
public class OneTickCopperBulbDelay extends Block {
    @Shadow
    public void update(BlockState state, ServerWorld world, BlockPos pos) {}

    public OneTickCopperBulbDelay(Settings settings) {super(settings);}

    //Replaced calling update with calling scheduleBlockTick
    @Inject(method = "neighborUpdate",at = @At("HEAD"), cancellable = true)
    protected void addDelay(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify, CallbackInfo ci) {
        if(!HarmonyConfig.oneTickCopperBulbDelay)
            return;
        if (!world.isClient) {
            world.scheduleBlockTick(pos, this, 1);
        }
        ci.cancel();
    }

    //Calling update
    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world instanceof ServerWorld) {
            this.update(state, world, pos);
        }
    }

}
