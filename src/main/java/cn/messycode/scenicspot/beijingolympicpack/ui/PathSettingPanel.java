package cn.messycode.scenicspot.beijingolympicpack.ui; 

import cn.messycode.scenicspot.beijingolympicpack.constant.ConfigConstant;
import cn.messycode.scenicspot.beijingolympicpack.Settings;
import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author simon.zhao 
 */
public class PathSettingPanel implements Configurable, Configurable.Composite {

    private Settings settings = Settings.getInstance();

    private JComboBox<String> selectedModule;
    private JPanel mainPanel;
    private JTextField interfacePackage;
    private JButton btnInterface;
    private JTextField entityPackage;
    private JTextField paramPackage;
    private JTextField xmlPath;
    private JButton btnEntity;
    private JButton btnParam;
    private JButton btnXml;

    private Map<String, Module> moduleMap = new HashMap<>();

    public PathSettingPanel() {
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        if (projects.length == 0) {
            return;
        }
        Project project = projects[0];

        Module[] modules = ModuleManager.getInstance(project).getModules();
        for (Module module : modules) {
            String moduleName = module.getName();
            selectedModule.addItem(moduleName);
            moduleMap.put(moduleName, module);
        }

        final String[] current = {selectedModule.getSelectedItem().toString()};
        selectedModule.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED && !selectedModule.getSelectedItem().toString().equalsIgnoreCase(current[0])){
                    current[0] = selectedModule.getSelectedItem().toString();

                    interfacePackage.setText("");
                    entityPackage.setText("");
                    paramPackage.setText("");
                    xmlPath.setText("");
                }
            }
        });


        btnInterface.addActionListener((e)->{
            String path = selectedModule.getSelectedItem().toString();
            Module module = moduleMap.get(path);
            PackageChooserDialog dialog = new PackageChooserDialog("选择接口包", module);
            dialog.show();
            interfacePackage.setText(dialog.getSelectedPackage().getQualifiedName());
        });

        btnEntity.addActionListener((e)->{
            String path = selectedModule.getSelectedItem().toString();
            Module module = moduleMap.get(path);
            PackageChooserDialog dialog = new PackageChooserDialog("选择实体包", module);
            dialog.show();
            entityPackage.setText(dialog.getSelectedPackage().getQualifiedName());
        });

        btnParam.addActionListener((e)->{
            String path = selectedModule.getSelectedItem().toString();
            Module module = moduleMap.get(path);
            PackageChooserDialog dialog = new PackageChooserDialog("选择参数包", module);
            dialog.show();
            paramPackage.setText(dialog.getSelectedPackage().getQualifiedName());
        });

        btnXml.addActionListener((e)->{
            String path = selectedModule.getSelectedItem().toString();
            Module module = moduleMap.get(path);

            String moduleRootPath = ModuleRootManager.getInstance(module).getContentRoots()[0].getPath();
            String resPath = moduleRootPath + "/src/main/resources/";

            JFileChooser fileChooser = new JFileChooser(resPath);
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.showOpenDialog(mainPanel);
            File file = fileChooser.getSelectedFile();
            xmlPath.setText(file.getPath().replace(resPath, ""));
        });
    }

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "Sissi MyBatis Generator";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return mainPanel;
    }

    @Override
    public boolean isModified() {
        String origSetting = settings.getSettingMap().getOrDefault(ConfigConstant.SELECTED_MODULE, "");
        String newSetting = selectedModule.getSelectedItem() != null ? selectedModule.getSelectedItem().toString() : "";
        if(!origSetting.equals(newSetting)){
            return true;
        }

        origSetting = settings.getSettingMap().getOrDefault(ConfigConstant.INTERFACE_PACKAGE, "");
        newSetting = interfacePackage.getText();
        if(!origSetting.equals(newSetting)){
            return true;
        }

        origSetting = settings.getSettingMap().getOrDefault(ConfigConstant.ENTITY_PACKAGE, "");
        newSetting = entityPackage.getText();
        if(!origSetting.equals(newSetting)){
            return true;
        }

        origSetting = settings.getSettingMap().getOrDefault(ConfigConstant.PARAM_PACKAGE, "");
        newSetting = paramPackage.getText();
        if(!origSetting.equals(newSetting)){
            return true;
        }

        origSetting = settings.getSettingMap().getOrDefault(ConfigConstant.XML_PATH, "");
        newSetting = xmlPath.getText();
        return !origSetting.equals(newSetting);
    }

    @Override
    public void apply() throws ConfigurationException {
        settings.getSettingMap().put(ConfigConstant.SELECTED_MODULE, selectedModule.getSelectedItem().toString());
        settings.getSettingMap().put(ConfigConstant.INTERFACE_PACKAGE, interfacePackage.getText());
        settings.getSettingMap().put(ConfigConstant.ENTITY_PACKAGE, entityPackage.getText());
        settings.getSettingMap().put(ConfigConstant.PARAM_PACKAGE, paramPackage.getText());
        settings.getSettingMap().put(ConfigConstant.XML_PATH, xmlPath.getText());

    }

    @Override
    public Configurable @NotNull [] getConfigurables() {
        return new Configurable[0];
    }

    private void init(){
        this.settings = Settings.getInstance();

        selectedModule.setSelectedItem(settings.getSettingMap().get(ConfigConstant.SELECTED_MODULE));
        interfacePackage.setText(settings.getSettingMap().get(ConfigConstant.INTERFACE_PACKAGE));
        entityPackage.setText(settings.getSettingMap().get(ConfigConstant.ENTITY_PACKAGE));
        paramPackage.setText(settings.getSettingMap().get(ConfigConstant.PARAM_PACKAGE));
        xmlPath.setText(settings.getSettingMap().get(ConfigConstant.XML_PATH));
    }

    @Override
    public void reset() {
        init();
    }
}

