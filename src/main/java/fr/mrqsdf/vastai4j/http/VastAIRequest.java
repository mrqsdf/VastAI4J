package fr.mrqsdf.vastai4j.http;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents a Vast.ai API request built via a fluent builder.
 */
public final class VastAIRequest {

    private final HttpMethod method;
    private final String path;
    private final Map<String, String> queryParams;
    private final Object body;
    private final Map<String, String> headers;

    /**
     * Private constructor used by the builder.
     * @param builder the builder instance
     */
    private VastAIRequest(Builder builder) {
        this.method = builder.method;
        this.path = builder.path;
        this.queryParams = Collections.unmodifiableMap(new LinkedHashMap<>(builder.queryParams));
        this.body = builder.body;
        this.headers = Collections.unmodifiableMap(new LinkedHashMap<>(builder.headers));
    }

    /**
     * Gets the HTTP method of the request.
     * @return the HTTP method
     */
    public HttpMethod getMethod() {
        return method;
    }

    /**
     * Gets the path of the request.
     * @return the request path
     */
    public String getPath() {
        return path;
    }

    /**
     * Gets the query parameters of the request.
     * @return an unmodifiable map of query parameters
     */
    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    /**
     * Gets the body of the request.
     * @return the request body, or null if none
     */
    public Object getBody() {
        return body;
    }

    /**
     * Gets the headers of the request.
     * @return an unmodifiable map of headers
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Converts the query parameters to a URL-encoded query string.
     * @return the query string starting with '?', or an empty string if no parameters
     */
    public String toQueryString() {
        if (queryParams.isEmpty()) {
            return "";
        }
        return queryParams.entrySet().stream()
                .map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue()))
                .collect(Collectors.joining("&", "?", ""));
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    /**
     * Creates a new builder for constructing a VastAIRequest.
     * @return a new Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for constructing VastAIRequest instances.
     */
    public static final class Builder {
        private HttpMethod method = HttpMethod.GET;
        private String path;
        private final Map<String, String> queryParams = new LinkedHashMap<>();
        private Object body;
        private final Map<String, String> headers = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Sets the HTTP method for the request.
         * @param method the HTTP method
         * @return the builder instance
         */
        public Builder method(HttpMethod method) {
            this.method = Objects.requireNonNull(method, "method");
            return this;
        }

        /**
         * Shortcut for setting the method to GET.
         * @return the builder instance
         */
        public Builder get() {
            return method(HttpMethod.GET);
        }

        /**
         * Shortcut for setting the method to POST.
         * @return the builder instance
         */
        public Builder post() {
            return method(HttpMethod.POST);
        }

        /**
         * Shortcut for setting the method to PUT.
         * @return the builder instance
         */
        public Builder put() {
            return method(HttpMethod.PUT);
        }

        /**
         * Shortcut for setting the method to PATCH.
         * @return the builder instance
         */
        public Builder patch() {
            return method(HttpMethod.PATCH);
        }

        /**
         * Shortcut for setting the method to DELETE.
         * @return the builder instance
         */
        public Builder delete() {
            return method(HttpMethod.DELETE);
        }

        /**
         * Sets the path for the request.
         * @param path the request path
         * @return the builder instance
         */
        public Builder path(String path) {
            Objects.requireNonNull(path, "path");
            this.path = path.startsWith("/") ? path : "/" + path;
            return this;
        }

        /**
         * Adds a single query parameter to the request.
         * @param key the parameter name
         * @param value the parameter value
         * @return the builder instance
         */
        public Builder addQueryParam(String key, Object value) {
            Objects.requireNonNull(key, "key");
            if (value != null) {
                queryParams.put(key, String.valueOf(value));
            }
            return this;
        }

        /**
         * Adds multiple query parameters to the request.
         * @param params a map of parameter names and values
         * @return the builder instance
         */
        public Builder queryParams(Map<String, ?> params) {
            Objects.requireNonNull(params, "params");
            params.forEach(this::addQueryParam);
            return this;
        }

        /**
         * Adds a single header to the request.
         * @param key the header name
         * @param value the header value
         * @return the builder instance
         */
        public Builder header(String key, String value) {
            Objects.requireNonNull(key, "key");
            if (value != null) {
                headers.put(key, value);
            }
            return this;
        }

        /**
         * Adds multiple headers to the request.
         * @param headers a map of header names and values
         * @return the builder instance
         */
        public Builder headers(Map<String, String> headers) {
            Objects.requireNonNull(headers, "headers");
            headers.forEach(this::header);
            return this;
        }

        /**
         * Sets the body of the request.
         * @param body the request body
         * @return the builder instance
         */
        public Builder body(Object body) {
            this.body = body;
            return this;
        }

        /**
         * Builds the VastAIRequest instance.
         * @return the constructed VastAIRequest
         * @throws IllegalStateException if the path is not set
         */
        public VastAIRequest build() {
            if (path == null || path.isBlank()) {
                throw new IllegalStateException("A request path must be provided");
            }
            return new VastAIRequest(this);
        }
    }
}
