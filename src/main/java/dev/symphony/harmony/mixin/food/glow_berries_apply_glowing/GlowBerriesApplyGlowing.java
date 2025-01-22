package dev.symphony.harmony.mixin.food.glow_berries_apply_glowing;

import dev.symphony.harmony.config.HarmonyConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class GlowBerriesApplyGlowing {

    @Inject(method = "finishUsing", at = @At("HEAD"))
    private void glowBerryGlowEffect(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {

        if(!world.isClient && HarmonyConfig.glowBerryEffect != 0) {
            // If eating Glow Berry, apply glowing
            if (stack.getItem() == Items.GLOW_BERRIES) {
                int glowDuration = HarmonyConfig.glowBerryEffect * 20;
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, glowDuration, 0, true, true));
            }
        }
    }
}
