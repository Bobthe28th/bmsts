package me.bobthe28th.bmsts.gamebase.minions;

import me.bobthe28th.bmsts.Main;
import me.bobthe28th.bmsts.gamebase.GameTeam;
import me.bobthe28th.bmsts.gamebase.minions.entities.LlamaEntity;

public class LlamaMinion extends Minion {
    public LlamaMinion(Main plugin, GameTeam team, Rarity rarity, Integer strength) {
        super(plugin, "Llama", LlamaEntity.class, team, 0, rarity, strength, 4);
    }
}
