package dev.symphony.harmony.mixin.mobs.wolves_growl_at_monsters;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.symphony.harmony.Harmony;
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

@Mixin(WolfEntity.class)
public abstract class WolfEntityMixin extends TameableEntity {

    protected WolfEntityMixin(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyExpressionValue(method = "getAmbientSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/WolfEntity;hasAngerTime()Z"))
    private boolean shouldGrowl(boolean original) {
        return original || (Harmony.CONFIG.mobsPetsCat.wolvesGrowlAtMonsters() && this.isTamed() && !this.getNearbyMonsters().isEmpty());
    }

    @Unique
    private List<HostileEntity> getNearbyMonsters() {
        double d = this.getAttributeValue(EntityAttributes.FOLLOW_RANGE);
        return this.getWorld().getEntitiesByClass(HostileEntity.class, this.getBoundingBox().expand(d, 4.0, d), hostileEntity -> true);
    }

}
