package me.bobthe28th.bmsts.gamebase;

import org.bukkit.Location;

import java.util.HashMap;

public class GameMap {

    String name;
    Location playerSpawn;
    HashMap<GameTeam, Location> minionSpawn = new HashMap<>();

    public GameMap(String name, Location playerSpawn, HashMap<GameTeam, Location> minionSpawn) {
        this.name = name;
        this.playerSpawn = playerSpawn.clone();
        this.minionSpawn.putAll(minionSpawn);
    }

    public Location getPlayerSpawn() {
        return playerSpawn;
    }

    public HashMap<GameTeam, Location> getMinionSpawn() {
        return minionSpawn;
    }
}
