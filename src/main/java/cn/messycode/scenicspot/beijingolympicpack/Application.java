package cn.messycode.scenicspot.beijingolympicpack;

import cn.messycode.scenicspot.beijingolympicpack.entity.ColumnEntity;
import cn.messycode.scenicspot.beijingolympicpack.entity.DatabaseEntity;
import cn.messycode.scenicspot.beijingolympicpack.entity.TableEntity;
import cn.messycode.scenicspot.beijingolympicpack.factory.SimpleDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author simon.zhao
 */
public class Application {
    public List<TableEntity> getTableList(DatabaseEntity database, String username, String password){
        List<TableEntity> data = new ArrayList<>();
        DataSource source = getSource(database, username, password);
        try (Connection connection = source.getConnection()) {
            DatabaseMetaData meta = connection.getMetaData();

            ResultSet tableResultSet = meta.getTables(database.getName(), null, null, new String[]{"TABLE"});
            while (tableResultSet.next()) {
                data.add(TableEntity.getInstance(tableResultSet.getString("TABLE_NAME"), tableResultSet.getString("REMARKS"), database));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    public void loadTableColumns(TableEntity table, String username, String password){
        Set<String> pk = getTableKeys(table, username, password);

        DataSource source = getSource(table.getDatabase(), username, password);
        try (Connection connection = source.getConnection()){
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet columns = meta.getColumns(table.getDatabase().getName(), null, table.getName(), null);
            while (columns.next()){
                String columnName = columns.getString("COLUMN_NAME");
                ColumnEntity column = ColumnEntity.getInstance(columnName, columns.getString("TYPE_NAME"), columns.getString("REMARKS") );
                column.setKey(pk.contains(columnName));
                table.addColumn(column);
            }

        } catch (SQLException e){
            throw  new RuntimeException(e);
        }
    }

    private DataSource getSource(DatabaseEntity database, String username, String password){
        return SimpleDataSourceFactory.getInstance().getDataSource(database, username, password);
    }

    private Set<String> getTableKeys(TableEntity table, String username, String password){
        Set<String> pk = new HashSet<>();
        DataSource source = getSource(table.getDatabase(), username, password);
        try (Connection connection = source.getConnection()){
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet columns = meta.getPrimaryKeys(table.getDatabase().getName(), null, table.getName());
            while (columns.next()){
                pk.add(columns.getString("COLUMN_NAME"));
            }
        } catch (SQLException e){
            throw  new RuntimeException(e);
        }
        return pk;
    }

}
