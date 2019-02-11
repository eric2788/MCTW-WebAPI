package com.ericlam.mc.handler.datahandler.data;


import com.ericlam.mc.handler.VaultHandler;
import com.ericlam.mc.handler.datahandler.DataHandler;
import com.ericlam.mc.handler.datahandler.DataPackage;
import com.ericlam.mc.main.ConfigManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class EconomyData implements DataHandler {

    private final Economy economy;
    private HashSet<DataPackage> datas = new HashSet<>();
    private static EconomyData data;
    //private final Essentials ess;

    private EconomyData() {
        economy = VaultHandler.economy;
        // ess = (Essentials) Essentials.getProvidingPlugin(Essentials.class);
    }

    public static EconomyData getInstance() {
        if (data == null) data = new EconomyData();
        return data;
    }

    @Override
    public void loadDatas() {

        HashSet<DataPackage> clone = new HashSet<>();
        List<OfflinePlayer> offlinePlayers = Arrays.stream(Bukkit.getOfflinePlayers()).filter(player -> !ConfigManager.filter_players.contains(player.getName())).collect(Collectors.toList());
        for (OfflinePlayer player : offlinePlayers) {
            if (ConfigManager.debug) plugin.getLogger().info("經濟資料: 正在獲取 " + player.getName() + " 的資料");
            double money = economy.getBalance(player, Bukkit.getWorlds().get(0).getName());
            /*if (money == 0) {
                User user = ess.getUser(player.getUniqueId());
                if (user == null) continue;
                money = user.getMoney().intValue();
            }*/
            if (Math.round(money) == 0) continue;
            DataPackage dataPackage = new DataPackage(player,money);
            clone.add(dataPackage);
            int row = clone.size();
            if (row % 500 == 0 && row != 0) {
                datas = (HashSet<DataPackage>) clone.clone(); // If data is too huge, will get every 300 data for buffer
            }
        }
        datas = (HashSet<DataPackage>) clone.clone();
        plugin.getLogger().info("經濟資料獲取完成。");
    }

    @Override
    public HashSet<DataPackage> gainDataList() {
        return datas;
    }
}
