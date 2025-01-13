package dev.symphony.harmony.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.symphony.harmony.Harmony;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.minecraft.registry.RegistryOps;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;

// FEATURE: Configurable Data-Driven Resources
// AUTHORS: Flatkat, WheatFlour 
public record HarmonyConfigCondition(String config_name) implements ResourceCondition {

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    public @interface ResourceConfigName {
        String config_name();
    }

    public static void init(dev.symphony.harmony.config.HarmonyConfig config) {
        Field[] fields = HarmonyConfigModel.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ResourceConfigName.class)) {
                ResourceConfigName configName = field.getAnnotation(ResourceConfigName.class);
                try {
                    // Because of owo lib config model, we use reflection to call the method of the same name as the field from model
                    // as owo lib doesn't copy annotations to the generated class
                    resourceMap.put(configName.config_name(), (Boolean) config.getClass().getDeclaredMethod(field.getName()).invoke(config));
                } catch (Exception e) {
                    Harmony.LOGGER.error("Failed to get resource config condition value for field {}", field.getName());
                    Harmony.LOGGER.error(Arrays.toString(e.getStackTrace()));
                }
            }
        }
    }

    public static HashMap<String, Boolean> resourceMap = new HashMap<>();

    public static final MapCodec<HarmonyConfigCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("config_name").forGetter(condition -> condition.config_name)
    ).apply(instance, HarmonyConfigCondition::new));


    @Override
    public ResourceConditionType<?> getType() {
        return ResourceConditionType.create(Identifier.of(Harmony.MOD_ID, "config"), CODEC);
    }

    @Override
    public boolean test(@Nullable RegistryOps.RegistryInfoGetter registryInfo) {
        return resourceMap.getOrDefault(config_name, false);
    }


}