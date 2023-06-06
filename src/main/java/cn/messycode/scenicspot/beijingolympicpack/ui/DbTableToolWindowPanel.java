package cn.messycode.scenicspot.beijingolympicpack.ui; 

import cn.messycode.scenicspot.beijingolympicpack.entity.TableEntity;
import cn.messycode.scenicspot.beijingolympicpack.util.ViewUtil;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.treeStructure.SimpleTree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author simon.zhao
 */
public class DbTableToolWindowPanel extends SimpleToolWindowPanel {

    private static final long serialVersionUID = -1678358382880024811L;

    private SimpleTree tree = new SimpleTree();

    private Project project;

    public DbTableToolWindowPanel(Project project) {
        super(true, true);
        this.project = project;
        TableTreeHandler handler = TableTreeHandler.getInstance(tree, project);
        init(handler);
        handler.initData();
    }

    private void init(TableTreeHandler handler) {
        DefaultActionGroup actionGroup = new DefaultActionGroup(
                new ConnectAction(project, handler),
                new ReloadAction(handler),
                new SettingAction(project));

        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar("action", actionGroup, true);
        setToolbar((JComponent) actionToolbar);

        tree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("root"), true));
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);

        tree.setCellRenderer(handler.getTreeCellRenderer());

        JPopupMenu menu = new JPopupMenu();
        JMenuItem item = new JMenuItem("构建", ViewUtil.getIcon("/images/build.png"));
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                if(node != null){
                    Object obj = node.getUserObject();
                    if(obj instanceof TableEntity){
                        TableEntity table = (TableEntity) obj;
                        table.build(project);
                    }
                }
            }
        });
        menu.add(item);

        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int x = e.getX();
                int y = e.getY();

                if(MouseEvent.BUTTON3 == e.getButton()){
                    TreePath path = tree.getPathForLocation(x, y);
                    tree.setSelectionPath(path);
                    menu.show(tree, x, y);
                }
            }
        });

        JScrollPane scrollpane = ScrollPaneFactory.createScrollPane(tree, true);
        setContent(scrollpane);
    }
}

