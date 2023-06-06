package cn.messycode.scenicspot.beijingolympicpack.util;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * @author simon.zhao 
 */
public class ViewUtil {
    public static Icon getIcon(String path) {
        return IconLoader.getIcon(path, ViewUtil.class);
    }
}

