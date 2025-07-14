
package orgs.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/tuasil_messaging?useSSL=false&serverTimezone=UTC";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "730673145";

    // The single instance of the Connection
    private static Connection connection = null;

    // Private constructor to prevent direct instantiation
    private DatabaseConnection() {
        // Private constructor
    }

    public static synchronized Connection getConnection() throws SQLException {
        // If the connection is null or closed, re-establish it
        if (connection == null || connection.isClosed()) {
            try {
                System.out.println("Attempting to establish new database connection...");
                connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
                System.out.println("Database connection established successfully!");
            } catch (SQLException e) {
                System.err.println("Failed to establish database connection: " + e.getMessage());
                throw e; // Re-throw the exception for the caller to handle
            }
        }
        return connection;
    }

    public static synchronized void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("Database connection closed.");
                }
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
                e.printStackTrace();
            } finally {
                connection = null; // Set to null after closing
            }
        }
    }
}