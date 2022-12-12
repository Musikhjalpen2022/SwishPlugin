package io.github.musikhjalpen2022.swishplugin;

import io.github.musikhjalpen2022.swishplugin.command.*;
import io.github.musikhjalpen2022.swishplugin.donation.DonationManager;
import io.github.musikhjalpen2022.swishplugin.log.DonationLogger;
import io.github.musikhjalpen2022.swishplugin.log.PlayerLogger;
import io.github.musikhjalpen2022.swishplugin.parkour.ParkourManager;
import io.github.musikhjalpen2022.swishplugin.payment.PaymentManager;
import io.github.musikhjalpen2022.swishplugin.scoreboard.DonationScoreboard;
import io.github.musikhjalpen2022.swishplugin.scoreboard.ScoreBoardManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;


public final class SwishPlugin extends JavaPlugin implements Listener{

    private DonationManager donationManager;
    private PaymentManager paymentManager;
    private ScoreBoardManager scoreBoardManager;

    private PlayerLogger playerLogger;
    private DonationLogger donationLogger;

    private ParkourManager parkourManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        paymentManager = new PaymentManager(this);
        donationManager = new DonationManager(this);
        parkourManager = new ParkourManager(this);
        scoreBoardManager = new ScoreBoardManager(this);
        donationManager.addDonationListener(scoreBoardManager);
        parkourManager.addParkourListener(scoreBoardManager);

        playerLogger = new PlayerLogger("playerlog");
        donationLogger = new DonationLogger("donationlog");
        donationManager.setDonationLogger(donationLogger);
        this.getServer().getPluginManager().registerEvents(this, this);
        getCommand("swish").setExecutor(new BossanPaymentCommand(this));
        getCommand("donate").setExecutor(new DonationCommand(this));
        getCommand("checkpoint").setExecutor(new CheckpointCommand(this));
        getCommand("parkour").setExecutor(new ParkourCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)  {
        playerLogger.logPlayerJoin(event.getPlayer());
        scoreBoardManager.onPlayerJoin(event);
    }

    @EventHandler
    public void onPLayerMove(PlayerMoveEvent event) {
        if (!event.getTo().equals(event.getFrom())) {
            parkourManager.onPlayerMove(event);
            scoreBoardManager.onPlayerMove(event);
        }
    }

    @EventHandler
    public  void onPlayerQuit(PlayerQuitEvent event) {
        playerLogger.logPlayerQuit(event.getPlayer());
        scoreBoardManager.onPlayerQuit(event);
        parkourManager.onPlayerLeaveArea(event.getPlayer());
    }

    public DonationManager getDonationManager() { return donationManager; }

    public PaymentManager getPaymentManager() {
        return paymentManager;
    }

    public ParkourManager getParkourManager() { return parkourManager; }

    public ScoreBoardManager getScoreboardManager() { return scoreBoardManager; }
}
