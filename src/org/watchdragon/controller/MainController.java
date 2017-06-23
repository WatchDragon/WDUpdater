package org.watchdragon.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

/**
 * Created by tt36999 on 2017/6/23.
 */
public class MainController {
    private Stage stage;
    private Scene scene;

    @FXML private TabPane mainTabPane;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }
}
