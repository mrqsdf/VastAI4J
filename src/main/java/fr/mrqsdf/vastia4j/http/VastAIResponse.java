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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
