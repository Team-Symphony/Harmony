package dev.symphony.harmony.mixin.transportation.trident.riptide_reduced_drag;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.symphony.harmony.Harmony;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class RiptideReducedDrag extends Entity {

    @Shadow protected int riptideTicks;

    public RiptideReducedDrag(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyExpressionValue(
        method = "travelInFluid",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;hasStatusEffect(Lnet/minecraft/registry/entry/RegistryEntry;)Z"
        )
    )
    private boolean boostWhenRiptide(boolean original) {
        if (Harmony.CONFIG.transRiptideCat.reduceRiptideWaterDrag()) {
            return original || this.riptideTicks > 0;
        }
        return original;
    }
}