package Results;

/**
 *  Result of request to do the fill
 */

public class FillResult extends ParentResult {

    /**
     * Constructor for subclass; inheriting message and success fields
     * @param message  a string that displays status
     * @param success a boolean that tells user is succeeded or not
     */
    public FillResult(String message, Boolean success) {
        super(message, success);
    }


}
