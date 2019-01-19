package com.ericlam.mc.main;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class ConfigManager {

    public static boolean premium;
    static int port;

    ConfigManager(Plugin plugin){
        File configFile = new File(plugin.getDataFolder(),"config.yml");
        if (!configFile.exists()) plugin.saveResource("config.yml",true);
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        premium = config.getBoolean("premium");
        port = config.getInt("web-port");
    }
}
