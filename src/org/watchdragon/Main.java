package org.watchdragon;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.watchdragon.api.IInitialzation;
import org.watchdragon.controller.MainController;
import org.watchdragon.controller.UpdaterController;

import java.util.ArrayList;

public class Main extends Application {
    public static final String CINFIG_DIR = "./config/";
    public static final String MC_VERSION_JSON = "";

    public ArrayList<IInitialzation> controllers = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/mainview.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 800, 600);
        MainController controller = fxmlLoader.getController();
        this.registerControllers();
        for(IInitialzation tabs : controllers){
            tabs.onInit();
        }
        controller.setStage(primaryStage);
        controller.setScene(scene);
        primaryStage.setTitle("看龙启动更新器");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void registerControllers(){
        //你的注册要在我的页面之前，这样能保证你的启动器页面先初始化

        //注册更新页面控制器，我也不知道这样行不行。我猜的
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/updaterview.fxml"));
        UpdaterController updaterController = fxmlLoader.getController();
        controllers.add(updaterController);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
