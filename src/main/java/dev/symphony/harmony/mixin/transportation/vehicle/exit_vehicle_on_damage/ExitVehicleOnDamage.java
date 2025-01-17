package dev.symphony.harmony.mixin.transportation.vehicle.exit_vehicle_on_damage;

import dev.symphony.harmony.config.HarmonyConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class ExitVehicleOnDamage extends Entity {
    @Shadow public abstract void stopRiding();

    public ExitVehicleOnDamage(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(
        method = "damage",
        at = @At( value = "TAIL" )
    )
    private void exitVehicleOnDamage(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (HarmonyConfig.exitVehicleOnDamage) {
            if (amount <= 0) return;
            if (this.isPlayer()) return;
            if (source == getDamageSources().enderPearl()) return;

            Entity vehicle = this.getVehicle();

            if (vehicle instanceof VehicleEntity) {
                this.stopRiding();
            }
        }
    }

}
