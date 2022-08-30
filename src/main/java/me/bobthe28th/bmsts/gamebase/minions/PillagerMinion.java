package me.bobthe28th.bmsts.gamebase.minions;

import me.bobthe28th.bmsts.Main;
import me.bobthe28th.bmsts.gamebase.GameTeam;
import me.bobthe28th.bmsts.gamebase.minions.entities.PillagerEntity;

public class PillagerMinion extends Minion {
    public PillagerMinion(Main plugin, GameTeam team, Rarity rarity, Integer strength) {
        super(plugin, "Pillager", PillagerEntity.class, team, 2, rarity, strength, 3);
    }
}
