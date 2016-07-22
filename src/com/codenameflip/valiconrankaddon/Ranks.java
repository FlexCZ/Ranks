package com.codenameflip.valiconrankaddon;

import org.bukkit.ChatColor;

public enum Ranks {

    DEFAULT("None", "default", ChatColor.DARK_GRAY),

    // DONATOR RANKS
    PREMIUM("Premium", "premium", ChatColor.AQUA),
    PREMIUM_PLUS("Premium§e+", "premiumplus", ChatColor.AQUA),

    // STAFF RANKS
    MOD("Mod", "mod", ChatColor.GOLD),
    ADMIN("Admin", "admin", ChatColor.RED),
    DEV("Dev", "dev", ChatColor.BLUE),
    OWNER("Owner", "owner", ChatColor.RED),

    // MISC. RANKS
    YOUTUBER("§cYT", "youtube", ChatColor.WHITE),
    POPULAR("Popular", "popular", ChatColor.GREEN),
    VIP("VIP", "vip", ChatColor.DARK_PURPLE),
    TWITCH("Twitch", "twitch", ChatColor.LIGHT_PURPLE),
    ERROR("Error", "error", ChatColor.DARK_RED);

    private String displayName;
    private String pexName;
    private ChatColor color;

    Ranks(String displayName, String pexName, ChatColor chatColor) {
        this.displayName = displayName;
        this.pexName = pexName;
        this.color = chatColor;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPexName() {
        return pexName;
    }

    public ChatColor getColor() {
        return color;
    }

    public static Ranks format(String rank){
        for (Ranks ranks : Ranks.values()){
            if (ranks.getDisplayName().equalsIgnoreCase(rank)) return ranks;
        }
        return ERROR;
    }
}
