package org.watchdragon.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.watchdragon.api.IInitialzation;

/**
 * Created by tt36999 on 2017/6/23.
 */
public class UpdaterController implements IInitialzation {
    @FXML private Button button;

    @FXML protected void onClick() throws Exception{
        button.setText("aaaaaa");
    }

    @Override
    public boolean onInit() {
        return false;
    }
}
