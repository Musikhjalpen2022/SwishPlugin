package io.github.musikhjalpen2022.swishplugin.donation;

import io.github.musikhjalpen2022.swishplugin.SwishPlugin;
import org.bukkit.entity.Player;

import java.util.*;

public class DonationManager {

    private final SwishPlugin swishPlugin;

    private Set<DonationListener> donationListeners;

    private int totalDonations;
    private Map<UUID, Donor> donors;

    private List<Donor> topDonors;


    public DonationManager(SwishPlugin swishPlugin) {
        this.swishPlugin = swishPlugin;
        totalDonations = 0;
        donors = new HashMap<>();
        donationListeners = new HashSet<>();
    }

    public void addDonationListener(DonationListener donationListener) {
        donationListeners.add(donationListener);
    }

    public void removeDonationListener(DonationListener donationListener) {
        donationListeners.remove(donationListener);
    }

    public int getTotalDonations() {
        return totalDonations;
    }

    public List<Donor> getTopDonors() {
        return Collections.unmodifiableList(topDonors);
    }

    public Donor getDonor(UUID playerId) {
        return donors.getOrDefault(playerId, new Donor(playerId));
    }

    public void registerDonation(UUID playerId, int amount) {
        addPlayerDonation(playerId, amount);
    }

    private void addPlayerDonation(UUID playerId, int amount) {
        Donor donor = donors.computeIfAbsent(playerId, Donor::new);
        donor.addDonation(amount);
        totalDonations += amount;

        notifyTotalDonationsChange();
        notifyDonorChange(donor);
        updateTopDonors();
    }

    private void updateTopDonors() {
        List<Donor> newTopDonors = donors.values().stream().sorted().toList();
        if (!newTopDonors.equals(topDonors)) {
            topDonors = newTopDonors;
            notifyTopsDonorsChange();
        }
    }

    private void notifyTotalDonationsChange() {
        donationListeners.forEach(donationListener -> donationListener.onTotalDonationsChange(getTotalDonations()));
        swishPlugin.getScoreboard().setTotalAmount(getTotalDonations());
    }

    private void notifyTopsDonorsChange() {
        donationListeners.forEach(donationListener -> donationListener.onTopDonorsChange(getTopDonors()));
        swishPlugin.getScoreboard().setTopList(getTopDonors());
    }

    private void notifyDonorChange(Donor donor) {
        donationListeners.forEach(donationListener -> donationListener.onDonorChange(donor));
        swishPlugin.getScoreboard().setPlayerDonation(donor);
    }





}
