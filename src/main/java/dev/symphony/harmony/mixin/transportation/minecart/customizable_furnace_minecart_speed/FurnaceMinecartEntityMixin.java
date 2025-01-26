package dev.symphony.harmony.mixin.transportation.minecart.customizable_furnace_minecart_speed;

import dev.symphony.harmony.Harmony;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.FurnaceMinecartEntity;
import net.minecraft.item.FuelRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FurnaceMinecartEntity.class)
public class FurnaceMinecartEntityMixin extends AbstractMinecartEntity {
    @Shadow private int fuel;
    @Shadow public Vec3d pushVec;

    protected FurnaceMinecartEntityMixin(EntityType<?> entityType, World world) {super(entityType, world);}

    @Inject(method = "getMaxSpeed",at= @At("RETURN"), cancellable = true)
    void ChangeFurnaceMinecartSpeed(ServerWorld world, CallbackInfoReturnable<Double> cir){
        if(super.isTouchingWater())
            cir.setReturnValue(super.getMaxSpeed(world) * Harmony.CONFIG.transMinecartCat.furnaceMinecartSpeedInWater());
        else
            cir.setReturnValue(super.getMaxSpeed(world) * Harmony.CONFIG.transMinecartCat.furnaceMinecartSpeed());
    }

    @Inject(method = "interact",at= @At(value = "HEAD"), cancellable = true)
    void AllowMoreFuels(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir){
        ItemStack itemStack = player.getStackInHand(hand);
        FuelRegistry fuelRegistry = player.getWorld().getFuelRegistry();
        if (fuelRegistry.isFuel(itemStack)) {
            if(fuelRegistry.getFuelTicks(itemStack)*2.25f+fuel < 32000) {
                itemStack.decrementUnlessCreative(1, player);
                this.fuel += (int) (fuelRegistry.getFuelTicks(itemStack)*2.25f);
            }
        }

        if (this.fuel > 0) {
            this.pushVec = this.getPos().subtract(player.getPos()).getHorizontal();
        }

        cir.setReturnValue(ActionResult.SUCCESS);
    }

    @Override public ItemStack getPickBlockStack() {return new ItemStack(Items.FURNACE_MINECART);}
    @Override public Item asItem() {return Items.FURNACE_MINECART;}

}
