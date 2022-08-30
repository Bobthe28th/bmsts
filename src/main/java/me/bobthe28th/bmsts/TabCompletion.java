package me.bobthe28th.bmsts;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TabCompletion implements TabCompleter {

//    List<String> playerList() {
//        List<String> playerNames = new ArrayList<>();
//        Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
//        Bukkit.getServer().getOnlinePlayers().toArray(players);
//        for (Player player : players) {
//            playerNames.add(player.getName());
//        }
//        return playerNames;
//    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        switch (cmd.getName().toLowerCase()) {
            case "jointeam":
                if (args.length == 1) {
                    return new ArrayList<>(Main.gameTeams.keySet());
                }
                break;
        }

        return new ArrayList<>();
    }
}
