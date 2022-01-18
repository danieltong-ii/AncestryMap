package services;

import DataAccess.AuthTokenDAO;
import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.PersonDAO;
import Model.AuthToken;
import Model.Person;
import Requests.PersonRequest;
import Service.PersonService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


public class PersonServiceTest {
    Database db = new Database();
    PersonService pService = new PersonService();
    PersonRequest pRequest;
    Person firstPerson;
    Person secondPerson;
    Person assocPerson;

    AuthToken firstToken;
    PersonDAO pDao;
    AuthTokenDAO aDao;
    List<Person> actualList = new ArrayList<>();

    @BeforeEach
    public void setUp() throws DataAccessException {

        db = new Database();
        Connection conn = db.getConnection();
        db.clearTables();

        pDao = new PersonDAO(conn);
        aDao = new AuthTokenDAO(conn);

        firstToken = new AuthToken("234890", "Gale");
        firstPerson = new Person("1", "Gale", "Gale",
                "Piper", "m", "4", "5",
                "3");
        secondPerson = new Person("4", "Gale", "Ben",
                "Song", "m", "6", "7",
                "9");
        assocPerson = new Person("5", "Gale", "Daniel",
                "Long", "m", "8", "2",
                "45");
        actualList.add(firstPerson);
        actualList.add(secondPerson);
        actualList.add(assocPerson);

        pDao.insert(firstPerson);
        pDao.insert(secondPerson);
        pDao.insert(assocPerson);

        aDao.insert(firstToken);

        pRequest = new PersonRequest("ALL", "234890");
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void getPersonsPass() throws DataAccessException {
        List<Person>personList;
        personList = pService.getPersons(pRequest).getData();

        assertEquals(personList,actualList);
    }

    @Test
    public void getPersonsFail() throws DataAccessException {
        PersonRequest badrequest = new PersonRequest("TOMMY_5", "23434890");

        List<Person>personList;
        personList = pService.getPersons(badrequest).getData();

        assertNotEquals(personList,actualList);

    }
}
