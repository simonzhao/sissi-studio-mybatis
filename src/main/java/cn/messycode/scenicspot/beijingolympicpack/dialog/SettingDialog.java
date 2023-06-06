package cn.messycode.scenicspot.beijingolympicpack.dialog;

import cn.messycode.scenicspot.beijingolympicpack.ui.ConnectSettingPanel;
import cn.messycode.scenicspot.beijingolympicpack.ui.PathSettingPanel;
import cn.messycode.scenicspot.beijingolympicpack.ui.TableTreeHandler;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author zhaoyingnan@messycode.cn
 */
public class SettingDialog extends DialogWrapper {

    private Project project;

    private PathSettingPanel panel;

    public SettingDialog(@Nullable Project project) {
        super(project);
        this.project = project;
        setTitle("设置");
        setSize(600, 300);
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        panel = new PathSettingPanel(project);
        return panel.createComponent();
    }

    @Override
    protected void doOKAction() {
        panel.apply();
        super.doOKAction();
    }
}
