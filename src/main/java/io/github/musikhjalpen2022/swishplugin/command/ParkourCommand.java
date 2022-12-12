package io.github.musikhjalpen2022.swishplugin.command;

import io.github.musikhjalpen2022.swishplugin.SwishPlugin;
import io.github.musikhjalpen2022.swishplugin.parkour.ParkourTrial;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Objects;

public class ParkourCommand implements CommandExecutor {

    private final SwishPlugin swishPlugin;

    public ParkourCommand(SwishPlugin swishPlugin) {
        this.swishPlugin = swishPlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            if (Objects.equals(args[0], "set")) {
                if (args.length == 3) {
                    String username = args[1];
                    String time = args[2];
                    String minutes = time.substring(0, 2);
                    String seconds = time.substring(3, 5);
                    Duration duration = Duration.ofMinutes(Long.parseLong(minutes)).plusSeconds(Long.parseLong(seconds));
                    Player player = Bukkit.getPlayer(username);
                    ParkourTrial trial = new ParkourTrial(duration);
                    swishPlugin.getParkourManager().setBestTrial(player.getUniqueId(), trial);
                    return true;
                }
            } else if (Objects.equals(args[0], "remove")) {
                if (args.length == 2) {
                    String username = args[1];
                    Player player = Bukkit.getPlayer(username);
                    swishPlugin.getParkourManager().removeTrial(player.getUniqueId());
                    return true;
                }
            }
        }
        return false;
    }
}
