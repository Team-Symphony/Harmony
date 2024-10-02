package dev.symphony.harmony.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.symphony.harmony.Harmony;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public record HarmonyConfigCondition(String config_name) implements ResourceCondition {

    public static Map<String, Boolean> resourceMap = Map.of(
            "stick", HarmonyConfig.enableStickRecipe,
            "stone_stick", HarmonyConfig.enableStoneStickRecipe
    );

    public static final MapCodec<HarmonyConfigCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("config_name").forGetter(condition -> condition.config_name)
    ).apply(instance, HarmonyConfigCondition::new));




    @Override
    public ResourceConditionType<?> getType() {
        return ResourceConditionType.create(Identifier.of(Harmony.MOD_ID, "config"), CODEC);
    }


    @Override
    public boolean test(@Nullable RegistryWrapper.WrapperLookup registryLookup) {
        return resourceMap.getOrDefault(config_name, false);
    }
}