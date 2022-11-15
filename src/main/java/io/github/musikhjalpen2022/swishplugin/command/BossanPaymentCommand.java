package io.github.musikhjalpen2022.swishplugin.command;

import io.github.musikhjalpen2022.swishplugin.SwishPlugin;
import io.github.musikhjalpen2022.swishplugin.payment.BossanPayment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BossanPaymentCommand implements CommandExecutor {

    private SwishPlugin swishPlugin;

    public BossanPaymentCommand(SwishPlugin swishPlugin) {
        this.swishPlugin = swishPlugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length != 2) {
                // Not enough arguments (phoneNumber, amount)
                return true;
            }
            String phoneNumberArg = args[0];
            String amountArg = args[1];

            String phoneNumber = phoneNumberArg; // Parse number
            int amount = Integer.parseInt(amountArg);

            BossanPayment payment = new BossanPayment(amount, player, phoneNumber);
            swishPlugin.getPaymentManager().request(payment);
            player.sendMessage(String.format("A Swish request of SEK %d for %s", amount, player.getDisplayName()));

            return true;
        } else {
            return false;
        }
    }
}
