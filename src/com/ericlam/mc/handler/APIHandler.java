package com.ericlam.mc.handler;

import com.ericlam.mc.handler.datahandler.*;
import com.ericlam.mc.handler.datahandler.data.EconomyData;
import com.ericlam.mc.handler.datahandler.data.ResidenceData;
import com.ericlam.mc.handler.datahandler.data.VIPRankData;
import com.ericlam.mc.main.ConfigManager;
import org.bukkit.OfflinePlayer;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

class APIHandler {

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
            object.put("data",aPackage.getData());
            object.put("uuid", ConfigManager.premium ? player.getUniqueId() : steveUUID);
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
}
