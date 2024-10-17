package dev.symphony.harmony.entity.equipment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.dynamic.Codecs;

public record HorseEquipment (float preventBuckingChance) {

    // Codecs
    public static final Codec<HorseEquipment> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codecs.POSITIVE_FLOAT.fieldOf("prevent_bucking_chance").forGetter(HorseEquipment::preventBuckingChance)
    ).apply(instance, HorseEquipment::new));

    public static final PacketCodec<RegistryByteBuf, HorseEquipment> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.FLOAT, HorseEquipment::preventBuckingChance,
        HorseEquipment::new
    );

    // Getters
    public float preventBuckingChance() { return this.preventBuckingChance; }

}
