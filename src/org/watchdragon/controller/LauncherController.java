package org.watchdragon.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.web.WebView;
import org.watchdragon.Main;
import org.watchdragon.updater.ProgramChecker;

import java.io.IOException;

/**
 * Created by zjyl1994 on 2017/6/24.
 */
public class LauncherController {
    @FXML private WebView wdNews;
    @FXML//界面初始化方法，自动被JavaFX调用
    public boolean initialize () {
        wdNews.getEngine().load("https://news.watchdragon.org/");
        return true;
    }
}
