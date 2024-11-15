package dev.symphony.harmony.mixin.redstone;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.symphony.harmony.config.HarmonyConfig;
import net.minecraft.block.Block;
import net.minecraft.block.RedstoneLampBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


// FEATURE: Removed Redstone Lamp delay
// AUTHORS: Randomvideos
@Mixin(RedstoneLampBlock.class)
public class RemoveRedstoneLampDelay extends Block  {

    public RemoveRedstoneLampDelay(Settings settings) {super(settings);}

    //Replaced calling scheduleBlockTick with calling scheduledTick
    @WrapOperation(method = "neighborUpdate",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V"))
    protected void addDelay(World instance, BlockPos blockPos, Block block, int i, Operation<Void> original) {
        if(!HarmonyConfig.removeRedstoneLampDelay) {
            original.call(instance, blockPos, block, i);
            return;
        }
        scheduledTick(instance.getBlockState(blockPos), (ServerWorld) instance, blockPos, instance.getRandom());
    }
}

