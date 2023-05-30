package com.company;
import com.company.interfaces.DbFunctions;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class PostgresDb implements DbFunctions {
    private final Config config = Config.INSTANCE;
    private final String tableName = config.getConfig("TABLE_NAME");
    private final String dbName;
    private final String userName;
    private final String password;

    public PostgresDb(String dbName, String userName, String password) {
        this.password = password;
        this.dbName = dbName;
        this.userName = userName;
    }

    @Override
    public Connection establishConnection() {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbName, userName, password);
            if (connection != null) {
                System.out.println("Connection Established ✅ ");
            } else {
                System.out.println("Connection Failed ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }


    @Override
    public void insertRow(Connection conn, String query) {
        Statement statement;
        try {
            statement = conn.createStatement();
            statement.executeUpdate(query);
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createTable(Connection conn) {
        Statement statement;
        try {
            String query = " CREATE TABLE " + tableName + " (id SERIAL, orderId varchar(200), OrderDate varchar(200), OrderQuantity varchar(200), Sales varchar(200), ShipMode varchar(200), primary key(id)); ";
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println(" Table Created ✅ ");
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

