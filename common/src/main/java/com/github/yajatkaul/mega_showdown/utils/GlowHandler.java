package com.github.yajatkaul.mega_showdown.utils;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.github.yajatkaul.mega_showdown.api.codec.ZCrystal;
import kotlin.Unit;
import net.minecraft.ChatFormatting;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.scores.PlayerTeam;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class GlowHandler {
    public static void applyDynamaxGlow(@Nullable PokemonEntity pokemonEntity) {
        if (pokemonEntity == null) return;

        if (pokemonEntity.level() instanceof ServerLevel serverLevel) {
            pokemonEntity.setGlowingTag(true);
            ServerScoreboard scoreboard = serverLevel.getScoreboard();

            String teamName;
            ChatFormatting teamColour;
            if (pokemonEntity.getPokemon().getSpecies().getName().equalsIgnoreCase("calyrex")) {
                teamName = "glow_dynamax_blue";
                teamColour = ChatFormatting.BLUE;
            } else {
                teamName = "glow_dynamax_red";
                teamColour = ChatFormatting.RED;
            }

            PlayerTeam team = scoreboard.getPlayerTeam(teamName);
            if (team == null) {
                team = scoreboard.addPlayerTeam(teamName);
                team.setColor(teamColour);
            }

            scoreboard.addPlayerToTeam(pokemonEntity.getScoreboardName(), team);
        }
    }

    public static void applyTeraGlow(PokemonEntity pokemonEntity, String aspect) {
        if (pokemonEntity.level() instanceof ServerLevel serverLevel) {
            pokemonEntity.setGlowingTag(true);
            ServerScoreboard scoreboard = serverLevel.getScoreboard();
            String teamName = "glow_tera_" + pokemonEntity.getPokemon().getTeraType().showdownId();

            PlayerTeam team = scoreboard.getPlayerTeam(teamName);

            ChatFormatting color = getFormattingForColor(aspect);

            if (team == null) {
                team = scoreboard.addPlayerTeam(teamName);
            }

            team.setColor(color);

            scoreboard.addPlayerToTeam(pokemonEntity.getScoreboardName(), team);
        }
    }

    public static void applyZGlow(PokemonEntity pokemon, ZCrystal zCrystal) {
        if (pokemon.level() instanceof ServerLevel serverLevel) {
            pokemon.setGlowingTag(true);
            ServerScoreboard scoreboard = serverLevel.getScoreboard();

            String teamName = "glow_type_" + zCrystal.color().toLowerCase(Locale.ROOT);
            ChatFormatting color = getGlowForColor(zCrystal.color());

            PlayerTeam team = scoreboard.getPlayerTeam(teamName);

            if (team == null) {
                team = scoreboard.addPlayerTeam(teamName);
            }

            team.setColor(color);

            scoreboard.addPlayerToTeam(pokemon.getScoreboardName(), team);

            PlayerTeam finalTeam = team;
            pokemon.after(7f, () -> {
                pokemon.setGlowingTag(false);
                scoreboard.removePlayerFromTeam(pokemon.getScoreboardName(), finalTeam);
                return Unit.INSTANCE;
            });
        }
    }

    private static ChatFormatting getFormattingForColor(String color) {
        return switch (color) {
            case "red" -> ChatFormatting.RED;
            case "blue" -> ChatFormatting.BLUE;
            case "green" -> ChatFormatting.GREEN;
            case "yellow" -> ChatFormatting.YELLOW;
            case "brown" -> ChatFormatting.DARK_RED;
            case "light_blue" -> ChatFormatting.AQUA;
            case "purple" -> ChatFormatting.DARK_BLUE;
            case "magenta" -> ChatFormatting.LIGHT_PURPLE;
            case "black" -> ChatFormatting.BLACK;
            case "gray" -> ChatFormatting.GRAY;
            case "lime" -> ChatFormatting.DARK_GREEN;
            case "indigo" -> ChatFormatting.DARK_PURPLE;
            case "tan" -> ChatFormatting.DARK_GRAY;
            default -> ChatFormatting.WHITE;
        };
    }

    private static ChatFormatting getGlowForColor(String color) {
        if (color == null) return ChatFormatting.WHITE;

        return switch (color.toLowerCase()) {
            case "black" -> ChatFormatting.BLACK;
            case "dark_blue" -> ChatFormatting.DARK_BLUE;
            case "dark_green" -> ChatFormatting.DARK_GREEN;
            case "dark_aqua" -> ChatFormatting.DARK_AQUA;
            case "dark_red" -> ChatFormatting.DARK_RED;
            case "dark_purple" -> ChatFormatting.DARK_PURPLE;
            case "gold" -> ChatFormatting.GOLD;
            case "gray" -> ChatFormatting.GRAY;
            case "dark_gray" -> ChatFormatting.DARK_GRAY;
            case "blue" -> ChatFormatting.BLUE;
            case "green" -> ChatFormatting.GREEN;
            case "aqua" -> ChatFormatting.AQUA;
            case "red" -> ChatFormatting.RED;
            case "light_purple" -> ChatFormatting.LIGHT_PURPLE;
            case "yellow" -> ChatFormatting.YELLOW;
            default -> ChatFormatting.WHITE;
        };
    }
}