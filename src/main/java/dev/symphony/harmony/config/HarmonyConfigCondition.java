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

public record HarmonyConfigCondition(String config_name) implements ResourceCondition {

    public static final MapCodec<HarmonyConfigCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("config_name").forGetter(condition -> condition.config_name)
    ).apply(instance, HarmonyConfigCondition::new));




    @Override
    public ResourceConditionType<?> getType() {
        return ResourceConditionType.create(Identifier.of(Harmony.MOD_ID, "config"), CODEC);
    }


    @Override
    public boolean test(@Nullable RegistryWrapper.WrapperLookup registryLookup) {
        return HarmonyConfig.recipeList.contains(config_name);
    }
}