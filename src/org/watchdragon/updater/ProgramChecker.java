package org.watchdragon.updater;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.scene.control.Alert;
import org.watchdragon.Main;
import org.watchdragon.file.FileUtils;
import org.watchdragon.http.HttpUtils;

import java.io.IOException;

/**
 * Created by tt36999 on 2017/6/23.
 */
public class ProgramChecker implements IUpdateAble {
    private JsonArray localJson;
    public ProgramChecker(){
        String res = HttpUtils.get(Main.GET_VERSION_JSON_URL,"");
        JsonParser parser = new JsonParser();
        String jsonString = this.readJson();
        if(null==jsonString){
            Main.showAlert("无法检查更新，请检查网络链接可用并且程序可以读写磁盘","错误", Alert.AlertType.ERROR);
            return;
        }
        this.localJson = (JsonArray) parser.parse(jsonString);
        JsonArray remoteJson = (JsonArray)parser.parse(res);
        for(JsonElement remot : remoteJson){
            for(JsonElement local : this.localJson){
                JsonObject r = remot.getAsJsonObject();
                JsonObject l = local.getAsJsonObject();
                if(r.get("name").equals(l.get("name"))&&!r.get("version").equals(l.get("version"))){
                    System.out.println(r.get("name").getAsString()+":need update");
                }
            }
        }
    }

    private String readJson(){
        try {
           return FileUtils.readFileAsString(Main.PROGRAM_VERSION_JSON);
        } catch (IOException e) {
            return createdNewJson();
        }
    }

    private String createdNewJson(){
        try {
            String json = HttpUtils.get(Main.GET_VERSION_JSON_URL,"");
            FileUtils.writeFileFromString(json,Main.PROGRAM_VERSION_JSON,false);
            return json;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
