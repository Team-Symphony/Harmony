package dev.symphony.harmony.entity.equipment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.symphony.harmony.registry.HarmonyRegistries;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.entry.RegistryElementCodec;
import net.minecraft.registry.entry.RegistryEntry;

public record Equipment (HorseEquipment horseEquipment) {

    // Codecs
    public static final Codec<Equipment> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        HorseEquipment.CODEC.fieldOf("horse").forGetter(Equipment::horseEquipment)
    ).apply(instance, Equipment::new));

    public static final PacketCodec<RegistryByteBuf, Equipment> PACKET_CODEC = PacketCodec.tuple(
        HorseEquipment.PACKET_CODEC, Equipment::horseEquipment,
        Equipment::new
    );
    public static final Codec<RegistryEntry<Equipment>> ENTRY_CODEC = RegistryElementCodec.of(
        HarmonyRegistries.EQUIPMENT_KEY, CODEC
    );
    public static final PacketCodec<RegistryByteBuf, RegistryEntry<Equipment>> ENTRY_PACKET_CODEC = PacketCodecs.registryEntry(
        HarmonyRegistries.EQUIPMENT_KEY, PACKET_CODEC
    );

    // Getters
    public HorseEquipment horseEquipment() { return this.horseEquipment; }

}
