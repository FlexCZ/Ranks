package com.codenameflip.valiconrankaddon.commands;

import com.codenameflip.valiconrankaddon.Core;
import com.codenameflip.valiconrankaddon.RankAPI;
import com.codenameflip.valiconrankaddon.Ranks;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

/*
  This class was created by @codenameflip on 6/13/16
*/

public class RankCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof ConsoleCommandSender){
            if (strings.length != 2){
                commandSender.sendMessage("[CONSOLE] -> Invalid amount of arguments when running /rank");
            } else {
                String target = strings[0];
                Ranks desired = Ranks.format(strings[1]);

                if (desired == null){
                    commandSender.sendMessage("[CONSOLE] -> Invalid rank input for /rank command");
                    return false;
                }

                try {
                    RankAPI.setRank(target, desired);
                    commandSender.sendMessage("[CONSOLE] -> Updated rank of " + target + " to " + desired.getDisplayName());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return true;
        } else if (commandSender instanceof Player){
            try {
                if (RankAPI.hasRank(commandSender.getName(), Ranks.ADMIN) ||
                        RankAPI.hasRank(commandSender.getName(), Ranks.DEV) ||
                        RankAPI.hasRank(commandSender.getName(), Ranks.OWNER)){
                    if (strings.length != 2){
                        commandSender.sendMessage(Core.TAG + "Invalid amount of arguments. Try /rank <player> <rank>");
                    } else {
                        String target = strings[0];
                        Ranks desired = Ranks.format(strings[1]);

                        if (desired == null){
                            commandSender.sendMessage(Core.TAG + "Invalid rank input. Did you spell it correctly?");
                            return false;
                        }

                        try {
                            RankAPI.setRank(target, desired);
                            commandSender.sendMessage(Core.TAG + "Updated rank of §a" + target + "§7 to " + desired.getColor() + desired.getDisplayName());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    commandSender.sendMessage("§cYou do not have permission to run this command!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}