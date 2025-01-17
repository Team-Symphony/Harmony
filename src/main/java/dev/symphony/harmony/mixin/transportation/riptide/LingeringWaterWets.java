package dev.symphony.harmony.mixin.transportation.riptide;

import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Potions;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

/**
 * FEATURE: Standing inside a cloud of lingering water allows the player to use Riptide.
 * @author Trigam
 */
@Mixin( PlayerEntity.class )
public abstract class LingeringWaterWets extends LivingEntity {

    protected LingeringWaterWets(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean isTouchingWaterOrRain() {
        return super.isTouchingWaterOrRain() || isTouchingLingeringWater();
    }

    @Unique public boolean isTouchingLingeringWater() {
        List<AreaEffectCloudEntity> lingeringPotions = this.getWorld().getNonSpectatingEntities(
            AreaEffectCloudEntity.class, this.getBoundingBox()
        );
        for (AreaEffectCloudEntity lingeringPotion : lingeringPotions) {
            PotionContentsComponent effects = lingeringPotion.potionContentsComponent;
            if (effects.matches(Potions.WATER)) return true;
        }
        return false;
    }

}
