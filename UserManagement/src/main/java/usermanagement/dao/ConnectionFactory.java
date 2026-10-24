package usermanagement.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Creates JDBC connections to the MySQL database.
 *
 * Credentials are read from environment variables so that no secret is
 * hard-coded in the source code:
 *   DB_URL      (default: jdbc:mysql://localhost:3306/usermanagement)
 *   DB_USER     (default: root)
 *   DB_PASSWORD (default: empty)
 */
public final class ConnectionFactory {

    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/usermanagement";
    private static final String DEFAULT_USER = "root";

    private static final String URL = env("DB_URL", DEFAULT_URL);
    private static final String USER = env("DB_USER", DEFAULT_USER);
    private static final String PASSWORD = env("DB_PASSWORD", "");

    static {
        try {
            // Explicit registration: needed when the driver jar lives in WEB-INF/lib
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("MySQL JDBC driver not found on the classpath");
        }
    }

    private ConnectionFactory() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static String env(String name, String defaultValue) {
        String value = System.getenv(name);
        return (value == null || value.isBlank()) ? defaultValue : value;
    }
}
