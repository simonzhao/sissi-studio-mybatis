package cn.messycode.scenicspot.beijingolympicpack.ui;

import cn.messycode.scenicspot.beijingolympicpack.util.ViewUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;

/**
 * @author simon.zhao 
 */
public class ReloadAction extends DumbAwareAction {
    private TableTreeHandler handler;

    public ReloadAction() {
    }

    public ReloadAction(TableTreeHandler handler){
        super("reload", "reload database tables", ViewUtil.getIcon("/images/reload.png"));
        this.handler = handler;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        handler.initData();
    }
}

