package orgs.tuasl_clint.utils;

import java.io.IOException;
import java.sql.*;
import java.util.List;

import static java.lang.System.exit;

public class DatabaseConnectionSQLite {
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

    public static void DeleteData() {
        try (var stmt = DatabaseConnectionSQLite.getInstance().getConnection().createStatement()) {
            CreateDatabase(stmt);
        } catch (SQLException | IOException e) {
            System.out.println("Error While Droping data from Database Error msg: "+ e.getMessage());
            e.printStackTrace();
            exit(0);
        }
    }

    private static final class InstanceHolder {
        private static final DatabaseConnectionSQLite instance = new DatabaseConnectionSQLite();
    }

    public static DatabaseConnectionSQLite getInstance() {
        return InstanceHolder.instance;
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
        // TODO: return this code(try and catch code) after finishing project final build or make it comment to create the  database with it's default data : Done
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
            CreateDatabase(stmt);
            insertDataToDatabase(stmt);
        } catch (SQLException | IOException e) {
            System.out.println("an Error occurred while trying create database tables or insert values error is : ");
            System.out.println(e.getMessage());
        }
    }

    private static void insertDataToDatabase(Statement stmt) throws IOException, SQLException {
        List<String> inserts = FilesHelperReader.readUntilChar("src\\main\\resources\\orgs\\tuasl_clint\\file\\tusalDB_insertion_SQLite.txt",';');
        for(String query : inserts){
            System.out.print("   QUERY: \n\""+ query + "\"\n   IS EXECUTING ... STATE IS : ");
            if(stmt.executeUpdate(query + ";") > 0)
                System.out.println("SUCCESS ...!!!");
            else
                System.out.println("FAILURE ...!!!");
        }
    }

    private static void CreateDatabase(Statement stmt) throws IOException, SQLException {
        List<String> queries = FilesHelperReader.readUntilChar("src\\main\\resources\\orgs\\tuasl_clint\\file\\SQLiteDatabase.txt",';');
        for(String query : queries){
            stmt.executeUpdate(query);
            System.out.println("   QUERY: \n"+ query + "\n   IS EXECUTED SUCCESSFULLY ...!!!");
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