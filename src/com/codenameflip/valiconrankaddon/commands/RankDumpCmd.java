package com.codenameflip.valiconrankaddon.commands;

/*
  This class was created by @codenameflip on 6/17/16
*/

import com.codenameflip.valiconrankaddon.Core;
import com.codenameflip.valiconrankaddon.RankAPI;
import com.codenameflip.valiconrankaddon.Ranks;
import com.codenameflip.valiconrankaddon.sql.DatabasePipeline;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RankDumpCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("rankDump")){
            if (commandSender instanceof Player){
                try {
                    Player player = (Player) commandSender;
                    if (RankAPI.hasRank(commandSender.getName(), Ranks.ADMIN) ||
                            RankAPI.hasRank(commandSender.getName(), Ranks.DEV) ||
                            RankAPI.hasRank(commandSender.getName(), Ranks.OWNER)){
                        DatabasePipeline pipe = Core.getCore().sql;
                        Statement checkStmt = pipe.getConnection().createStatement();
                        checkStmt.executeQuery("SELECT * FROM `ranks`");
                        ResultSet set = checkStmt.getResultSet();

                        player.sendMessage(" §f§l** §6§l§nDumping all rank data on you!§r §6§o(Brace yourself!)");
                        while (set.next()){
                            player.sendMessage(" §f§l* §e" + set.getString("player") + " §7[" + set.getString("rank") + "] §8§o(" + set.getString("uuid") + ")");
                        }
                        player.sendMessage(" §f§l** §8§lEND OF RANK DATA DUMP!");
                    } else {
                        commandSender.sendMessage("§cYou do not have permission to run this command!");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
        return false;
    }
}
