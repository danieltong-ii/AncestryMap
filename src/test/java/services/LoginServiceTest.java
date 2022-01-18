package services;

import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.UserDAO;
import Model.User;
import Requests.LoginRequest;
import Results.LoginResult;
import Service.LoginService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


public class LoginServiceTest {
    Database db = new Database();
    User firstUser;
    LoginRequest lr;
    UserDAO uDao;
    LoginService loginService = new LoginService();

    @BeforeEach
    public void setUp() throws DataAccessException {

        db = new Database();
        Connection conn = db.getConnection();
        db.clearTables();

        uDao = new UserDAO(conn);

        firstUser = new User("daniel", "password", "danieltong.h@gmail.com",
                "Daniel", "Tong", "m", "1");

        lr = new LoginRequest("daniel", "password");
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void loginPass() throws DataAccessException {
        LoginResult result;
        uDao.insert(firstUser);
        result = loginService.login(lr);
        assertEquals(result.getPersonID(),"1");
        assertEquals(result.getUsername(),"daniel");
    }

    @Test
    public void loginFail() throws DataAccessException {
        LoginResult result;
        uDao.insert(firstUser);
        Boolean success = true;
        LoginRequest wrongPassword = new LoginRequest("daniel", "lollipop");

        result = loginService.login(wrongPassword);
        assertNotEquals(result.getSuccess(),success);
    }
}
