package org.watchdragon.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.watchdragon.api.IInitialzation;

/**
 * Created by tt36999 on 2017/6/23.
 */
public class MainController implements IInitialzation {
    private Stage stage;
    private Scene scene;

    @FXML private TabPane mainTabPane;

    @Override
    public boolean onInit() {
        //Do somethig init
        return true;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }
}
