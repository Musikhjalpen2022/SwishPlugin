package io.github.musikhjalpen2022.swishplugin.donation;

import io.github.musikhjalpen2022.swishplugin.SwishPlugin;
import io.github.musikhjalpen2022.swishplugin.reward.Fireworks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import javax.xml.crypto.Data;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class DonationManager {

    private final static String SAVE_FILE = "donors.data";

    private final SwishPlugin swishPlugin;

    private Set<DonationListener> donationListeners;

    private int totalDonations;
    private Map<UUID, Donor> donors;

    private List<Donor> topDonors;


    public DonationManager(SwishPlugin swishPlugin) {
        this.swishPlugin = swishPlugin;
        totalDonations = 0;
        donationListeners = new HashSet<>();
        if (load(SAVE_FILE)) {
            System.out.println("Loaded donors from file");
        } else {
            donors = new HashMap<>();
            topDonors = new ArrayList<>();
        }
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

        Player player = Bukkit.getPlayer(playerId);

        if (amount > 0) Fireworks.spawnFireworks(swishPlugin, player, (int)(Math.sqrt(10f*amount)/2));
        if (getDonor(playerId).getTotalDonations() >= 50) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + Bukkit.getPlayer(playerId).getDisplayName() + " group add donor");
        }
    }

    private void addPlayerDonation(UUID playerId, int amount) {
        System.out.println(playerId);
        Donor donor = donors.computeIfAbsent(playerId, Donor::new);
        System.out.println(donor.getPlayerId());
        donor.addDonation(amount);
        totalDonations += amount;

        notifyTotalDonationsChange();
        notifyDonorChange(donor);
        updateTopDonors();
        save(SAVE_FILE);
    }

    private void updateTopDonors() {
        topDonors = donors.values().stream().sorted().toList();
        notifyTopsDonorsChange();
    }

    private void notifyTotalDonationsChange() {
        donationListeners.forEach(donationListener -> donationListener.onTotalDonationsChange(getTotalDonations()));
    }

    private void notifyTopsDonorsChange() {
        donationListeners.forEach(donationListener -> donationListener.onTopDonorsChange(getTopDonors()));
    }

    private void notifyDonorChange(Donor donor) {
        donationListeners.forEach(donationListener -> donationListener.onDonorChange(donor));
    }


    public boolean save(String filePath) {
        try {
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(new GZIPOutputStream(new FileOutputStream(filePath)));
            out.writeObject(donors);
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean load(String filePath) {
        try {
            BukkitObjectInputStream in = new BukkitObjectInputStream(new GZIPInputStream(new FileInputStream(filePath)));
            donors = (Map<UUID, Donor>) in.readObject();
            in.close();
            updateTopDonors();
            totalDonations = donors.values().stream().mapToInt(Donor::getTotalDonations).sum();
            return true;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }


}
