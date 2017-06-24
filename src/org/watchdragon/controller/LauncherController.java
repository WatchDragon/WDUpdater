package org.watchdragon.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import org.watchdragon.Main;
import org.watchdragon.updater.ProgramChecker;

import java.io.IOException;

/**
 * Created by zjyl1994 on 2017/6/24.
 */
public class LauncherController {
    @FXML private Button button;

    @FXML protected void onClick() throws Exception{

    }

    @FXML//界面初始化方法，自动被JavaFX调用
    public boolean initialize () {

        return true;
    }
}
