package io.github.musikhjalpen2022.swishplugin.scoreboard;

import io.github.musikhjalpen2022.swishplugin.SwishPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class Scoreboard {

    private List<ScoreHelper> scores;
    private SwishPlugin swish;
    public Scoreboard(SwishPlugin swosh){
        swish = swosh;
        scores = new ArrayList<ScoreHelper>();
    }

    public void newPlayer(Player player){
        UUID playeruuid = player.getUniqueId();
        ScoreHelper sh = new ScoreHelper(player);
        sh.setTitle("&aMusikhjälpens Minecraft-Bössa");
        sh.setSlot(8, "Total Swishat från Minecraft:");
        sh.setSlot(6, "Top 3 spelare:");
        sh.setSlot(2, "Ditt bidrag till musikhjälpen:");
        scores.add(sh);
    }

    public void setTotalAmount(Integer amount){
        for (ScoreHelper sh: scores)
        {
            sh.setSlot(7, amount + " SEK");
        }
    }




}
