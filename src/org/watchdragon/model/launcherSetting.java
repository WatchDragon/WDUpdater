package org.watchdragon.model;

/**
 * Created by zjyl1 on 2017/6/25.
 */
public class LauncherSetting {
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getMemSelect() {
        return memSelect;
    }

    public void setMemSelect(int memSelect) {
        this.memSelect = memSelect;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLegal() {
        return isLegal;
    }

    public void setLegal(boolean legal) {
        isLegal = legal;
    }

    private int memSelect;
    private String username;
    private String password;
    private boolean isLegal;
}
