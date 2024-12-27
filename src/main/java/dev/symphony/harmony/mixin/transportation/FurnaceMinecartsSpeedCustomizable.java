package dev.symphony.harmony.mixin.transportation;

import dev.symphony.harmony.config.HarmonyConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.FurnaceMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * FEATURE: Made Furnace Minecart speeds customizable.
 * @author RandomVideos
 */
@Mixin(FurnaceMinecartEntity.class)
public class FurnaceMinecartsSpeedCustomizable extends AbstractMinecartEntity {
    protected FurnaceMinecartsSpeedCustomizable(EntityType<?> entityType, World world) {super(entityType, world);}

    @Inject(method = "getMaxSpeed",at= @At("RETURN"), cancellable = true)
    void ChangeFurnaceMinecartSpeed(ServerWorld world, CallbackInfoReturnable<Double> cir){
        if(super.isTouchingWater())
            cir.setReturnValue(super.getMaxSpeed(world) * HarmonyConfig.furnaceMinecartSpeedInWater);
        else
            cir.setReturnValue(super.getMaxSpeed(world) * HarmonyConfig.furnaceMinecartSpeed);
    }

    @Override public ItemStack getPickBlockStack() {return new ItemStack(Items.FURNACE_MINECART);}
    @Override public Item asItem() {return Items.FURNACE_MINECART;}

}
