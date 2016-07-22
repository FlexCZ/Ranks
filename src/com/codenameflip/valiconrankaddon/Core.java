package com.codenameflip.valiconrankaddon;

import com.codenameflip.valiconrankaddon.commands.RankCmd;
import com.codenameflip.valiconrankaddon.commands.RankDumpCmd;
import com.codenameflip.valiconrankaddon.sql.DatabasePipeline;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class Core extends JavaPlugin implements Listener {

    private static Core core;
    public static Core getCore(){ return core; }

    public static final String TAG = "§8[§d§lV§6§lN§8]: §7";

    public boolean overrideChat;
    public boolean isOverrideChat() {
        return overrideChat;
    }
    public void setOverrideChat(boolean overrideChat) {
        this.overrideChat = overrideChat;
    }

    public DatabasePipeline sql;

    Map<String, Ranks> cachedRanks = new HashMap<>();

    @Override
    public void onEnable() {
        core = this;
        overrideChat = false;
        if (!(new File(getDataFolder(), "config.yml").exists())) saveDefaultConfig();
        try { sql = new DatabasePipeline(parse("host"), parse("port"), parse("data"), parse("user"), parse("pass")); } catch (SQLException | ClassNotFoundException e) { e.printStackTrace(); }
        Bukkit.getLogger().info("[Valicon Rank Manager] -> Rank manager now operational, caching all database entries for use later.");

        try {
            loadRanks();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        getCommand("rank").setExecutor(new RankCmd());
        getCommand("rankDump").setExecutor(new RankDumpCmd());

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        core = null;
        sql = null;
        Bukkit.getLogger().info("[Valicon Rank Manager] -> Rank manager is no longer running, ranks will no longer load/sync as normal.");
        cachedRanks.clear();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws SQLException {
        Player player = event.getPlayer();

        if (cachedRanks.containsKey(player.getName())){
            RankAPI.setRank(player.getName(), cachedRanks.get(player.getName()));
            player.sendMessage(TAG + "Downloaded rank! Current rank: §f§l" + cachedRanks.get(player.getName()).getDisplayName());
        } else {
            player.sendMessage(TAG + "Welcome to the server! You're new, so you're rank is: §f§l" + Ranks.DEFAULT.getDisplayName());
            RankAPI.setRank(player.getName(), Ranks.DEFAULT);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();

        if (!isOverrideChat()){
            event.setCancelled(true);

            Ranks rankOfSender = RankAPI.getRankByName(RankAPI.getRank(player.getName()));
            for (Player online : Bukkit.getServer().getOnlinePlayers()){
                if (rankOfSender != Ranks.DEFAULT){
                    online.sendMessage(rankOfSender.getColor() + "[" + rankOfSender.getDisplayName() + "§r" + rankOfSender.getColor() + "] " + player.getName() + " §8» §f" + event.getMessage());
                } else {
                    online.sendMessage(rankOfSender.getColor() +  player.getName() + " §8» §f" + event.getMessage());
                }
            }
            Bukkit.getLogger().info("[CHAT LOG]: (" + player.getName() + "): " + event.getMessage());
        }
    }

    private void loadRanks() throws SQLException {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                try {
                    Statement stmt = sql.getConnection().createStatement();
                    ResultSet res = stmt.executeQuery("SELECT * FROM `ranks`");

                    while (res.next()){
                        cachedRanks.put(res.getString("player"), Ranks.format(res.getString("rank")));
                    }

                    res.close();
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }
        }, 0, 20 * 10);
    }

    private String parse(String path){
        return getConfig().getString(path);
    }

}