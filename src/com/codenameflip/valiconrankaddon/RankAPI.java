package com.codenameflip.valiconrankaddon;

import com.codenameflip.valiconrankaddon.sql.DatabasePipeline;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RankAPI {

    public static String getRank(String player){
        if (Core.getCore().cachedRanks.containsKey(player))
            return Core.getCore().cachedRanks.get(player).getDisplayName();
        else
            return Ranks.ERROR.getDisplayName();
    }

    public static Ranks getRankByName(String name){
        for (Ranks all : Ranks.values()){
            if (all.getDisplayName().equalsIgnoreCase(name)) return all;
        }
        return Ranks.ERROR;
    }

    public static boolean hasRank(String player, Ranks needed) throws SQLException {
        DatabasePipeline pipe = Core.getCore().sql;
        Statement checkStmt = pipe.getConnection().createStatement();
        checkStmt.executeQuery("SELECT * FROM `ranks` WHERE `uuid` = '" + Bukkit.getOfflinePlayer(player).getUniqueId().toString() + "'");
        ResultSet set = checkStmt.getResultSet();

        if (set.next()){
            if (needed == getRankByName(set.getString("rank"))){
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    public static void setRank(String player, Ranks rank) throws SQLException {
        Core.getCore().cachedRanks.put(player, rank);

        DatabasePipeline pipe = Core.getCore().sql;

        Statement checkStmt = pipe.getConnection().createStatement();
        checkStmt.executeQuery("SELECT * FROM `ranks` WHERE `uuid` = '" + Bukkit.getOfflinePlayer(player).getUniqueId().toString() + "'");
        ResultSet set = checkStmt.getResultSet();

        if (set.next()){
            Statement stmt = pipe.getConnection().createStatement();
            stmt.executeUpdate("DELETE FROM `ranks` WHERE `uuid` = '" + Bukkit.getOfflinePlayer(player).getUniqueId().toString() + "'");
            stmt.close();
        }

        PreparedStatement pstmt = pipe.getConnection().prepareStatement("INSERT INTO `ranks`(uuid, player, rank) VALUES (?, ?, ?);");
        pstmt.setString(1, Bukkit.getOfflinePlayer(player).getUniqueId().toString());
        pstmt.setString(2, player);
        pstmt.setString(3, rank.getDisplayName());
        pstmt.executeUpdate();

        Bukkit.getLogger().info("[Valicon Rank Manager] -> Set rank of '" + player + "' to " + rank.getDisplayName());
        pstmt.close();
    }

}
