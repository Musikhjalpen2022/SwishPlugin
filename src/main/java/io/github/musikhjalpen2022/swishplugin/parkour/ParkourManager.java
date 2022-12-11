package io.github.musikhjalpen2022.swishplugin.parkour;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ParkourManager {

    private final static String SAVE_FILE = "parkour.data";
    private static final Location START = new Location(Bukkit.getWorlds().get(0), 54.5, -57, 11.5);
    private static final Location FINISH = new Location(Bukkit.getWorlds().get(0), 65.5, -32, 43.5);

    private static final double START_THRESHOLD = 0.5;
    private static final double FINISH_THRESHOLD = 1.5;

    private final Map<UUID, ParkourTrial> trials = new HashMap<>();
    private Map<UUID, ParkourPlayer> parkourPlayers;

    // 293
    public ParkourManager() {
        if (load(SAVE_FILE)) {
            System.out.println("Loaded parkour players from file");
        } else {
            parkourPlayers = new HashMap<>();
        }
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
        if (trial != null) {
            trials.remove(player.getUniqueId());
            trial.setEnd(LocalDateTime.now());
            player.sendMessage(String.format("Du klarade parkouren på %d sekunder!", trial.getTimeWithPenalty().getSeconds()));
            ParkourPlayer parkourPlayer = parkourPlayers.computeIfAbsent(player.getUniqueId(), p -> new ParkourPlayer(player));
            if (trial.isBetterThan(parkourPlayer.getBestTrial())) {
                parkourPlayer.setBestTrial(trial);
                player.sendMessage("Nytt personbästa!");
                save(SAVE_FILE);
            }
        }
    }

    public ParkourTrial getTrial(UUID playerId) {
        return trials.get(playerId);
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
