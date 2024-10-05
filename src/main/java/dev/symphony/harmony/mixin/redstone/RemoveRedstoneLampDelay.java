package dev.symphony.harmony.mixin.redstone;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneLampBlock;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// FEATURE: Removed Redstone Lamp delay
// AUTHORS: Randomvideos
@Mixin(RedstoneLampBlock.class)
public abstract class RemoveRedstoneLampDelay extends Block {

    @Final @Shadow
    public static BooleanProperty LIT;

    public RemoveRedstoneLampDelay(Settings settings) {super(settings);}

    //Set the delay to 0 in scheduleBlockTick
    @Inject(method = "neighborUpdate",at = @At("HEAD"), cancellable = true)
    protected void zeroDelay(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify, CallbackInfo ci) {
        if (!world.isClient) {
            boolean bl = state.get(LIT);
            if (bl != world.isReceivingRedstonePower(pos)) {
                if (bl) {
                    world.scheduleBlockTick(pos, this, 0);
                } else {
                    world.setBlockState(pos, state.cycle(LIT), Block.NOTIFY_LISTENERS);
                }
            }
        }
        ci.cancel();
    }

}
