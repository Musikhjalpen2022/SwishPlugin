package io.github.musikhjalpen2022.swishplugin.payment;

import org.bukkit.entity.Player;

import java.util.*;

public abstract class Payment {

    protected Set<PaymentListener> paymentResultListeners = new HashSet<>();
    protected int amount;
    protected Player player;
    protected PaymentResult result = null;

    public Payment(int amount, Player player) {
        this.amount = amount;
        this.player = player;
    }

    protected abstract void sendRequest();

    public void addPaymentResultListener(PaymentListener listener) {
        paymentResultListeners.add(listener);
    }

    public void removePaymentResultListener(PaymentListener listener) {
        paymentResultListeners.remove(listener);
    }

    protected void onPaymentResult(PaymentResult paymentResult) {
        paymentResultListeners.forEach(listener -> listener.onPaymentResult(paymentResult));
    }


}
