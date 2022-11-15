package io.github.musikhjalpen2022.swishplugin.payment;

import org.bukkit.entity.Player;

public abstract class Payment {

    protected PaymentManager paymentManager = null;
    protected double amount;
    protected Player player;
    protected PaymentResult result = null;

    public Payment(double amount, Player player) {
        this.amount = amount;
        this.player = player;
    }

    public abstract void sendRequest();

    protected void onPaymentResult(PaymentResult paymentResult) {
        paymentManager.onPaymentResult(this, paymentResult);
    }


}
