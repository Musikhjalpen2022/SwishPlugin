package io.github.musikhjalpen2022.swishplugin.payment;

import io.github.musikhjalpen2022.swishplugin.SwishPlugin;
import io.github.musikhjalpen2022.swishplugin.reward.Fireworks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class PaymentManager implements PaymentListener {

    private Set<Payment> payments;

    private final ScheduledExecutorService executor =
            Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture updateCycle = null;
    private static final long UPDATE_PERIOD = 5;

    private SwishPlugin swishPlugin;

    public PaymentManager(SwishPlugin swishPlugin) {
        this.swishPlugin = swishPlugin;
        payments = new HashSet<>();
    }

    public void request(Payment payment) {
        payments.add(payment);
        payment.addPaymentResultListener(this);
        payment.sendRequest();

        if (payment instanceof UpdatablePayment && updateCycle == null) {
            startUpdateCycle();
        }
    }

    private void startUpdateCycle() {
        updateCycle = executor.scheduleAtFixedRate(this::updatePayments, UPDATE_PERIOD, UPDATE_PERIOD, TimeUnit.SECONDS);
    }

    private void stopUpdateCycle() {
        if (updateCycle != null) {
            updateCycle.cancel(false);
            updateCycle = null;
        }
    }

    private void updatePayments() {
        boolean hasUpdatablePayment = false;
        for (Payment payment : payments) {
            if (payment instanceof UpdatablePayment updatablePayment) {
                hasUpdatablePayment = true;
                updatablePayment.update();
            }
        }
        if (!hasUpdatablePayment) {
            stopUpdateCycle();
        }
    }

    public void onPaymentResult(PaymentResult paymentResult) {
        Payment payment = paymentResult.getPayment();
        payments.remove(payment);
        payment.removePaymentResultListener(this);
        Player player = Bukkit.getPlayer(paymentResult.getPlayerId());
        String playerName = player != null ? player.getName() : "UNKNOWN";
        if (paymentResult.isPayed()) {
            // Player has donated.
            swishPlugin.getDonationManager().registerDonation(player.getUniqueId(), paymentResult.getAmount());
            System.out.printf("%s donated %d SEK%n", playerName, paymentResult.getAmount());
            player.sendMessage("Musikhjälpen har fått din gåva. Tack!");
        } else {
            // Payment cancelled.
            System.out.printf("%s cancelled %d SEK donation%n", playerName, paymentResult.getAmount());
            player.sendMessage("Swish avbruten.");
        }
    }

}
