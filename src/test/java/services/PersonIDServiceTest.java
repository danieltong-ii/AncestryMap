package services;

import DataAccess.AuthTokenDAO;
import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.PersonDAO;
import Model.AuthToken;
import Model.Person;
import Requests.PersonRequest;
import Results.PersonIDResult;
import Service.PersonIDService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


public class PersonIDServiceTest {
    Database db = new Database();
    PersonIDService pService = new PersonIDService();
    PersonRequest pRequest;
    Person firstPerson;
    AuthToken firstToken;
    PersonDAO pDao;
    AuthTokenDAO aDao;

    @BeforeEach
    public void setUp() throws DataAccessException {

        db = new Database();
        Connection conn = db.getConnection();
        db.clearTables();

        pDao = new PersonDAO(conn);
        aDao = new AuthTokenDAO(conn);

        firstToken = new AuthToken("234890", "Gale");
        firstPerson = new Person("TOMMY_6", "Gale", "Gale",
                "Piper", "m", "4", "5",
                "3");
        pDao.insert(firstPerson);
        aDao.insert(firstToken);

        pRequest = new PersonRequest("TOMMY_6", "234890");
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void getPersonPass() throws DataAccessException {
        PersonIDResult pResult = pService.getPerson(pRequest);

        assertEquals(pResult.getAssociatedUsername(),"Gale");
        assertEquals(pResult.getPersonID(),"TOMMY_6");
        assertEquals(pResult.getGender(),"m");
        assertEquals(pResult.getFatherID(),"4");

    }

    @Test
    public void getPersonFail() throws DataAccessException {
        PersonRequest badrequest = new PersonRequest("TOMMY_5", "234890");

        PersonIDResult pResult = pService.getPerson(badrequest);

        assertNotEquals(pResult.getAssociatedUsername(),"Gale");
        assertNotEquals(pResult.getPersonID(),"TOMMY_6");
        assertNotEquals(pResult.getGender(),"m");
        assertNotEquals(pResult.getFatherID(),"4");

    }
}
