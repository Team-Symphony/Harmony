package dev.symphony.harmony.mixin.transportation;

import dev.symphony.harmony.config.HarmonyConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AbstractHorseEntity.class)
public class SmallerVehicleHitboxInBoat {

//    @Unique
//    @Override
    // Appears to have trouble overriding the method
    // which is several extensions up
    // an ECS would be so nice
    // anyways this isn't finished so don't merge
    public EntityDimensions getDimensions(EntityPose pose) {
        AbstractHorseEntity horse = (AbstractHorseEntity) (Object) this;
        Entity vehicle = horse.getVehicle();

        if (HarmonyConfig.vehiclesRideInBoats && vehicle instanceof BoatEntity) {
            return horse.getDimensions(pose).scaled(0.5f);
        } else return horse.getDimensions(pose);
    }

}
