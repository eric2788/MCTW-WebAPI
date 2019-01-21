package com.ericlam.mc.handler.datahandler.data;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.containers.ResidencePlayer;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.ericlam.mc.handler.datahandler.DataHandler;
import com.ericlam.mc.handler.datahandler.DataPackage;
import com.ericlam.mc.main.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class ResidenceData implements DataHandler {
    private HashSet<DataPackage> datas = new HashSet<>();
    private static ResidenceData data;

    public static ResidenceData getInstance() {
        if (data == null) data = new ResidenceData();
        return data;
    }

    @Override
    public void loadDatas() {
        HashSet<DataPackage> clone = new HashSet<>();
        ResidencePlayer user;
        List<OfflinePlayer> offlinePlayers = Arrays.stream(Bukkit.getOfflinePlayers()).filter(player -> !ConfigManager.filter_players.contains(player.getName())).collect(Collectors.toList());
        for (OfflinePlayer player : offlinePlayers) {
            if (ConfigManager.debug) plugin.getLogger().info("領地資料: 正在獲取 " + player.getName() + " 的資料");
            user = Residence.getInstance().getPlayerManager().getResidencePlayer(player.getUniqueId());
            int size = 0;
            for (ClaimedResidence residence : user.getResList()) {
                size += residence.getTotalSize();
            }
            if (size == 0) continue;
            clone.add(new DataPackage(player, size));
            int row = clone.size();
            if (row % 300 == 0 && row != 0) {
                datas = (HashSet<DataPackage>) clone.clone(); // If data is too huge, will get every 300 data for buffer
            }
        }
        datas = (HashSet<DataPackage>) clone.clone();
        plugin.getLogger().info("領地資料獲取完成。");
        datas.size();
    }

    @Override
    public HashSet<DataPackage> gainDataList() {
        return datas;
    }
}
