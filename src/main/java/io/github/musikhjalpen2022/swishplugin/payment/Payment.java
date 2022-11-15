package io.github.musikhjalpen2022.swishplugin.payment;

import org.bukkit.entity.Player;

public abstract class Payment {

    private PaymentManager paymentManager = null;
    private double amount;
    private Player player;

    public Payment(double amount, Player player) {
        this.amount = amount;
        this.player = player;
    }

    private void onPaymentResult(PaymentResult paymentResult) {
        paymentManager.onPaymentResult(this, paymentResult);
    }


}
