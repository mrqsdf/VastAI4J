package fr.mrqsdf.vastia4j.auth;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;

import fr.mrqsdf.vastia4j.http.HttpMethod;

import java.util.*;

/**
 * Représente la réponse qui contient les droits (ex: depuis /api/v0/auth/sessions/ ou similaire).
 * Exemple vu : {"rights": { ... }, "sid": 1}
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

    // ---------- Modèle imbriqué ----------

    /**
     * Le nœud "rights" racine.
     * On y trouve au moins: "api": { ... }
     */
    public static final class Rights {
        @SerializedName("api")
        private ApiRights api;

        public ApiRights getApi() {
            return api;
        }
    }

}
