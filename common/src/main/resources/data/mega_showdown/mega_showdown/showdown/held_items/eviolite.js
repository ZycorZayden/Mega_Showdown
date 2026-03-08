{
    name: "Eviolite",
    spritenum: 130,
    fling: {
      basePower: 40
    },
    onModifyDefPriority: 2,
    onModifyDef(def, pokemon) {
      const exceptions = ["Pikachu-Original", "Pikachu-Hoenn", "Pikachu-Sinnoh", "Pikachu-Unova", "Pikachu-Kalos", "Pikachu-Alola", "Pikachu-Partner", "Pikachu-World"];
      if (pokemon.baseSpecies.nfe) {
        if (exceptions.includes(pokemon.species.name)) {
          return;
        }
        return this.chainModify(1.5);
      }
    },
    onModifySpDPriority: 2,
    onModifySpD(spd, pokemon) {
      const exceptions = ["Pikachu-Original", "Pikachu-Hoenn", "Pikachu-Sinnoh", "Pikachu-Unova", "Pikachu-Kalos", "Pikachu-Alola", "Pikachu-Partner", "Pikachu-World"];
      if (pokemon.baseSpecies.nfe) {
        if (exceptions.includes(pokemon.species.name)) {
          return;
        }
        return this.chainModify(1.5);
      }
    },
    num: 538,
    gen: 5
}