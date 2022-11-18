package io.github.musikhjalpen2022.swishplugin;

import io.github.musikhjalpen2022.swishplugin.command.BossanPaymentCommand;
import io.github.musikhjalpen2022.swishplugin.payment.PaymentManager;
import io.github.musikhjalpen2022.swishplugin.scoreboard.Scoreboard;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;


public final class SwishPlugin extends JavaPlugin implements Listener{

    private PaymentManager paymentManager;
    private Scoreboard scoreboard;

    @Override
    public void onEnable() {
        // Plugin startup logic
        paymentManager = new PaymentManager(this);
        scoreboard = new Scoreboard(this);
        getCommand("swish").setExecutor(new BossanPaymentCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)  {
        scoreboard.newPlayer(event.getPlayer());
    }

    public PaymentManager getPaymentManager() {
        return paymentManager;
    }
}
