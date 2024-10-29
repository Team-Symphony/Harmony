package dev.symphony.harmony.mixin.enchanting;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.symphony.harmony.config.HarmonyConfig;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GrindstoneScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// FEATURE: Allowed grindstones to use base materials to repair
// AUTHORS: Randomvideos
@Mixin(GrindstoneScreenHandler.class)
public class GrindstoneRepairsWithMaterials {
    @Final @Shadow Inventory input;
    @Shadow private ItemStack grind(ItemStack item) {return null;}
    @Unique int slotIndex = -1;

    @WrapOperation(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/GrindstoneScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;"))
    protected Slot allowAnyItemToBeInserted(GrindstoneScreenHandler instance, Slot slot, Operation<Slot> original) {
        //if the feature isn't enabled, make the rest of the code think that no slots are the first or second slots
        if(!HarmonyConfig.grindstoneRepairsWithMaterials)
            slotIndex = 2;
        slotIndex++;
        //If the slot being created is the first or second one, make an identical slot that allows any items to be put in
        if(slotIndex==0)
            slot = new Slot(this.input, 0, 49, 19);
        else if (slotIndex==1)
            slot = new Slot(this.input, 1, 49, 40);
        //if the slot being created isn't the first or second one, the code runs normally because the slot hasn't been overwritten
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
        } else if (secondInput.isDamageable()) {
            itemToRepair = secondInput.copy();
            material = firstInput.copy();
        }
        else
            return;
        if(itemToRepair.canRepairWith(material)){
            //it's the same code as the anvil repairing mechanic, each material repairs at most 25% of the item's durability. If the item that can be repaired is already at full durability, don't return any output item
            int k = Math.min(itemToRepair.getDamage(), itemToRepair.getMaxDamage() / 4);
            if(k <= 0)
                return;
            for(int m = 0; k > 0 && m < material.getCount(); ++m) {
                int n = itemToRepair.getDamage() - k;
                itemToRepair.setDamage(n);
                k = Math.min(itemToRepair.getDamage(), itemToRepair.getMaxDamage() / 4);
            }
            cir.setReturnValue(this.grind(itemToRepair));
        }
    }
}
