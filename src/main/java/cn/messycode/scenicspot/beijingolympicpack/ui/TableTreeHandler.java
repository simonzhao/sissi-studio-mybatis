package cn.messycode.scenicspot.beijingolympicpack.ui;

import cn.messycode.scenicspot.beijingolympicpack.Application;
import cn.messycode.scenicspot.beijingolympicpack.constant.ConfigConstant;
import cn.messycode.scenicspot.beijingolympicpack.entity.DatabaseEntity;
import cn.messycode.scenicspot.beijingolympicpack.entity.TableEntity;
import cn.messycode.scenicspot.beijingolympicpack.util.ConfigUtil;
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

    private JTree tree;

    private Project project;

    private ConfigUtil configUtil;

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
        this.project = project;
        this.configUtil = ConfigUtil.getInstance(project);
    }

    private synchronized void loadTableNodes() {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
        root.removeAllChildren();

        String databaseName = configUtil.getOrDefault(ConfigConstant.DATABASE, "mysql");
        String host = configUtil.getOrDefault(ConfigConstant.HOST, "localhost");
        String port = configUtil.getOrDefault(ConfigConstant.PORT, "3306");
        String username = configUtil.getOrDefault(ConfigConstant.USERNAME, "");
        String password = configUtil.getOrDefault(ConfigConstant.PASSWORD, "");

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

