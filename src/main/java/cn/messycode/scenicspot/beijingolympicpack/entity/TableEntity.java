package cn.messycode.scenicspot.beijingolympicpack.entity; 

import cn.messycode.scenicspot.beijingolympicpack.Application;
import cn.messycode.scenicspot.beijingolympicpack.util.ConfigUtil;
import cn.messycode.scenicspot.beijingolympicpack.util.VelocityUtil;
import cn.messycode.scenicspot.beijingolympicpack.constant.ConfigConstant;
import cn.messycode.scenicspot.beijingolympicpack.util.TemplateUtil;
import com.google.common.base.CaseFormat;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.ModuleRootManager;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author simon.zhao 
 */
public class TableEntity {
    private final String name;

    private final String comment;

    private final DatabaseEntity database;

    private List<ColumnEntity> columns;

    private final Application application = new Application();

    private final TemplateUtil templateUtil = new TemplateUtil();

    private Project project;

    private ConfigUtil configUtil;

    private TableEntity(String name, String comment, DatabaseEntity database){
        this.name = name;
        this.comment = comment;
        this.database = database;
        this.columns = new ArrayList<>();

        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        project = projects[0];
        configUtil = ConfigUtil.getInstance(project);
    }

    public static TableEntity getInstance(String name, String comment, DatabaseEntity database){
        return new TableEntity(name, comment, database);
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public DatabaseEntity getDatabase() {
        return database;
    }

    public List<ColumnEntity> getColumns() {
        return this.columns;
    }

    public void addColumn(ColumnEntity column){
        this.columns.add(column);
    }

    public void build(Project project){
        String username = configUtil.getOrDefault(ConfigConstant.USERNAME, "");
        String password = configUtil.getOrDefault(ConfigConstant.PASSWORD, "");

        application.loadTableColumns(this, username, password);

        Map<String, Object> context = new HashMap<>(2);
        context.put("table", this);
        context.put("columnPackageList", this.getColumns().stream()
                .map(ColumnEntity::getPropertyTypePackage)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .distinct()
                .collect(Collectors.toList()));

        String moduleName = configUtil.getOrDefault(ConfigConstant.SELECTED_MODULE, "");
        Optional<Module> optional = getCurrentModule(moduleName);

        if(optional.isPresent()) {
            Module module = optional.get();
            String moduleRootPath = ModuleRootManager.getInstance(module).getContentRoots()[0].getPath();

            makeXml(moduleRootPath, context);
            makeDomainObjectFile(moduleRootPath, context);
            makeInterfaceFile(moduleRootPath, context);
            makeParamFile(moduleRootPath, context);

            NotificationGroupManager.getInstance().getNotificationGroup("Custom Notification Group INFORMATION")
                    .createNotification("代码生成完成", NotificationType.INFORMATION)
                    .notify(null);
        } else {
            NotificationGroupManager.getInstance().getNotificationGroup("Custom Notification Group INFORMATION")
                    .createNotification("获取项目信息失败！", NotificationType.ERROR)
                    .notify(null);
        }
    }

    private Optional<Module> getCurrentModule(String moduleName){
        Module[] modules = ModuleManager.getInstance(project).getModules();
        for (Module module : modules) {
            if(module.getName().equalsIgnoreCase(moduleName)){
                return Optional.of(module);
            }
        }
        return Optional.empty();
    }

    private void makeXml(String rootPath, Map<String, Object> context){
        if(configUtil.get(ConfigConstant.XML_PATH).isPresent()) {
            String path = rootPath + "/src/main/resources";
            String route = configUtil.get(ConfigConstant.XML_PATH).get();
            String localPath = path + "/" + route;
            makeContentFile(localPath, this.getMapperFileName(), context, "/template/mapper.vm");
        }
    }

    private void makeDomainObjectFile(String rootPath, Map<String, Object> context) {
        if(configUtil.get(ConfigConstant.ENTITY_PACKAGE).isPresent()) {
            String path = rootPath + "/src/main/java";
            String route = getDomainObjectPackageName().replace(".", "/");
            String localPath = path + "/" + route;
            makeContentFile(localPath, this.getDomainObjectFileName(), context, "/template/do.vm");
        }
    }

    private void makeInterfaceFile(String rootPath, Map<String, Object> context) {
        if(configUtil.get(ConfigConstant.INTERFACE_PACKAGE).isPresent()) {
            String path = rootPath + "/src/main/java";
            String route = getInterfacePackageName().replace(".", "/");
            String localPath = path + "/" + route;
            makeContentFile(localPath, this.getInterfaceFileName(), context, "/template/dao.vm");
        }
    }
    private void makeParamFile(String rootPath, Map<String, Object> context) {
        if(configUtil.get(ConfigConstant.PARAM_PACKAGE).isPresent()) {
            String path = rootPath + "/src/main/java";
            String route = getParamClassPackageName().replace(".", "/");
            String localPath = path + "/" + route;
            makeContentFile(localPath, this.getParamFileName(), context, "/template/param.vm");
        }
    }

    private void makeContentFile(String path, String fileName, Map<String, Object> context, String templateName) {
        String finalFileName = path + "/" + fileName;

        try {
            File file = new File(path);
            if (!file.exists()) {
                if(!file.mkdirs()){
                    NotificationGroupManager.getInstance().getNotificationGroup("Custom Notification Group WARNING")
                            .createNotification("创建目录失败", path, NotificationType.WARNING)
                            .notify(null);
                    return;
                }
            }
            File mapper = new File(finalFileName);

            String templateContent = templateUtil.getTemplateContent(templateName);

            String fileContent = VelocityUtil.evaluate(templateContent, context);
            FileWriter writer = new FileWriter(mapper);
            writer.write(fileContent);
            writer.close();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getInterfaceName() {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, this.name) + "DAO";
    }

    public String getFullInterfaceName() {
        return getInterfacePackageName() + "." + getInterfaceName();
    }

    public String getInterfacePackageName() {
        return configUtil.get(ConfigConstant.INTERFACE_PACKAGE).orElse("");
    }

    public String getInterfaceFileName() {
        return getInterfaceName() + ".java";
    }

    public String getDomainObjectName() {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, this.name) + "DO";
    }

    public String getDomainObjectPackageName() {
        return configUtil.get(ConfigConstant.ENTITY_PACKAGE).orElse("");
    }

    public String getDomainObjectFileName() {
        return getDomainObjectName() + ".java";
    }

    public String getParamName() {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, this.name) + "Param";
    }

    public String getParamClassName() {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, this.name) + "Param";
    }

    public String getParamFileName() {
        return getParamClassName() + ".java";
    }

    public String getFullParamName() {
        return getParamClassPackageName() + "." + getParamClassName();
    }

    public String getParamClassPackageName() {
        return configUtil.getOrDefault(ConfigConstant.PARAM_PACKAGE, "");
    }

    public String getFullDomainObjectName() {
        return getDomainObjectPackageName() + "." + getDomainObjectName();
    }

    public String getMapperName() {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, this.name) + "DAO";
    }

    public String getMapperFileName() {
        return getMapperName() + ".xml";
    }

    public ColumnEntity getPrimaryKey(){
        return this.columns.stream().filter(ColumnEntity::isKey).findFirst().orElseThrow();
    }
}

