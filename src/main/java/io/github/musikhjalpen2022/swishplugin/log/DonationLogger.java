package io.github.musikhjalpen2022.swishplugin.log;

import io.github.musikhjalpen2022.swishplugin.donation.Donor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class DonationLogger {

    private final Path file;
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public DonationLogger(String filename) {
        this.file = Paths.get(filename);
    }


    private void logLine(String line) {
        try {
            Files.writeString(file, line + "\n", StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void logDonation(String username, UUID playerId, int amount, boolean real) {
        LocalDateTime eventTime = LocalDateTime.now();
        String type = real ? "REAL" : "FAKE";
        String log = String.format("%s,%s,%s,%s,%d", eventTime.format(formatter), type, username, playerId.toString(), amount);
        logLine(log);
    }

}
