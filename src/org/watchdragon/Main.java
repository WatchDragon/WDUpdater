package org.watchdragon;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.watchdragon.controller.MainController;

import java.util.Optional;

public class Main extends Application {
    public static final String SERVER_ROOT="http://127.0.0.1/WDUpdater/";
    public static final String GET_VERSION_JSON_URL = SERVER_ROOT+"Json/getGoJarJson";

    public static final String CINFIG_DIR = System.getProperty("user.dir")+"/config/";
    public static final String PROGRAM_VERSION_JSON = CINFIG_DIR+"prgram.json";

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/mainview.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 800, 600);
        MainController controller = fxmlLoader.getController();
        controller.setStage(primaryStage);
        controller.setScene(scene);
        primaryStage.setTitle("看龙启动更新器");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Optional<ButtonType> showAlert(String content, String title, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        return alert.showAndWait();
    }
}
