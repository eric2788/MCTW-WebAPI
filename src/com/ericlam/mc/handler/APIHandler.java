package com.ericlam.mc.handler;

import com.ericlam.mc.handler.datahandler.*;
import com.ericlam.mc.handler.datahandler.data.EconomyData;
import com.ericlam.mc.handler.datahandler.data.ResidenceData;
import com.ericlam.mc.handler.datahandler.data.VIPRankData;
import com.ericlam.mc.main.ConfigManager;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_13_R2.util.MojangNameLookup;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class APIHandler {

    static ArrayList<JSONObject> getAPIDatas(APIData dataType){
       ArrayList<JSONObject> datalist = new ArrayList<>();
       String steveUUID = "8667ba71-b85a-4004-af54-457a9734eed7";
       DataHandler data;
       switch (dataType){
           case ECONOMY:
               data = new EconomyData();
               break;
           case RESIDENCE:
               data = new ResidenceData();
               break;
           case VIPRANK:
               data = new VIPRankData();
               break;
            default:
                return datalist;
       }

        if (!data.loadDatas()) return datalist;
        for (DataPackage aPackage : data.gainDataList()) {
            LinkedHashMap<String,Object> object = new LinkedHashMap<>();
            OfflinePlayer player = aPackage.getPlayer();
            object.put("player",player.getName());
            object.put("data",(int)aPackage.getData());
            object.put("uuid", ConfigManager.premium ? player.getUniqueId().toString() : steveUUID);
            if (dataType == APIData.VIPRANK) object.put("group",VaultHandler.permission.getPrimaryGroup(null,player));
            JSONObject jsonObject = new JSONObject(object);
            datalist.add(jsonObject);
        }
        return datalist;
    }

    static JSONObject refreshDatas(){
        boolean eco =new EconomyData().loadDatas();
        boolean res = new ResidenceData().loadDatas();
        boolean vip = new VIPRankData().loadDatas();
        HashMap<String,Boolean> result = new HashMap<>();
        result.put("success",eco && vip && res);
        return new JSONObject(result);
    }

    public static boolean checkDuplicates(OfflinePlayer player, ArrayList<DataPackage> datas) {
        boolean duplicate = false;
        for (DataPackage data : datas) {
            if (data.getPlayer().getName().equalsIgnoreCase(player.getName()))  duplicate = true;
        }
        return duplicate;
    }

    public static void clearNonPremiumPlayers() throws IOException {
        File worldFiles = Bukkit.getServer().getWorldContainer();
        for (World world : Bukkit.getServer().getWorlds()) {
            File worldFile = new File(worldFiles,world.getName()+File.separator+"playerdata");
            if (worldFile.listFiles() == null) return;
            for (File file : Objects.requireNonNull(worldFile.listFiles())) {
                String uuid = file.getName().substring(0,36);
                System.out.println(uuid);
                if (MojangNameLookup.lookupName(UUID.fromString(uuid)) == null) FileUtils.forceDelete(file);
            }
        }
    }
}
