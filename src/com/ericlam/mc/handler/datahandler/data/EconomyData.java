package com.ericlam.mc.handler.datahandler.data;


import com.ericlam.mc.handler.VaultHandler;
import com.ericlam.mc.handler.datahandler.DataHandler;
import com.ericlam.mc.handler.datahandler.DataPackage;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class EconomyData implements DataHandler {

    private final Economy economy;
    private ArrayList<DataPackage> datas = new ArrayList<>();

    public EconomyData(){
        economy = VaultHandler.economy;
    }

    @Override
    public boolean loadDatas() {
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            double money = economy.getBalance(player);
            DataPackage dataPackage = new DataPackage(player,money);
            if (!datas.contains(dataPackage))datas.add(dataPackage);
        }
        return datas.size() > 0;
    }

    @Override
    public ArrayList<DataPackage> gainDataList() {
        return datas.stream().filter(data -> data.getData() != 0).collect(Collectors.toCollection(ArrayList::new));
    }
}
