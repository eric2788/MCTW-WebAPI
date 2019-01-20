package com.ericlam.mc.handler;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDateTime;

public class RefreshScheduler {
    public static LocalDateTime localDateTime;

    public RefreshScheduler(Plugin plugin) {
        synchronized (this) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    APIHandler.refreshDatas(plugin);
                    localDateTime = LocalDateTime.now();
                }
            }.runTaskTimerAsynchronously(plugin, 3600 * 20L, 3600 * 20L);
        }
    }

}
