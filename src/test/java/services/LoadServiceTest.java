package services;

import DataAccess.*;
import Model.Event;
import Model.Person;
import Model.User;
import Requests.LoadRequest;
import Results.LoadResult;
import Service.LoadService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


public class LoadServiceTest {
    Database db = new Database();
    LoadService loadService = new LoadService();
    LoadRequest loadRequest;
    LoadResult expected;

    private User firstUser;
    private User secondUser;
    private Event event;
    private Person person;



    @BeforeEach
    public void setUp() throws DataAccessException {

        db = new Database();
        Connection conn = db.getConnection();
        db.clearTables();

        firstUser = new User("daniel", "password", "danieltong.h@gmail.com",
                "Daniel", "Tong", "m", "1");

        secondUser = new User("tom", "abc", "tom@gmail.com",
                "Tom", "Tope", "m", "2");
        event = new Event("Biking_123A", "Gale", "Gale123A",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);

        person = new Person("1", "Gale", "Gale",
                "Piper", "m", "4", "5",
                "3");

        List<User> users = new ArrayList<>();
        List<Person> persons = new ArrayList<>();
        List<Event> events = new ArrayList<>();

        users.add(firstUser);
        users.add(secondUser);
        events.add(event);
        persons.add(person);

        loadRequest = new LoadRequest(users,persons,events);
        expected = new LoadResult("Successfully added 2 users, 1 persons, and 1 events to the database.", true);

    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void loadPass() throws DataAccessException {
        LoadResult actualResult;
        expected = new LoadResult("Successfully added 2 users, 1 persons, and 1 events to the database.", true);

        actualResult = loadService.load(loadRequest);
        assertEquals(actualResult.getMessage(), expected.getMessage());
        assertEquals(actualResult.getSuccess(), expected.getSuccess());
    }

    @Test
    public void loadFail() throws DataAccessException {
        LoadResult wrongResult = new LoadResult("Successfully added 5 users, 1 persons, and 1 events to the database.", true);
        LoadResult actualResult = loadService.load(loadRequest);
        assertNotEquals(actualResult.getMessage(), wrongResult.getMessage());

    }
}
