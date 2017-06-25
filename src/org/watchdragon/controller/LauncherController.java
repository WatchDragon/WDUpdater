package org.watchdragon.controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
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
import org.watchdragon.model.LauncherSetting;
import org.watchdragon.model.MCVerisonItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

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
        memorySize.getItems().addAll("500MB","750MB","1000MB","1250MB","1500MB","2000MB","2500MB","3000MB","3500MB","4000MB","6000MB","8000MB");
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
        Optional<LauncherSetting> launcherSetting = loadSetting();
        if(launcherSetting.isPresent()){
            LauncherSetting setting = launcherSetting.get();
            playerName.setText(setting.getUsername());
            passWord.setText(setting.getPassword());
            legalSwitch.setSelected(setting.isLegal());
            memorySize.getSelectionModel().select(setting.getMemSelect());
            if(mcVersion.getItems().contains(setting.getVersion())){
                mcVersion.getSelectionModel().select(setting.getVersion());
            }else{
                mcVersion.getSelectionModel().select(0);
            }
        }
        return true;
    }

    public void onLegalSwitch(ActionEvent actionEvent) {
        this.passWord.setDisable(!this.legalSwitch.isSelected());
    }

    public void startGame(ActionEvent actionEvent) {
        String username = playerName.getText();
        if(!Pattern.compile("[a-zA-Z0-9_]{4,16}").matcher(username).matches()){
            Main.showAlert("用户名只允许英文字母数字下划线，大小写不限，4-16个字符。","请不要搞事情", Alert.AlertType.WARNING);
            return;
        };
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
                    auther = YggdrasilAuthenticator.password(username, passWord.getText());
                } catch (AuthenticationException e) {
                    Main.showAlert("无法登陆正版账号，请确定用户密码正确并保证网络链接通畅。","正版登录", Alert.AlertType.ERROR);
                    return;
                }
            }else{
                auther = new OfflineAuthenticator(username);
            }
            try {
                LaunchOption lo = new LaunchOption(mcInstance.getForgeVersion(), auther, dir);
                lo.setMaxMemory(StringMem2int((String) memorySize.getSelectionModel().getSelectedItem()));
                List<String> exArgs=new ArrayList<>();
                exArgs.add(ExtraArgumentsTemplates.FML_IGNORE_INVALID_MINECRAFT_CERTIFICATES);
                exArgs.add(ExtraArgumentsTemplates.FML_IGNORE_PATCH_DISCREPANCISE);
                lo.setExtraMinecraftArguments(exArgs);
                launcher.launch(lo);
                saveSetting();
                Platform.exit();
                System.exit(0);
            } catch (LaunchException e) {
                Main.showAlert("无法启动游戏！","启动失败", Alert.AlertType.ERROR);
                return;
            } catch (IOException e) {
                Main.showAlert("未知I/O错误！请确定磁盘可读写","启动失败", Alert.AlertType.ERROR);
                return;
            }
        }else{
            Main.showAlert("完整性遭到破坏，无法启动游戏。","无法启动", Alert.AlertType.ERROR);
        }
    }

    private int StringMem2int(String mem){
        return Integer.parseInt(mem.replace("MB",""));
    }

    private void saveSetting() throws IOException {
        LauncherSetting ls = new LauncherSetting();
        ls.setLegal(legalSwitch.isSelected());
        ls.setMemSelect(memorySize.getSelectionModel().getSelectedIndex());
        ls.setPassword(passWord.getText());
        ls.setUsername(playerName.getText());
        ls.setVersion((String)mcVersion.getSelectionModel().getSelectedItem());
        Gson gson = new Gson();
        String cfg = gson.toJson(ls);
        System.out.print(cfg);
        FileUtils.writeFileFromString(cfg,"config.json",false);
    }
    private Optional<LauncherSetting> loadSetting(){
        try {
            String cfg = FileUtils.readFileAsString("config.json");
            Gson gson = new Gson();
            return Optional.of(gson.fromJson(cfg, LauncherSetting.class));
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
