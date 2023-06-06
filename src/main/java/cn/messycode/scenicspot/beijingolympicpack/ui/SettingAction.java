package cn.messycode.scenicspot.beijingolympicpack.ui;

import cn.messycode.scenicspot.beijingolympicpack.dialog.SettingDialog;
import cn.messycode.scenicspot.beijingolympicpack.util.ViewUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;

/**
 * @author simon.zhao 
 */
public class SettingAction extends DumbAwareAction {
    private Project project;

    public SettingAction() {
    }

    public SettingAction(Project project){
        super("setting", "setting", ViewUtil.getIcon("/images/setting.png"));
        this.project = project;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        SettingDialog dialog = new SettingDialog(project);
        dialog.setResizable(true);
        dialog.show();
    }
}

