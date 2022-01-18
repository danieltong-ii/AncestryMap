package services;

import DataAccess.*;
import Model.User;
import Requests.FillRequest;
import Results.FillResult;
import Service.FillService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


public class FillServiceTest {
    Database db = new Database();
    FillService fillService = new FillService();
    FillRequest fillrequest;
    FillResult fillresult;
    FillResult expected = new FillResult("Successfully added 31 persons and 90 events to the database", true);
    UserDAO uDao;
    User user;

    @BeforeEach
    public void setUp() throws DataAccessException {

        db = new Database();
        Connection conn = db.getConnection();
        db.clearTables();

        user = new User("Michelle", "password", "danieltong.h@gmail.com",
                "Daniel", "Tong", "m", "1");

        uDao = new UserDAO(conn);
        uDao.insert(user);

        fillrequest = new FillRequest("Michelle");


    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void fillPass() throws DataAccessException {
        fillresult = fillService.fill(fillrequest);
        assertEquals(expected.getMessage(),fillresult.getMessage());
        assertEquals(expected.getSuccess(),fillresult.getSuccess());
    }

    @Test
    public void fillFail() throws DataAccessException {
        //Username not in database
        FillRequest badRequest = new FillRequest("William");
        fillresult = fillService.fill(badRequest);

        assertNotEquals(expected.getMessage(),fillresult.getMessage());
        assertNotEquals(expected.getSuccess(),fillresult.getSuccess());
    }
}
