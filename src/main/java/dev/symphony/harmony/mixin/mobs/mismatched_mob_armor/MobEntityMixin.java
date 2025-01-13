package dev.symphony.harmony.mixin.mobs.mismatched_mob_armor;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.symphony.harmony.Harmony;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends Entity {

    public MobEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @WrapOperation(method = "initEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobEntity;getEquipmentForSlot(Lnet/minecraft/entity/EquipmentSlot;I)Lnet/minecraft/item/Item;"))
    private Item repeatRandomForEachSlot(EquipmentSlot equipmentSlot, int equipmentLevel, Operation<Item> original, @Local(argsOnly = true) Random random) {
        if (!Harmony.CONFIG.mismatchedMobArmor())
            return original.call(equipmentSlot, equipmentLevel);

        int level = random.nextInt(2);

        for (int i = 0; i < 3; i++) {
            if (random.nextFloat() < 0.095F)
                level++;
        }

        return original.call(equipmentSlot, level);
    }

}
