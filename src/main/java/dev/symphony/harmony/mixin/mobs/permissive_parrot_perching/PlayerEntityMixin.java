package dev.symphony.harmony.mixin.mobs.permissive_parrot_perching;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.symphony.harmony.Harmony;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends EntityImplMixin {

    @Shadow protected abstract void dropShoulderEntities();

    @Shadow @Final private PlayerAbilities abilities;
    @Unique private long lastSneakTime;
    @Unique private static final int SNEAK_COOLDOWN = 10;

    @Unique private final LivingEntity asLivingEntity = (LivingEntity) (Object) this;

    @ModifyExpressionValue(method = "tickMovement", slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;updateShoulderEntity(Lnet/minecraft/nbt/NbtCompound;)V", ordinal = 0)), at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerEntity;fallDistance:F"))
    private float removeFallCheck(float original) {
        if (!Harmony.CONFIG.permissiveParrotPerching())
            return original;

        return Integer.MIN_VALUE;
    }

    @Inject(method = "startGliding", at = @At("HEAD"))
    private void dropOnStartedFallFlying(CallbackInfo ci) {
        World world = this.asLivingEntity.getWorld();

        if (!world.isClient)
            this.dropShoulderEntities();
    }

    @Override
    public void setSneakingImpl(boolean sneaking, Operation<Void> original) {
        World world = this.asLivingEntity.getWorld();
        long time = world.getTime();

        if (sneaking && !this.abilities.flying) {
            this.lastSneakTime = time;

            super.setSneakingImpl(true, original);
            return;
        }

        if (time - this.lastSneakTime < SNEAK_COOLDOWN && !world.isClient)
            this.dropShoulderEntities();

        super.setSneakingImpl(false, original);
    }

}
