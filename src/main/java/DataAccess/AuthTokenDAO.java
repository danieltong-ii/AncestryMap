package DataAccess;

import Model.AuthToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The AuthTokenDAO contains methods for accessing authtoken data.
 */

public class AuthTokenDAO {

    private final Connection conn;

    public AuthTokenDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Method adds an AuthToken to the database
     * @param token : the token object
     */
    public void insert(AuthToken token) throws DataAccessException {
        String sql = "INSERT INTO Tokens (Username, AuthToken) VALUES (?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token.getUsername());
            stmt.setString(2, token.getAuthtoken());

            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting authToken into the database");
        }
    }
    /**
     *
     * @param token : a token Object
     * @return returns a AuthToken object
     */
    public AuthToken find(String token) throws DataAccessException {
        AuthToken authToken;
        ResultSet rs = null;
        String sql = "SELECT * FROM Tokens WHERE AuthToken = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token);

            rs = stmt.executeQuery();
            conn.commit();

            if (rs.next()) {
                authToken = new AuthToken(rs.getString("AuthToken"), rs.getString("Username"));
                return authToken;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error occurred while finding AuthToken");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    /**
     * This function deletes ALL AuthTokens from the database
     */
    public void clear() throws SQLException {
        PreparedStatement stmt = null;
        try {
            String sql = "DELETE FROM Tokens";
            stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new SQLException("Error occurred while deleting tokens");
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
}
