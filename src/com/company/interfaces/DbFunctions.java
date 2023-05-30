package com.company.interfaces;
import java.sql.Connection;

public interface DbFunctions {
     Connection establishConnection();
     void insertRow(Connection conn, String query);
     void createTable(Connection conn);
}






