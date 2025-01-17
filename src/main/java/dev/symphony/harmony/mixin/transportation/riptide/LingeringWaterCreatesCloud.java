package dev.symphony.harmony.mixin.transportation.riptide;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.potion.Potions;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * FEATURE: Standing inside a cloud of lingering water allows the player to use Riptide.
 * @author Trigam
 */
@Mixin( PotionEntity.class )
public abstract class LingeringWaterCreatesCloud {

    @Shadow protected abstract boolean isLingering();
    @Shadow protected abstract void applyLingeringPotion(PotionContentsComponent potion);

    @Inject(
        method = "onCollision",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/component/type/PotionContentsComponent;potion()Ljava/util/Optional;"
        )
    )
    private void lingeringWaterCreatesAffectCloud(HitResult hitResult, CallbackInfo ci, @Local PotionContentsComponent potionContents) {
        if ( potionContents.matches(Potions.WATER ) ) {
            if ( this.isLingering() ) {
                this.applyLingeringPotion( potionContents );
            }
        }
    }

}
