package com.company;
import java.sql.Connection;


public class Main  {
    public static void main(String[] args) {
        try {

            String flag = args[0];
            Config configReader = Config.INSTANCE;

            String dbName = configReader.getConfig("DB_NAME", "test1");
            String userName = configReader.getConfig("USER_NAME", "sathish");
            String password = configReader.getConfig("PASSWORD", "321");

            // Database Connection
            PostgresDb db = new PostgresDb(dbName, userName, password);
            Connection connection = db.establishConnection();

            // Reading Excel data from the file
            ExcelReadWrite wr = new ExcelReadWrite(db, connection);

            // Calling method based on the arguments
            if ("0".equals(flag)) wr.writeDataWithoutThread();
            else wr.writeDataUsingThreadPool();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}










