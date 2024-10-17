package dev.symphony.harmony.mixin.mobs;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.symphony.harmony.config.HarmonyConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

/**
 * Tamed wolves growl when there is a hostile mob nearby, regardless of whether that mob has made them "angry".
 * @author axialeaa
 */
@Mixin(WolfEntity.class)
public abstract class WolfEntityMixin extends TameableEntity {

    protected WolfEntityMixin(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyExpressionValue(method = "getAmbientSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/WolfEntity;hasAngerTime()Z"))
    private boolean shouldGrowl(boolean original) {
        return original || (HarmonyConfig.wolvesGrowlAtMonsters && this.isTamed() && !this.getNearbyMonsters().isEmpty());
    }

    @Unique
    private List<HostileEntity> getNearbyMonsters() {
        double d = this.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE);
        return this.getWorld().getEntitiesByClass(HostileEntity.class, this.getBoundingBox().expand(d, 4.0, d), hostileEntity -> true);
    }

}
