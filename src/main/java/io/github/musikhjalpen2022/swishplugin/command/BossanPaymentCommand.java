package io.github.musikhjalpen2022.swishplugin.command;

import io.github.musikhjalpen2022.swishplugin.SwishPlugin;
import io.github.musikhjalpen2022.swishplugin.payment.BossanPayment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BossanPaymentCommand implements CommandExecutor {

    private final SwishPlugin swishPlugin;

    public BossanPaymentCommand(SwishPlugin swishPlugin) {
        this.swishPlugin = swishPlugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0){
                return false;
            }
            else if (args.length != 2) {
                player.sendMessage(String.format("Något gick fel, hade du mellanrum mellan telefonnumret och summan?"));
                return false;
            }
            String phoneNumberArg = args[0];
            String amountArg = args[1];

            String phoneNumber = phoneNumberArg.replaceAll("[^0-9]+", ""); // Parse number
            if (phoneNumber.startsWith("0")) { phoneNumber = phoneNumber.replaceFirst("0", "46"); }
            int amount = Integer.parseInt(amountArg);

            BossanPayment payment = new BossanPayment(amount, player, phoneNumber);
            swishPlugin.getPaymentManager().request(payment);
            player.sendMessage(String.format("A Swish förfrågan på %d kr har gjorts för %s", amount, player.getDisplayName()));

            return true;
        } else {
            return false;
        }
    }
}
