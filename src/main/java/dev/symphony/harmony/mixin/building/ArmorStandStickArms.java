package dev.symphony.harmony.mixin.building;


import dev.symphony.harmony.config.HarmonyConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// FEATURE: Allow Armor stands to be given arms using a Stick, and removed using Shears
// AUTHORS: WheatFlour
@Mixin(ArmorStandEntity.class)
public abstract class ArmorStandStickArms {

    @Shadow public abstract void setShowArms(boolean showArms);

    @Shadow public abstract boolean isMarker();

    @Shadow public abstract boolean shouldShowArms();

    @Shadow public abstract Iterable<ItemStack> getHandItems();

    @Inject(
            method = "interactAt",
            at = @At( value = "HEAD" ),
            cancellable = true
    )
    public void stickArms(PlayerEntity player, Vec3d hitPos, Hand hand, CallbackInfoReturnable<ActionResult> callback) {
        if (isMarker() || !HarmonyConfig.armorStandStickArms) return;

        ItemStack stack = player.getStackInHand(hand);

        if (stack.getItem().equals(Registries.ITEM.get(Identifier.ofVanilla("shears"))) &&
                shouldShowArms() &&
                getHandItems().iterator().next().isEmpty()) {

            setShowArms(false);
            stack.damage(1, player, LivingEntity.getSlotForHand(hand));
            player.getWorld().playSoundFromEntity(null, (ArmorStandEntity) (Object) this, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.PLAYERS, 1.0F, 1.0F);
            player.giveItemStack(new ItemStack(Registries.ITEM.get(Identifier.ofVanilla("stick")), 2));
            callback.setReturnValue(ActionResult.SUCCESS);
        } else if (stack.getItem().equals(Registries.ITEM.get(Identifier.ofVanilla("stick"))) &&
                !shouldShowArms() &&
                stack.getCount() > 1) {

            setShowArms(true);
            stack.decrement(2);
            callback.setReturnValue(ActionResult.SUCCESS);
        }

    }
}
