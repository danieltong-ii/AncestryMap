package Results;

/**
 *  Returns the result of the loadRequest
 */

public class LoadResult extends ParentResult{

    /**
     * Constructor for LoadResult, just inherits from Parent
     * @param message a string that displays status
     * @param success a boolean that tells user is succeeded or not
     */
    public LoadResult(String message, Boolean success) {
        super(message, success);
    }
}
