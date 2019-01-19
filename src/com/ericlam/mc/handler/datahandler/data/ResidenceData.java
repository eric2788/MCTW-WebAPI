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

    @Override
    public boolean loadDatas() {
        ResidencePlayer user;
        PermissionGroup group;
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            user = new ResidencePlayer(player);
            group = user.getGroup();
            int x = group.getMaxX();
            int y = group.getMaxY();
            int z = group.getMaxZ();
            datas.add(new DataPackage(player,x*y*z));
        }
        return datas.size() > 0;
    }

    @Override
    public ArrayList<DataPackage> gainDataList() {
        return datas.stream().filter(data -> data.getData() != 0).collect(Collectors.toCollection(ArrayList::new));
    }
}
