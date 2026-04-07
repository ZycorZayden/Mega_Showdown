package com.github.yajatkaul.mega_showdown.networking.client.packet;

import com.github.yajatkaul.mega_showdown.MegaShowdown;
import com.github.yajatkaul.mega_showdown.api.codec.ElementMSD;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record ElementsSyncPacket(ElementMSD elementMSD) implements CustomPacketPayload {
    public static final ResourceLocation PACKET_ID
            = ResourceLocation.fromNamespaceAndPath(MegaShowdown.MOD_ID, "element_sync");
    public static final Type<ElementsSyncPacket> TYPE = new Type<>(PACKET_ID);

    public static final StreamCodec<ByteBuf, ElementsSyncPacket> STREAM_CODEC = StreamCodec.composite(
            ElementMSD.STREAM_CODEC,
            ElementsSyncPacket::elementMSD,

            ElementsSyncPacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}