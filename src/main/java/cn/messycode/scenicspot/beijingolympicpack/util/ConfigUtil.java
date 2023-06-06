package cn.messycode.scenicspot.beijingolympicpack.util;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;

import java.util.Optional;

/**
 * @author zhaoyingnan@messycode.cn
 */
public class ConfigUtil {
    private PropertiesComponent propertiesComponent;

    ConfigUtil(PropertiesComponent propertiesComponent){
        this.propertiesComponent = propertiesComponent;
    }

    public Optional<String> get(String key){
        return Optional.ofNullable(propertiesComponent.getValue(key));
    }

    public String getOrDefault(String key, String val){
        return get(key).orElse(val);
    }

    public void set(String key, String val){
        propertiesComponent.setValue(key, val);
    }

    public static ConfigUtil getInstance(Project project){
        return new ConfigUtil(PropertiesComponent.getInstance(project));
    }
}
