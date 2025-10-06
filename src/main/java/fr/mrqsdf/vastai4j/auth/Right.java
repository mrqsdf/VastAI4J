package fr.mrqsdf.vastai4j.auth;

import com.google.gson.annotations.SerializedName;

/**
 * Represents the authentication rights payload (e.g. returned by {@code /api/v0/auth/sessions/}).
 * A typical response resembles {@code {"rights": {...}, "sid": 1}}.
 */
public final class Right {

    @SerializedName("rights")
    private Rights rights;

    @SerializedName("sid")
    private long sid;

    /**
     * The HTTP methods allowed for a given endpoint.
     * @return the set of allowed HTTP methods.
     */
    public Rights getRights() {
        return rights;
    }

    /**
     * The session ID associated with these rights.
     * @return the session ID.
     */
    public long getSid() {
        return sid;
    }

    // ---------- Nested model ----------

    /**
     * Root "rights" node which typically exposes at least the {@code api} section.
     */
    public static final class Rights {
        @SerializedName("api")
        private ApiRights api;

        /**
         * The API rights section.
         * @return the API rights.
         */
        public ApiRights getApi() {
            return api;
        }
    }

}
