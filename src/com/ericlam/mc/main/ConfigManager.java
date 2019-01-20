package com.ericlam.mc.main;

import com.ericlam.mc.handler.APIHandler;
import com.ericlam.mc.handler.RefreshScheduler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    public static boolean filter_enabled, premiumServer;
    static int port;

    ConfigManager(Plugin plugin){
        File configFile = new File(plugin.getDataFolder(),"config.yml");
        if (!configFile.exists()) plugin.saveResource("config.yml",true);
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        filter_enabled = config.getBoolean("filter");
        port = config.getInt("web-port");
        premiumServer = config.getBoolean("premium");
        plugin.getLogger().info("正版資料過濾將在稍後在後台啟動。");
        new RefreshScheduler(plugin);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (ConfigManager.filter_enabled) {
                    try {
                        APIHandler.clearPlayerData(plugin, premiumServer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    APIHandler.refreshDatas(plugin);
                }
            }
        }.runTaskAsynchronously(plugin);

    }
}
