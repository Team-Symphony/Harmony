package dev.symphony.harmony.util;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.symphony.harmony.config.HarmonyConfig;
import net.minecraft.world.Difficulty;
import net.minecraft.world.WorldAccess;

/**
 * Provides some utilities for use in the peacefulMonsterSpawning implementation.
 */
public class PeacefulMonsterSpawningUtils {

    public static <T extends WorldAccess> Difficulty getEasyIfOptionEnabled(T instance, Operation<Difficulty> original) {
        return getEasyIfOptionEnabled(original.call(instance));
    }

    public static Difficulty getEasyIfOptionEnabled(Difficulty original) {
        return HarmonyConfig.peacefulMonsterSpawning ? Difficulty.EASY : original;
    }

    public static int capAbovePeacefulId(int original) {
        if (HarmonyConfig.peacefulMonsterSpawning && original == Difficulty.PEACEFUL.getId())
            return Difficulty.EASY.getId();

        return original;
    }

}
