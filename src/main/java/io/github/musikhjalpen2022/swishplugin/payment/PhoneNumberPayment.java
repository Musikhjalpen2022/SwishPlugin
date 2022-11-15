package io.github.musikhjalpen2022.swishplugin.payment;

import org.bukkit.entity.Player;

public class PhoneNumberPayment extends Payment {

    private String phoneNumber;

    public PhoneNumberPayment(PaymentManager paymentManager, Player player, String phoneNumber, double amount) {
        super(paymentManager, player);
        this.phoneNumber = phoneNumber;
    }

}
