package dev.symphony.harmony.mixin.transportation;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.symphony.harmony.config.HarmonyConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    /**
     * FEATURE: Flying through liquids deactivates the Elytra
     * @author Kiku
     * @author Flatkat
     */
    @ModifyExpressionValue(method = "canGlide", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;hasStatusEffect(Lnet/minecraft/registry/entry/RegistryEntry;)Z"))
    private boolean cancelElytraInLiquid(boolean original) {
        if(HarmonyConfig.liquidsDeactivateElytra){
            if (original || this.isSubmergedInWater() || this.isInLava()) {
                setSprinting(true);
                return true;
            }
        }
        return false;
    }
}
