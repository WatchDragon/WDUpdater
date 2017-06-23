package org.watchdragon.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import org.watchdragon.Main;
import org.watchdragon.updater.ProgramChecker;

import java.io.IOException;

/**
 * Created by tt36999 on 2017/6/23.
 */
public class UpdaterController{
    @FXML private Button button;

    @FXML protected void onClick() throws Exception{

    }

    @FXML//界面初始化方法，自动被JavaFX调用
    public boolean initialize () {
        try {
            ProgramChecker programChecker = new ProgramChecker();
        } catch (IOException e) {
            Main.showAlert("无法检查更新，请检查网络链接可用并且程序可以读写磁盘","错误", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }
}
