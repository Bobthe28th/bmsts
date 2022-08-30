package me.bobthe28th.bmsts.gamebase.minions;

import me.bobthe28th.bmsts.Main;
import me.bobthe28th.bmsts.gamebase.GameTeam;
import me.bobthe28th.bmsts.gamebase.minions.entities.SilverfishEntity;

public class SilverfishMinion extends Minion {

    public SilverfishMinion(Main plugin, GameTeam team, Rarity rarity, Integer strength) {
        super(plugin, "Silverfish", SilverfishEntity.class, team, 0, rarity, strength, 2);
    }
}