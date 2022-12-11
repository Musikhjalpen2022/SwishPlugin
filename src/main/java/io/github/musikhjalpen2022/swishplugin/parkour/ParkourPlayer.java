package io.github.musikhjalpen2022.swishplugin.parkour;

import io.github.musikhjalpen2022.swishplugin.donation.Donor;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class ParkourPlayer implements Serializable, Comparable<ParkourPlayer> {

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

    @Override
    public int compareTo(ParkourPlayer otherPlayer) {
        if (otherPlayer == null) return -1;
        if (bestTrial == null || !bestTrial.hasFinished()) return 1;
        return bestTrial.compareTo(otherPlayer.bestTrial);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkourPlayer that = (ParkourPlayer) o;
        return playerId.equals(that.playerId) && bestTrial.equals(that.bestTrial);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId, bestTrial);
    }
}
