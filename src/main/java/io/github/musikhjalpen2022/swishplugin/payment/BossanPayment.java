package io.github.musikhjalpen2022.swishplugin.payment;

import io.github.musikhjalpen2022.swishplugin.api.bossan.BossanAPI;
import io.github.musikhjalpen2022.swishplugin.api.bossan.PaymentData;
import io.github.musikhjalpen2022.swishplugin.api.bossan.PaymentStatus;
import org.bukkit.entity.Player;

public class BossanPayment extends Payment implements UpdatablePayment {

    private String phoneNumber;
    private String referenceNumber;

    public BossanPayment(double amount, Player player, String phoneNumber) {
        super(amount, player);
        this.phoneNumber = phoneNumber;
        this.referenceNumber = null;
    }

    @Override
    public void sendRequest() {
        PaymentData paymentData = new PaymentData(phoneNumber, amount, player.getUniqueId().toString());
        BossanAPI.requestPayment(paymentData, this::onRequestResponse);
    }

    private void onRequestResponse(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    @Override
    public void update() {
        if (referenceNumber != null) {
            BossanAPI.checkPayment(referenceNumber, this::onUpdateResponse);
        }
    }

    private void onUpdateResponse(PaymentStatus status) {
        if (status != PaymentStatus.WAITING) {
            boolean paid = status == PaymentStatus.PAID;
            result = new PaymentResult(player.getUniqueId(), amount, paid);
            onPaymentResult(result);
        }
    }

}
