package cn.messycode.scenicspot.beijingolympicpack.ui; 

import cn.messycode.scenicspot.beijingolympicpack.constant.ConfigConstant;
import cn.messycode.scenicspot.beijingolympicpack.util.ConfigUtil;
import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.NlsContexts;
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

    private ConfigUtil configUtil;

    public PathSettingPanel(Project project) {

        configUtil = ConfigUtil.getInstance(project);

        Module[] modules = ModuleManager.getInstance(project).getModules();
        for (Module module : modules) {
            String moduleName = module.getName();
            selectedModule.addItem(moduleName);
            moduleMap.put(moduleName, module);
        }

        final String[] current = {getSelectedItemName(selectedModule, "")};
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

        init();
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
        String origSetting = configUtil.get(ConfigConstant.SELECTED_MODULE).orElse( "");
        String newSetting = getSelectedItemName(selectedModule, "");
        if(!origSetting.equals(newSetting)){
            return true;
        }

        origSetting = configUtil.get(ConfigConstant.INTERFACE_PACKAGE).orElse("");
        newSetting = interfacePackage.getText();
        if(!origSetting.equals(newSetting)){
            return true;
        }

        origSetting = configUtil.get(ConfigConstant.ENTITY_PACKAGE).orElse("");
        newSetting = entityPackage.getText();
        if(!origSetting.equals(newSetting)){
            return true;
        }

        origSetting = configUtil.get(ConfigConstant.PARAM_PACKAGE).orElse("");
        newSetting = paramPackage.getText();
        if(!origSetting.equals(newSetting)){
            return true;
        }

        origSetting = configUtil.get(ConfigConstant.XML_PATH).orElse("");
        newSetting = xmlPath.getText();
        return !origSetting.equals(newSetting);
    }

    @Override
    public void apply() {
        configUtil.set(ConfigConstant.SELECTED_MODULE, getSelectedItemName(selectedModule, ""));
        configUtil.set(ConfigConstant.INTERFACE_PACKAGE, interfacePackage.getText());
        configUtil.set(ConfigConstant.ENTITY_PACKAGE, entityPackage.getText());
        configUtil.set(ConfigConstant.PARAM_PACKAGE, paramPackage.getText());
        configUtil.set(ConfigConstant.XML_PATH, xmlPath.getText());

    }

    @Override
    public Configurable[] getConfigurables() {
        return new Configurable[0];
    }

    public void init(){
        selectedModule.setSelectedItem(configUtil.get(ConfigConstant.SELECTED_MODULE).orElse(""));
        interfacePackage.setText(configUtil.get(ConfigConstant.INTERFACE_PACKAGE).orElse(""));
        entityPackage.setText(configUtil.get(ConfigConstant.ENTITY_PACKAGE).orElse(""));
        paramPackage.setText(configUtil.get(ConfigConstant.PARAM_PACKAGE).orElse(""));
        xmlPath.setText(configUtil.get(ConfigConstant.XML_PATH).orElse(""));
    }

    @Override
    public void reset() {
        init();
    }

    private String getSelectedItemName(JComboBox<String> selectedModule, String defaultVal){
        return selectedModule.getSelectedItem() != null ? selectedModule.getSelectedItem().toString() : defaultVal;
    }
}

