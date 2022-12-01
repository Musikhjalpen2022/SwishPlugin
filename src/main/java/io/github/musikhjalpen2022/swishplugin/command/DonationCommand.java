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
        if (args.length == 0) {
            // Not enough arguments (player, amount)
            return false;
        }
        String playerArg = args[0];
        Player player = Bukkit.getPlayer(playerArg);

        if ((sender instanceof Player playersender && playersender.isOp()) || !(sender instanceof Player)) {
            if (args.length == 2) {
                String amountArg = args[1];
                int amount = Integer.parseInt(amountArg);
                swishPlugin.getDonationManager().registerDonation(player.getUniqueId(), amount);
            } else {
                int donatedAmount = swishPlugin.getDonationManager().getDonor(player.getUniqueId()).getTotalDonations();
                player.sendMessage(String.format("%s has donated %d SEK in total", player.getDisplayName(), donatedAmount));
            }
        }

        return true;
    }
}
