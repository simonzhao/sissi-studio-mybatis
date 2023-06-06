package cn.messycode.scenicspot.beijingolympicpack.factory; 

import cn.messycode.scenicspot.beijingolympicpack.entity.DatabaseEntity;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author simon.zyn 
 */
public class SimpleDataSourceFactory {

    private static SimpleDataSourceFactory instance = new SimpleDataSourceFactory();

    public static SimpleDataSourceFactory getInstance() {
        return instance;
    }

    public DataSource getDataSource(DatabaseEntity database, String username, String password) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName(database.getHost());
        dataSource.setPort(database.getPort());
        dataSource.setUser(username);
        dataSource.setPassword(password);
        dataSource.setDatabaseName(database.getName());
        try {
            dataSource.setAllowPublicKeyRetrieval(true);
        } catch (SQLException e) {
        }
        dataSource.setUseInformationSchema(true);
        dataSource.setUseSSL(false);
        return dataSource;
    }
}

