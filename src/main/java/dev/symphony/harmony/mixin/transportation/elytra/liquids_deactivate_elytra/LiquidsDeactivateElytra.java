package dev.symphony.harmony.mixin.transportation.elytra.liquids_deactivate_elytra;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.symphony.harmony.Harmony;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LiquidsDeactivateElytra extends Entity {

    public LiquidsDeactivateElytra(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyExpressionValue(method = "canGlide", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;hasStatusEffect(Lnet/minecraft/registry/entry/RegistryEntry;)Z"))
    private boolean cancelElytraInLiquid(boolean original) {
        if(Harmony.CONFIG.transElytraCat.liquidsDeactivateElytra()){
            if (original || this.isSubmergedInWater() || this.isInLava()) {
                setSprinting(true);
                return true;
            }
        }
        return false;
    }
}
