package com.ericlam.mc.handler.datahandler;

import java.util.ArrayList;

public interface DataHandler {

    ArrayList<DataPackage> datas = new ArrayList<>();

    boolean loadDatas();

    ArrayList<DataPackage> gainDataList();

}

