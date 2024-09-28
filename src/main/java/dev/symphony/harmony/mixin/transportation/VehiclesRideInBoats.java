package dev.symphony.harmony.mixin.transportation;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.symphony.harmony.config.HarmonyConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BoatEntity.class)
public class VehiclesRideInBoats {

    @ModifyReturnValue(method = "isSmallerThanBoat", at = @At("RETURN"))
    private boolean isSmallerThanBoat(boolean original, @Local(argsOnly = true) Entity entity) {
        if (HarmonyConfig.vehiclesRideInBoats && entity instanceof AbstractHorseEntity)
            return true;
        else return original;
    }

}
