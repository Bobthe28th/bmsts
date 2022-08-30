package me.bobthe28th.bmsts.gamebase.minions;

import me.bobthe28th.bmsts.Main;
import me.bobthe28th.bmsts.gamebase.GameTeam;
import me.bobthe28th.bmsts.gamebase.minions.entities.ChickenEntity;

public class ChickenMinion extends Minion {

    public ChickenMinion(Main plugin, GameTeam team, Rarity rarity, Integer strength) {
        super(plugin, "Chicken", ChickenEntity.class, team, 0, rarity, strength, 5);
    }
}