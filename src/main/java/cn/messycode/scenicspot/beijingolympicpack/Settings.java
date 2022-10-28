package cn.messycode.scenicspot.beijingolympicpack;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author simon.zhao 
 */
@State(name = "MainSetting", storages = @Storage("main-setting.xml"))
public class Settings implements PersistentStateComponent<Settings> {
    private Map<String, String> settingMap;

    public Settings(){
        init();
    }

    private void init(){
        settingMap = new HashMap<>(10);
    }

    @Override
    public @Nullable Settings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull Settings mainSetting) {
        setSettingMap(mainSetting.getSettingMap());
    }

    public Map<String, String> getSettingMap() {
        return settingMap;
    }

    public void setSettingMap(Map<String, String> settingMap) {
        this.settingMap = settingMap;
    }

    public static Settings getInstance() {
        return ApplicationManager.getApplication().getService(Settings.class);
    }
}
