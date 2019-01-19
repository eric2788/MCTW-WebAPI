package com.ericlam.mc.handler.datahandler.data;

import com.bekvon.bukkit.residence.containers.ResidencePlayer;
import com.bekvon.bukkit.residence.permissions.PermissionGroup;
import com.ericlam.mc.handler.datahandler.DataHandler;
import com.ericlam.mc.handler.datahandler.DataPackage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ResidenceData implements DataHandler {
    private ArrayList<DataPackage> datas = new ArrayList<>();

    @Override
    public boolean loadDatas() {
        ResidencePlayer user;
        PermissionGroup group;
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            user = new ResidencePlayer(player);
            DataPackage dataPackage = new DataPackage(player,user.getResAmount());
            if (!datas.contains(dataPackage)) datas.add(dataPackage);
        }
        return datas.size() > 0;
    }

    @Override
    public ArrayList<DataPackage> gainDataList() {
        return datas.stream().filter(data -> data.getData() != 0).collect(Collectors.toCollection(ArrayList::new));
    }
}
