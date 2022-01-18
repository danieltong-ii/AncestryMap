package Service;

import DataAccess.AuthTokenDAO;
import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.UserDAO;
import Model.AuthToken;
import Model.User;
import Requests.LoginRequest;
import Results.LoginResult;

import java.sql.Connection;
import java.util.UUID;

/**
 * Logs in the user and returns an auth token.
 */

public class LoginService {

    /**
     * Logs the user in
     * @param l gets the loginRequest from the LoginHandler
     * @return the java response object from LoginResult class that we need to next convert into Json String
     */

    public LoginResult login(LoginRequest l) {
        Database db = new Database();
        UserDAO uDao;
        AuthTokenDAO aDao;
        String personID;

        try {
            Connection conn = db.getConnection();
            uDao = new UserDAO(conn);
            aDao = new AuthTokenDAO(conn);

            User returnedUser = uDao.find(l.getUsername());
            // Check if the same username was found
            if (returnedUser != null) {
                if (l.getPassword().equals(returnedUser.getPassword())) {
                    personID = returnedUser.getPersonID();

                    AuthToken newToken = new AuthToken(UUID.randomUUID().toString(),l.getUsername());
                    aDao.insert(newToken);

                    db.closeConnection(true);
                    return new LoginResult(newToken.getAuthtoken(),newToken.getUsername(), personID, true);
                }
                else {
                    db.closeConnection(false);
                    return new LoginResult("Error: Wrong password", false);
                }
            }
            else {
                db.closeConnection(false);
                return new LoginResult("Error: Wrong username", false);
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            try {
                db.closeConnection(false);
                return new LoginResult("Error: Internal error in login service", false);
            } catch (DataAccessException dataAccessException) {
                dataAccessException.printStackTrace();
            }
        }
        return null;
    }

}
