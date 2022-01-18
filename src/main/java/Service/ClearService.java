package Service;

import DataAccess.*;
import Results.ClearResult;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *  Deletes ALL data from the database, including user accounts, auth tokens, and generated person and event data.
 */

public class ClearService {
    /**
     * Clear function calls the clear functions of each DAO
     * @throws SQLException
     */
    public ClearResult Clear() throws SQLException {
       Database db = new Database();
        try {
            Connection conn = db.getConnection();
            db.clearTables();
            db.closeConnection(true);
            return new ClearResult("Clear succeeded", true);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return new ClearResult("Error: The database could not be cleared", false);
        }
    }
}
