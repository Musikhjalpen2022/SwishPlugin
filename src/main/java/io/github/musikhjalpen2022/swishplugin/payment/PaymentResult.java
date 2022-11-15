package io.github.musikhjalpen2022.swishplugin.payment;

import java.util.UUID;

public class PaymentResult {

    private final boolean paid;
    private final double amount;
    private final UUID playerId;

    public PaymentResult(UUID playerId, double amount, boolean paid) {
        this.paid = paid;
        this.amount = amount;
        this.playerId = playerId;
    }

    public boolean isPayed() {
        return paid;
    }

    public double getAmount() {
        return amount;
    }

    public UUID getPlayerId() {
        return playerId;
    }
}
