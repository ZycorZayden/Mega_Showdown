package com.github.yajatkaul.mega_showdown.mixin.battle;

import com.cobblemon.mod.common.battles.BattleRegistry;
import com.github.yajatkaul.mega_showdown.utils.AspectUtils;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BattleRegistry.class)
public class BattleRegistryMixin {
    @Inject(method = "onPlayerDisconnect", at = @At(value = "HEAD"), remap = false)
    private void onPlayerDisconnect(ServerPlayer player, CallbackInfo ci) {
        AspectUtils.battleDisconnecter.add(player.getUUID());
    }
}
