package dev.symphony.harmony.mixin.food;

import dev.symphony.harmony.Harmony;
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

// FEATURE: When Glowberries are eaten, they give the glowing effect for a configurable amount of time
// AUTHORS: Michiel1106
@Mixin(Item.class)
public class GlowBerryMixin {

    // Injects into the finishusing class, affecting every item.
    @Inject(method = "finishUsing", at = @At("HEAD"))

    private void glowBerryGlowEffect(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        // Checks if its not done on the client and if the glowberry effect isnt 0, if its zero it wont do anything
        if(!world.isClient && Harmony.CONFIG.glowBerryEffect() != 0) {
            // Checks if the item the person is holding is a glowberry
            if (stack.getItem() == Items.GLOW_BERRIES) {
                // Convert the input from ticks to seconds as 20 ticks is 1 second
                int glowduration = Harmony.CONFIG.glowBerryEffect() * 20;
                // Apply the effect using the glowduration above
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, glowduration, 0, true, true));
            }
        }
    }
}
