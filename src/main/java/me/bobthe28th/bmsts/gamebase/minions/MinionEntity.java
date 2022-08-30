package me.bobthe28th.bmsts.gamebase.minions;

import me.bobthe28th.bmsts.gamebase.GameTeam;

public interface MinionEntity {
    GameTeam getGameTeam();

    boolean isPreview();
}
