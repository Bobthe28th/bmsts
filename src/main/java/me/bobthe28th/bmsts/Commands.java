package me.bobthe28th.bmsts;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Commands implements CommandExecutor {

    Main plugin;
    public Commands(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        if (!(sender instanceof Player player)) {
            return true;
        }

        switch (cmd.getName().toLowerCase()) {
            case "jointeam":
                if (args.length >= 1) {
                    if (Main.gameTeams.containsKey(args[0])) {
                        Main.gamePlayers.get(player).setTeam(Main.gameTeams.get(args[0]));
                    }
                }
                return true;
        }

        return false;
    }

}
