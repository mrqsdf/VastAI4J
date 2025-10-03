package fr.mrqsdf.vastia4j.http;

/**
 * Generic runtime exception thrown when Vast.ai API calls fail or return an error.
 */
public class VastAIException extends RuntimeException {

    /**
     * Constructs a new VastAIException with the specified detail message.
     *
     * @param message the detail message.
     */
    public VastAIException(String message) {
        super(message);
    }

    /**
     * Constructs a new VastAIException with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause of the exception.
     */
    public VastAIException(String message, Throwable cause) {
        super(message, cause);
    }
}
