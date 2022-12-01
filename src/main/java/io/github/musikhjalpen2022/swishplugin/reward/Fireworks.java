package io.github.musikhjalpen2022.swishplugin.reward;

import io.github.musikhjalpen2022.swishplugin.SwishPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class Fireworks {

    public static FireworkEffect getFireworkEffect(Color color1, Color color2, org.bukkit.FireworkEffect.Type fireworkType, Boolean flicker, Boolean trail) {
        Random rand = new Random();
        if (color1 == null) {
            color1 = Color.fromRGB(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
        }
        if (color2 == null) {
            color2 = Color.fromRGB(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
        }
        if (fireworkType == null) {
            fireworkType = org.bukkit.FireworkEffect.Type.values()[rand.nextInt(org.bukkit.FireworkEffect.Type.values().length)];
        }
        if (flicker == null) {
            flicker = rand.nextBoolean();
        }
        if (trail == null) {
            trail = rand.nextBoolean();
        }
        return FireworkEffect.builder().flicker(flicker).withColor(color1).withFade(color2).with(fireworkType).trail(trail).build();
    }

    public static void spawnFireworks(SwishPlugin swishPlugin, Player player, int amount){

        BukkitRunnable bukkitRunnable = new BukkitRunnable() {

            private int count = amount;

            @Override
            public void run() {
                Firework fw = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
                FireworkMeta fwm = fw.getFireworkMeta();
                fwm.setPower(2);
                fwm.addEffect(getFireworkEffect(null, null, null, null, null));
                //fwm.addEffect(FireworkEffect.builder().withColor(Color.LIME).flicker(true).build());
                fw.setFireworkMeta(fwm);

                if (--count == 0) {
                    this.cancel();
                }
            }
        };
        bukkitRunnable.runTaskTimer(swishPlugin, 0, (long)(0.2*20L));
    }

}
