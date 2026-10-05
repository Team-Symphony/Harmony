package dev.symphony.harmony.mixin.mobs.mob_trims;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.symphony.harmony.Harmony;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.trim.*;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mixin(MobEntity.class)
public class MobEntityMixin {
    @Unique private static final List<RegistryKey<ArmorTrimMaterial>> UNIVERSAL_MATERIALS = new ArrayList<>();
    @Unique private static final HashMap<EntityType<?>, RegistryKey<ArmorTrimMaterial>> MOB_EXCLUSIVE_MATERIALS = new HashMap<>();
    @Unique private static final List<RegistryKey<ArmorTrimPattern>> UNIVERSAL_TRIMS = new ArrayList<>();
    @Unique private static final HashMap<EntityType<?>, RegistryKey<ArmorTrimPattern>> MOB_EXCLUSIVE_TRIMS = new HashMap<>();

    static {
        UNIVERSAL_MATERIALS.addAll(List.of(
                ArmorTrimMaterials.IRON,
                ArmorTrimMaterials.REDSTONE,
                ArmorTrimMaterials.COPPER,
                ArmorTrimMaterials.GOLD,
                ArmorTrimMaterials.DIAMOND,
                ArmorTrimMaterials.LAPIS,
                ArmorTrimMaterials.AMETHYST,
                ArmorTrimMaterials.RESIN
        ));
        MOB_EXCLUSIVE_MATERIALS.computeIfAbsent(EntityType.ZOMBIE_VILLAGER, e -> ArmorTrimMaterials.EMERALD);
        MOB_EXCLUSIVE_MATERIALS.computeIfAbsent(EntityType.PIGLIN, e -> ArmorTrimMaterials.QUARTZ);
        UNIVERSAL_TRIMS.addAll(List.of(
                ArmorTrimPatterns.WAYFINDER,
                ArmorTrimPatterns.RAISER,
                ArmorTrimPatterns.SHAPER,
                ArmorTrimPatterns.HOST
        ));
        MOB_EXCLUSIVE_TRIMS.computeIfAbsent(EntityType.HUSK, e -> ArmorTrimPatterns.DUNE);
        MOB_EXCLUSIVE_TRIMS.computeIfAbsent(EntityType.ZOMBIE_VILLAGER, e -> ArmorTrimPatterns.SENTRY);
        MOB_EXCLUSIVE_TRIMS.computeIfAbsent(EntityType.DROWNED, e -> ArmorTrimPatterns.COAST);
        MOB_EXCLUSIVE_TRIMS.computeIfAbsent(EntityType.BOGGED, e -> ArmorTrimPatterns.WILD);
//        MOB_EXCLUSIVE_TRIMS.computeIfAbsent(EntityType.PARCHED, e -> ArmorTrimPatterns.DUNE);
        MOB_EXCLUSIVE_TRIMS.computeIfAbsent(EntityType.PIGLIN, e -> ArmorTrimPatterns.SNOUT);
    }

    @WrapOperation(method = "initEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobEntity;equipStack(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/ItemStack;)V"))
    private void repeatRandomForEachSlot(MobEntity instance, EquipmentSlot slot, ItemStack stack, Operation<Void> original, Random random) {
        if (Harmony.CONFIG.mobTrims.mobTrims() && Harmony.CONFIG.mobTrims.mobTrimChance() >= random.nextFloat()) {
            var materials = instance.getWorld().getRegistryManager().getOptional(RegistryKeys.TRIM_MATERIAL);
            var patterns = instance.getWorld().getRegistryManager().getOptional(RegistryKeys.TRIM_PATTERN);
            if (materials.isPresent() && patterns.isPresent()) {
                var material = UNIVERSAL_MATERIALS.get(random.nextInt(UNIVERSAL_MATERIALS.size()));
                if (MOB_EXCLUSIVE_MATERIALS.containsKey(instance.getType()) && Harmony.CONFIG.mobTrims.mobExclusiveMaterialChance() >= random.nextFloat()) {
                    material = MOB_EXCLUSIVE_MATERIALS.get(instance.getType());
                }
                var pattern = UNIVERSAL_TRIMS.get(random.nextInt(UNIVERSAL_TRIMS.size()));
                if (MOB_EXCLUSIVE_TRIMS.containsKey(instance.getType()) && Harmony.CONFIG.mobTrims.mobExclusiveTrimChance() >= random.nextFloat()) {
                    pattern = MOB_EXCLUSIVE_TRIMS.get(instance.getType());
                }
                stack.set(DataComponentTypes.TRIM, new ArmorTrim(materials.get().getEntry(materials.get().get(material)), patterns.get().getEntry(patterns.get().get(pattern))));
            }
        }
        original.call(instance, slot, stack);
    }
}
