package dev.symphony.harmony.mixin.transportation;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.symphony.harmony.config.HarmonyConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.TeleportTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * FEATURE: Allowed Ender Pearls to teleport all riders and passengers, all taking damage.
 * @author RandomVideos
 */
@Mixin(EnderPearlEntity.class)
public class EnderPearlsDontDismount {

    @WrapOperation(method = "onCollision",at= @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;teleportTo(Lnet/minecraft/world/TeleportTarget;)Lnet/minecraft/server/network/ServerPlayerEntity;"))
    ServerPlayerEntity TeleportMount(ServerPlayerEntity instance, TeleportTarget teleportTarget, Operation<ServerPlayerEntity> original){
        if(instance.hasVehicle() && HarmonyConfig.enderPearlsTeleportVehicles){
            //Find the mount that isnt riding any other mount and teleport it instead of the player
            Entity vehicle = instance.getVehicle();
            while(vehicle.hasVehicle()) {
                vehicle = vehicle.getVehicle();
            }
            vehicle.teleportTo(teleportTarget);
            return instance;
        }
        else return original.call(instance,teleportTarget);
    }

    @WrapOperation(method = "onCollision",at= @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;damage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    boolean DamageAfterTeleportation(ServerPlayerEntity instance, ServerWorld world, DamageSource source, float amount, Operation<Boolean> original){
        if(instance.hasVehicle() && HarmonyConfig.enderPearlsDamageVehicles && HarmonyConfig.enderPearlsTeleportVehicles) {
            //The damage each entity takes from teleporting is halved to be consistent with horses taking fall damage
            amount /= 2;
            //Find the mount that isnt riding any other mount
            Entity vehicle = instance.getVehicle();
            while (vehicle.hasVehicle()) {
                vehicle = vehicle.getVehicle();
            }
            //Damage that vehicle
            vehicle.damage(world, source, amount);
            //For each passenger of the vehicle(this counts passengers of passengers), damage that entity
            for (Entity entity : vehicle.getPassengersDeep()) {
                entity.damage(world, source, amount);
            }
            return true;
        }
        else return original.call(instance, world, source, amount);
    }

    //Stop the player from leaving their vehicle when using an Ender Pearl
    @WrapOperation(method = "onCollision",at= @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;detach()V"))
    void StopDetaching(Entity instance, Operation<Void> original){if(!HarmonyConfig.enderPearlsTeleportVehicles) {original.call(instance);}}

}
