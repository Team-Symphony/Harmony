package dev.symphony.harmony.mixin.transportation.riptide;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.symphony.harmony.Harmony;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    /**
     * FEATURE: Using riptide trident enchant underwater makes the player gain acceleration during its effect.
     * @author Kiku
     * @author Flatkat
     */
    @SuppressWarnings("JavadocDeclaration")
    @Unique
    private static final float MODIFIER = Harmony.CONFIG.riptideAccelerationOnWater();
    @Unique
    private static final float DEG = (float) (Math.PI / 180F);

    @Shadow
    protected int riptideTicks;

    @Shadow public abstract void setSprinting(boolean sprinting);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

        //Applies constant acceleration when using riptide and touching water.
    @Inject(
            method = "tickMovement", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;tickRiptide(Lnet/minecraft/util/math/Box;Lnet/minecraft/util/math/Box;)V"
    )
    )
    private void accelerateWhenRiptide(CallbackInfo ci) {
        if(Harmony.CONFIG.riptideAccelerationOnWater() != 0){
            if (!this.isTouchingWater()) return;
            float f = getYaw();
            float g = getPitch();
            float h = -MathHelper.sin(f * DEG) * MathHelper.cos(g * DEG);
            float k = -MathHelper.sin(g * DEG);
            float l = MathHelper.cos(f * DEG) * MathHelper.cos(g * DEG);
            float m = MathHelper.sqrt(h * h + k * k + l * l);
            h *= MODIFIER / m;
            k *= MODIFIER / m;
            l *= MODIFIER / m;
            addVelocity(h, k, l);
        }
    }

    
     /**
     * FEATURE: Reduced water drag when using riptide
     * @author Kiku
     * @author Flatkat
     **/
    @ModifyExpressionValue(
            method = "travelInFluid", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;hasStatusEffect(Lnet/minecraft/registry/entry/RegistryEntry;)Z"
    )
    )
    private boolean boostWhenRiptide(boolean original) {
        if(Harmony.CONFIG.reduceRiptideWaterDrag()){
            return original || this.riptideTicks>0;
        }
        return original;
    }


}
