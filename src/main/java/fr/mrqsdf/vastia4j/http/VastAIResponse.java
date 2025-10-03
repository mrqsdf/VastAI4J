package fr.mrqsdf.vastia4j.http;

/**
 * Represents a generic Vast.ai API response payload.
 *
 * @param <T> type of the deserialized result data
 */
public class VastAIResponse<T> {

    private boolean success;
    private String error;
    private T result;

    /**
     * Indicates if the API request was successful.
     * @return true if successful, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }


    /**
     * Retrieves the error message if the request failed.
     * @return the error message, or null if there was no error
     */
    public String getError() {
        return error;
    }

    /**
     * Retrieves the deserialized result data from the response.
     * @return the result data of type T
     */
    public T getResult() {
        return result;
    }

}
