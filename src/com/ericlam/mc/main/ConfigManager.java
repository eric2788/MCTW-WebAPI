package com.ericlam.mc.main;

import com.ericlam.mc.handler.APIHandler;
import com.ericlam.mc.handler.RefreshScheduler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ConfigManager {

    public static boolean premiumServer, debug;
    public static String server_name, steve_uuid;
    public static List<String> filter_players, except_players;
    private boolean filter_enabled;
    static int port;

    ConfigManager(Plugin plugin){
        File configFile = new File(plugin.getDataFolder(),"config.yml");
        if (!configFile.exists()) plugin.saveResource("config.yml",true);
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        filter_enabled = config.getBoolean("filter");
        port = config.getInt("web-port");
        premiumServer = config.getBoolean("premium");
        filter_players = config.getStringList("filter-players");
        //except_players = config.getStringList("exception-players");
        server_name = config.getString("server-name");
        steve_uuid = config.getString("steve-uuid");
        debug = config.getBoolean("debug");
        new RefreshScheduler(plugin);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (filter_enabled) {
                    try {
                        plugin.getLogger().info("玩家資料過濾將在稍後在後台啟動。");
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
