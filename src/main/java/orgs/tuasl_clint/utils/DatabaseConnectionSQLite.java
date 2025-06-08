package orgs.tuasl_clint.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DatabaseConnectionSQLite {
    private static DatabaseConnectionSQLite instance;
    private Connection connection;
    private static final String DB_URL = "jdbc:sqlite:SQLiteDatabase.db";

    private DatabaseConnectionSQLite() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Connection to SQLite has been established.");
            initializeDatabase();
        } catch (SQLException e) {
            System.out.println("Error Cannot Create Connection In DatabaseConnectionSqlite file......!!!");
            System.out.println(e.getMessage());
        }
    }

    public static DatabaseConnectionSQLite getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnectionSQLite.class) {
                if (instance == null) {
                    instance = new DatabaseConnectionSQLite();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }

    private void initializeDatabase() {
        // Example: Create tables if they don't exist
        System.out.println("init the database tables:");
        // TODO: return this code after finishing project final build
//        try {
//            if(tableExists("users")){
//                System.out.println("Database Has Been Created Before..!!");
//                return;
//            }
//        } catch (SQLException e) {
//            System.out.println("Error cannot test database last creation...!!!");
//            e.printStackTrace();
//        }
        try (var stmt = connection.createStatement()) {
            List<String> queries = FilesHelperReader.readUntilChar("src\\main\\resources\\orgs\\tuasl_clint\\file\\SQLiteDatabase.txt",';');
            for(String query : queries){
                stmt.executeUpdate(query);
                System.out.println("   QUERY: \n"+ query + "\n   IS EXECUTED SUCCESSFULLY ...!!!");
            }
            List<String> inserts = FilesHelperReader.readUntilChar("src\\main\\resources\\orgs\\tuasl_clint\\file\\tusalDB_insertion_SQLite.txt",';');
            for(String query : inserts){
                System.out.print("   QUERY: \n\""+ query + "\"\n   IS EXECUTING ... STATE IS : ");
                if(stmt.executeUpdate(query + ";") > 0)
                    System.out.println("SUCCESS ...!!!");
                else
                    System.out.println("FAILURE ...!!!");
            }
        } catch (SQLException | IOException e) {
            System.out.println("an Error occurred while trying create database tables or insert values error is : ");
            System.out.println(e.getMessage());
        }
    }
    private boolean tableExists(String tableName) throws SQLException {
        try (ResultSet rs = connection.getMetaData().getTables(null, null, tableName, null)) {
            return rs.next();
        }
    }
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection to SQLite has been closed.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}