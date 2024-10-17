package dev.symphony.harmony.mixin.transportation;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
abstract public class LivingEntityMixin extends Entity {
    @Unique
    private static final float MODIFIER = 0.1F;
    @Unique
    private static final float DEG = (float) (Math.PI / 180F);

    @Shadow
    protected int riptideTicks;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }


    //Reduces water drag when using riptide.
    @ModifyExpressionValue(
            method = "travel", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;hasStatusEffect(Lnet/minecraft/registry/entry/RegistryEntry;)Z",
            ordinal = 1
    )
    )
    private boolean boostWhenRiptide(boolean original) {
        return original || this.riptideTicks>0;
    }


    //Applies constant acceleration when using riptide and touching water.
    @Inject(
            method = "tickMovement", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;tickRiptide(Lnet/minecraft/util/math/Box;Lnet/minecraft/util/math/Box;)V"
    )
    )
    private void accelerateWhenRiptide(CallbackInfo ci) {
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

    @Redirect(method = "tickFallFlying", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;hasStatusEffect(Lnet/minecraft/registry/entry/RegistryEntry;)Z"))
    private boolean cancelElytraInLiquid(LivingEntity instance, RegistryEntry<StatusEffect> effect) {
        return !(!instance.hasStatusEffect(effect) && !instance.isWet() && !instance.isInLava());
    }

}