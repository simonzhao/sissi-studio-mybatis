package cn.messycode.scenicspot.beijingolympicpack.entity;

/**
 * @author simon.zhao 
 */
public class DatabaseEntity {
    private final String name;

    private final String host;

    private final Integer port;

    private DatabaseEntity(String name, String host, String port){
        this.name = name;
        this.host = host;
        this.port = Integer.parseInt(port);
    }

    public static DatabaseEntity getInstance(String name, String host, String port){
        return new DatabaseEntity(name, host, port);
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return this.port;
    }

    public String toString() {
        return String.format("%s@%s:%d", this.name, this.host, this.port);
    }
}

