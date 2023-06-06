package cn.messycode.scenicspot.beijingolympicpack.dialog;

import cn.messycode.scenicspot.beijingolympicpack.ui.ConnectSettingPanel;
import cn.messycode.scenicspot.beijingolympicpack.ui.TableTreeHandler;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * @author zhaoyingnan@messycode.cn
 */
public class ConnectDialog extends DialogWrapper {

    private Project project;

    private ConnectSettingPanel panel;

    private TableTreeHandler handler;

    public ConnectDialog(@Nullable Project project, TableTreeHandler handler) {
        super(project);
        this.project = project;
        this.handler = handler;
        setTitle("设置数据库连接");

        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        panel = new ConnectSettingPanel(project);
        return panel.createComponent();
    }

    @Override
    protected void doOKAction() {
        panel.apply();
        handler.initData();
        super.doOKAction();
    }
}
