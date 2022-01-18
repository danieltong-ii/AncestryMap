package Results;

/**
 * Result shows the response to the ClearService
 */

public class ClearResult extends ParentResult{
    /**
     * Constructor for ClearResult class
     * @param message a string that displays status
     * @param success a boolean that tells user is succeeded or not
     */
    public ClearResult(String message, Boolean success) {
        super(message, success);
    }
}
