package orgs.tuasl_clint.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionSQLite {
    private static DatabaseConnectionSQLite instance;
    private Connection connection;
    private static final String DB_URL = "jdbc:sqlite:SqliteToasolDB.db";

    private DatabaseConnectionSQLite() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Connection to SQLite has been established.");
            initializeDatabase();
        } catch (SQLException e) {
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
        String createTableSQL = "" +
                "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "email TEXT NOT NULL UNIQUE" +
                ")";

        try (var stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Tables checked/created successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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