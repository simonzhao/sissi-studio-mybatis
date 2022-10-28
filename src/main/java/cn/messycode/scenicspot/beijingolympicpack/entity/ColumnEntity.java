package cn.messycode.scenicspot.beijingolympicpack.entity;

import com.google.common.base.CaseFormat;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author simon.zhao 
 */
public class ColumnEntity {
    private final String name;

    private final String type;

    private final String remarks;

    private boolean isKey;

    private final Map<String, String> dict = new HashMap<>();
    private final Map<String, String> packageDict = new HashMap<>();

    public ColumnEntity(String name, String type, String remarks){
        this.name = name;
        this.type = type;
        this.remarks = remarks;
        this.isKey = false;

        dict.put("CHAR", "String");
        dict.put("VARCHAR", "String");
        dict.put("LONGVARCHAR", "String");
        dict.put("TEXT", "String");

        dict.put("BIGINT UNSIGNED", "Long");
        dict.put("BIGINT", "Long");

        dict.put("INT", "Integer");
        dict.put("INT UNSIGNED", "Integer");

        dict.put("DATETIME", "Date");

        dict.put("TINYINT", "Byte");
        dict.put("TINYINT UNSIGNED", "Byte");

        dict.put("SMALLINT", "Short");
        dict.put("SMALLINT UNSIGNED", "Short");

        dict.put("DECIMAL", "BigDecimal");

        packageDict.put("DATETIME", "java.util.Date");
        packageDict.put("DECIMAL", "java.math.BigDecimal");
    }

    public static ColumnEntity getInstance(String name, String type, String remarks){
        return new ColumnEntity(name, type, remarks);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getRemarks() {
        return remarks;
    }

    public boolean isKey() {
        return isKey;
    }

    public void setKey(boolean key) {
        isKey = key;
    }

    public String getPropertyName() {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, this.name);
    }

    public String getUpperPropertyName() {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, this.name);
    }

    public String getPropertyType() {
        return dict.getOrDefault(this.type, this.type);
    }

    public Optional<String> getPropertyTypePackage() {
        return Optional.ofNullable(packageDict.get(this.type));
    }
}
