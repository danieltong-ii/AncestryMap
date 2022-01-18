package DataAccess;

import Model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Contains methods for creating, returning, and clearing all user data in the db.
 */

public class UserDAO {

    /**
     * A connection is the session between java application and database
     */
    private final Connection conn;


    /**
     *  Constructor for userDAO, gets the connection from the function that called this one
     * @param conn
     */
    public UserDAO(Connection conn) {
        this.conn = conn;
    }
    /**
     *
     * @param user adds a user to the database
     */
    public void insert(User user) throws DataAccessException{
        String sql = "INSERT INTO Users (Username, Password, Email, FirstName, "
                + "LastName, Gender, PersonID) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());
            stmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while creating user in the database");
        }
    }
    /**
     *
     * @param username returns a user from the database
     */
    public User find(String username) throws DataAccessException {
        User user;
        ResultSet rs = null;
        String sql = "SELECT * FROM Users WHERE Username = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,username);
            rs = stmt.executeQuery();
            conn.commit();
            if (rs.next()) {
                user = new User(rs.getString("Username"), rs.getString("Password"),
                        rs.getString("Email"), rs.getString("FirstName"), rs.getString("LastName"),
                        rs.getString("Gender"), rs.getString("PersonID"));
                return user;
            }
            else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error occurred while finding user");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * This method removes ALL Persons associated with a user from the database
     */

    public void clear() throws SQLException {
        PreparedStatement stmt = null;

        try {
            String sql = "DELETE FROM Users";
            stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new SQLException("Error occurred while deleting persons");
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
}
