package io.github.musikhjalpen2022.swishplugin.log;

import org.bukkit.entity.Player;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PlayerLogger {

    private final Path file;
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public PlayerLogger(String filename) {
        this.file = Paths.get(filename);
        logServerStart();
    }


    private void logLine(String line) {
        try {
            Files.writeString(file, line + "\n", StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void logServerStart() {
        LocalDateTime eventTime = LocalDateTime.now();
        String log = String.format("%s,%s", eventTime.format(formatter), "START");
        logLine(log);
    }


    public void logPlayerJoin(Player player) {
        LocalDateTime eventTime = LocalDateTime.now();
        String log = String.format("%s,%s,%s,%s", eventTime.format(formatter), "JOIN", player.getDisplayName(), player.getUniqueId().toString());
        logLine(log);
    }

    public void logPlayerQuit(Player player) {
        LocalDateTime eventTime = LocalDateTime.now();
        String log = String.format("%s,%s,%s,%s", eventTime.format(formatter), "QUIT", player.getDisplayName(), player.getUniqueId().toString());
        logLine(log);
    }


}
