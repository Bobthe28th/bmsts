package me.bobthe28th.bmsts.gamebase;

import me.bobthe28th.bmsts.Main;
import org.bukkit.Material;
import org.bukkit.block.data.Powerable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class GamePlayer implements Listener {

    Player player;
    GameTeam team;

    public GamePlayer(Player player, Main plugin) {
        this.player = player;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void setTeam(GameTeam team) {
        if (this.team != null) {
            this.team.dropKeptBy(player);
            this.team.getTeam().removeEntry(player.getName());
        }
        this.team = team;
        team.getTeam().addEntry(player.getName());
    }

    public void remove(boolean fromList) {
        if (fromList) {
            Main.gamePlayers.remove(player);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getPlayer() != player) return;
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && team != null && event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.LEVER && event.getClickedBlock().getLocation().equals(team.getReadySwitch())) {
            if (((Powerable) event.getClickedBlock().getBlockData()).isPowered()) {
                if (team.isReady()) {
                    event.setCancelled(true);
                }
            } else {
                team.setReady(true);
            }
            event.getClickedBlock().getState().update(true, true);
        }
    }

    public GameTeam getTeam() {
        return team;
    }
}
