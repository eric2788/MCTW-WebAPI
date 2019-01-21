package com.ericlam.mc.handler.datahandler.data;


import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.ericlam.mc.handler.VaultHandler;
import com.ericlam.mc.handler.datahandler.DataHandler;
import com.ericlam.mc.handler.datahandler.DataPackage;
import com.ericlam.mc.main.ConfigManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EconomyData implements DataHandler {

    private final Economy economy;
    private ArrayList<DataPackage> datas = new ArrayList<>();
    private static EconomyData data;

    private EconomyData() {
        economy = VaultHandler.economy;
    }

    public static EconomyData getInstance() {
        if (data == null) data = new EconomyData();
        return data;
    }

    @Override
    public boolean loadDatas() {
        datas.clear();
        List<OfflinePlayer> offlinePlayers = Arrays.stream(Bukkit.getOfflinePlayers()).filter(player -> !ConfigManager.filter_players.contains(player.getName())).collect(Collectors.toList());
        for (OfflinePlayer player : offlinePlayers) {
            double money = economy.getBalance(player);
            if (money == 0) {
                Essentials ess = (Essentials) Essentials.getProvidingPlugin(Essentials.class);
                User user = ess.getUser(player.getUniqueId());
                if (user == null) continue;
                money = user.getMoney().intValue();
            }
            if (money == 0) continue;
            DataPackage dataPackage = new DataPackage(player,money);
            datas.add(dataPackage);
        }
        return datas.size() > 0;
    }

    @Override
    public ArrayList<DataPackage> gainDataList() {
        return datas;
    }
}
