package org.watchdragon.controller;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.web.WebView;
import org.watchdragon.Main;
import org.watchdragon.file.FileUtils;
import org.watchdragon.model.MCVerisonItem;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by zjyl1994 on 2017/6/24.
 */
public class LauncherController {
    @FXML private WebView wdNews;
    @FXML private Button startButton;
    @FXML private ToggleButton legalSwitch;
    @FXML private TextField playerName;
    @FXML private PasswordField passWord;
    @FXML private ComboBox mcVersion;
    @FXML private ComboBox memorySize;
    private Optional<MCVerisonItem[]> versions;


    @FXML//界面初始化方法，自动被JavaFX调用
    public boolean initialize () {
        memorySize.getItems().removeAll(memorySize.getItems());
        memorySize.getItems().addAll("512MB","768MB","1GB","1.25GB","1.5GB","1.75GB","2GB","2.5GB","3GB","3.5GB","4GB","6GB","8GB");
        memorySize.getSelectionModel().select(2);
        wdNews.getEngine().load("https://news.watchdragon.org/");
        try {
            String packages = FileUtils.readFileAsString("package.json");
            Gson gson=new Gson();
            versions = Optional.of(gson.fromJson(packages,MCVerisonItem[].class));
        } catch (IOException e) {
            Main.showAlert("无法加载整合包列表","完整性遭到破坏", Alert.AlertType.ERROR);
            startButton.setDisable(true);
            versions = Optional.empty();
            return false;
        }
        if(versions.isPresent()){
            MCVerisonItem[] mcVerisons = versions.get();
            mcVersion.getItems().clear();
            for(MCVerisonItem item:mcVerisons){
                mcVersion.getItems().add(item.getName());
            }
            mcVersion.getSelectionModel().select(0);
        }
        return true;
    }

    public void onLegalSwitch(ActionEvent actionEvent) {
        this.passWord.setDisable(!this.legalSwitch.isSelected());
    }
}
