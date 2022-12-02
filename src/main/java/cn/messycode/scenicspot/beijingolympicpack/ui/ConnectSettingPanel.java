package cn.messycode.scenicspot.beijingolympicpack.ui;

import cn.messycode.scenicspot.beijingolympicpack.constant.ConfigConstant;
import cn.messycode.scenicspot.beijingolympicpack.util.ConfigUtil;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


/**
 * @author zhaoyingnan@messycode.cn
 */
public class ConnectSettingPanel implements Configurable {
    private JTextField host;
    private JTextField port;
    private JTextField username;
    private JPasswordField password;
    private JTextField database;
    private JPanel mainPanel;

    private ConfigUtil configUtil;

    public ConnectSettingPanel(){
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        if (projects.length == 0) {
            return;
        }
        Project project = projects[0];

        configUtil = ConfigUtil.getInstance(project);
    }

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "数据库连接";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return mainPanel;
    }

    @Override
    public boolean isModified() {
        String originSetting = configUtil.get(ConfigConstant.HOST).orElse("");
        String newSetting = host.getText();
        if(!originSetting.equals(newSetting)){
            return true;
        }

        originSetting = configUtil.get(ConfigConstant.PORT).orElse("");
        newSetting = port.getText();
        if(!originSetting.equals(newSetting)){
            return true;
        }

        originSetting = configUtil.get(ConfigConstant.USERNAME).orElse("");
        newSetting = username.getText();
        if(!originSetting.equals(newSetting)){
            return true;
        }

        originSetting = configUtil.get(ConfigConstant.PASSWORD).orElse("");
        newSetting = String.valueOf(password.getPassword());
        if(!originSetting.equals(newSetting)){
            return true;
        }

        originSetting = configUtil.get(ConfigConstant.DATABASE).orElse("");
        newSetting = database.getText();

        return !originSetting.equals((newSetting));
    }

    @Override
    public void apply() throws ConfigurationException {
        configUtil.set(ConfigConstant.HOST, host.getText());
        configUtil.set(ConfigConstant.PORT, port.getText());
        configUtil.set(ConfigConstant.USERNAME, username.getText());
        configUtil.set(ConfigConstant.PASSWORD, String.valueOf(password.getPassword()));
        configUtil.set(ConfigConstant.DATABASE, database.getText());
    }

    public void reset(){
        init();
    }

    private void init() {
        host.setText(configUtil.get("host").orElse(""));
        port.setText(configUtil.get("port").orElse(""));
        username.setText(configUtil.get("username").orElse(""));
        password.setText(configUtil.get("password").orElse(""));
        database.setText(configUtil.get("database").orElse(""));
    }
}
