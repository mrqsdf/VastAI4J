package fr.mrqsdf.vastia4j.http;

/**
 * Generic runtime exception thrown when Vast.ai API calls fail or return an error.
 */
public class VastAIException extends RuntimeException {

    public VastAIException(String message) {
        super(message);
    }

    public VastAIException(String message, Throwable cause) {
        super(message, cause);
    }
}
