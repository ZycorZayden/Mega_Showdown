package com.github.yajatkaul.mega_showdown.networking.client.handler;

import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.api.types.tera.TeraType;
import com.cobblemon.mod.common.api.types.tera.elemental.ElementalTypeTeraType;
import com.cobblemon.mod.common.util.MiscUtilsKt;
import com.github.yajatkaul.mega_showdown.mixin.ElementsAccessor;
import com.github.yajatkaul.mega_showdown.mixin.TeraTypesAccessor;
import com.github.yajatkaul.mega_showdown.networking.client.packet.ElementsSyncPacket;
import dev.architectury.networking.NetworkManager;

public class ElementSyncHandler {
    public static void handle(ElementsSyncPacket packet, NetworkManager.PacketContext context) {
        ElementalType type = packet.elementMSD().create();
        ElementsAccessor.getTypes().add(type);
        TeraType newTeraType = new ElementalTypeTeraType(type);
        TeraTypesAccessor.getTypes().put(MiscUtilsKt.cobblemonResource(type.showdownId()), newTeraType);
    }
}