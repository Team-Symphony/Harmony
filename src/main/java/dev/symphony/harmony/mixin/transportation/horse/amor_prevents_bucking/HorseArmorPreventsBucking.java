package dev.symphony.harmony.mixin.transportation.horse.amor_prevents_bucking;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.symphony.harmony.Harmony;
import it.unimi.dsi.fastutil.objects.Object2FloatArrayMap;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin (AbstractHorseEntity.class)
public class HorseArmorPreventsBucking {

    @Unique
    private static final Map<Item, Float> preventBuckingChance = new Object2FloatArrayMap<>();
    @Shadow @Final private Inventory armorInventory;

    static {
        // TODO: Make preventBuckingChance for every type of armor configurable
        // TODO: Make this data-driven or configurable via config screen with a custom map config screen
        preventBuckingChance.put(Items.DIAMOND_HORSE_ARMOR, 0.9F);
        preventBuckingChance.put(Items.IRON_HORSE_ARMOR, 0.75F);
        preventBuckingChance.put(Items.GOLDEN_HORSE_ARMOR, 0.6F);
        preventBuckingChance.put(Items.LEATHER_HORSE_ARMOR, 0.45F);
    }

    public HorseArmorPreventsBucking(Inventory armorinventory) {
        this.armorInventory.getStack(0);
    }

    @ModifyExpressionValue(method = "updateAnger", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/AbstractHorseEntity;shouldAmbientStand()Z"))
    private boolean rejectAngryWhenDrip(boolean original) {
        if(Harmony.CONFIG.horseArmorPreventsBucking()){
            if(FabricLoader.getInstance().isModLoaded("melody") && preventBuckingChance.get(Registries.ITEM.get(Identifier.of("melody:netherite_horse_armor"))) == null) {
                // Temporary solution until we move this to a better, configurable system
                // This is inside rejectAngryWhenDrip because if not it gets called too early, so it cant detect Melody nor is the armor item registered yet, so it doesnt work.
                // Yes, this is kind of a crappy workaround, but as stated earlier, its temporary
                preventBuckingChance.put(Registries.ITEM.get(Identifier.of("melody:netherite_horse_armor")), 1F);
            }
            ItemStack armor = this.armorInventory.getStack(0);
            float chance = preventBuckingChance.getOrDefault(armor.getItem(), 0F);
            System.out.println(preventBuckingChance);
            if (chance > 0 && chance < 1 || Math.random() <= chance) return false;
        }
        return original;
    }
}
