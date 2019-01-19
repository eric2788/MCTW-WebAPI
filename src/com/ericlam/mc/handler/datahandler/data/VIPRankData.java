package com.ericlam.mc.handler.datahandler.data;

import com.ericlam.mc.handler.VaultHandler;
import com.ericlam.mc.handler.datahandler.DataHandler;
import com.ericlam.mc.handler.datahandler.DataPackage;
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

    public VIPRankData(){
        permission = VaultHandler.permission;
    }

    @Override
    public boolean loadDatas() {
        List<OfflinePlayer> vipers = Arrays.stream(Bukkit.getOfflinePlayers()).filter(player -> !player.isOp()).collect(Collectors.toList());
        for (OfflinePlayer viper : vipers) {
            String group = permission.getPrimaryGroup(null,viper);
            if (group == null) continue;
            if (!groups.contains(group)) groups.add(group);
        }

        Comparator<String> comparator = (prev, next) -> {
            int prevN = Integer.parseInt(prev.replaceAll("\\D",""));
            int nextN = Integer.parseInt(next.replaceAll("\\D",""));
            return Integer.compare(prevN, nextN);
        };
        
        groups.sort(comparator);

        for (OfflinePlayer viper : vipers) {
            String group = permission.getPrimaryGroup(null,viper);
            if (group == null) continue;
            datas.add(new DataPackage(viper,groups.indexOf(group)));
        }
        return datas.size() > 0;
    }

    @Override
    public ArrayList<DataPackage> gainDataList() {
        return datas;
    }
}
