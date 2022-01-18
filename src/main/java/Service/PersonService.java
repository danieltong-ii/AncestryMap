package Service;

import DataAccess.AuthTokenDAO;
import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.PersonDAO;
import Model.AuthToken;
import Model.Person;
import Requests.PersonRequest;
import Results.PersonResult;

import java.sql.Connection;
import java.util.List;

/**
 * Description: Returns ALL people, packaged in a Person array
 */

public class PersonService {

    /**
     * This function handles the personrequest object and returns a personResult object
     * (which contains the array of people requested by the user)
     * @param pr the PersonRequest object
     * @return the PersonResult object with the person array
     */
    public PersonResult getPersons(PersonRequest pr) {
        Database db = new Database();
        List<Person> personList;

        try {
            Connection conn = db.getConnection();
            PersonDAO pDao = new PersonDAO(conn);
            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            AuthToken token = aDao.find(pr.getAuthToken());
            if (token == null) {
                db.closeConnection(false);
                return new PersonResult("Error: no persons found associated with this token", false, null);
            }
            String assocUser = token.getUsername();
            personList = pDao.findPersonsAssociatedWithUser(assocUser);

            //Check if the array of persons is empty or not
            if (!personList.isEmpty()) {
                db.closeConnection(false);
                 return new PersonResult(personList, true);
            }
            else {
                db.closeConnection(false);
                return new PersonResult("Error: no persons found associated with this token", false, null);
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