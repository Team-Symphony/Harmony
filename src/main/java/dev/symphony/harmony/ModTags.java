package dev.symphony.harmony;

import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModTags {
    // Add documentation abt data-driven vehicles tag maybe :P
    public static final TagKey<EntityType<?>> VEHICLES = TagKey.of(RegistryKeys.ENTITY_TYPE, Harmony.id("vehicles"));
}
