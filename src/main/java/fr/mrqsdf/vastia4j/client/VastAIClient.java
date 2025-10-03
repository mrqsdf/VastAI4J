package fr.mrqsdf.vastia4j.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import fr.mrqsdf.vastia4j.auth.ApiRights;
import fr.mrqsdf.vastia4j.auth.EndpointMethods;
import fr.mrqsdf.vastia4j.auth.Right;
import fr.mrqsdf.vastia4j.gson.ApiRightsAdapter;
import fr.mrqsdf.vastia4j.gson.EndpointMethodsAdapter;
import fr.mrqsdf.vastia4j.http.HttpMethod;
import fr.mrqsdf.vastia4j.http.VastAIException;
import fr.mrqsdf.vastia4j.http.VastAIRequest;
import fr.mrqsdf.vastia4j.http.VastAIResponse;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Core HTTP client for the Vast.ai REST API.
 */
public class VastAIClient {

    public static final String DEFAULT_BASE_URL = "https://console.vast.ai/api/v0";


    private final HttpClient httpClient;
    private final Gson gson;
    private final String apiKey;
    private final URI baseUri;

    /**
     * Create a new Vast.ai API client with the given API key and default settings.
     * @param apiKey the API key to use for authentication.
     */
    public VastAIClient(String apiKey) {
        this(apiKey, DEFAULT_BASE_URL, defaultHttpClient(), defaultGson());
    }

    /**
     * Create a new Vast.ai API client with the given API key and base URL.
     * @param apiKey the API key to use for authentication.
     * @param baseUrl the base URL of the Vast.ai API (e.g. {@code https://console.vast.ai/api/v0}).
     */
    public VastAIClient(String apiKey, String baseUrl) {
        this(apiKey, baseUrl, defaultHttpClient(), defaultGson());
    }

    /**
     * Create a new Vast.ai API client with the given settings.
     * @param apiKey the API key to use for authentication.
     * @param baseUrl the base URL of the Vast.ai API (e.g. {@code https://console.vast.ai/api/v0}).
     * @param httpClient the HTTP client to use for requests.
     * @param gson the Gson instance to use for JSON serialization/deserialization.
     */
    public VastAIClient(String apiKey, String baseUrl, HttpClient httpClient, Gson gson) {
        this.apiKey = Objects.requireNonNull(apiKey, "apiKey");
        this.baseUri = normalizeBaseUri(Objects.requireNonNull(baseUrl, "baseUrl"));

        this.httpClient = Objects.requireNonNull(httpClient, "httpClient");
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    /**
     * Normalize the base URL by ensuring it ends with a slash and converting it to a URI.
     * @param baseUrl the base URL to normalize.
     * @return the normalized URI.
     */
    private static URI normalizeBaseUri(String baseUrl) {
        String normalized = baseUrl.trim();
        if (!normalized.endsWith("/")) {
            normalized = normalized + "/";
        }
        return URI.create(normalized);
    }

    /**
     * Get the rights associated with the current API key.
     * @return the API rights.
     */
    public static HttpClient defaultHttpClient() {
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(20))
                .build();
    }

    /**
     * Create a default Gson instance with custom adapters for Vast.ai API models.
     * @return the default Gson instance.
     */
    public static Gson defaultGson() {
        return new GsonBuilder()
                .registerTypeAdapter(ApiRights.class, new ApiRightsAdapter())
                .registerTypeAdapter(EndpointMethods.class, new EndpointMethodsAdapter())
                .serializeNulls()
                .create();
    }

    /**
     * Get the Gson instance used by this client.
     * @return the Gson instance.
     */
    public Gson getGson() {
        return gson;
    }

    /**
     * Get the API key used by this client.
     * @return the API key.
     */
    public VastAIRequest.Builder requestBuilder() {
        return VastAIRequest.builder();
    }

    /**
     * Execute a Vast.ai API request and parse the response into the specified type.
     * @param request the Vast.ai request to execute.
     * @param responseType the class of the response type.
     * @param <T> the type of the response.
     * @return the parsed response.
     */
    public <T> T execute(VastAIRequest request, Class<T> responseType) {
        return execute(request, (Type) responseType);
    }

    /**
     * Execute a Vast.ai API request and parse the response into the specified type.
     * @param request the Vast.ai request to execute.
     * @param responseType the type of the response (can be a generic type).
     * @param <T> the type of the response.
     * @return the parsed response.
     */
    @SuppressWarnings("unchecked")
    public <T> T execute(VastAIRequest request, Type responseType) {
        HttpResponse<String> httpResponse = send(request);
        if (responseType == null) {
            return null;
        }
        if (responseType == String.class) {
            return (T) httpResponse.body();
        }
        String body = httpResponse.body();
        //System.out.println("DEBUG: Vast.ai response for " + request.getPath() + ": " + body);
        return gson.fromJson(body, responseType);
    }

    /**
     * Execute a Vast.ai API request and parse the response into a JsonElement.
     * @param request the Vast.ai request to execute.
     * @return the parsed response as a JsonElement.
     */
    public JsonElement executeJson(VastAIRequest request) {
        HttpResponse<String> httpResponse = send(request);
        return gson.fromJson(httpResponse.body(), JsonElement.class);
    }

    /**
     * Execute a Vast.ai API request and parse the response into a VastAIResponse with the specified type token.
     * This method checks the "success" field in the response and throws an exception if it's false.
     * @param request the Vast.ai request to execute.
     * @param typeToken the type token of the response type.
     * @param <T> the type of the response data.
     * @return the parsed VastAIResponse.
     */
    public <T> VastAIResponse<T> executeWrapped(VastAIRequest request, TypeToken<VastAIResponse<T>> typeToken) {
        HttpResponse<String> httpResponse = send(request);
        VastAIResponse<T> response = gson.fromJson(httpResponse.body(), typeToken.getType());
        if (!response.isSuccess()) {
            throw new VastAIException(Optional.ofNullable(response.getError()).orElse("Unknown Vast.ai API error"));
        }
        return response;
    }

    private HttpResponse<String> send(VastAIRequest request) {
        Objects.requireNonNull(request, "request");
        HttpRequest httpRequest = buildHttpRequest(request);
        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            System.err.println("[VastAI] " + request.getMethod() + " " + buildHttpRequest(request).uri());
            if (response.statusCode() >= 400) {
                throw new VastAIException("HTTP " + response.statusCode() + " - " + response.body());
            }
            return response;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new VastAIException("Vast.ai request interrupted", e);
        } catch (IOException e) {
            throw new VastAIException("Failed to execute Vast.ai request", e);
        }
    }

    private HttpRequest buildHttpRequest(VastAIRequest request) {
        String resolvedPath = resolvePath(request.getPath());
        Map<String, String> queryParams = new LinkedHashMap<>(request.getQueryParams());

        String query = buildQueryString(queryParams);
        URI uri = baseUri.resolve(resolvedPath + query);

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + apiKey)
                .uri(uri)
                .timeout(Duration.ofSeconds(30));

        request.getHeaders().forEach(builder::header);
        builder.header("Accept", "application/json");

        HttpMethod method = request.getMethod();
        String bodyJson = request.getBody() == null ? null : gson.toJson(request.getBody());
        switch (method) {
            case POST:
            case PUT:
            case PATCH:
                builder.header("Content-Type", "application/json");
                builder.method(method.name(), bodyJson == null
                        ? HttpRequest.BodyPublishers.noBody()
                        : HttpRequest.BodyPublishers.ofString(bodyJson, StandardCharsets.UTF_8));
                break;
            case DELETE:
                if (request.getBody() != null) {
                    builder.header("Content-Type", "application/json");
                    builder.method(method.name(), HttpRequest.BodyPublishers.ofString(bodyJson, StandardCharsets.UTF_8));
                } else {
                    builder.DELETE();
                }
                break;
            case GET:
            default:
                builder.GET();
        }

        return builder.build();
    }

    private static String buildQueryString(Map<String, String> params) {
        if (params.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder("?");
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!first) {
                builder.append('&');
            }
            first = false;
            builder.append(encode(entry.getKey()))
                    .append('=')
                    .append(encode(entry.getValue()));
        }
        return builder.toString();
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private String resolvePath(String path) {
        if (path == null || path.isEmpty()) {
            return "";
        }
        return path.startsWith("/") ? path.substring(1) : path;
    }
}
