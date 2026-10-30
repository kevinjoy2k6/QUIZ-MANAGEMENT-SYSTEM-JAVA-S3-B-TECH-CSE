
/**
 * DatabaseConnection.java
 *
 * Manages the MySQL JDBC connection for the Quiz Management System.
 * Uses a singleton pattern to ensure only one connection exists at a time.
 *
 * OOP Concept: Encapsulation — connection details are private, access is via method.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // ─── CONFIGURATION ────────────────────────────────────────────────────────
    // Change these values to match your MySQL installation
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DATABASE = "quiz_management";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "PASSWORD"; // <-- Change to your MySQL password

    // Full JDBC URL
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE
            + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    // Singleton connection instance
    private static Connection connection = null;

    // Private constructor — prevents instantiation
    private DatabaseConnection() {
    }

    /**
     * Returns an active database connection.
     * Creates a new connection if none exists or if the current one is closed.
     *
     * @return Connection object
     * @throws SQLException if the connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                // Load the MySQL JDBC driver
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("[DB] Connection established successfully.");
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                    "MySQL JDBC Driver not found. Please add mysql-connector-j.jar to the lib/ folder.\n"
                            + "Error: " + e.getMessage());
        }
        return connection;
    }

    /**
     * Closes the current database connection if open.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("[DB] Connection closed.");
                }
            } catch (SQLException e) {
                System.err.println("[DB] Error closing connection: " + e.getMessage());
            }
        }
    }

    /**
     * Tests whether the database is reachable.
     *
     * @return true if connection succeeds, false otherwise
     */
    public static boolean testConnection() {
        try {
            getConnection();
            return true;
        } catch (SQLException e) {
            System.err.println("[DB] Connection test failed: " + e.getMessage());
            return false;
        }
    }
}
