package usermanagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import usermanagement.model.User;

/**
 * Data Access Object for the {@code users} table.
 *
 * Every query uses a {@link PreparedStatement} with bound parameters, so user
 * input is never concatenated into SQL (protection against SQL injection).
 * Errors are propagated to the caller instead of being swallowed.
 */
public class UserDAO {

    private static final String INSERT_USER_SQL =
            "INSERT INTO users (name, email, country) VALUES (?, ?, ?)";
    private static final String SELECT_USER_BY_ID =
            "SELECT id, name, email, country FROM users WHERE id = ?";
    private static final String SELECT_ALL_USERS =
            "SELECT id, name, email, country FROM users ORDER BY id";
    private static final String UPDATE_USER_SQL =
            "UPDATE users SET name = ?, email = ?, country = ? WHERE id = ?";
    private static final String DELETE_USER_SQL =
            "DELETE FROM users WHERE id = ?";

    /** Create: inserts a new user. */
    public void insertUser(User user) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_USER_SQL)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getCountry());
            ps.executeUpdate();
        }
    }

    /** Read: returns the user with this id, or {@code null} if none exists. */
    public User selectUser(int id) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_USER_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    /** Read: returns all users ordered by id. */
    public List<User> selectAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_USERS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                users.add(mapRow(rs));
            }
        }
        return users;
    }

    /** Update: returns {@code true} if a row was updated. */
    public boolean updateUser(User user) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_USER_SQL)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getCountry());
            ps.setInt(4, user.getId());
            return ps.executeUpdate() > 0;
        }
    }

    /** Delete: returns {@code true} if a row was deleted. */
    public boolean deleteUser(int id) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_USER_SQL)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private static User mapRow(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("country"));
    }
}
