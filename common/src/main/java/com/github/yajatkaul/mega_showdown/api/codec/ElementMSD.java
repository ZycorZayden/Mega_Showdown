package com.github.yajatkaul.mega_showdown.api.codec;

import com.cobblemon.mod.common.api.types.ElementalType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record ElementMSD(
        String name,
        String displayName,
        int hue,
        int textureXMultiplier,
        ResourceLocation resourceLocation,
        String showdownId
) {
    public static final Codec<ElementMSD> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(ElementMSD::name),
            Codec.STRING.fieldOf("displayName").forGetter(ElementMSD::displayName),
            Codec.INT.fieldOf("hue").forGetter(ElementMSD::hue),
            Codec.INT.fieldOf("textureXMultiplier").forGetter(ElementMSD::textureXMultiplier),
            ResourceLocation.CODEC.fieldOf("resourceLocation").forGetter(ElementMSD::resourceLocation),
            Codec.STRING.fieldOf("showdownId").forGetter(ElementMSD::showdownId)
    ).apply(instance, ElementMSD::new));

    public static final StreamCodec<ByteBuf, ElementMSD> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8, ElementMSD::name,
                    ByteBufCodecs.STRING_UTF8, ElementMSD::displayName,
                    ByteBufCodecs.INT, ElementMSD::hue,
                    ByteBufCodecs.INT, ElementMSD::textureXMultiplier,
                    ResourceLocation.STREAM_CODEC, ElementMSD::resourceLocation,
                    ByteBufCodecs.STRING_UTF8, ElementMSD::showdownId,
                    ElementMSD::new
            );

    public ElementalType create() {
        return new ElementalType(
                this.name,
                Component.translatable(this.displayName),
                this.hue,
                this.textureXMultiplier,
                this.resourceLocation,
                this.showdownId
        );
    }

    public static ElementMSD create(ElementalType element) {
        return new ElementMSD(
                element.getName(),
                element.getDisplayName().getString(),
                element.getHue(),
                element.getTextureXMultiplier(),
                element.getResourceLocation(),
                element.showdownId()
        );
    }
}
