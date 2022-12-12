package io.github.musikhjalpen2022.swishplugin.scoreboard;

import io.github.musikhjalpen2022.swishplugin.SwishPlugin;
import io.github.musikhjalpen2022.swishplugin.donation.DonationListener;
import io.github.musikhjalpen2022.swishplugin.donation.DonationManager;
import io.github.musikhjalpen2022.swishplugin.donation.Donor;
import io.github.musikhjalpen2022.swishplugin.parkour.ParkourListener;
import io.github.musikhjalpen2022.swishplugin.parkour.ParkourPlayer;
import io.github.musikhjalpen2022.swishplugin.parkour.ParkourTrial;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ScoreBoardManager implements DonationListener, ParkourListener {

    private static final double CUTOFF = 40.0;

    private final SwishPlugin swishPlugin;

    private final Map<UUID, Scoreboard> scoreboards = new HashMap<>();

    public ScoreBoardManager(SwishPlugin swishPlugin) {
        this.swishPlugin = swishPlugin;
    }

    private void setScoreboard(Player player, Scoreboard scoreboard) {
        scoreboards.put(player.getUniqueId(), scoreboard);
    }

    private void removeScoreboard(Player player) {
        Scoreboard scoreboard = scoreboards.get(player.getUniqueId());
        if (scoreboard != null) {
            scoreboard.removeFrom(player);
            scoreboards.remove(player.getUniqueId());
        }
    }

    private void setDonationScoreboard(Player player) {
        DonationScoreboard scoreboard = new DonationScoreboard();
        DonationManager donationManager = swishPlugin.getDonationManager();
        scoreboard.giveTo(player);
        scoreboard.setTotalAmount(donationManager.getTotalDonations());
        scoreboard.setTopList(donationManager.getTopDonors());
        scoreboard.setPlayerDonation(donationManager.getDonor(player.getUniqueId()).getTotalDonations());

        setScoreboard(player, scoreboard);
    }

    private void setParkourScoreboard(Player player) {
        ParkourScoreboard scoreboard = new ParkourScoreboard();
        scoreboard.giveTo(player);
        scoreboard.setTopList(swishPlugin.getParkourManager().getTopPlayers());
        scoreboard.setBestTrial(swishPlugin.getParkourManager().getBestTrial(player.getUniqueId()));
        scoreboard.setCurrentTrial(swishPlugin.getParkourManager().getTrial(player.getUniqueId()));

        setScoreboard(player, scoreboard);
    }

    public void onPlayerJoin(PlayerJoinEvent event) {
        setDonationScoreboard(event.getPlayer());
    }

    public void onPlayerQuit(PlayerQuitEvent event) {
        removeScoreboard(event.getPlayer());
    }

    public void onPlayerMove(PlayerMoveEvent event) {
        boolean inParkour = isInParkourArea(event.getTo());
        if (inParkour != isInParkourArea(event.getFrom())) {
            if (inParkour) {
                setParkourScoreboard(event.getPlayer());
            } else {
                setDonationScoreboard(event.getPlayer());
                swishPlugin.getParkourManager().onPlayerLeaveArea(event.getPlayer());
            }
        }
    }

    private boolean isInParkourArea(Location location) {
        return location.getX() > CUTOFF;
    }

    @Override
    public void onTotalDonationsChange(int totalDonations) {
        for (Scoreboard scoreboard : scoreboards.values()) {
            if (scoreboard instanceof DonationScoreboard donationScoreboard) {
                donationScoreboard.setTotalAmount(totalDonations);
            }
        }
    }

    @Override
    public void onTopDonorsChange(List<Donor> topDonors) {
        for (Scoreboard scoreboard : scoreboards.values()) {
            if (scoreboard instanceof DonationScoreboard donationScoreboard) {
                donationScoreboard.setTopList(topDonors);
            }
        }
    }

    @Override
    public void onDonorChange(Donor donor) {
        Scoreboard scoreboard = scoreboards.get(donor.getPlayerId());
        if (scoreboard instanceof DonationScoreboard donationScoreboard) {
            donationScoreboard.setPlayerDonation(donor.getTotalDonations());
        }
    }

    @Override
    public void onTopPlayersChanged(List<ParkourPlayer> topPlayers) {
        for (Scoreboard scoreboard : scoreboards.values()) {
            if (scoreboard instanceof ParkourScoreboard parkourScoreboard) {
                parkourScoreboard.setTopList(topPlayers);
            }
        }
    }

    @Override
    public void onPersonalBestChanged(ParkourPlayer parkourPlayer) {
        Scoreboard scoreboard = scoreboards.get(parkourPlayer.getPlayerId());
        if (scoreboard instanceof ParkourScoreboard parkourScoreboard) {
            parkourScoreboard.setBestTrial(parkourPlayer.getBestTrial());
        }
    }

    @Override
    public void onCurrentTrialsChanged(Map<UUID, ParkourTrial> updatedTrials) {
        for (Scoreboard scoreboard : scoreboards.values()) {
            if (scoreboard instanceof ParkourScoreboard parkourScoreboard) {
                ParkourTrial trial = updatedTrials.get(scoreboard.getPlayerId());
                parkourScoreboard.setCurrentTrial(trial);
            }
        }
    }
}
