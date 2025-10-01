package fr.mrqsdf.vastia4j.http;

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

    private VastAIRequest(Builder builder) {
        this.method = builder.method;
        this.path = builder.path;
        this.queryParams = Collections.unmodifiableMap(new LinkedHashMap<>(builder.queryParams));
        this.body = builder.body;
        this.headers = Collections.unmodifiableMap(new LinkedHashMap<>(builder.headers));
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public Object getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

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

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private HttpMethod method = HttpMethod.GET;
        private String path;
        private final Map<String, String> queryParams = new LinkedHashMap<>();
        private Object body;
        private final Map<String, String> headers = new LinkedHashMap<>();

        private Builder() {
        }

        public Builder method(HttpMethod method) {
            this.method = Objects.requireNonNull(method, "method");
            return this;
        }

        public Builder get() {
            return method(HttpMethod.GET);
        }

        public Builder post() {
            return method(HttpMethod.POST);
        }

        public Builder put() {
            return method(HttpMethod.PUT);
        }

        public Builder patch() {
            return method(HttpMethod.PATCH);
        }

        public Builder delete() {
            return method(HttpMethod.DELETE);
        }

        public Builder path(String path) {
            Objects.requireNonNull(path, "path");
            this.path = path.startsWith("/") ? path : "/" + path;
            return this;
        }

        public Builder addQueryParam(String key, Object value) {
            Objects.requireNonNull(key, "key");
            if (value != null) {
                queryParams.put(key, String.valueOf(value));
            }
            return this;
        }

        public Builder queryParams(Map<String, ?> params) {
            Objects.requireNonNull(params, "params");
            params.forEach(this::addQueryParam);
            return this;
        }

        public Builder header(String key, String value) {
            Objects.requireNonNull(key, "key");
            if (value != null) {
                headers.put(key, value);
            }
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            Objects.requireNonNull(headers, "headers");
            headers.forEach(this::header);
            return this;
        }

        public Builder body(Object body) {
            this.body = body;
            return this;
        }

        public VastAIRequest build() {
            if (path == null || path.isBlank()) {
                throw new IllegalStateException("A request path must be provided");
            }
            return new VastAIRequest(this);
        }
    }
}
