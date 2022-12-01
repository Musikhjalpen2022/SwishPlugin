package io.github.musikhjalpen2022.swishplugin.scoreboard;

import io.github.musikhjalpen2022.swishplugin.SwishPlugin;
import io.github.musikhjalpen2022.swishplugin.donation.DonationListener;
import io.github.musikhjalpen2022.swishplugin.donation.Donor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class Scoreboard implements DonationListener {

    private List<ScoreHelper> scores;
    private SwishPlugin swish;
    public Scoreboard(SwishPlugin swosh){
        swish = swosh;
        scores = new ArrayList<ScoreHelper>();
    }

    public void newPlayer(Player player){
        UUID playeruuid = player.getUniqueId();
        ScoreHelper sh = new ScoreHelper(player);
        sh.setTitle("&aMusikhjälpen");
        sh.setSlot(10, "");
        sh.setSlot(9, " Minecraft Bidrag ");
        sh.setSlot(7, "");
        sh.setSlot(6, "     Toppgivare");
        sh.setSlot(1, "     Ditt bidrag");
        scores.add(sh);
    }

    public void removePlayer(Player player) {
        for (ScoreHelper scoreHelper : scores) {
            if (scoreHelper.getPlayerUuid().equals(player.getUniqueId())) {
                scores.remove(scoreHelper);
                break;
            }
        }
    }

    public void setTotalAmount(Integer amount){
        for (ScoreHelper sh: scores)
        {
            sh.setSlot(8, String.format("&a        %d kr", amount));
        }
    }

    public void setTopList(List<Donor> donors) {
        for (ScoreHelper sh: scores)
        {
            for (int i = 0; i < 3; i++) {
                if (i < donors.size()) {
                    int amount = donors.get(i).getTotalDonations();
                    sh.setSlot(5-i, String.format("%d %s &a%d kr", i+1, donors.get(i).getUsername(), amount));
                } else {
                    sh.setSlot(5-i, "");
                }
            }

        }
    }


    public void setPlayerDonation(Donor donor) {
        for (ScoreHelper scoreHelper : scores) {
            if (scoreHelper.getPlayerUuid().equals(donor.getPlayerId())) {
                scoreHelper.setSlot(0, String.format("&a        %d kr", donor.getTotalDonations()));
            }
        }
    }

    @Override
    public void onTotalDonationsChange(int totalDonations) {
        setTotalAmount(totalDonations);
    }

    @Override
    public void onTopDonorsChange(List<Donor> topDonors) {
        setTopList(topDonors);
    }

    @Override
    public void onDonorChange(Donor donor) {
        setPlayerDonation(donor);
    }
}
