package io.github.musikhjalpen2022.swishplugin;

import io.github.musikhjalpen2022.swishplugin.command.*;
import io.github.musikhjalpen2022.swishplugin.donation.DonationManager;
import io.github.musikhjalpen2022.swishplugin.payment.PaymentManager;
import io.github.musikhjalpen2022.swishplugin.scoreboard.Scoreboard;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;


public final class SwishPlugin extends JavaPlugin implements Listener{

    private DonationManager donationManager;
    private PaymentManager paymentManager;
    private Scoreboard scoreboard;

    @Override
    public void onEnable() {
        // Plugin startup logic
        donationManager = new DonationManager(this);
        paymentManager = new PaymentManager(this);
        scoreboard = new Scoreboard(this);
        getCommand("swish").setExecutor(new BossanPaymentCommand(this));
        getCommand("donate").setExecutor(new DonationCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)  {
        scoreboard.newPlayer(event.getPlayer());
    }

    public DonationManager getDonationManager() { return donationManager; }

    public PaymentManager getPaymentManager() {
        return paymentManager;
    }

    public Scoreboard getScoreboard() { return scoreboard; }
}
