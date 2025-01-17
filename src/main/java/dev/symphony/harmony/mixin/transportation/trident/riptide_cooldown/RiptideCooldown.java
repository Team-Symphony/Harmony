package dev.symphony.harmony.mixin.transportation.trident.riptide_cooldown;

import dev.symphony.harmony.config.HarmonyConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TridentItem.class)
public abstract class RiptideCooldown extends Item {

    public RiptideCooldown(Settings settings) {
        super(settings);
    }

    @Inject(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;useRiptide(IFLnet/minecraft/item/ItemStack;)V"))
    public void addCooldown(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfoReturnable<Boolean> cir) {
        if(HarmonyConfig.riptideCooldown){
            RegistryEntry<Enchantment> entry = stack.getEnchantments().getEnchantments().stream()
                .filter(act -> act.matchesId(Identifier.ofVanilla("riptide")))
                .findFirst()
                .orElse(null);
            int level = EnchantmentHelper.getLevel(entry, stack);

            if(user instanceof PlayerEntity && level > 0){
                ((PlayerEntity) user).getItemCooldownManager().set(this.getDefaultStack(), 15+level*HarmonyConfig.riptideTimeMultiplier);
            }
        }
    }
}