package com.github.yajatkaul.mega_showdown.gimmick.codec;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record AspectSetCodec(
        List<String> required_forms_apply,
        List<String> blacklisted_forms_revert,
        List<String> required_forms_revert,
        List<String> blacklisted_forms_apply,
        List<List<String>> required_aspects_apply,
        List<List<String>> blacklist_aspects_apply,
        List<List<String>> required_aspects_revert,
        List<List<String>> blacklist_aspects_revert,
        List<String> apply_aspects,
        List<String> revert_aspects
) {
    public static final Codec<AspectSetCodec> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.list(Codec.STRING).optionalFieldOf("required_forms_apply", List.of()).forGetter(AspectSetCodec::required_forms_apply),
            Codec.list(Codec.STRING).optionalFieldOf("blacklisted_forms_revert", List.of()).forGetter(AspectSetCodec::blacklisted_forms_revert),
            Codec.list(Codec.STRING).optionalFieldOf("required_forms_revert", List.of()).forGetter(AspectSetCodec::required_forms_revert),
            Codec.list(Codec.STRING).optionalFieldOf("blacklisted_forms_apply", List.of()).forGetter(AspectSetCodec::blacklisted_forms_apply),
            Codec.list(Codec.list(Codec.STRING)).optionalFieldOf("required_aspects_apply", List.of()).forGetter(AspectSetCodec::required_aspects_apply),
            Codec.list(Codec.list(Codec.STRING)).optionalFieldOf("blacklist_aspects_apply", List.of()).forGetter(AspectSetCodec::blacklist_aspects_apply),
            Codec.list(Codec.list(Codec.STRING)).optionalFieldOf("required_aspects_revert", List.of()).forGetter(AspectSetCodec::required_aspects_revert),
            Codec.list(Codec.list(Codec.STRING)).optionalFieldOf("blacklist_aspects_revert", List.of()).forGetter(AspectSetCodec::blacklist_aspects_revert),
            Codec.list(Codec.STRING).optionalFieldOf("apply_aspects", List.of()).forGetter(AspectSetCodec::apply_aspects),
            Codec.list(Codec.STRING).optionalFieldOf("revert_aspects", List.of()).forGetter(AspectSetCodec::revert_aspects)
    ).apply(instance, AspectSetCodec::new));

    public boolean validate_apply(Pokemon pokemon) {
        if (!blacklist_aspects_apply.isEmpty() && blacklist_aspects_apply.stream().anyMatch(group -> pokemon.getAspects().containsAll(group)))
            return false;
        if (!blacklisted_forms_apply.isEmpty() && blacklisted_forms_apply.contains(pokemon.getForm().getName()))
            return false;
        if (!required_forms_apply.isEmpty() && !required_forms_apply.contains(pokemon.getForm().getName()))
            return false;
        return required_aspects_apply.isEmpty() || required_aspects_apply.stream().anyMatch(group -> pokemon.getAspects().containsAll(group));
    }

    public boolean validate_revert(Pokemon pokemon) {
        if (!blacklist_aspects_revert.isEmpty() && blacklist_aspects_revert.stream().anyMatch(group -> pokemon.getAspects().containsAll(group)))
            return false;
        if (!blacklisted_forms_revert.isEmpty() && blacklisted_forms_revert.contains(pokemon.getForm().getName()))
            return false;
        if (!required_forms_apply.isEmpty() && !required_forms_revert.contains(pokemon.getForm().getName()))
            return false;
        return required_aspects_revert.isEmpty() || required_aspects_revert.stream().anyMatch(group -> pokemon.getAspects().containsAll(group));
    }

    public static AspectSetCodec DEFAULT() {
        return new AspectSetCodec(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of()
        );
    }
}
