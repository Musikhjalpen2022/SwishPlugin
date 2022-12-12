package io.github.musikhjalpen2022.swishplugin.scoreboard;

import io.github.musikhjalpen2022.swishplugin.parkour.ParkourPlayer;
import io.github.musikhjalpen2022.swishplugin.parkour.ParkourTrial;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ParkourScoreboard extends Scoreboard{

    private static final String DISPLAY_NAME = "Parkour Toplista";
    private static final int TOP_LIST_LENGTH = 10;
    private static final int SLOTS = TOP_LIST_LENGTH + 6;

    public ParkourScoreboard() {
        super(SLOTS, DISPLAY_NAME);
        // Top list.
        setSlot(5, "");
        setSlot(4, "     Personbästa");
        // Personal best.
        setSlot(2, "");
        setSlot(1, "   Pågående försök");
        // Current trial.
    }

    public void setTopList(List<ParkourPlayer> topPlayers) {
        for (int i = 0; i < TOP_LIST_LENGTH; i++) {
            if (i < topPlayers.size()) {
                String placeColor = i < 3 ? TOP_LIST_COLORS[i] : "&a";
                ParkourPlayer player = topPlayers.get(i);
                setSlot(SLOTS-i-1, String.format("%1$-16s &a%2$s", String.format("%s%d&f %s",
                        placeColor,
                        i+1,
                        colorUsername(player.getUsername(), player.getPlayerId())),
                        formatTime(player.getBestTrial().getTimeWithPenalty())
                ));
            }
        }
    }

    public void setBestTrial(ParkourTrial trial) {
        if (trial == null) {
            setSlot(3, "&8          --:--");
        } else {
            setSlot(3, String.format("&a          %s", formatTime(trial.getTimeWithPenalty())));
        }
    }

    private static final float BLINK_RATE = 2;
    public void setCurrentTrial(ParkourTrial trial) {
        if (trial == null) {
            setSlot(0, "&8          --:--");
        } else {
            if (trial.hasFinished() && trial.getEnd().until(LocalDateTime.now(), ChronoUnit.SECONDS)%2 == 0) {
                setSlot(0, "");
            } else if (trial.hasPenalty()) {
                setSlot(0, String.format("&b    %s  &c+%s s ",
                        formatTime(trial.getTime()),
                        trial.getPenalty().getSeconds()
                ));
            } else {
                setSlot(0, String.format("&b        %s", formatTime(trial.getTime())));
            }
        }
    }

    public static String formatTime(Duration duration) {
        return String.format("%02d:%02d", duration.toMinutes(), duration.toSecondsPart());
    }
}
