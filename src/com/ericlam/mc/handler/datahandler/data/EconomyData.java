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
        ArrayList<DataPackage> clone = new ArrayList<>();
        List<OfflinePlayer> offlinePlayers = Arrays.stream(Bukkit.getOfflinePlayers()).filter(player -> !ConfigManager.filter_players.contains(player.getName())).collect(Collectors.toList());
        for (OfflinePlayer player : offlinePlayers) {
            if (ConfigManager.debug) plugin.getLogger().info("經濟資料: 正在獲取 " + player.getName() + " 的資料");
            double money = economy.getBalance(player);
            if (money == 0) {
                Essentials ess = (Essentials) Essentials.getProvidingPlugin(Essentials.class);
                User user = ess.getUser(player.getUniqueId());
                if (user == null) continue;
                money = user.getMoney().intValue();
            }
            if (Math.round(money) == 0) continue;
            DataPackage dataPackage = new DataPackage(player,money);
            clone.add(dataPackage);
            int row = clone.size();
            if (row % 300 == 0 && row != 0) {
                datas = clone; // If data is too huge, will get every 300 data for buffer
            }
        }
        datas = clone;
        return datas.size() > 0;
    }

    @Override
    public ArrayList<DataPackage> gainDataList() {
        return datas;
    }
}
