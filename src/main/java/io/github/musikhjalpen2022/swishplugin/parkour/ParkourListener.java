package io.github.musikhjalpen2022.swishplugin.parkour;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ParkourListener {

    void onTopPlayersChanged(List<ParkourPlayer> topPlayers);

    void onPersonalBestChanged(ParkourPlayer parkourPlayer);

    void onCurrentTrialsChanged(Map<UUID, ParkourTrial> updatedTrials);

}
