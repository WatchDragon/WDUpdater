package org.watchdragon.controller;

import javafx.application.Platform;
import org.to2mbn.jmccc.auth.AuthenticationException;
import org.to2mbn.jmccc.auth.Authenticator;
import org.to2mbn.jmccc.auth.OfflineAuthenticator;
import org.to2mbn.jmccc.auth.yggdrasil.YggdrasilAuthenticator;
import org.to2mbn.jmccc.launch.LaunchException;
import org.to2mbn.jmccc.launch.Launcher;
import org.to2mbn.jmccc.launch.LauncherBuilder;
import org.to2mbn.jmccc.option.LaunchOption;
import org.to2mbn.jmccc.option.MinecraftDirectory;
import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.web.WebView;
import org.to2mbn.jmccc.util.ExtraArgumentsTemplates;
import org.watchdragon.Main;
import org.watchdragon.file.FileUtils;
import org.watchdragon.model.MCVerisonItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private Optional<MCVerisonItem[]> versions;


    @FXML//界面初始化方法，自动被JavaFX调用
    public boolean initialize () {
        memorySize.getItems().removeAll(memorySize.getItems());
        memorySize.getItems().addAll("512MB","768MB","1GB","1.25GB","1.5GB","2GB","2.5GB","3GB","3.5GB","4GB","6GB","8GB");
        memorySize.getSelectionModel().select(2);
        wdNews.getEngine().load("https://news.watchdragon.org/");
        try {
            String packages = FileUtils.readFileAsString("package.json");
            Gson gson=new Gson();
            versions = Optional.of(gson.fromJson(packages,MCVerisonItem[].class));
        } catch (IOException e) {
            Main.showAlert("无法加载整合包列表","完整性遭到破坏", Alert.AlertType.ERROR);
            startButton.setDisable(true);
            versions = Optional.empty();
            return false;
        }
        if(versions.isPresent()){
            MCVerisonItem[] mcVerisons = versions.get();
            mcVersion.getItems().clear();
            for(MCVerisonItem item:mcVerisons){
                mcVersion.getItems().add(item.getName());
            }
            mcVersion.getSelectionModel().select(0);
        }
        return true;
    }

    public void onLegalSwitch(ActionEvent actionEvent) {
        this.passWord.setDisable(!this.legalSwitch.isSelected());
    }

    public void startGame(ActionEvent actionEvent) {
        Optional<MCVerisonItem> vi = Optional.empty();
        if(versions.isPresent()){
            MCVerisonItem[] mcVerisons = versions.get();
            for(MCVerisonItem item:mcVerisons){
                if(item.getName().equals((String)mcVersion.getSelectionModel().getSelectedItem())){
                    vi = Optional.of(item);
                }
            }
        }
        if(vi.isPresent()){
            MCVerisonItem mcInstance = vi.get();
            MinecraftDirectory dir = new MinecraftDirectory("package/"+mcInstance.getPackageName());
            Launcher launcher = LauncherBuilder.buildDefault();
            Authenticator auther;
            if(this.legalSwitch.isSelected()){
                try {
                    auther = YggdrasilAuthenticator.password(playerName.getText(), passWord.getText());
                } catch (AuthenticationException e) {
                    Main.showAlert("无法登陆正版账号，请确定用户密码正确并保证网络链接通畅。","正版登录", Alert.AlertType.ERROR);
                    return;
                }
            }else{
                auther = new OfflineAuthenticator(playerName.getText());
            }
            try {
                LaunchOption lo = new LaunchOption(mcInstance.getForgeVersion(), auther, dir);
                lo.setMaxMemory(StringMem2int((String) memorySize.getSelectionModel().getSelectedItem()));
                List<String> exArgs=new ArrayList<>();
                exArgs.add(ExtraArgumentsTemplates.FML_IGNORE_INVALID_MINECRAFT_CERTIFICATES);
                exArgs.add(ExtraArgumentsTemplates.FML_IGNORE_PATCH_DISCREPANCISE);
                lo.setExtraMinecraftArguments(exArgs);
                launcher.launch(lo);
                Platform.exit();
                System.exit(0);
            } catch (LaunchException e) {
                Main.showAlert("无法启动游戏！","启动失败", Alert.AlertType.ERROR);
                return;
            } catch (IOException e) {
                Main.showAlert("未知I/O错误！请确定整合包存在","启动失败", Alert.AlertType.ERROR);
                return;
            }
        }else{
            Main.showAlert("完整性遭到破坏，无法启动游戏。","无法启动", Alert.AlertType.ERROR);
        }
    }

    private int StringMem2int(String mem){
        if(mem.endsWith("GB")){
            return (int)(1024 * Float.parseFloat(mem.replace("GB","")));
        }else{
            return Integer.parseInt(mem.replace("MB",""));
        }
    }
}
