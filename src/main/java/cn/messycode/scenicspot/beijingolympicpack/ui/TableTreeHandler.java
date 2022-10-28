package cn.messycode.scenicspot.beijingolympicpack.ui;

import cn.messycode.scenicspot.beijingolympicpack.Application;
import cn.messycode.scenicspot.beijingolympicpack.entity.DatabaseEntity;
import cn.messycode.scenicspot.beijingolympicpack.entity.TableEntity;
import cn.messycode.scenicspot.beijingolympicpack.Settings;
import cn.messycode.scenicspot.beijingolympicpack.util.ViewUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.ui.ColoredTreeCellRenderer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

/**
 * @author simon.zhao 
 */
public class TableTreeHandler {
    private Application application = new Application();
    private Settings settings = Settings.getInstance();

    private JTree tree;

    private static class CustomTreeCellRenderer extends ColoredTreeCellRenderer {
        private Icon tableIcon = ViewUtil.getIcon("/images/table.png");
        private Icon databaseIcon = ViewUtil.getIcon("/images/database.png");

        @Override
        public void customizeCellRenderer(@NotNull JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            if (value instanceof DefaultMutableTreeNode) {
                Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
                if (userObject instanceof TableEntity) {
                    TableEntity item = (TableEntity) userObject;
                    setIcon(tableIcon);
                    setToolTipText(item.getComment());
                    append(item.getName());
                }
                if (userObject instanceof DatabaseEntity) {
                    DatabaseEntity item = (DatabaseEntity) userObject;
                    setIcon(databaseIcon);
                    append(item.toString());
                }
            }
        }
    }

    private TableTreeHandler(JTree tree, Project project){
        this.tree = tree;
    }

    private synchronized void loadTableNodes() {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
        root.removeAllChildren();

        String databaseName = settings.getSettingMap().getOrDefault("database", "mysql");
        String host = settings.getSettingMap().getOrDefault("host", "localhost");
        String port = settings.getSettingMap().getOrDefault("port", "3306");
        String username = settings.getSettingMap().getOrDefault("username", "");
        String password = settings.getSettingMap().getOrDefault("password", "");

        DatabaseEntity database = DatabaseEntity.getInstance(databaseName, host, port);

        DefaultMutableTreeNode node = new DefaultMutableTreeNode(database, true);
        for(TableEntity table : application.getTableList(database, username, password)) {
            node.add(new DefaultMutableTreeNode(table, false));
        }

        root.add(node);
        tree.updateUI();

        TreePath path = new TreePath(node.getPath());
        tree.expandPath(path);
    }

    public void initData() {
        ApplicationManager.getApplication().invokeLater(this::loadTableNodes);
    }

    public TreeCellRenderer getTreeCellRenderer(){
        return new CustomTreeCellRenderer();
    }
    public static TableTreeHandler getInstance(JTree tree, Project project) {
        return new TableTreeHandler(tree, project);
    }
}

