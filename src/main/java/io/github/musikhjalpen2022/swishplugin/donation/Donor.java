package io.github.musikhjalpen2022.swishplugin.donation;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Donor implements Comparable<Donor>, Serializable {

    private final UUID playerId;
    private String lastUsername = "-";
    private int totalDonations;

    public Donor(UUID playerId) {
        this.playerId = playerId;
        this.totalDonations = 0;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public int getTotalDonations() {
        return totalDonations;
    }

    public String getUsername() {
        Player player = Bukkit.getPlayer(playerId);
        if (player != null) {
            lastUsername = player.getDisplayName();
        }
        return lastUsername;
    }

    protected void setTotalDonations(int totalDonations) {
        this.totalDonations = totalDonations;
    }

    protected void addDonation(int donationAmount) {
        this.totalDonations += donationAmount;
    }

    @Override
    public int compareTo(Donor otherDonor) {
        return -Integer.compare(getTotalDonations(), otherDonor.getTotalDonations());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Donor donor = (Donor) o;
        return totalDonations == donor.totalDonations && playerId.equals(donor.playerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId, totalDonations);
    }
}
