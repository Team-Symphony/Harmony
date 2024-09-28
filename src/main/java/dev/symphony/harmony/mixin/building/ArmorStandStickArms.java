package dev.symphony.harmony.mixin.building;


import dev.symphony.harmony.config.HarmonyConfig;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// FEATURE: Allow Armor stands to be given arms using a Stick, and removed using Shears
// AUTHORS: WheatFlour, Trigam
@Mixin(ArmorStandEntity.class)
public abstract class ArmorStandStickArms {

    @Shadow public abstract void setShowArms(boolean showArms);
    @Shadow public abstract boolean isMarker();
    @Shadow public abstract boolean shouldShowArms();
    @Final @Shadow private DefaultedList<ItemStack> heldItems;

    @Inject(method = "interactAt", at = @At( value = "HEAD" ), cancellable = true)
    public void stickArms(PlayerEntity player, Vec3d hitPos, Hand hand, CallbackInfoReturnable<ActionResult> callback) {
        if (isMarker() || !HarmonyConfig.armorStandStickArms) return;
        if (player.isSneaking()) return;

        ItemStack heldItem = player.getStackInHand(hand);
        // Add arms
        if (heldItem.getItem().equals(Items.STICK) && heldItem.getCount() >= HarmonyConfig.armorStandSticks && !shouldShowArms()) {
            equipArms(player, heldItem);

            callback.setReturnValue(ActionResult.SUCCESS);
        // Remove arms
        } else if (heldItem.getItem().equals(Items.SHEARS) && shouldShowArms()) {
            shearArms(player, heldItem, hand);

            callback.setReturnValue(ActionResult.SUCCESS);
        }

//        if (stack.getItem().equals(Registries.ITEM.get(Identifier.ofVanilla("shears"))) &&
//                shouldShowArms() &&
//                getHandItems().iterator().next().isEmpty()) {
//
//            setShowArms(false);
//            stack.damage(1, player, LivingEntity.getSlotForHand(hand));
//            player.getWorld().playSoundFromEntity(null, (ArmorStandEntity) (Object) this, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.PLAYERS, 1.0F, 1.0F);
//            player.giveItemStack(new ItemStack(Registries.ITEM.get(Identifier.ofVanilla("stick")), 2));
//            callback.setReturnValue(ActionResult.SUCCESS);
//        } else if (stack.getItem().equals(Registries.ITEM.get(Identifier.ofVanilla("stick"))) &&
//                !shouldShowArms() &&
//                stack.getCount() > 1) {
//
//            setShowArms(true);
//            stack.decrement(2);
//            callback.setReturnValue(ActionResult.SUCCESS);
//        }
    }

    @Unique
    public void equipArms(PlayerEntity player, ItemStack stickStack) {
        setShowArms(true);
        stickStack.decrementUnlessCreative(HarmonyConfig.armorStandSticks, player);

        player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC.value(), 1F, 1F);
    }

    @Unique
    public void shearArms(PlayerEntity player, ItemStack shearStack, Hand hand) {
        ArmorStandEntity thisStand = (ArmorStandEntity) (Object) this;

        setShowArms(false);
        shearStack.damage(1, player, LivingEntity.getSlotForHand(hand));

        ItemStack sticksDrop = Items.STICK.getDefaultStack();
        sticksDrop.setCount(HarmonyConfig.armorStandSticks);
        Block.dropStack(thisStand.getWorld(), thisStand.getBlockPos().up(), sticksDrop);

        // Drop hand items
        for (int i = 0; i < heldItems.size(); i++) {
            ItemStack handItem = heldItems.get(i);
            if (!handItem.isEmpty()) {
                Block.dropStack(thisStand.getWorld(), thisStand.getBlockPos().up(), handItem);
                heldItems.set(i, ItemStack.EMPTY);
            }
        }

        player.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1F, 1F);
    }
}
