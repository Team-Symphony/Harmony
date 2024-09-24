package dev.symphony.harmony.mixin.transportation;

import dev.symphony.harmony.ModTags;
import dev.symphony.harmony.config.HarmonyConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class ExitVehicleOnDamage {
    @Inject(
            method = "damage",
            at = @At( value = "TAIL" )
    )
    private void exitVehicleOnDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if(HarmonyConfig.exitVehicleOnDamage) {
            LivingEntity entity = ((LivingEntity) (Object) this);
            if (amount <= 0) return;
            if (entity.isPlayer()) return;

            Entity vehicle = entity.getVehicle();
            if (vehicle == null) return;
            EntityType<?> vehicleType = vehicle.getType();

            if (vehicleType.isIn(ModTags.VEHICLES)) {
                entity.stopRiding();
            }
        }

    }
}
