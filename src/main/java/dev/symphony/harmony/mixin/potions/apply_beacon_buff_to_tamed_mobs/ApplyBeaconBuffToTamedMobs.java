package dev.symphony.harmony.mixin.potions.apply_beacon_buff_to_tamed_mobs;

import com.llamalad7.mixinextras.sugar.Local;
import dev.symphony.harmony.Harmony;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;

@Mixin(BeaconBlockEntity.class)
public class ApplyBeaconBuffToTamedMobs {

    @Inject(method = "applyPlayerEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getNonSpectatingEntities(Ljava/lang/Class;Lnet/minecraft/util/math/Box;)Ljava/util/List;"))
    private static void applyTamedEntityEffects(World world, BlockPos pos, int beaconLevel, @Nullable RegistryEntry<StatusEffect> primaryEffect, @Nullable RegistryEntry<StatusEffect> secondaryEffect, CallbackInfo ci,
        @Local Box box, @Local(ordinal = 1) int amplifier, @Local(ordinal = 2) int duration
    ) {
        if (!Harmony.CONFIG.beaconsAffectTamedMobs())
            return;

        List<TameableEntity> tameableEntities = world.getEntitiesByClass(TameableEntity.class, box, TameableEntity::isTamed);

        StatusEffectInstance primary = new StatusEffectInstance(primaryEffect, duration, amplifier, true, true);
        StatusEffectInstance secondary = null;

        if (beaconLevel >= 4 && secondaryEffect != null && !Objects.equals(primaryEffect, secondaryEffect))
            secondary = new StatusEffectInstance(secondaryEffect, duration, 0, true, true);

        for (TameableEntity tameableEntity : tameableEntities) {
            tameableEntity.addStatusEffect(primary);

            if (secondary != null)
                tameableEntity.addStatusEffect(secondary);
        }
    }

}
