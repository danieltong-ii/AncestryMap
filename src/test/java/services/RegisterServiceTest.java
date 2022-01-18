package services;

import DataAccess.DataAccessException;
import DataAccess.Database;
import Requests.RegisterRequest;
import Results.RegisterResult;
import Service.RegisterService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class RegisterServiceTest {
    Database db = new Database();
    RegisterService rService = new RegisterService();
    RegisterRequest rRequest;

    @BeforeEach
    public void setUp() throws DataAccessException {

        db = new Database();
        Connection conn = db.getConnection();
        db.clearTables();

        rRequest = new RegisterRequest("Selina", "batman", "cat@gmail.com", "Selina", "Kyle", "f");
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void registerPass() throws DataAccessException {
        RegisterResult rr = rService.register(rRequest);
        Boolean succ = true;
        assertEquals(rr.getUsername(), "Selina");
        assertEquals(rr.getSuccess(), succ);
    }

    @Test
    public void registerFail() throws DataAccessException {
        //attempting to add same username twice
        rService.register(rRequest);
        RegisterResult rr = rService.register(rRequest);
        Boolean succ = false;

        assertEquals(rr.getSuccess(), succ);
    }
}
