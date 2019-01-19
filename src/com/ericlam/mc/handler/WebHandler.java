package com.ericlam.mc.handler;

import com.ericlam.mc.handler.datahandler.APIData;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class WebHandler extends AbstractHandler {
    @Override
    public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json; charset=utf-8");
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET");

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);

        PrintWriter writer = httpServletResponse.getWriter();

        switch (s){
            case "/economy":
                writer.println(JSONArray.toJSONString(APIHandler.getAPIDatas(APIData.ECONOMY)));
                break;
            case "/residence":
                writer.println(JSONArray.toJSONString(APIHandler.getAPIDatas(APIData.RESIDENCE)));
                break;
            case "/viprank":
                writer.println(JSONArray.toJSONString(APIHandler.getAPIDatas(APIData.VIPRANK)));
                break;
            case "/refresh":
                writer.println(APIHandler.refreshDatas().toJSONString());
                break;
            default:
                writer.println(new JSONObject().toJSONString());
                break;
        }

        request.setHandled(true);
    }
}
