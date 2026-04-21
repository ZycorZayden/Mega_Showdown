package com.github.yajatkaul.mega_showdown.mixin;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.PlayerTeam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TamableAnimal.class)
public abstract class TamableAnimalMixin extends Animal {
    protected TamableAnimalMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "getTeam", at = @At("HEAD"), cancellable = true)
    private void getTeamInject(CallbackInfoReturnable<PlayerTeam> cir) {
        TamableAnimal tamableAnimal = (TamableAnimal) (Object) this;

        if (tamableAnimal instanceof PokemonEntity) {
            cir.setReturnValue(super.getTeam());
            cir.cancel();
        }
    }
}
