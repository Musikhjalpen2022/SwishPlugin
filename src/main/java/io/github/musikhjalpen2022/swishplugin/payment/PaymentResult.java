package io.github.musikhjalpen2022.swishplugin.payment;

import java.util.UUID;

public class PaymentResult {

    private final boolean paid;
    private final int amount;
    private final UUID playerId;
    private final Payment payment;

    public PaymentResult(UUID playerId, int amount, boolean paid, Payment payment) {
        this.paid = paid;
        this.amount = amount;
        this.playerId = playerId;
        this.payment = payment;
    }

    public boolean isPayed() {
        return paid;
    }

    public int getAmount() {
        return amount;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public Payment getPayment() {
        return payment;
    }
}
