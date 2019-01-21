package com.ericlam.mc.handler.datahandler;

import org.bukkit.OfflinePlayer;

public class DataPackage {
    private OfflinePlayer player;
    private double data;

    public DataPackage(OfflinePlayer player, double data) {
        this.player = player;
        this.data = data;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }

    public double getData() {
        return data;
    }
}
