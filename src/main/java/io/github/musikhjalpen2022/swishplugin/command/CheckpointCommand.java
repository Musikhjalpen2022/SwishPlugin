package io.github.musikhjalpen2022.swishplugin.command;

import io.github.musikhjalpen2022.swishplugin.SwishPlugin;
import io.github.musikhjalpen2022.swishplugin.parkour.Checkpoint;
import io.github.musikhjalpen2022.swishplugin.parkour.ParkourTrial;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Objects;

public class CheckpointCommand implements CommandExecutor {

    private final SwishPlugin swishPlugin;

    public CheckpointCommand(SwishPlugin swishPlugin) {
        this.swishPlugin = swishPlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (Objects.equals(args[0], "set")) {
                    ParkourTrial trial = swishPlugin.getParkourManager().getTrial(player.getUniqueId());
                    if (trial != null) {
                        trial.setCheckpoint(new Checkpoint(player.getLocation()));
                        player.sendMessage("Checkpoint satt");
                    }
                    return true;
                } else if (Objects.equals(args[0], "goto")) {
                    ParkourTrial trial = swishPlugin.getParkourManager().getTrial(player.getUniqueId());
                    if (trial != null && trial.getCheckpoint() != null) {
                        player.teleport(trial.getCheckpoint().location, PlayerTeleportEvent.TeleportCause.PLUGIN);
                        trial.addRespawn();
                        player.sendMessage("Teleporterar till checkpoint");
                    }
                    return true;
                } else if (Objects.equals(args[0], "show")) {
                    ParkourTrial trial = swishPlugin.getParkourManager().getTrial(player.getUniqueId());
                    if (trial != null) {
                        if (trial.getCheckpoint() != null) {
                            Location location = trial.getCheckpoint().location;
                            player.sendMessage(String.format("Checkpoint är vid %f, %f, %f", location.getX(), location.getY(), location.getZ()));
                        } else {
                            player.sendMessage("Ingen checkpoint satt");
                        }
                    }
                }
            }
        }
        return false;
    }
}
