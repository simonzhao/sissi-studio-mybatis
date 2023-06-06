package cn.messycode.scenicspot.beijingolympicpack.ui;

import cn.messycode.scenicspot.beijingolympicpack.dialog.ConnectDialog;
import cn.messycode.scenicspot.beijingolympicpack.util.ViewUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.IconButton;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.components.JBLabel;

import java.awt.*;

/**
 * @author simon.zhao 
 */
public class ConnectAction extends DumbAwareAction {

    private Project project;

    private TableTreeHandler handler;

    public ConnectAction() {
    }

    public ConnectAction(Project project, TableTreeHandler handler){
        super("connect", "connect database", ViewUtil.getIcon("/images/connect.png"));
        this.project = project;
        this.handler = handler;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project1 = e.getProject();

        ConnectDialog dialog = new ConnectDialog(project1, handler);
        dialog.setResizable(true);
        dialog.show();
    }
}

