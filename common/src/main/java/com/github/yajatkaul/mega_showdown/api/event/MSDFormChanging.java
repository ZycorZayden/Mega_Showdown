package com.github.yajatkaul.mega_showdown.api.event;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.pokemon.Pokemon;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import org.jetbrains.annotations.Nullable;

public interface MSDFormChanging {
    Event<MSDFormChanging> EVENT = EventFactory.createLoop(MSDFormChanging.class);

    void onFormChange(FormChangeEvent event);

    class FormChangeEvent {
        public final Pokemon context;
        public final PokemonBattle battle;
        public final State state;

        private boolean cancelled = false;

        public FormChangeEvent(Pokemon context, @Nullable PokemonBattle battle, State state) {
            this.context = context;
            this.battle = battle;
            this.state = state;
        }

        public void cancel() {
            this.cancelled = true;
        }

        public boolean isCancelled() {
            return cancelled;
        }
    }

    enum State {
        Apply,
        Revert
    }
}