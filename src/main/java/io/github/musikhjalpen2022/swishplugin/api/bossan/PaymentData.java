package io.github.musikhjalpen2022.swishplugin.api.bossan;

public class PaymentData {

    public final String phoneNumber;
    public final int amount;
    public final String username;

    public PaymentData(String phoneNumber, int amount, String username) {
        this.phoneNumber = phoneNumber;
        this.amount = amount;
        this.username = username;
    }

}
