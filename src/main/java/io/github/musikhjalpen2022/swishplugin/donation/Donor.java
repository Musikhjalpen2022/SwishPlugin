package io.github.musikhjalpen2022.swishplugin.donation;

import java.util.UUID;

public class Donor {

    private final UUID playerId;
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

    protected void setTotalDonations(int totalDonations) {
        this.totalDonations = totalDonations;
    }

    protected void addDonation(int donationAmount) {
        this.totalDonations += donationAmount;
    }


}
