package dev.symphony.harmony.mixin.food;


import net.minecraft.component.type.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.SuspiciousStewItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;


@Mixin(Items.class)
public class StewStack {
    // Would be nice if this was applied to all items with a specific custom item tag to facilitate mod compat.

    @Redirect(method = "<clinit>",slice = @Slice(from = @At(value = "CONSTANT",args= {
            "stringValue=mushroom_stew"},ordinal = 0)),at = @At(
            value = "NEW",target = "(Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", ordinal = 0 ))
    private static Item stacked_mushroom_stew(Item.Settings settings) {
        return new Item((new Item.Settings()).maxCount(16).food(FoodComponents.MUSHROOM_STEW));
    }

    @Redirect(method = "<clinit>",slice = @Slice(from = @At(value = "CONSTANT",args= {
            "stringValue=beetroot_soup"},ordinal = 0)),at = @At(
            value = "NEW",target = "(Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", ordinal = 0 ))
    private static Item stacked_beetroot_soup(Item.Settings settings) {
        return new Item((new Item.Settings()).maxCount(16).food(FoodComponents.BEETROOT_SOUP));
    }

    @Redirect(method = "<clinit>",slice = @Slice(from = @At(value = "CONSTANT",args= {
            "stringValue=rabbit_stew"},ordinal = 0)),at = @At(
            value = "NEW",target = "(Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", ordinal = 0 ))
    private static Item stacked_rabbit_stew(Item.Settings settings) {
        return new Item((new Item.Settings()).maxCount(16).food(FoodComponents.RABBIT_STEW));
    }

    // Note: the rest of the stews come from the Item class, but the Suspicious Stews extend from the SuspiciousStewItem class
    @Redirect(method = "<clinit>",slice = @Slice(from = @At(value = "CONSTANT",args= {
            "stringValue=suspicious_stew"},ordinal = 0)),at = @At(
            value = "NEW",target = "(Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/SuspiciousStewItem;", ordinal = 0 ))
    private static SuspiciousStewItem stacked_suspicious_stew(Item.Settings settings) {
        return new SuspiciousStewItem((new Item.Settings()).maxCount(16).food(FoodComponents.SUSPICIOUS_STEW));
    }



}
