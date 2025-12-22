package com.github.yajatkaul.mega_showdown.mixin;

import com.cobblemon.mod.common.api.pokemon.feature.SpeciesFeatureAssignments;
import com.cobblemon.mod.common.battles.ShowdownMoveset;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.github.yajatkaul.mega_showdown.gimmick.GimmickTurnCheck;
import com.github.yajatkaul.mega_showdown.gimmick.MegaGimmick;
import com.github.yajatkaul.mega_showdown.gimmick.UltraGimmick;
import com.github.yajatkaul.mega_showdown.networking.client.packet.InteractionWheelPacket;
import com.github.yajatkaul.mega_showdown.tag.MegaShowdownTags;
import com.github.yajatkaul.mega_showdown.utils.AccessoriesUtils;
import dev.architectury.networking.NetworkManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@Mixin(value = PokemonEntity.class, remap = false)
public abstract class PokemonEntityMixin {
    @Inject(
            method = "recallWithAnimation",
            at = @At("HEAD"),
            cancellable = true
    )
    private void cancelRecallDuringEvolution(CallbackInfoReturnable<CompletableFuture<Pokemon>> cir) {
        PokemonEntity self = (PokemonEntity) (Object) this;

        boolean form_changing = self.getPokemon().getPersistentData().getBoolean("form_changing");
        if (form_changing) {
            CompletableFuture<Pokemon> future = new CompletableFuture<>();
            future.complete(self.getPokemon());
            cir.setReturnValue(future);
        }
    }

    @Inject(
            method = "showInteractionWheel",
            at = @At("HEAD")
    )
    private void showInteractionWheelInject(ServerPlayer player, ItemStack itemStack, CallbackInfo ci) {
        PokemonEntity self = (PokemonEntity) (Object) this;
        Pokemon pokemon = self.getPokemon();

        if (pokemon.getOwnerPlayer() == player) {
            boolean shouldPokemonMega = SpeciesFeatureAssignments.getFeatures(pokemon.getSpecies()).contains("mega_evolution");
            boolean shouldPokemonUltra = pokemon.getSpecies().getName().equals("Necrozma");

            boolean hasMegaAccessory = AccessoriesUtils.checkTagInAccessories(player, MegaShowdownTags.Items.MEGA_BRACELET) || AccessoriesUtils.checkTagInAccessories(player, MegaShowdownTags.Items.OMNI_RING);
            boolean hasUltraAccessory = GimmickTurnCheck.hasGimmick(ShowdownMoveset.Gimmick.Z_POWER, player);

            boolean canPokemonMega = MegaGimmick.isMega(pokemon) || MegaGimmick.canMega(pokemon) && hasMegaAccessory;
            boolean canPokemonUltra = UltraGimmick.isUltra(pokemon) || UltraGimmick.canUltraBurst(pokemon) && hasUltraAccessory;

            NetworkManager.sendToPlayer(player, new InteractionWheelPacket(shouldPokemonMega, shouldPokemonUltra, canPokemonMega, canPokemonUltra));
        }
    }
}