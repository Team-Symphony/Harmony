package dev.symphony.harmony.mixin.transportation;

import dev.symphony.harmony.Harmony;
import dev.symphony.harmony.config.HarmonyConfig;
import dev.symphony.harmony.entity.equipment.Equipment;
import dev.symphony.harmony.registry.HarmonyRegistries;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin (AbstractHorseEntity.class)
public abstract class HorseArmorPreventsBucking {
    // FEATURE:
    // AUTHORS: KikuGie, Flatkat
    @Unique
    private static final Map<Item, Identifier> equipments = new HashMap<>();

    @Shadow @Final private Inventory inventory;

    static {
        // TODO: Make preventBuckingChance for every type of armor configurable
        // TODO: Make this data-driven or configurable via config screen with a custom map config screen
        equipments.put(Items.DIAMOND_HORSE_ARMOR, Harmony.id("diamond"));
        equipments.put(Items.IRON_HORSE_ARMOR, Harmony.id("iron"));
        equipments.put(Items.GOLDEN_HORSE_ARMOR, Harmony.id("golden"));
        equipments.put(Items.LEATHER_HORSE_ARMOR, Harmony.id("leather"));
    }

    @Inject(method = "updateAnger", at = @At("HEAD"), cancellable = true)
    private void rejectAngryWhenDrip(CallbackInfo ci) {
        AbstractHorseEntity thisHorse = (AbstractHorseEntity) (Object) this;

        if (HarmonyConfig.horseArmorPreventsBucking) {
            if (Harmony.isMelodyPresent() && equipments.get(Registries.ITEM.get(Identifier.of("melody:netherite_horse_armor"))) == null) {
                // Temporary solution until we move this to a better, configurable system
                // This is inside rejectAngryWhenDrip because if not it gets called too early,
                // so it can't detect Melody nor is the armor item registered yet, so it doesn't work.
                // Yes, this is kind of a crappy workaround, but as stated earlier, its temporary
                equipments.put(Registries.ITEM.get(Identifier.of("melody:netherite_horse_armor")), Harmony.id("netherite"));
            }

            ItemStack armor = this.inventory.getStack(0);
            float preventBuckingChance = getPreventBuckingChance(thisHorse.getWorld(), armor);
            if (Math.random() <= preventBuckingChance) ci.cancel();
        }
    }

    @Unique
    public float getPreventBuckingChance(World world, ItemStack horseArmor) {
        final DynamicRegistryManager registryManager = world.getRegistryManager();
        Identifier equipmentId = equipments.get(horseArmor.getItem());
        Equipment horseEquipment = registryManager.get(HarmonyRegistries.EQUIPMENT_KEY).get(equipmentId);

        Harmony.LOGGER.info(String.valueOf(horseEquipment));

        if (horseEquipment == null) return 0F;
        return horseEquipment.horseEquipment().preventBuckingChance();
    }
}
