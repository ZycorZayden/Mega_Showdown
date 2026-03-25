package com.github.yajatkaul.mega_showdown.mixin.client.ui;

import com.cobblemon.mod.common.api.moves.MoveTemplate;
import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.client.gui.battle.subscreen.BattleMoveSelection;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.github.yajatkaul.mega_showdown.client.battle.hud.MovePreviewWidget;
import com.github.yajatkaul.mega_showdown.config.MegaShowdownConfig;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BattleMoveSelection.MoveTile.class)
public abstract class MoveTileMixin {
    @Shadow
    public abstract boolean isHovered(double mouseX, double mouseY);

    @Shadow private MoveTemplate moveTemplate;
    @Shadow @Final private Pokemon pokemon;
    @Shadow @Final private ElementalType elementalType;

    @Unique private MovePreviewWidget mega_showdown$previewWidget;

    @Inject(method = "render", at = @At("TAIL"))
    private void addPreview (GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo info) {
        if (this.isHovered(mouseX, mouseY) && MegaShowdownConfig.showMoveInspector) {
            if (this.mega_showdown$previewWidget == null) this.mega_showdown$previewWidget = new MovePreviewWidget(this.moveTemplate);
            this.mega_showdown$previewWidget.setSTAB(this.pokemon, this.elementalType);
            this.mega_showdown$previewWidget.render(context, mouseX, mouseY, delta);
        }
    }
}
