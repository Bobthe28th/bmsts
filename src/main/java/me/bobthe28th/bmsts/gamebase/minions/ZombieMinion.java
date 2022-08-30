package me.bobthe28th.bmsts.gamebase.minions;

import me.bobthe28th.bmsts.Main;
import me.bobthe28th.bmsts.gamebase.GameTeam;
import me.bobthe28th.bmsts.gamebase.minions.entities.ZombieEntity;

public class ZombieMinion extends Minion {

    public ZombieMinion(Main plugin, GameTeam team, Rarity rarity, Integer strength) {
        super(plugin, "Zombie", ZombieEntity.class, team, 1, rarity, strength, 1);
    }
}
