package cn.messycode.scenicspot.beijingolympicpack.util;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import java.net.URL;

/**
 * @author simon.zhao 
 */
public class TemplateUtil {
    public String getTemplateContent(String path){
        try {
            URL url = this.getClass().getResource(path);
            return Resources.toString(url, Charsets.UTF_8);
        } catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
}

