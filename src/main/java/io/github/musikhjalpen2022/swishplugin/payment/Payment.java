package io.github.musikhjalpen2022.swishplugin.payment;

import org.bukkit.entity.Player;

public abstract class Payment {

    private PaymentManager paymentManager;
    private Player player;

    public Payment(PaymentManager paymentManager, Player player) {
        this.paymentManager = paymentManager;
        this.player = player;
    }


    private void onPaymentResult(PaymentResult paymentResult) {
        paymentManager.onPaymentResult(this, paymentResult);
    }


}
