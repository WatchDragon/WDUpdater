package org.watchdragon.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.watchdragon.updater.ProgramChecker;

/**
 * Created by tt36999 on 2017/6/23.
 */
public class UpdaterController{
    @FXML private Button button;

    @FXML protected void onClick() throws Exception{

    }

    @FXML//界面初始化方法，自动被JavaFX调用
    public boolean initialize () {
        ProgramChecker programChecker = new ProgramChecker();
        return true;
    }

    private void checkProgramVersion(){

    }
}
