package com.ericlam.mc.handler.datahandler.data;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.containers.ResidencePlayer;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.ericlam.mc.handler.datahandler.DataHandler;
import com.ericlam.mc.handler.datahandler.DataPackage;
import com.ericlam.mc.main.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ResidenceData implements DataHandler {
    private ArrayList<DataPackage> datas = new ArrayList<>();
    private static ResidenceData data;

    public static ResidenceData getInstance() {
        if (data == null) data = new ResidenceData();
        return data;
    }

    @Override
    public boolean loadDatas() {
        datas.clear();
        ResidencePlayer user;
        List<OfflinePlayer> offlinePlayers = Arrays.stream(Bukkit.getOfflinePlayers()).filter(player -> !ConfigManager.filter_players.contains(player.getName())).collect(Collectors.toList());
        for (OfflinePlayer player : offlinePlayers) {
            user = Residence.getInstance().getPlayerManager().getResidencePlayer(player.getUniqueId());
            int size = 0;
            for (ClaimedResidence residence : user.getResList()) {
                size += residence.getTotalSize();
            }
            if (size == 0) continue;
            DataPackage dataPackage = new DataPackage(player, size);
            datas.add(dataPackage);
        }
        return datas.size() > 0;
    }

    @Override
    public ArrayList<DataPackage> gainDataList() {
        return datas;
    }
}
