package dev.symphony.harmony.mixin.redstone;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;

// Extending + overriding super methods is not great for inter-mod compatibility.
// Luckily, it's easily resolvable by mixing into the super class, overriding the handler method from this class instead.
// Now the changes will stack, rather than crashing whenever another modder touches the same bit of code!
// AUTHOR: axialeaa
@Mixin(AbstractBlock.class)
public class AbstractBlockImplMixin {

    @WrapMethod(method = "scheduledTick")
    public void scheduledTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, Operation<Void> original) {
        original.call(state, world, pos, random);
    }

}
