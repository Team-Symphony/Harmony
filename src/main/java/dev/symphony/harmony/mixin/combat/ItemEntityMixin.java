package dev.symphony.harmony.mixin.combat;

import dev.symphony.harmony.config.HarmonyConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

// FEATURE: Make items despawn slower on lower difficulties
// AUTHOR: axialeaa
@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {

    @Shadow @Final private static int NEVER_DESPAWN_AGE;

    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyConstant(method = "tick", constant = @Constant(intValue = 6000))
    private int modifyDespawnTime(int constant) {
        int time = 20 * switch (this.getWorld().getDifficulty()) {
            case PEACEFUL, EASY -> HarmonyConfig.itemDespawnTimeEasy;
            case NORMAL -> HarmonyConfig.itemDespawnTimeNormal;
            case HARD -> HarmonyConfig.itemDespawnTimeHard;
        };

        return time == 0 ? NEVER_DESPAWN_AGE : time;
    }

}
