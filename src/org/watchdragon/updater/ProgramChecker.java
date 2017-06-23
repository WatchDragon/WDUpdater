package org.watchdragon.updater;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.scene.control.Alert;
import org.watchdragon.Main;
import org.watchdragon.file.FileUtils;
import org.watchdragon.http.HttpUtils;
import org.watchdragon.http.downloader.Download;

import java.io.IOException;

/**
 * Created by tt36999 on 2017/6/23.
 */
public class ProgramChecker {
    private JsonArray localJson;
    public ProgramChecker() throws IOException {
        HttpUtils http = new HttpUtils(Main.GET_VERSION_JSON_URL);
        String res = http.get().getResponse();
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
                    Download.downloadFile(r.get("url").getAsString(),"");
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
            HttpUtils http = new HttpUtils(Main.GET_VERSION_JSON_URL);
            String json = http.get().getResponse();
            FileUtils.writeFileFromString(json,Main.PROGRAM_VERSION_JSON,false);
            return json;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
