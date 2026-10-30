/**
 * UserDAO.java
 *
 * Data Access Object for User-related database operations.
 * Handles login, registration, and user retrieval using JDBC PreparedStatements.
 *
 * OOP Concept: Abstraction — hides database implementation details from the UI layer.
 * JDBC: Uses PreparedStatement to prevent SQL injection.
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    /**
     * Authenticates a user with the given username and password.
     *
     * @param username the entered username
     * @param password the entered password (plain text for simplicity)
     * @return User object if credentials are valid, null otherwise
     */
    public User login(String username, String password) {
        // SQL query using PreparedStatement (safe — no SQL injection possible)
        String sql = "SELECT id, username, password, role, full_name " +
                     "FROM users WHERE username = ? AND password = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);   // bind parameter 1
            stmt.setString(2, password);   // bind parameter 2
            rs = stmt.executeQuery();

            if (rs.next()) {
                // Build and return User object from result row
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getString("full_name")
                );
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] Login error: " + e.getMessage());
        } finally {
            // Always close resources to prevent leaks
            closeResources(rs, stmt);
        }
        return null;  // login failed
    }

    /**
     * Registers a new student user.
     *
     * @param user the User object with registration data
     * @return true if registration succeeded, false otherwise
     */
    public boolean register(User user) {
        String sql = "INSERT INTO users (username, password, role, full_name) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, "student");          // new registrations are always students
            stmt.setString(4, user.getFullName());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Set the auto-generated ID back on the user object
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                }
                generatedKeys.close();
                return true;
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            // Duplicate username — caller should show friendly message
            System.err.println("[UserDAO] Duplicate username: " + user.getUsername());
        } catch (SQLException e) {
            System.err.println("[UserDAO] Registration error: " + e.getMessage());
        } finally {
            closeResources(null, stmt);
        }
        return false;
    }

    /**
     * Creates a temporary guest user record in the database.
     * Guests are stored so results can be linked to them.
     *
     * @param guestName the display name entered by the guest
     * @return User object for the guest, or null on failure
     */
    public User createGuestUser(String guestName) {
        // Generate a unique guest username using timestamp
        String guestUsername = "guest_" + System.currentTimeMillis();
        String sql = "INSERT INTO users (username, password, role, full_name) VALUES (?, ?, 'guest', ?)";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, guestUsername);
            stmt.setString(2, "");           // guests have no password
            stmt.setString(3, guestName);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                int newId = 0;
                if (keys.next()) {
                    newId = keys.getInt(1);
                }
                keys.close();
                User guest = new User(newId, guestUsername, "", "guest", guestName);
                return guest;
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] Guest creation error: " + e.getMessage());
        } finally {
            closeResources(null, stmt);
        }
        return null;
    }

    /**
     * Checks if a username already exists in the database.
     *
     * @param username the username to check
     * @return true if username is taken
     */
    public boolean usernameExists(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            return rs.next();   // true if a row was found
        } catch (SQLException e) {
            System.err.println("[UserDAO] Username check error: " + e.getMessage());
        } finally {
            closeResources(rs, stmt);
        }
        return false;
    }

    /**
     * Returns the total number of registered users (admin + student only).
     */
    public int getTotalUsers() {
        String sql = "SELECT COUNT(*) FROM users WHERE role IN ('admin', 'student')";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[UserDAO] Count error: " + e.getMessage());
        } finally {
            closeResources(rs, stmt);
        }
        return 0;
    }

    // ─── PRIVATE HELPER ───────────────────────────────────────────────────────

    /** Safely closes ResultSet and PreparedStatement */
    private void closeResources(ResultSet rs, PreparedStatement stmt) {
        try { if (rs   != null) rs.close();   } catch (SQLException ignored) {}
        try { if (stmt != null) stmt.close(); } catch (SQLException ignored) {}
    }
}
