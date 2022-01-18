package DataAccess;

/**
 * The custom exception we use in our DAO classes.
 */

public class DataAccessException extends Exception {
    DataAccessException(String message)
    {
        super(message);
    }

    DataAccessException()
    {
        super();
    }
}
