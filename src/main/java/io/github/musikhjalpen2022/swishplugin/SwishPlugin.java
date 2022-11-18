package io.github.musikhjalpen2022.swishplugin;

import io.github.musikhjalpen2022.swishplugin.command.BossanPaymentCommand;
import io.github.musikhjalpen2022.swishplugin.payment.PaymentManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SwishPlugin extends JavaPlugin {

    private PaymentManager paymentManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        paymentManager = new PaymentManager(this);

        getCommand("swish").setExecutor(new BossanPaymentCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public PaymentManager getPaymentManager() {
        return paymentManager;
    }
}
