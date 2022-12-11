package io.github.musikhjalpen2022.swishplugin.parkour;

import io.github.musikhjalpen2022.swishplugin.donation.Donor;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class ParkourTrial implements Serializable, Comparable<ParkourTrial> {

    private static int respawnPenalty = 20;

    transient private LocalDateTime start;
    transient private LocalDateTime end;
    private Duration time = null;

    transient private Checkpoint checkpoint = null;

    private int respawns = 0;
    private boolean finished = false;

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
        finished = true;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public Duration getTime() {
        return time == null ? Duration.between(start, LocalDateTime.now()) : time;
    }

    public Duration getPenalty() { return Duration.ofSeconds((long) respawns * respawnPenalty); }

    public boolean hasPenalty() {
        return respawns > 0;
    }

    public boolean hasFinished() {
        return finished;
    }

    public Duration getTimeWithPenalty() {
        return getTime().plus(getPenalty());
    }

    public boolean isBetterThan(ParkourTrial trial) {
        if (trial == null || !trial.hasFinished()) return true;
        if (!hasFinished()) return false;
        return getTimeWithPenalty().compareTo(trial.getTimeWithPenalty()) < 0;
    }

    @Override
    public int compareTo(ParkourTrial otherTrial) {
        return isBetterThan(otherTrial) ? -1 : 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkourTrial that = (ParkourTrial) o;
        return respawns == that.respawns && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, respawns);
    }
}
