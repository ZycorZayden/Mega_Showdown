package com.github.yajatkaul.mega_showdown.mixin.packet;

import com.cobblemon.mod.common.CobblemonNetwork;
import com.cobblemon.mod.common.api.net.NetworkPacket;
import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.net.messages.client.data.MovesRegistrySyncPacket;
import com.github.yajatkaul.mega_showdown.api.codec.ElementMSD;
import com.github.yajatkaul.mega_showdown.datapack.CustomTypeRegistry;
import com.github.yajatkaul.mega_showdown.networking.client.packet.ElementsSyncPacket;
import dev.architectury.networking.NetworkManager;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CobblemonNetwork.class)
public class CobblemonNetworkMixin {
    @Inject(
            method = "sendPacketToPlayer",
            at = @At("HEAD")
    )
    private static void mega_showdown$injectElementSync(
            ServerPlayer player, NetworkPacket<?> packet, CallbackInfo ci
    ) {
        if (packet instanceof MovesRegistrySyncPacket) {
            for (ElementalType elementType : CustomTypeRegistry.customTypes.values()) {
                NetworkManager.sendToPlayer(
                        player,
                        new ElementsSyncPacket(ElementMSD.create(elementType))
                );
            }
        }
    }
}