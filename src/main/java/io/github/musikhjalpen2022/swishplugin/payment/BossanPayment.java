package io.github.musikhjalpen2022.swishplugin.payment;

import io.github.musikhjalpen2022.swishplugin.payment.Payment;
import io.github.musikhjalpen2022.swishplugin.payment.PaymentManager;
import org.bukkit.entity.Player;

public class BossanPayment extends Payment {

    private String phoneNumber;

    public BossanPayment(double amount, Player player, String phoneNumber) {
        super(amount, player);
        this.phoneNumber = phoneNumber;
    }

}
