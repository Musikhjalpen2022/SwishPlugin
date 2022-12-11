package io.github.musikhjalpen2022.swishplugin.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import java.util.UUID;

public abstract class Scoreboard {

    protected static final String[] TOP_LIST_COLORS = {"&e", "&7", "&6"};
    private static final String USER_NAME_COLOR = "&b";

    private org.bukkit.scoreboard.Scoreboard scoreboard;
    private Objective sidebar;

    private UUID playerId = null;

    public Scoreboard(int size, String displayName) {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        displayName = ChatColor.translateAlternateColorCodes('&', displayName);
        sidebar = scoreboard.registerNewObjective("sidebar", Criteria.DUMMY, displayName);
        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
        // Create Teams
        for(int i=0; i<size; i++) {
            Team team = scoreboard.registerNewTeam("SLOT_" + i);
            team.addEntry(genEntry(i));
        }
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public void giveTo(Player player) {
        player.setScoreboard(scoreboard);
        playerId = player.getUniqueId();
    }

    public void removeFrom(Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        playerId = null;
    }

    public void setSlot(int slot, String text) {
        Team team = scoreboard.getTeam("SLOT_" + slot);
        String entry = genEntry(slot);
        if(!scoreboard.getEntries().contains(entry)) {
            sidebar.getScore(entry).setScore(slot);
        }

        text = ChatColor.translateAlternateColorCodes('&', text);
        String pre = getFirstSplit(text);
        String suf = getFirstSplit(ChatColor.getLastColors(pre) + getSecondSplit(text));
        team.setPrefix(pre);
        team.setSuffix(suf);
    }

    public void removeSlot(int slot) {
        String entry = genEntry(slot);
        if(scoreboard.getEntries().contains(entry)) {
            scoreboard.resetScores(entry);
        }
    }

    private String genEntry(int slot) {
        return ChatColor.values()[slot].toString();
    }

    private String getFirstSplit(String s) {
        return s.length()>16 ? s.substring(0, 16) : s;
    }

    private String getSecondSplit(String s) {
        if(s.length()>32) {
            s = s.substring(0, 32);
        }
        return s.length()>16 ? s.substring(16) : "";
    }

    protected String colorUsername(String username, UUID playerId) {
        if (this.playerId == playerId) {
            return String.format("%s%s", USER_NAME_COLOR, username);
        } else {
            return username;
        }
    }

}