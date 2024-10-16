package dev.symphony.harmony.mixin.food;

import dev.symphony.harmony.config.HarmonyConfig;
import net.minecraft.component.type.FoodComponent;
import org.spongepowered.asm.mixin.*;

// FEATURE: Change how long it takes to eat food items based on how much nutrition it gives.
// AUTHORS: Michiel1106

// Mixin into the FoodComponent class, changing every single food item
@Mixin(FoodComponent.class)
public abstract class ProportionalFoodTime {

    // variables to use later
    @Shadow private float eatSeconds = this.eatSeconds();
    @Shadow @Final private int nutrition;
    @Shadow public abstract float eatSeconds();

    // Define a tolerance for floating-point comparison because if I dont do this then it doesnt work. yay.
    @Unique
    private static final float TOLERANCE = 0.0001F;

    @Overwrite
    // How long it takes to eat in ticks. Eat seconds is 1.6 seconds for every food item except dried kelp.
    public int getEatTicks() {
        if (HarmonyConfig.foodEatTime != 0) {
            float eatSeconds = this.eatSeconds();  // Get the current eatSeconds value

            // Check if eatSeconds is approximately 1.6 (non-snack food)
            if (Math.abs(eatSeconds - 1.6F) < TOLERANCE) {
                return (int) (eatSeconds * this.nutrition * HarmonyConfig.foodEatTime);
            }
            // Check if eatSeconds is approximately 0.8 (snack food)
            if (Math.abs(eatSeconds - 0.8F) < TOLERANCE) {
                return (int) (eatSeconds * this.nutrition * HarmonyConfig.foodEatTime * 5);
            }
            // Fallback for other cases
            return (int) (eatSeconds * this.nutrition * HarmonyConfig.foodEatTime);
        }
        // Default case if HarmonyConfig.foodEatTime is 1
        return (int)(this.eatSeconds * 20.0F);
    }
}

