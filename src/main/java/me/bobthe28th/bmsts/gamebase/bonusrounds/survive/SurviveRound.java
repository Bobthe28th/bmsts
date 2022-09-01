package me.bobthe28th.bmsts.gamebase.bonusrounds.survive;

import me.bobthe28th.bmsts.Main;
import me.bobthe28th.bmsts.gamebase.GamePlayer;
import me.bobthe28th.bmsts.gamebase.bonusrounds.BonusRound;
import me.bobthe28th.bmsts.gamebase.bonusrounds.BonusRoundMap;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Ravager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;

public class SurviveRound extends BonusRound implements Listener {

    Main plugin;

    HashMap<Player,SurvivePlayer> players = new HashMap<>();
    ArrayList<Ravager> ravagers = new ArrayList<>();
    BukkitTask spawn;
    BukkitTask timer;
    int time = 90;

    public SurviveRound(Main plugin) {
        super(new BonusRoundMap(new Location(plugin.getServer().getWorld("world"),232.5, 101, -229.5)));
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void start() {
        World w = plugin.getServer().getWorld("world");
        if (w != null) {
            running = true;
            for (GamePlayer p : Main.gamePlayers.values()) {
                p.getPlayer().teleport(new Location(w, 232.5, 95, -229.5));
                players.put(p.getPlayer(),new SurvivePlayer(p));
            }
            spawn = new BukkitRunnable() {
                @Override
                public void run() {
                    if (ravagers.size() < 10 && !this.isCancelled()) {
                        ravagers.add((Ravager) w.spawnEntity(new Location(w, 232.5, 95, -229.5), EntityType.RAVAGER));
                    } else {
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin, 100, 100);
            timer = new BukkitRunnable() {
                @Override
                public void run() {
                    if (!this.isCancelled()) {
                        for (GamePlayer p : Main.gamePlayers.values()) {
                            p.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.LIGHT_PURPLE + "" + time));
                        }
                        if (time <= 0) {
                            this.cancel();
                            end(true);
                        }
                        time--;
                    }
                }
            }.runTaskTimer(plugin, 0, 20);
        }
    }

    @Override
    public void end(boolean teleport) {
        running = false;
        timer.cancel();
        spawn.cancel();
        for (Ravager r : ravagers) {
            r.remove();
        }
        ravagers.clear();
        if (teleport) {
            for (GamePlayer p : Main.gamePlayers.values()) {
                p.getPlayer().teleport(p.getTeam().getPlayerSpawn().clone().add(0.5, 0, 0.5));
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player p) {
            if (p.getHealth() - event.getDamage() <= 0) {
                event.setCancelled(true);
                if (players.containsKey(p)) {
                    players.get(p).setAlive(false);
                }
                event.getEntity().teleport(getMap().getPlayerRespawn()); //TODO end
                boolean allDead = true;
                for (SurvivePlayer s : players.values()) {
                    if (s.isAlive()) {
                        allDead = false;
                        break;
                    }
                }
                if (allDead) {
                    end(true);
                }
            }
        }
    }
}
