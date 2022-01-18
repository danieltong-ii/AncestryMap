package Service;

import DataAccess.AuthTokenDAO;
import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.PersonDAO;
import Model.AuthToken;
import Model.Person;
import Requests.PersonRequest;
import Results.PersonIDResult;

import java.sql.Connection;

/**
 * Returns the single Person object with the specified ID.
 */

public class PersonIDService {


    private PersonIDResult createPersonIDResult(Person pFound) {
        if ((pFound.getMotherID() == null) && (pFound.getSpouseID() == null)) {
            return new PersonIDResult(pFound.getAssociatedUsername(), pFound.getPersonID(), pFound.getFirstName(), pFound.getLastName(), pFound.getGender(), true);
        }
        else if ((pFound.getMotherID() != null) && (pFound.getSpouseID() == null)) {
            return new PersonIDResult(pFound.getAssociatedUsername(), pFound.getPersonID(), pFound.getFirstName(), pFound.getLastName(), pFound.getGender(), pFound.getFatherID(), pFound.getMotherID(), true);
        }
        else if ((pFound.getMotherID() == null) && (pFound.getSpouseID() != null)) {
            return new PersonIDResult(pFound.getAssociatedUsername(), pFound.getPersonID(), pFound.getFirstName(), pFound.getLastName(), pFound.getGender(), pFound.getSpouseID(), true);
        }
        else {
            return new PersonIDResult(pFound.getAssociatedUsername(), pFound.getPersonID(), pFound.getFirstName(), pFound.getLastName(), pFound.getGender(), pFound.getFatherID(), pFound.getMotherID(), pFound.getSpouseID(), true);
        }
    }

    /**
     * Returns the single Person object with the specified ID.
     * @param p gets the personRequest from PersonIDHandler
     * @return the java response object from PersonIDResult class that we need to next convert into Json String
     */
    public PersonIDResult getPerson(PersonRequest p) {
        Database db = new Database();
        try {
            Connection conn = db.getConnection();
            PersonDAO pDao = new PersonDAO(conn);
            AuthTokenDAO aDao = new AuthTokenDAO(conn);

            Person pFound = pDao.find(p.getPersonID());
            AuthToken tFound = aDao.find(p.getAuthToken());
            db.closeConnection(false);

            //If either the token or person is null, return an error
            if ((tFound == null) || (pFound == null)) {
                return new PersonIDResult("Error: AuthToken not found", false);
            }

            //If the Person's username and the Token's username match
            if (pFound.getAssociatedUsername().equals(tFound.getUsername())) {
                return createPersonIDResult(pFound);
            }
            else {
                return new PersonIDResult("Error: PersonID requested does not match this AuthToken", false);
            }
        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
            } catch (DataAccessException dataAccessException) {
                dataAccessException.printStackTrace();
            }
            e.printStackTrace();
        }
        return null;
    }
}
