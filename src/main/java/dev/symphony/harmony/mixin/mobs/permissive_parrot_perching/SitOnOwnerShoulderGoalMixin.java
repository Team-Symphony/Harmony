package dev.symphony.harmony.mixin.mobs.permissive_parrot_perching;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.symphony.harmony.Harmony;
import net.minecraft.entity.ai.goal.SitOnOwnerShoulderGoal;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SitOnOwnerShoulderGoal.class)
public class SitOnOwnerShoulderGoalMixin {

    @ModifyExpressionValue(method = "canStart", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerAbilities;flying:Z"))
    private boolean modifyIsFlying(boolean original, @Local ServerPlayerEntity serverPlayerEntity) {
        if (!Harmony.CONFIG.mobsPetsCat.permissiveParrotPerching())
            return original;

        return serverPlayerEntity.isGliding();
    }

}
