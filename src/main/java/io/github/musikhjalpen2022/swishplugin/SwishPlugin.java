package io.github.musikhjalpen2022.swishplugin;

import io.github.musikhjalpen2022.swishplugin.command.*;
import io.github.musikhjalpen2022.swishplugin.donation.DonationManager;
import io.github.musikhjalpen2022.swishplugin.payment.PaymentManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SwishPlugin extends JavaPlugin {

    private DonationManager donationManager;
    private PaymentManager paymentManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        donationManager = new DonationManager(this);
        paymentManager = new PaymentManager(this);

        getCommand("swish").setExecutor(new BossanPaymentCommand(this));
        getCommand("donate").setExecutor(new DonationCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public DonationManager getDonationManager() { return donationManager; }

    public PaymentManager getPaymentManager() {
        return paymentManager;
    }
}
