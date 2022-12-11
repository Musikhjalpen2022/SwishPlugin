package io.github.musikhjalpen2022.swishplugin.parkour;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Checkpoint extends Location {

    public Checkpoint(double x, double y, double z) {
        super(Bukkit.getWorlds().get(0), x, y, z);
    }

    public Checkpoint(Location location) {
        super(Bukkit.getWorlds().get(0), location.getX(), location.getY(), location.getZ());
    }

}
