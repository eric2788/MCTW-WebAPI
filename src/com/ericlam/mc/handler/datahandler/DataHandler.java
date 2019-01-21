package com.ericlam.mc.handler.datahandler;

import com.ericlam.mc.main.MCTWWebAPI;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public interface DataHandler {

    Plugin plugin = MCTWWebAPI.getProvidingPlugin(MCTWWebAPI.class);

    boolean loadDatas();

    ArrayList<DataPackage> gainDataList();

}

