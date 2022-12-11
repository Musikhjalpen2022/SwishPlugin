package io.github.musikhjalpen2022.swishplugin.scoreboard;

import io.github.musikhjalpen2022.swishplugin.donation.Donor;

import java.util.*;

public class DonationScoreboard extends Scoreboard {

    private static final String DISPLAY_NAME = "&aMusikhjälpen";
    private static final int SLOTS = 12;

    public DonationScoreboard(){
        super(SLOTS, DISPLAY_NAME);
        setSlot(11, "");
        setSlot(10, " Minecraft Bidrag ");
        // Total donations
        setSlot(8, "");
        setSlot(7, "     Toppgivare");
        // Top donations
        setSlot(2, "");
        setSlot(1, "     Ditt bidrag");
        // Player donation
    }

    public void setTotalAmount(Integer amount){
        setSlot(9, String.format("&a        %d kr", amount));
    }

    public void setTopList(List<Donor> donors) {
        for (int i = 0; i < 3; i++) {
            if (i < donors.size()) {
                Donor donor = donors.get(i);
                setSlot(6-i, String.format("%s%d&f %s &a%d kr",
                        TOP_LIST_COLORS[i],
                        i+1,
                        colorUsername(donor.getUsername(), donor.getPlayerId()),
                        donor.getTotalDonations()
                ));
            } else {
                setSlot(6-i, "");
            }
        }
    }

    public void setPlayerDonation(int amount) {
        setSlot(0, String.format("&a        %d kr", amount));
    }
}
