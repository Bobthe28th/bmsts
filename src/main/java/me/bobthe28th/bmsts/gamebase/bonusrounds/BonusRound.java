package me.bobthe28th.bmsts.gamebase.bonusrounds;

public abstract class BonusRound {

    BonusRoundMap map;
    public boolean running;

    public BonusRound(BonusRoundMap map) {
        this.map = map;
    }

    public abstract void start();

    public abstract void end(boolean teleport);

    public BonusRoundMap getMap() {
        return map;
    }

    public boolean isRunning() {
        return running;
    }
}
