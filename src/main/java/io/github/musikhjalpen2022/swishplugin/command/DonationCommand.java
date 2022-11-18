package io.github.musikhjalpen2022.swishplugin.command;

import io.github.musikhjalpen2022.swishplugin.SwishPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DonationCommand implements CommandExecutor {

    private final SwishPlugin swishPlugin;

    public DonationCommand(SwishPlugin swishPlugin) {
        this.swishPlugin = swishPlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            // Not enough arguments (player, amount)
            return false;
        }
        String playerArg = args[0];
        String amountArg = args[1];

        Player player = Bukkit.getPlayer(playerArg);
        int amount = Integer.parseInt(amountArg);

        swishPlugin.getDonationManager().registerDonation(player.getUniqueId(), amount);
        return true;
    }
}
