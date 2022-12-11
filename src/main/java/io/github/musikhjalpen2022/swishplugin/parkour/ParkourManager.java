package io.github.musikhjalpen2022.swishplugin.parkour;

import io.github.musikhjalpen2022.swishplugin.SwishPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ParkourManager {

    private final static String SAVE_FILE = "parkour.data";
    private static final Location START = new Location(Bukkit.getWorlds().get(0), 54.5, -57, 11.5);
    private static final Location FINISH = new Location(Bukkit.getWorlds().get(0), 65.5, -32, 43.5);

    private static final double START_THRESHOLD = 0.5;
    private static final double FINISH_THRESHOLD = 1.5;
    private static final float CURRENT_TRIAL_UPDATE_CYCLE = 1.0f; // in seconds;
    private static final float KEEP_CURRENT_TRIAL_TIME = 10; // in seconds;

    private Set<ParkourListener> parkourListeners = new HashSet<>();

    private Map<UUID, ParkourTrial> trials = new HashMap<>();
    private Map<UUID, ParkourPlayer> parkourPlayers;

    private List<ParkourPlayer> topPlayers;

    private final SwishPlugin swishPlugin;

    // 293
    public ParkourManager(SwishPlugin swishPlugin) {
        this.swishPlugin = swishPlugin;
        if (load(SAVE_FILE)) {
            System.out.println("Loaded parkour players from file");
            updateTopList();
        } else {
            parkourPlayers = new HashMap<>();
            topPlayers = new ArrayList<>();
        }

        BukkitRunnable currentTrialUpdate = new BukkitRunnable() {
            @Override
            public void run() {
                updateCurrentTrials();
            }
        };

        currentTrialUpdate.runTaskTimer(swishPlugin, 0, (long)(CURRENT_TRIAL_UPDATE_CYCLE*20L));
    }

    public void addParkourListener(ParkourListener parkourListener) {
        parkourListeners.add(parkourListener);
    }

    public void removeParkourListener(ParkourListener parkourListener) {
        parkourListeners.remove(parkourListener);
    }


    public ParkourTrial getTrial(UUID playerId) {
        return trials.get(playerId);
    }

    public ParkourTrial getBestTrial(UUID playerId) {
        ParkourPlayer parkourPlayer = parkourPlayers.get(playerId);
        if (parkourPlayer != null) {
            return parkourPlayer.getBestTrial();
        }
        return null;
    }

    public List<ParkourPlayer> getTopPlayers() {
        return Collections.unmodifiableList(topPlayers);
    }

    private void updateTopList() {
        int hashBefore = parkourListeners.hashCode();
        List<ParkourPlayer>  newTopPlayers = parkourPlayers.values().stream().sorted().toList();
        if (newTopPlayers.hashCode() != hashBefore) {
            topPlayers = newTopPlayers;
            notifyTopListChange();
        }
    }

    private void updateCurrentTrials() {
        trials = trials.entrySet().stream().filter(entry -> !isOldTrial(entry.getValue())).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
        notifyCurrentTrialChange();
    }

    private boolean isOldTrial(ParkourTrial trial) {
        if (!trial.hasFinished()) return false;
        return trial.getEnd().until(LocalDateTime.now(), ChronoUnit.SECONDS) > KEEP_CURRENT_TRIAL_TIME;
    }

    private void notifyTopListChange() {
        parkourListeners.forEach(parkourListener -> parkourListener.onTopPlayersChanged(topPlayers));
    }

    private void notifyPersonalBestChange(ParkourPlayer parkourPlayer) {
        parkourListeners.forEach(parkourListener -> parkourListener.onPersonalBestChanged(parkourPlayer));
    }

    private void notifyCurrentTrialChange() {
        parkourListeners.forEach(parkourListener -> parkourListener.onCurrentTrialsChanged(Collections.unmodifiableMap(trials)));
    }


    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getTo().distance(START) < START_THRESHOLD) {
            onPlayerStart(event.getPlayer());
        } else if (event.getTo().distance(FINISH) < FINISH_THRESHOLD) {
            onPlayerFinish(event.getPlayer());
        }
    }

    private void onPlayerStart(Player player) {
        ParkourTrial trial = new ParkourTrial(LocalDateTime.now());
        trials.put(player.getUniqueId(), trial);
    }

    private void onPlayerFinish(Player player) {
        ParkourTrial trial = trials.get(player.getUniqueId());
        if (trial != null && !trial.hasFinished()) {
            trial.setEnd(LocalDateTime.now());
            player.sendMessage(String.format("Du klarade parkouren på %d sekunder!", trial.getTimeWithPenalty().getSeconds()));
            ParkourPlayer parkourPlayer = parkourPlayers.computeIfAbsent(player.getUniqueId(), p -> new ParkourPlayer(player));
            if (trial.isBetterThan(parkourPlayer.getBestTrial())) {
                parkourPlayer.setBestTrial(trial);
                notifyPersonalBestChange(parkourPlayer);
                updateTopList();
                player.sendMessage("Nytt personbästa!");
                save(SAVE_FILE);
            }
        }
    }

    public boolean save(String filePath) {
        try {
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(new GZIPOutputStream(new FileOutputStream(filePath)));
            out.writeObject(parkourPlayers);
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
            parkourPlayers = (Map<UUID, ParkourPlayer>) in.readObject();
            in.close();
            return true;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
