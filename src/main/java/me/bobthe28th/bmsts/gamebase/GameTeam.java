package me.bobthe28th.bmsts.gamebase;

import me.bobthe28th.bmsts.Main;
import me.bobthe28th.bmsts.gamebase.bonusrounds.survive.SurviveRound;
import me.bobthe28th.bmsts.gamebase.minions.Minion;
import me.bobthe28th.bmsts.gamebase.minions.Rarity;
import me.bobthe28th.bmsts.gamebase.minions.SilverfishMinion;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class GameTeam {

    Main plugin;
    Team team;
    boolean ready = false;
    boolean dead = false;

    //Locations
    Location playerSpawn;
    Location randomizer;
    Location techUpgrade;
    List<Location> spawners = new ArrayList<>();
    Location minionItemSpawn;
    Location readySwitch;

    //Minons
    ArrayList<Minion> minions = new ArrayList<>();

    Color bColor;
    ChatColor color;
    ChatColor darkColor;

    int researchPoints = 400;

    public GameTeam(String name, Color bColor, ChatColor color, ChatColor darkColor, Main plugin, Location playerSpawn, Location randomizer, Location techUpgrade, List<Location> spawners, Location readySwitch, Location minionItemSpawn) {
        this.plugin = plugin;
        this.playerSpawn = playerSpawn.clone();
        this.randomizer = randomizer.clone();
        this.techUpgrade = techUpgrade.clone();
        for (Location l : spawners) {
            this.spawners.add(l.clone());
        }
        this.bColor = bColor;
        this.color = color;
        this.darkColor = darkColor;
        this.readySwitch = readySwitch.clone();
        this.minionItemSpawn = minionItemSpawn.clone();
        if (readySwitch.getBlock().getType() == Material.LEVER) {
            readySwitch.getBlock().setBlockData(readySwitch.getBlock().getBlockData().merge(Bukkit.getServer().createBlockData("minecraft:lever[powered=false]")));
        }
        team = Main.board.registerNewTeam("game" + name);
        team.setDisplayName(name.substring(0, 1).toUpperCase() + name.substring(1) + " Team");
        team.setColor(color);
        team.setAllowFriendlyFire(false);
        team.setCanSeeFriendlyInvisibles(true);
        team.setPrefix(ChatColor.DARK_GRAY + "[" + color + Character.toUpperCase(name.charAt(0)) + ChatColor.DARK_GRAY + "]" + " ");
        new SilverfishMinion(plugin, this, Rarity.COMMON,1).drop(minionItemSpawn.clone().add(0.5,0,0.5));
        new SilverfishMinion(plugin, this, Rarity.RARE,2).drop(minionItemSpawn.clone().add(-0.5,0,0.5));
        new SilverfishMinion(plugin, this, Rarity.GODLIKE,3).drop(minionItemSpawn.clone().add(-1.5,0,0.5));
        new SilverfishMinion(plugin, this, Rarity.AWESOME,1).drop(minionItemSpawn.clone().add(-2.5,0,0.5));
    }

    public int getResearchPoints() {
        return researchPoints;
    }

    public void addResearchPoints(int amount) {
        this.researchPoints += amount;
    }

    public void removeMinions() {
        for (Minion minion : minions) {
            minion.remove(false);
        }
        minions.clear();
    }

    public void minionDeath() {
        for (Minion m : minions) {
            if (m.getEntities().size() != 0) return;
        }
        dead = true;
        Bukkit.broadcastMessage(ChatColor.GRAY + "[" + team.getColor() + "☠" + ChatColor.GRAY + "] " + team.getColor() + team.getDisplayName() + " dead");
        for (GamePlayer p : Main.gamePlayers.values()) {
            p.getPlayer().sendTitle("",team.getColor() + team.getDisplayName() + " dead",10,20,10);
        }
        GameTeam winner = null;
        for (GameTeam t : Main.gameTeams.values()) {
            if (!t.isDead()) {
                if (winner == null) {
                    winner = t;
                } else {
                    return;
                }
            }
        }
        if (winner != null) {
            for (GameTeam t : Main.gameTeams.values()) {
                t.removeEntities();
            }
            Bukkit.broadcastMessage(ChatColor.GRAY + "[" + winner.getTeam().getColor() + "✪" + ChatColor.GRAY + "] " + winner.getTeam().getColor() + winner.getTeam().getDisplayName() + " won the round");
            for (GamePlayer p : Main.gamePlayers.values()) {
                p.getPlayer().sendTitle("", winner.getTeam().getColor() + winner.getTeam().getDisplayName() + " won the round", 10, 20, 10);
                p.getPlayer().teleport(p.getTeam().getPlayerSpawn().clone().add(0.5,0,0.5));
            }
            Main.setRound(Main.getRound() + 1);
            if (Main.getRound() % 2 == 1) {
                Main.currentBonusRound = new SurviveRound(plugin);
                Main.currentBonusRound.start();
            }
        }
    }

    public void removeEntities() {
        for (Minion m : minions) {
            m.removeEntities();
        }
    }

    public void addMinion(Minion m) {
        minions.add(m);
    }

    public void removeMinion(Minion m) {
        minions.remove(m);
    }

    public boolean isDead() {
        return dead;
    }

    public boolean isReady() {
        return ready;
    }

    public void spawnAll(Location l) {
        if (minions.size() > 0) {
            dead = false;
        }
        for (Minion m : minions) {
            if (m.getPlacedLoc() != null && spawners.contains(m.getPlacedLoc())) {
                m.spawnGroup(l); //TODO suffocate
            }
        }
    }

    public void dropKept() { //TODO replace with place
        int offset = 0;
        for (Minion m : minions) {
            if (m.dropKept(minionItemSpawn.clone().add(0.5 - offset,0,0.5))) {
                offset ++;
            }
        }
    }

    public void dropKeptBy(Player p) { //TODO replace with place
        int offset = 0;
        for (Minion m : minions) {
            if (m.dropKeptBy(p, minionItemSpawn.clone().add(0.5 - offset,0,0.5))) {
                offset ++;
            }
        }
    }

    public void showTargets() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Minion m : minions) {
                    m.showTargets();
                }
            }
        }.runTaskTimer(plugin,0,5);

    }

    public void setReady(boolean ready) {
        this.ready = ready;
        if (ready) {
            boolean allReady = true;
            Bukkit.broadcastMessage(ChatColor.GRAY + "[" + team.getColor() + "✔" + ChatColor.GRAY + "] " + team.getColor() + team.getDisplayName() + " ready");
            for (GamePlayer p : Main.gamePlayers.values()) {
                p.getPlayer().sendTitle("", team.getColor() + team.getDisplayName() + " ready", 10, 20, 10);
            }
            for (GameTeam g : Main.gameTeams.values()) {
                if (!g.isReady()) {
                    allReady = false;
                    break;
                }
            }
            if (allReady) {

                for (GameTeam g : Main.gameTeams.values()) {
                    g.dropKept();
                    g.setReady(false);
                }

                for (GamePlayer p : Main.gamePlayers.values()) {
                    p.getPlayer().teleport(Main.getCurrentMap().getPlayerSpawn().clone().add(0.5, 0, 0.5));
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (GameTeam g : Main.gameTeams.values()) {
                            g.spawnAll(Main.getCurrentMap().getMinionSpawn().get(g));
                            if (g.getReadySwitch().getBlock().getType() == Material.LEVER) {
                                g.getReadySwitch().getBlock().setBlockData(g.getReadySwitch().getBlock().getBlockData().merge(Bukkit.getServer().createBlockData("minecraft:lever[powered=false]")));
                            }
//                        g.showTargets();
                        }
                    }
                }.runTaskLater(plugin, 40);
            }
        }
    }

    public Location getPlayerSpawn() {
        return playerSpawn;
    }

    public Location getReadySwitch() {
        return readySwitch;
    }

    public Team getTeam() {
        return team;
    }

    public List<Location> getSpawners() {
        return spawners;
    }

    public Location getRandomizer() {
        return randomizer;
    }

    public Location getTechUpgrade() {
        return techUpgrade;
    }

    public Color getBColor() {
        return bColor;
    }

    public ChatColor getColor() {
        return color;
    }

    public ChatColor getDarkColor() {
        return darkColor;
    }
}
