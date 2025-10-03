package fr.mrqsdf.vastia4j.auth;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;

import fr.mrqsdf.vastia4j.http.HttpMethod;

import java.util.*;

/**
 * Represents the authentication rights payload (e.g. returned by {@code /api/v0/auth/sessions/}).
 * A typical response resembles {@code {"rights": {...}, "sid": 1}}.
 */
public final class Right {

    @SerializedName("rights")
    private Rights rights;

    @SerializedName("sid")
    private long sid;

    public Rights getRights() {
        return rights;
    }

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

        public ApiRights getApi() {
            return api;
        }
    }

}
