package dev.symphony.harmony.mixin.food;

import dev.symphony.harmony.config.HarmonyConfig;
import net.minecraft.component.type.FoodComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

// AUTHOR michiel1106
// FEATURE change how long it takes to eat fooditems based on how much nutrition it gives

// mixin into the foodcomponent class, changing every single food item
@Mixin(FoodComponent.class)
public class ProportionalFoodTime {

    // variable getter to use later
    @Shadow
    private float eatSeconds = 1.6F;
    @Shadow
    @Final
    private int nutrition;
    @Overwrite
    // how long it takes to eat in ticks, eat seconds is 1.6 seconds for every food item except dried kelp
    public int getEatTicks() {
        return (int)(this.eatSeconds * this.nutrition * HarmonyConfig.foodEatTime);
    }
}

