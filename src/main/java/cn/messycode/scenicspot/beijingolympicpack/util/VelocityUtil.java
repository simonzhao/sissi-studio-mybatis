package cn.messycode.scenicspot.beijingolympicpack.util;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.NullLogChute;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

/**
 * @author simon.zhao 
 */
public class VelocityUtil {
    private final static VelocityEngine VELOCITY_ENGINE;

    static {
        Properties properties = new Properties();
        properties.setProperty("resource.loader", "class");
        properties.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        properties.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        properties.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, NullLogChute.class.getName());

        VELOCITY_ENGINE = new VelocityEngine();
        VELOCITY_ENGINE.init(properties);
    }

    public static String evaluate(String template, Map<String, Object> map) {
        VelocityContext context = new VelocityContext();
        map.forEach(context::put);

        context.put("tab", "\t");
        context.put("newLine", "\n");
        StringWriter writer = new StringWriter();
        VELOCITY_ENGINE.evaluate(context, writer, "", template);
        return writer.toString();
    }

    public static  String merge(String templateFile, Map<String, Object> map){
        VelocityContext context = new VelocityContext();
        map.forEach(context::put);
        StringWriter writer = new StringWriter();

        Template template = VELOCITY_ENGINE.getTemplate(templateFile);
        template.merge(context, writer);

        return writer.toString();
    }
}

