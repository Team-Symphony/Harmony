package dev.symphony.harmony.mixin.combat.slower_item_despawn;

import dev.symphony.harmony.Harmony;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ItemEntity.class)
public abstract class ModifyItemDespawnTimes extends Entity {

    @Shadow @Final private static int NEVER_DESPAWN_AGE;

    public ModifyItemDespawnTimes(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyConstant(method = "tick", constant = @Constant(intValue = 6000))
    private int modifyDespawnTime(int constant) {
        if(!Harmony.CONFIG.changeItemDespawnTime()){
            return constant;
        }


        int time = 20 * switch (this.getWorld().getDifficulty()) {
            case PEACEFUL, EASY -> Harmony.CONFIG.itemDespawnTimeCat.itemDespawnTimeEasy();
            case NORMAL -> Harmony.CONFIG.itemDespawnTimeCat.itemDespawnTimeNormal();
            case HARD -> Harmony.CONFIG.itemDespawnTimeCat.itemDespawnTimeHard();
        };

        return time == 0 ? NEVER_DESPAWN_AGE : time;
    }

}
