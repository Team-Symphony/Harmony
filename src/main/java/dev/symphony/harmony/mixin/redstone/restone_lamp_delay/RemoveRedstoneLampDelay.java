package dev.symphony.harmony.mixin.redstone.restone_lamp_delay;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.symphony.harmony.Harmony;
import net.minecraft.block.Block;
import net.minecraft.block.RedstoneLampBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RedstoneLampBlock.class)
public class RemoveRedstoneLampDelay extends Block  {

    public RemoveRedstoneLampDelay(Settings settings) {super(settings);}

    //Replaced calling scheduleBlockTick with calling scheduledTick
    @WrapOperation(method = "neighborUpdate",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V"))
    protected void addDelay(World instance, BlockPos blockPos, Block block, int i, Operation<Void> original) {
        if(!Harmony.CONFIG.removeRedstoneLampDelay()) {
            original.call(instance, blockPos, block, i);
            return;
        }
        scheduledTick(instance.getBlockState(blockPos), (ServerWorld) instance, blockPos, instance.getRandom());
    }
}

