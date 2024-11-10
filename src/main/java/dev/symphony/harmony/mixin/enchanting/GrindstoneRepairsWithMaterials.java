package dev.symphony.harmony.mixin.enchanting;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.symphony.harmony.config.HarmonyConfig;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.screen.GrindstoneScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * FEATURE: Allowed grindstones to use base materials to repair.
 * @author RandomVideos
 */
@Mixin(GrindstoneScreenHandler.class)
public class GrindstoneRepairsWithMaterials {
    @Final @Shadow Inventory input;
    @Final @Shadow private Inventory result;
    @Shadow private ItemStack grind(ItemStack item) {return null;}
    @Final @Shadow private ScreenHandlerContext context;

    @Unique int slotIndex = -1;
    @Unique int materialsUsed = 0;
    @Unique int slotWithMaterial;
    @Unique int slotWithItem;

    @WrapOperation(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/GrindstoneScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;"))
    protected Slot allowAnyItemToBeInserted(GrindstoneScreenHandler instance, Slot slot, Operation<Slot> original) {
        //if the feature isn't enabled, make the rest of the code think that no slots are the first or second slots
        if(!HarmonyConfig.grindstoneRepairsWithMaterials)
            slotIndex = 3;
        slotIndex++;
        //If the slot being created is the first or second one, make an identical slot that allows any items to be put in
        if(slotIndex==0)
            slot = new Slot(slot.inventory, 0, slot.x, slot.y);
        else if (slotIndex==1)
            slot = new Slot(slot.inventory, 1, slot.x, slot.y);
        else if (slotIndex==2) {
            //If the slot being created is the third, make an identical slot, but when taking items out, decrease the materials used instead of deleting the entire stack
            slot = new Slot(this.result, 2, slot.x, slot.y) {
                @Override
                public boolean canInsert(ItemStack stack) {
                    return false;
                }

                @Override
                public void onTakeItem(PlayerEntity player, ItemStack stack) {
                    context.run((world, pos) -> {
                        if (world instanceof ServerWorld) {
                            ExperienceOrbEntity.spawn((ServerWorld)world, Vec3d.ofCenter(pos), this.getExperience(world));
                        }

                        world.syncWorldEvent(WorldEvents.GRINDSTONE_USED, pos, 0);
                    });
                    if(materialsUsed == 0) {
                        input.setStack(0, ItemStack.EMPTY);
                        input.setStack(1, ItemStack.EMPTY);
                    }
                    else {
                        input.getStack(slotWithMaterial).decrement(materialsUsed);
                        input.setStack(slotWithItem, ItemStack.EMPTY);
                        materialsUsed = 0;
                    }
                }

                private int getExperience(World world) {
                    int i = 0;
                    i += this.getExperience(input.getStack(0));
                    i += this.getExperience(input.getStack(1));
                    if (i > 0) {
                        int j = (int)Math.ceil((double)i / 2.0);
                        return j + world.random.nextInt(j);
                    } else {
                        return 0;
                    }
                }

                private int getExperience(ItemStack stack) {
                    int i = 0;
                    ItemEnchantmentsComponent itemEnchantmentsComponent = EnchantmentHelper.getEnchantments(stack);

                    for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : itemEnchantmentsComponent.getEnchantmentEntries()) {
                        RegistryEntry<Enchantment> registryEntry = (RegistryEntry<Enchantment>)entry.getKey();
                        int j = entry.getIntValue();
                        if (!registryEntry.isIn(EnchantmentTags.CURSE)) {
                            i += registryEntry.value().getMinPower(j);
                        }
                    }

                    return i;
                }
            };
        }
        //if the slot being created isn't the first, second or third one, the code runs normally because the slot hasn't been overwritten
        return original.call(instance, slot);
    }

    @Inject(method = "getOutputStack", at = @At("HEAD"), cancellable = true)
    private void makeMaterialsRepair(ItemStack firstInput, ItemStack secondInput, CallbackInfoReturnable<ItemStack> cir) {
        if(!HarmonyConfig.grindstoneRepairsWithMaterials)
            return;
        ItemStack itemToRepair;
        ItemStack material;
        //check if the item that needs to be repaired is in the first or second slot. If none can be repaired, don't return any output item
        if(firstInput.isDamageable()){
            itemToRepair = firstInput.copy();
            material = secondInput.copy();
            slotWithMaterial = 1;
            slotWithItem = 0;
        } else if (secondInput.isDamageable()) {
            itemToRepair = secondInput.copy();
            material = firstInput.copy();
            slotWithMaterial = 0;
            slotWithItem = 1;
        }
        else
            return;
        if(itemToRepair.canRepairWith(material)){
            //it's the same code as the anvil repairing mechanic, each material repairs at most 25% of the item's durability. If the item that can be repaired is already at full durability, don't return any output item
            int k = Math.min(itemToRepair.getDamage(), itemToRepair.getMaxDamage() / 4);
            materialsUsed = 0;
            if(k <= 0)
                return;
            for(int m = 0; k > 0 && m < material.getCount(); ++m) {
                int n = itemToRepair.getDamage() - k;
                itemToRepair.setDamage(n);
                k = Math.min(itemToRepair.getDamage(), itemToRepair.getMaxDamage() / 4);
                materialsUsed++;
            }
            cir.setReturnValue(this.grind(itemToRepair));
        }
    }
}
