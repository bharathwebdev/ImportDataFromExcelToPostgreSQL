package com.company.interfaces;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.sql.Connection;
import java.sql.SQLException;

public interface DbFunctions {
    Connection establishConnection();

    void insertRow(Connection conn, int id, Sheet sheet);

    void createTable(Connection conn);
}






