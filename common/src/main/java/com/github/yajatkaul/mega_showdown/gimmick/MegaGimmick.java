package com.github.yajatkaul.mega_showdown.gimmick;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.api.storage.pc.PCStore;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.github.yajatkaul.mega_showdown.codec.Effect;
import com.github.yajatkaul.mega_showdown.config.MegaShowdownConfig;
import com.github.yajatkaul.mega_showdown.gimmick.codec.AspectSetCodec;
import com.github.yajatkaul.mega_showdown.tag.MegaShowdownTags;
import com.github.yajatkaul.mega_showdown.utils.AccessoriesUtils;
import com.github.yajatkaul.mega_showdown.utils.AspectUtils;
import com.github.yajatkaul.mega_showdown.utils.RegistryLocator;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record MegaGimmick(
        String showdown_id,
        List<String> pokemons,
        AspectSetCodec aspect_conditions
) {
    public static final Codec<MegaGimmick> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("showdown_id").forGetter(MegaGimmick::showdown_id),
            Codec.list(Codec.STRING).fieldOf("pokemons").forGetter(MegaGimmick::pokemons),
            AspectSetCodec.CODEC.fieldOf("aspect").forGetter(MegaGimmick::aspect_conditions)
    ).apply(instance, MegaGimmick::new));

    public static boolean isMega(Pokemon pokemon) {
        return pokemon.getPersistentData().getBoolean("mega_evolved");
    }

    public static boolean hasMega(ServerPlayer player) {
        if (MegaShowdownConfig.multipleMegas) {
            return false;
        }

        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        PCStore pcStore = Cobblemon.INSTANCE.getStorage().getPC(player);

        for (Pokemon pokemon : playerPartyStore) {
            if (pokemon.getPersistentData().getBoolean("mega_evolved")) {
                return true;
            }
        }

        for (Pokemon pokemon : pcStore) {
            if (pokemon.getPersistentData().getBoolean("mega_evolved")) {
                return true;
            }
        }

        return false;
    }

    public static void megaEvolveInBattle(Pokemon pokemon, BattlePokemon battlePokemon) {
        ItemStack heldItem = pokemon.heldItem();
        MegaGimmick megaGimmick = RegistryLocator.getComponent(MegaGimmick.class, heldItem);

        if (megaGimmick != null || pokemon.getSpecies().getName().equals("Rayquaza")) {
            if (pokemon.getSpecies().getName().equals("Rayquaza")) {
                Effect.getEffect("mega_showdown:mega_evolution").applyEffectsBattle(pokemon, List.of("mega_evolution=mega"), null, battlePokemon);

                AspectUtils.appendRevertDataPokemon(
                        Effect.getEffect("mega_showdown:mega_evolution"),
                        List.of("mega_evolution=none"),
                        pokemon,
                        "battle_end_revert"
                );
            } else {
                Effect.getEffect("mega_showdown:mega_evolution").applyEffectsBattle(pokemon, megaGimmick.aspect_conditions.apply_aspects(), null, battlePokemon);

                AspectUtils.appendRevertDataPokemon(
                        Effect.getEffect("mega_showdown:mega_evolution"),
                        megaGimmick.aspect_conditions.revert_aspects(),
                        pokemon,
                        "battle_end_revert"
                );
            }
        }
    }

    public static void megaToggle(Pokemon pokemon) {
        if (!MegaShowdownConfig.outSideMega || pokemon == null || pokemon.getPersistentData().getBoolean("form_changing")) {
            return;
        }
        if (pokemon.getPersistentData().getBoolean("mega_evolved")) {
            unmegaEvolve(pokemon);
        } else {
            megaEvolve(pokemon);
        }
    }

    private static void megaEvolve(Pokemon pokemon) {
        ItemStack heldItem = pokemon.heldItem();
        MegaGimmick megaGimmick = RegistryLocator.getComponent(MegaGimmick.class, heldItem);

        if (pokemon.getSpecies().getName().equals("Rayquaza")) {
            AspectUtils.appendRevertDataPokemon(
                    Effect.getEffect("mega_showdown:mega_evolution"),
                    List.of("mega_evolution=none"),
                    pokemon,
                    "revert_aspects"
            );
            Effect.getEffect("mega_showdown:mega_evolution").applyEffects(pokemon, List.of("mega_evolution=none"), null);
        } else if (megaGimmick != null){
            AspectUtils.appendRevertDataPokemon(
                    Effect.getEffect("mega_showdown:mega_evolution"),
                    megaGimmick.aspect_conditions.revert_aspects(),
                    pokemon,
                    "revert_aspects"
            );
            Effect.getEffect("mega_showdown:mega_evolution").applyEffects(pokemon, megaGimmick.aspect_conditions.apply_aspects(), null);
        }
        pokemon.getPersistentData().putBoolean("mega_evolved", true);
        pokemon.setTradeable(false);
    }

    private static void unmegaEvolve(Pokemon pokemon) {
        ItemStack heldItem = pokemon.heldItem();
        MegaGimmick megaGimmick = RegistryLocator.getComponent(MegaGimmick.class, heldItem);

        if (pokemon.getSpecies().getName().equals("Rayquaza")) {
            pokemon.getPersistentData().remove("mega_evolved");
            Effect.getEffect("mega_showdown:mega_evolution").revertEffects(pokemon, List.of("mega_evolution=none"), null);
        } else if (megaGimmick != null) {
            pokemon.getPersistentData().remove("mega_evolved");
            Effect.getEffect("mega_showdown:mega_evolution").revertEffects(pokemon, megaGimmick.aspect_conditions.revert_aspects(), null);
        }
        pokemon.setTradeable(true);
    }

    public static boolean canMega(Pokemon pokemon) {
        ServerPlayer player = pokemon.getOwnerPlayer();

        ItemStack heldItem = pokemon.heldItem();
        MegaGimmick megaGimmick = RegistryLocator.getComponent(MegaGimmick.class, heldItem);

        if (!pokemon.getSpecies().getName().equals("Rayquaza") && megaGimmick == null) return false;

        if (player != null && hasMega(player)) {
            return false;
        }

        if (pokemon.getSpecies().getName().equals("Rayquaza")) {
            if (player != null &&
                    !AccessoriesUtils.checkTagInAccessories(player, MegaShowdownTags.Items.MEGA_BRACELET) &&
                    !AccessoriesUtils.checkTagInAccessories(player, MegaShowdownTags.Items.OMNI_RING)) {
                player.displayClientMessage(Component.translatable("message.mega_showdown.no_mega_bracelet")
                        .withStyle(ChatFormatting.RED), true);
                return false;
            }

            boolean found = false;
            for (int i = 0; i < 4; i++) {
                if (pokemon.getMoveSet().getMoves().get(i).getName().equals("dragonascent")) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                if (player != null) {
                    player.displayClientMessage(Component.translatable("message.mega_showdown.rayquaza_no_dragonascent")
                            .withStyle(ChatFormatting.RED), true);
                    return false;
                } else {
                    return true;
                }
            }
        }

        if (megaGimmick == null) return false;

        if (!megaGimmick.aspect_conditions.validate_apply(pokemon)) {
            return false;
        }

        return megaGimmick.pokemons.contains(pokemon.getSpecies().getName());
    }
}
