package com.company;

import com.company.interfaces.DbFunctions;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.sql.*;

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
    public void insertRow(Connection conn,int rowId, Sheet sheet) {
        try {
            String query = String.format("INSERT INTO %s(orderId, OrderDate, OrderQuantity, Sales, ShipMode) VALUES(?, ?, ?, ?, ?)", tableName);
            PreparedStatement statement = conn.prepareStatement(query);

            Row row = sheet.getRow(rowId);

            String orderId = row.getCell(0).toString();
            String orderDate = row.getCell(1).toString();
            String orderQuantity = row.getCell(2).toString();
            String sales = row.getCell(3).toString();
            String shipMode = row.getCell(4).toString();

            statement.setString(1, orderId);
            statement.setString(2, orderDate);
            statement.setString(3, orderQuantity);
            statement.setString(4, sales);
            statement.setString(5, shipMode);

            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
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

