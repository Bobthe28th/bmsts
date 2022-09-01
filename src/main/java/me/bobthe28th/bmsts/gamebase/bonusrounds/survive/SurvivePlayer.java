package me.bobthe28th.bmsts.gamebase.bonusrounds.survive;

import me.bobthe28th.bmsts.gamebase.GamePlayer;

public class SurvivePlayer {

    GamePlayer player;
    boolean alive = true;

    public SurvivePlayer(GamePlayer player) {
        this.player = player;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
