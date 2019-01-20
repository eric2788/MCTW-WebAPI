package com.ericlam.mc.handler.datahandler.data;

import com.ericlam.mc.handler.VaultHandler;
import com.ericlam.mc.handler.datahandler.DataHandler;
import com.ericlam.mc.handler.datahandler.DataPackage;
import com.ericlam.mc.main.ConfigManager;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class VIPRankData implements DataHandler {
    private Permission permission;
    private ArrayList<String> groups = new ArrayList<>();
    private ArrayList<DataPackage> datas = new ArrayList<>();
    private static VIPRankData data;

    private VIPRankData() {
        permission = VaultHandler.permission;
    }

    public static VIPRankData getInstance() {
        if (data == null) data = new VIPRankData();
        return data;
    }

    @Override
    public boolean loadDatas() {
        datas.clear();
        List<OfflinePlayer> vipers = Arrays.stream(Bukkit.getOfflinePlayers()).filter(player -> !ConfigManager.filter_players.contains(player.getName())).collect(Collectors.toList());
        for (OfflinePlayer viper : vipers) {
            String group = permission.getPrimaryGroup(null,viper);
            if (group == null) {
                group = permission.getPrimaryGroup(Bukkit.getWorlds().get(0).getName(), viper);
            }
            if (!group.contains("VIP")) continue;
            if (!groups.contains(group)) groups.add(group);
        }

        Comparator<String> comparator = (prev, next) -> {
            String prevS = prev.replaceAll("\\D", "");
            int prevN = Integer.parseInt(prevS.isEmpty() ? "0" : prevS);
            String nextS = next.replaceAll("\\D", "");
            int nextN = Integer.parseInt(nextS.isEmpty() ? "0" : nextS);
            return Integer.compare(prevN, nextN);
        };
        
        groups.sort(comparator);

        for (OfflinePlayer viper : vipers) {
            String group = permission.getPrimaryGroup(null,viper);
            if (group == null) {
                group = permission.getPrimaryGroup(Bukkit.getWorlds().get(0).getName(), viper);
            }
            if (!group.contains("VIP")) continue;
            String groupS = group.replaceAll("\\D", "");
            int groupIndex = Integer.parseInt(groupS.isEmpty() ? "0" : groupS);
            datas.add(new DataPackage(viper, (double) groupIndex));
        }
        return datas.size() > 0;
    }

    @Override
    public ArrayList<DataPackage> gainDataList() {
        return datas;
    }
}
