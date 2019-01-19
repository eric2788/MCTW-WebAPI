package com.ericlam.mc.main;

import com.ericlam.mc.handler.VaultHandler;
import com.ericlam.mc.handler.WebHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.eclipse.jetty.server.Server;

public class MCTWWebAPI extends JavaPlugin {
    @Override
    public void onEnable() {
        new ConfigManager(this);
        VaultHandler handler = new VaultHandler(this);

        if (!handler.setupEconomy() || !handler.setupPermissions()){
           this.getLogger().info("找不到經濟或權限插件! 插件將不會啟用。");
           this.getPluginLoader().disablePlugin(this);
           return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                Server server = new Server(ConfigManager.port);
                server.setHandler(new WebHandler());
                try{
                    server.start();
                    server.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(this);
        this.getLogger().info("MCTW - Web API 插件已啟用");
        this.getLogger().info("將開啟port "+ConfigManager.port+" 作為 HTTP API 伺服器");
    }


}
