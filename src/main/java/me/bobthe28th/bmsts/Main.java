package me.bobthe28th.bmsts;

import me.bobthe28th.bmsts.gamebase.GamePlayer;
import me.bobthe28th.bmsts.gamebase.GameTeam;
import me.bobthe28th.bmsts.gamebase.minions.*;
import net.minecraft.world.entity.Entity;
import org.bukkit.*;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Arrays;
import java.util.HashMap;

public class Main extends JavaPlugin implements Listener {

    public static Scoreboard board;

    //Game hashmaps
    public static HashMap<String, GameTeam> gameTeams = new HashMap<>();
    public static HashMap<Player, GamePlayer> gamePlayers = new HashMap<>();

    //Colors
    public static ChatColor[] strengthColor = new ChatColor[]{ChatColor.WHITE,ChatColor.YELLOW,ChatColor.GOLD};
    public static ChatColor[] techLevelColor = new ChatColor[]{ChatColor.RED,ChatColor.AQUA,ChatColor.GREEN,ChatColor.LIGHT_PURPLE,ChatColor.WHITE,ChatColor.BLACK};

    //Minions
    public static Class<?>[][] minionTypes = new Class<?>[][]{{SilverfishMinion.class, LlamaMinion.class, ChickenMinion.class},{ZombieMinion.class},{PillagerMinion.class}};

    FileConfiguration config = getConfig();

    @Override
    public void onEnable() {

        this.saveDefaultConfig();

        getServer().getPluginManager().registerEvents(this, this);
        Bukkit.broadcastMessage("h");

        //Commands
        Commands commands = new Commands(this);
        TabCompletion tabCompleter = new TabCompletion();

        String[] commandNames = new String[]{"jointeam"};

        for (String commandName : commandNames) {
            PluginCommand command = getCommand(commandName);
            if (command != null) {
                command.setExecutor(commands);
                command.setTabCompleter(tabCompleter);
            }
        }

        //Teams
        if (Bukkit.getScoreboardManager() != null) {
            board = Bukkit.getScoreboardManager().getMainScoreboard();
        }

        for (Team t : board.getTeams()) {
            if (t.getName().startsWith("game")) {
                t.unregister();
            }
        }

        World w = getServer().getWorld("world");
        Location[] l = new Location[]{new Location(w,261, 95, -234),new Location(w,267, 95, -234),new Location(w,273, 95, -234),new Location(w,279, 95, -234)};
        gameTeams.put("blue",new GameTeam("blue", Color.BLUE,ChatColor.BLUE, ChatColor.DARK_BLUE, this, new Location(getServer().getWorld("world"), 284, 95, -228),new Location(getServer().getWorld("world"), 284, 95, -225), Arrays.asList(l), new Location(w,284, 95, -230), new Location(w,242, 95, -227), new Location(w,279, 95, -223)));
        for (Location s : l) {
            s.add(0,0,-17);
        }
        gameTeams.put("red",new GameTeam("red", Color.RED,ChatColor.RED, ChatColor.DARK_RED, this, new Location(getServer().getWorld("world"), 284, 95, -245),new Location(getServer().getWorld("world"), 284, 95, -242), Arrays.asList(l), new Location(w,284, 95, -247), new Location(w,252, 95, -252), new Location(w,279, 95, -240)));

        //Update players
        for (Player p : Bukkit.getOnlinePlayers()) {
            gamePlayers.put(p, new GamePlayer(p, this));
        }
    }

    @Override
    public void onDisable() {
        for (GameTeam team : gameTeams.values()) {
            team.removeMinions();
        }
    }

    public static String getHealthString(double health, ChatColor fullColor, ChatColor halfColor) {
        int fullHearts = (int) Math.floor(health / 2);
        boolean halfHeart = health - (fullHearts * 2) >= 1;
        StringBuilder healthString = new StringBuilder();
        for (int i = 0; i < fullHearts; i++) {
            healthString.append(fullColor).append("♥");
        }
        if (halfHeart) {
            healthString.append(halfColor).append("♥");
        }
        return healthString.toString();
    }

    public static String rainbow(String s) {
        StringBuilder newString = new StringBuilder();
        for (char c : s.toCharArray()) {
            newString.append(randomColor()).append(c);
        }
        return newString.toString();
    }

    static char[] rainbowChars = "c6ea9b5".toCharArray();
    static int rainbowCharPos = 0;
    public static ChatColor randomColor() {
        rainbowCharPos++;
        if (rainbowCharPos >= rainbowChars.length) {
            rainbowCharPos = 0;
        }
        return ChatColor.getByChar(rainbowChars[rainbowCharPos]);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity damager;
        if (event.getDamager() instanceof Projectile p && p.getShooter() instanceof org.bukkit.entity.Entity pe) {
            damager = ((CraftEntity)pe).getHandle();
        } else {
            damager = ((CraftEntity)event.getDamager()).getHandle();
        }

        if (((CraftEntity)event.getEntity()).getHandle() instanceof MinionEntity e && damager instanceof MinionEntity d) {
            if (e.getGameTeam() == d.getGameTeam()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        gamePlayers.put(event.getPlayer(), new GamePlayer(event.getPlayer(), this));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        gamePlayers.get(event.getPlayer()).remove(true);
    }

}
