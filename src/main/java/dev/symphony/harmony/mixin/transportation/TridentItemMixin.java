package dev.symphony.harmony.mixin.transportation;

import com.llamalad7.mixinextras.sugar.Local;
import dev.symphony.harmony.config.HarmonyConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// FEATURE: Trident accelerates on water
// AUTHORS: Kiku, Flatkat
// ALSO SEE: mixin.transportation.LivingEntityMixin
@Mixin(TridentItem.class)
public abstract class TridentItemMixin extends Item {

    @Shadow public abstract UseAction getUseAction(ItemStack stack);

    public TridentItemMixin(Settings settings) {
        super(settings);
    }

    // Extends the duration of the riptide effect based on the enchantment level, similar to rocket boosts.
    @ModifyArg(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;useRiptide(IFLnet/minecraft/item/ItemStack;)V"), index = 0)
    private int modifyRiptideTicks(int riptideTicks, @Local(argsOnly = true) ItemStack stack) {

        if(HarmonyConfig.riptideTimeMultiplier != 0){
            RegistryEntry<Enchantment> entry = stack.getEnchantments().getEnchantments().stream()
                    .filter(act -> act.matchesId(Identifier.ofVanilla("riptide")))
                    .findFirst()
                    .orElse(null);
            int level = EnchantmentHelper.getLevel(entry, stack);

            return 15 + level*HarmonyConfig.riptideTimeMultiplier;
        }
        return riptideTicks;
    }

    // Adds a cooldown to tridents with riptide, to avoid acceleration stacking
    @Inject(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;useRiptide(IFLnet/minecraft/item/ItemStack;)V"))
    public void addCooldown(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
        if(HarmonyConfig.riptideAccelerationOnWater!=0){
            RegistryEntry<Enchantment> entry = stack.getEnchantments().getEnchantments().stream()
                    .filter(act -> act.matchesId(Identifier.ofVanilla("riptide")))
                    .findFirst()
                    .orElse(null);
            int level = EnchantmentHelper.getLevel(entry, stack);

            if(user instanceof PlayerEntity && level > 0){
                ((PlayerEntity) user).getItemCooldownManager().set(this, 20);
            }
        }
    }


    @Override
    public boolean allowComponentsUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
        return super.allowComponentsUpdateAnimation(player, hand, oldStack, newStack);
    }
}
