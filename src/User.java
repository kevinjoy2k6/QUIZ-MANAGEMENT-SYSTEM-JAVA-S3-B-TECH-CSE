/**
 * User.java
 *
 * Model class representing a user of the Quiz Management System.
 *
 * OOP Concept: Encapsulation — all fields are private with public getters/setters.
 *              This class models real-world entity data cleanly.
 */
public class User {

    // ─── FIELDS (private — encapsulated) ──────────────────────────────────────
    private int    id;
    private String username;
    private String password;
    private String role;       // "admin", "student", "guest"
    private String fullName;

    // ─── CONSTRUCTORS ─────────────────────────────────────────────────────────

    /** Default constructor */
    public User() {}

    /** Full constructor for creating a user object from database data */
    public User(int id, String username, String password, String role, String fullName) {
        this.id       = id;
        this.username = username;
        this.password = password;
        this.role     = role;
        this.fullName = fullName;
    }

    /** Constructor for registration (no ID yet) */
    public User(String username, String password, String role, String fullName) {
        this.username = username;
        this.password = password;
        this.role     = role;
        this.fullName = fullName;
    }

    // ─── GETTERS ──────────────────────────────────────────────────────────────

    public int    getId()       { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole()     { return role; }
    public String getFullName() { return fullName; }

    // ─── SETTERS ──────────────────────────────────────────────────────────────

    public void setId(int id)             { this.id = id; }
    public void setUsername(String u)     { this.username = u; }
    public void setPassword(String p)     { this.password = p; }
    public void setRole(String r)         { this.role = r; }
    public void setFullName(String name)  { this.fullName = name; }

    // ─── ROLE CHECKS ──────────────────────────────────────────────────────────

    /** Returns true if this user has admin privileges */
    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(role);
    }

    /** Returns true if this user is a guest (no registered account) */
    public boolean isGuest() {
        return "guest".equalsIgnoreCase(role);
    }

    /** Returns true if this user is a registered student */
    public boolean isStudent() {
        return "student".equalsIgnoreCase(role);
    }

    // ─── toString ─────────────────────────────────────────────────────────────

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', role='" + role + "', fullName='" + fullName + "'}";
    }
}
