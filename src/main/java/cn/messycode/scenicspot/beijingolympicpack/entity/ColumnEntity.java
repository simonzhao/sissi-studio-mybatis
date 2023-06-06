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
    private final Map<String, String> mysqlTypeToJdbcType = new HashMap<>();
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
        dict.put("JSON", "String");
        dict.put("LONGTEXT", "String");
        dict.put("MEDIUMTEXT", "String");
        dict.put("TINYTEXT", "String");
        dict.put("ENUM", "String");
        dict.put("SET", "String");

        dict.put("BIGINT UNSIGNED", "Long");
        dict.put("BIGINT", "Long");

        dict.put("INT", "Integer");
        dict.put("INT UNSIGNED", "Integer");


        dict.put("TINYINT", "Byte");
        dict.put("TINYINT UNSIGNED", "Byte");

        dict.put("SMALLINT", "Short");
        dict.put("SMALLINT UNSIGNED", "Short");

        dict.put("DECIMAL", "BigDecimal");
        dict.put("BLOB", "byte[]");
        dict.put("BINARY", "byte[]");
        dict.put("LONGBLOB", "byte[]");
        dict.put("MEDIUMBLOB", "byte[]");
        dict.put("TINYBLOB", "byte[]");
        dict.put("VARBINARY", "byte[]");
        dict.put("BIT", "byte[]");

        dict.put("DATE", "LocalDate");
        dict.put("TIME", "LocalTime");
        dict.put("DATETIME", "LocalDateTime");
        dict.put("TIMESTAMP", "LocalDateTime");

        dict.put("GEOMETRY", "Object");
        dict.put("GEOMCOLLECTION", "Object");
        dict.put("LINESTRING", "Object");
        dict.put("MULTILINESTRING", "Object");
        dict.put("MULTIPOINT", "Object");
        dict.put("MULTIPOLYGON", "Object");
        dict.put("POINT", "Object");
        dict.put("POLYGON", "Object");

        dict.put("DOUBLE", "Double");
        dict.put("FLOAT", "Float");

        packageDict.put("DATETIME", "java.time.LocalDateTime");
        packageDict.put("TIMESTAMP", "java.time.LocalDateTime");
        packageDict.put("DATE", "java.time.LocalDate");
        packageDict.put("TIME", "java.time.LocalTime");
        packageDict.put("DECIMAL", "java.math.BigDecimal");

        mysqlTypeToJdbcType.put("DATETIME", "DATE");
        mysqlTypeToJdbcType.put("BIGINT UNSIGNED", "BIGINT");
        mysqlTypeToJdbcType.put("INT", "INTEGER");
        mysqlTypeToJdbcType.put("INT UNSIGNED", "INTEGER");
        mysqlTypeToJdbcType.put("TINYINT UNSIGNED", "TINYINT");
        mysqlTypeToJdbcType.put("SMALLINT UNSIGNED", "SMALLINT");

        mysqlTypeToJdbcType.put("LONGBLOB", "LONGVARBINARY");
        mysqlTypeToJdbcType.put("MEDIUMBLOB", "LONGVARBINARY");
        mysqlTypeToJdbcType.put("TINYBLOB", "BINARY");

        mysqlTypeToJdbcType.put("GEOMETRY", "BINARY");

        mysqlTypeToJdbcType.put("GEOMCOLLECTION", "OTHER");
        mysqlTypeToJdbcType.put("LINESTRING", "OTHER");
        mysqlTypeToJdbcType.put("MULTILINESTRING", "OTHER");
        mysqlTypeToJdbcType.put("MULTIPOINT", "OTHER");
        mysqlTypeToJdbcType.put("MULTIPOLYGON", "OTHER");
        mysqlTypeToJdbcType.put("POINT", "OTHER");
        mysqlTypeToJdbcType.put("POLYGON", "OTHER");

        mysqlTypeToJdbcType.put("JSON", "CHAR");
        mysqlTypeToJdbcType.put("ENUM", "CHAR");
        mysqlTypeToJdbcType.put("SET", "CHAR");

        mysqlTypeToJdbcType.put("LONGTEXT", "LONGVARCHAR");
        mysqlTypeToJdbcType.put("MEDIUMTEXT", "LONGVARCHAR");
        mysqlTypeToJdbcType.put("TEXT", "LONGVARCHAR");
        mysqlTypeToJdbcType.put("TINYTEXT", "VARCHAR");
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

    public String getJdbcType() {
        return mysqlTypeToJdbcType.getOrDefault(this.type, this.type);
    }
}
