package io.github.musikhjalpen2022.swishplugin.donation;

import java.util.List;

public interface DonationListener {

    void onTotalDonationsChange(int totalDonations);

    void onTopDonorsChange(List<Donor> topDonors);

    void onDonorChange(Donor donor);

}
