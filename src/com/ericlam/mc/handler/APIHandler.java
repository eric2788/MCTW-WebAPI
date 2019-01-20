package com.ericlam.mc.handler;

import com.ericlam.mc.handler.datahandler.APIData;
import com.ericlam.mc.handler.datahandler.DataHandler;
import com.ericlam.mc.handler.datahandler.DataPackage;
import com.ericlam.mc.handler.datahandler.data.EconomyData;
import com.ericlam.mc.handler.datahandler.data.ResidenceData;
import com.ericlam.mc.handler.datahandler.data.VIPRankData;
import com.ericlam.mc.main.ConfigManager;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_13_R2.util.MojangNameLookup;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class APIHandler {

    static ArrayList<JSONObject> getAPIDatas(APIData dataType){
       ArrayList<JSONObject> datalist = new ArrayList<>();
       String steveUUID = "8667ba71-b85a-4004-af54-457a9734eed7";
       DataHandler data;
       switch (dataType){
           case ECONOMY:
               data = EconomyData.getInstance();
               break;
           case RESIDENCE:
               data = ResidenceData.getInstance();
               break;
           case VIPRANK:
               data = VIPRankData.getInstance();
               break;
            default:
                return datalist;
       }
        if (data.gainDataList().size() == 0) return datalist;
        for (DataPackage aPackage : data.gainDataList()) {
            LinkedHashMap<String,Object> object = new LinkedHashMap<>();
            OfflinePlayer player = aPackage.getPlayer();
            object.put("player",player.getName());
            object.put("data",(int)aPackage.getData());
            object.put("uuid", ConfigManager.premiumServer ? player.getUniqueId().toString() : steveUUID);
            if (dataType == APIData.VIPRANK) {
                String group = VaultHandler.permission.getPrimaryGroup(null, player);
                if (group == null) {
                    group = VaultHandler.permission.getPrimaryGroup(Bukkit.getWorlds().get(0).getName(), player);
                }
                if (!group.contains("VIP")) continue;
                object.put("group", group);
            }
            JSONObject jsonObject = new JSONObject(object);
            datalist.add(jsonObject);
        }
        return datalist;
    }

    public static JSONObject refreshDatas() {
        boolean eco = EconomyData.getInstance().loadDatas();
        boolean res = ResidenceData.getInstance().loadDatas();
        boolean vip = VIPRankData.getInstance().loadDatas();
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

    public static void clearPlayerData(Plugin plugin, boolean isPremium) throws IOException {
        File worldFiles = Bukkit.getServer().getWorldContainer();
        for (World world : Bukkit.getServer().getWorlds()) {
            File worldFile = new File(worldFiles,world.getName()+File.separator+"playerdata");
            if (worldFile.listFiles() == null) return;
            File directory = new File(worldFile, isPremium ? "non-premium-data" : "premium-data");
            for (File file : Objects.requireNonNull(worldFile.listFiles())) {
                String uuid = file.getName().substring(0,36);
                String name = MojangNameLookup.lookupName(UUID.fromString(uuid));
                if (name == null && !isPremium) continue;
                else if (name != null && isPremium) continue;
                if (!directory.exists()) FileUtils.forceMkdir(directory);
                FileUtils.copyFileToDirectory(file, directory);
                FileUtils.forceDelete(file);
            }
            if (!directory.exists() || directory.listFiles() == null) return;
            byte[] buffer = new byte[1024];
            FileOutputStream fileOutputStream = new FileOutputStream(directory.getName().concat(".zip"));
            ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
            for (File file : Objects.requireNonNull(directory.listFiles())) {
                ZipEntry zip = new ZipEntry(file.getName());
                zipOutputStream.putNextEntry(zip);
                FileInputStream in = new FileInputStream(file.getAbsolutePath());
                int len;
                while ((len = in.read(buffer)) > 0) {
                    zipOutputStream.write(buffer, 0, len);
                }

                in.close();
            }
            plugin.getLogger().info("已把過濾後的玩家資料檔案壓縮在了 " + worldFile.getPath());
            zipOutputStream.closeEntry();
            zipOutputStream.close();
            fileOutputStream.close();
            FileUtils.forceDelete(directory);
        }
        refreshDatas();
    }
}
