package io.github.musikhjalpen2022.swishplugin.payment;

import org.bukkit.entity.Player;

public abstract class UpdatablePayment extends Payment {

    public UpdatablePayment(int amount, Player player) {
        super(amount, player);
    }

    protected abstract void update();

}
