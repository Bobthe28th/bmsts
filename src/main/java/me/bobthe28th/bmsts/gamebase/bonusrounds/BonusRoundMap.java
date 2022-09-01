package me.bobthe28th.bmsts.gamebase.bonusrounds;

import org.bukkit.Location;

public class BonusRoundMap {

    Location playerRespawn;

    public BonusRoundMap(Location playerRespawn) {
        this.playerRespawn = playerRespawn;
    }

    public Location getPlayerRespawn() {
        return playerRespawn;
    }
}
