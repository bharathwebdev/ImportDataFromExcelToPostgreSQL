package com.company;
import com.company.interfaces.ReadWriteFunctions;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExcelReadWrite implements ReadWriteFunctions{
    private final Connection conn;
    private final PostgresDb db;
    private final Config config = Config.INSTANCE;
    private final String tableName = config.getConfig("TABLE_NAME");
    private Workbook workbook;
    private Sheet sheet;
    private static final int THREAD_POOL_COUNT = Runtime.getRuntime().availableProcessors();

    public ExcelReadWrite(PostgresDb db, Connection conn) throws IOException, InvalidFormatException {
        this.conn = conn;
        this.db = db;
        String FILE_NAME = config.getConfig("FILE_NAME", "20000Data.xlsx");
        createSheet(FILE_NAME);
    }


    public void writeDataWithoutThread() {
        System.out.println("Writing Data Without Thread ...");
        long startTime = System.currentTimeMillis();
        try {
            for (Row row : sheet) {
                String query = String.format("INSERT INTO %s(orderId, OrderDate, OrderQuantity, Sales, ShipMode) VALUES('%s', '%s', '%s', '%s', '%s');", tableName, row.getCell(0).toString(), row.getCell(1).toString(), row.getCell(2).toString(), row.getCell(3).toString(), row.getCell(4).toString());
                db.insertRow(conn, query);
            }
            long endTime = System.currentTimeMillis();
            System.out.println("Execution time: " + (endTime - startTime) + " ms ⏰");
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void writeDataUsingThreadPool() {

        System.out.println("Writing Data With Thread ...");
        long startTime = System.currentTimeMillis();

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_COUNT);
        final int rowCount = sheet.getLastRowNum() + 1;

        for (int i = 0; i < rowCount; i++) {
            final int rowId = i;
            executorService.submit(() -> insertRow(rowId, sheet));
        }
        executorService.shutdown();

        try {
            // Wait for all tasks to complete
            boolean tasksCompleted = executorService.awaitTermination(10, TimeUnit.MINUTES);

            if (tasksCompleted) {
                long endTime = System.currentTimeMillis();
                System.out.println("Execution time : " + (endTime - startTime) + " ms ⏰ ");
            } else {
                System.out.println("Timeout occurred before all tasks completed.");
            }

            workbook.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void createSheet(String fileName) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File("./src/" + fileName));
        this.workbook = workbook;
        this.sheet = workbook.getSheetAt(0);
    }

    private void insertRow(int rowId, Sheet sheet) {
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
}
