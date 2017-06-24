package org.watchdragon.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.web.WebView;
import org.watchdragon.Main;
import org.watchdragon.updater.ProgramChecker;

import java.io.IOException;

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

    @FXML//界面初始化方法，自动被JavaFX调用
    public boolean initialize () {
        memorySize.getItems().removeAll(memorySize.getItems());
        memorySize.getItems().addAll("512MB","768MB","1GB","1.25GB","1.5GB","1.75GB","2GB","2.5GB","3GB","3.5GB","4GB","6GB","8GB");
        memorySize.getSelectionModel().select(2);
        wdNews.getEngine().load("https://news.watchdragon.org/");
        return true;
    }

    public void onLegalSwitch(ActionEvent actionEvent) {
        this.passWord.setDisable(!this.legalSwitch.isSelected());
    }
}
