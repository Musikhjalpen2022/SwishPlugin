package io.github.musikhjalpen2022.swishplugin.payment;

import io.github.musikhjalpen2022.swishplugin.api.bossan.BossanAPI;
import io.github.musikhjalpen2022.swishplugin.api.bossan.PaymentData;
import io.github.musikhjalpen2022.swishplugin.api.bossan.PaymentStatus;
import org.bukkit.entity.Player;

public class BossanPayment extends UpdatablePayment {

    private String phoneNumber;
    private String referenceNumber;

    public BossanPayment(int amount, Player player, String phoneNumber) {
        super(amount, player);
        this.phoneNumber = phoneNumber;
        this.referenceNumber = null;
    }

    @Override
    public void sendRequest() {
        PaymentData paymentData = new PaymentData(phoneNumber, amount, player.getDisplayName());
        BossanAPI.requestPayment(paymentData, this::onRequestResponse);
    }

    private void onRequestResponse(String referenceNumber) {
        System.out.println("Got reference number: " + referenceNumber);
        if (referenceNumber == null) {
            result = new PaymentResult(player.getUniqueId(), amount, false, this);
            onPaymentResult(result);
        }
        this.referenceNumber = referenceNumber;
    }

    @Override
    protected void update() {
        if (referenceNumber != null) {
            BossanAPI.checkPayment(referenceNumber, this::onUpdateResponse);
        }
    }

    private void onUpdateResponse(PaymentStatus status) {
        System.out.println("Update complete: " + status.name());
        if (status != PaymentStatus.WAITING) {
            boolean paid = status == PaymentStatus.PAID;
            result = new PaymentResult(player.getUniqueId(), amount, paid, this);
            onPaymentResult(result);
        }
    }

}
