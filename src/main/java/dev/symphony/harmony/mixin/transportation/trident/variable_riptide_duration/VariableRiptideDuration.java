package dev.symphony.harmony.mixin.transportation.trident.variable_riptide_duration;

import com.llamalad7.mixinextras.sugar.Local;
import dev.symphony.harmony.Harmony;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.item.consume.UseAction;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;


@Mixin(TridentItem.class)
public abstract class VariableRiptideDuration extends Item {

    @Shadow public abstract UseAction getUseAction(ItemStack stack);

    public VariableRiptideDuration(net.minecraft.item.Item.Settings settings) {
        super(settings);
    }

    // Extends the duration of the riptide effect based on the enchantment level, similar to rocket boosts.
    @ModifyArg(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;useRiptide(IFLnet/minecraft/item/ItemStack;)V"), index = 0)
    private int modifyRiptideTicks(int riptideTicks, @Local(argsOnly = true) ItemStack stack) {

        if(Harmony.CONFIG.riptideTimeMultiplier()!=0){
            RegistryEntry<Enchantment> entry = stack.getEnchantments().getEnchantments().stream()
                .filter(act -> act.matchesId(Identifier.ofVanilla("riptide")))
                .findFirst()
                .orElse(null);
            int level = EnchantmentHelper.getLevel(entry, stack);
            return 15 + level*Harmony.CONFIG.riptideTimeMultiplier();
        }
        return riptideTicks;
    }
}
