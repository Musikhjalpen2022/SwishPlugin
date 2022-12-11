package io.github.musikhjalpen2022.swishplugin.parkour;

import org.bukkit.entity.Player;

import java.util.UUID;

public class ParkourPlayer {

    private final UUID playerId;
    private final String username;

    private ParkourTrial bestTrial;

    public ParkourPlayer(Player player) {
        this.playerId = player.getUniqueId();
        this.username = player.getDisplayName();
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public String getUsername() {
        return username;
    }

    public ParkourTrial getBestTrial() {
        return bestTrial;
    }

    public void setBestTrial(ParkourTrial bestTrial) {
        this.bestTrial = bestTrial;
    }
}
