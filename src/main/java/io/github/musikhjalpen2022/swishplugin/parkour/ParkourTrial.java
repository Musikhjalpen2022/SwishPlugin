package io.github.musikhjalpen2022.swishplugin.parkour;

import java.time.Duration;
import java.time.LocalDateTime;

public class ParkourTrial {

    private static int respawnPenalty = 20;

    transient private LocalDateTime start;
    transient private LocalDateTime end;
    private Duration time;

    transient private Checkpoint checkpoint = null;

    private int respawns = 0;

    public ParkourTrial(LocalDateTime start) {
        this.start = start;
    }

    public Checkpoint getCheckpoint() {
        return checkpoint;
    }

    public void setCheckpoint(Checkpoint checkpoint) {
        this.checkpoint = checkpoint;
    }

    public void addRespawn() {
        respawns += 1;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
        time = Duration.between(start, end);
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public Duration getTime() {
        return time;
    }

    public Duration getTimeWithPenalty() {
        return time.plusSeconds(respawnPenalty);
    }

    public boolean isBetterThan(ParkourTrial trial) {
        return getTimeWithPenalty().compareTo(trial.getTimeWithPenalty()) < 0;
    }
}
