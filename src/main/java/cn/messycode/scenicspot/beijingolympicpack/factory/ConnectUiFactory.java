package cn.messycode.scenicspot.beijingolympicpack.factory;

import cn.messycode.scenicspot.beijingolympicpack.ui.DbTableToolWindowPanel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

/**
 * @author simon.zhao 
 */
public class ConnectUiFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        DbTableToolWindowPanel window = new DbTableToolWindowPanel(project);

        ContentFactory contentFactory = toolWindow.getContentManager().getFactory();

        Content content = contentFactory.createContent(window.getComponent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }
}

