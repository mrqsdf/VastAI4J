package fr.mrqsdf.vastia4j.service;

import fr.mrqsdf.vastia4j.client.VastAIClient;
import fr.mrqsdf.vastia4j.http.VastAIRequest;
import fr.mrqsdf.vastia4j.model.instance.CreateInstanceRequest;
import fr.mrqsdf.vastia4j.model.instance.CreateInstanceResponse;
import fr.mrqsdf.vastia4j.model.instance.InstanceDetails;

import java.util.Objects;

/**
 * Service pour créer/gérer des instances Vast.ai
 * Endpoints :
 *  - PUT  /api/v0/asks/{offerId}/                 (create)
 *  - PUT  /api/v0/instances/{id}/                 (start/stop/label)
 *  - PUT  /api/v0/instances/reboot/{id}/          (reboot)
 *  - DELETE /api/v0/instances/{id}/               (destroy)
 *  - GET  /api/v0/instances/{id}/                 (show)
 */
public final class InstanceService implements Service {

    private final VastAIClient client;

    public InstanceService(VastAIClient client) {
        this.client = Objects.requireNonNull(client, "client");
    }

    /** Crée une instance en acceptant l’offre (ask) {offerId}. */
    public CreateInstanceResponse createInstance(long offerId, CreateInstanceRequest in) {
        com.google.gson.JsonObject body = new com.google.gson.JsonObject();

        // Exiger un tag d'image si image est fournie
        if (in.getImage() != null) {
            String img = in.getImage();
            if (!img.contains(":")) {
                throw new IllegalArgumentException("Docker image must include a version tag, e.g. 'pytorch/pytorch:latest'.");
            }
            body.addProperty("image", img);
        }

        if (in.getTemplateId() != null)        body.addProperty("template_id", in.getTemplateId());
        if (in.getTemplateHashId() != null)    body.addProperty("template_hash_id", in.getTemplateHashId());

        // disk >= 8
        double disk = (in.getDisk() == null ? 10.0 : in.getDisk());
        if (disk < 8.0) throw new IllegalArgumentException("disk must be >= 8 GB");
        body.addProperty("disk", disk);

        if (in.getRuntype() != null)           body.addProperty("runtype", in.getRuntype()); // "ssh" | "jupyter" | "args" | ...
        if (in.getOnstart() != null)           body.addProperty("onstart", in.getOnstart());
        if (in.getLabel() != null)             body.addProperty("label", in.getLabel());
        if (in.getImageLogin() != null)        body.addProperty("image_login", in.getImageLogin());
        if (in.getPrice() != null)             body.addProperty("price", in.getPrice());
        if (in.getTargetState() != null)       body.addProperty("target_state", in.getTargetState());
        if (in.getCancelUnavail() != null)     body.addProperty("cancel_unavail", in.getCancelUnavail());
        if (in.getVm() != null)                body.addProperty("vm", in.getVm());
        if (in.getClientId() != null)          body.addProperty("client_id", in.getClientId());
        if (in.getApikeyId() != null)          body.addProperty("apikey_id", in.getApikeyId());
        if (in.getEntrypoint() != null)        body.addProperty("entrypoint", in.getEntrypoint());
        if (in.getUseSsh() != null)            body.addProperty("use_ssh", in.getUseSsh());
        if (in.getPythonUtf8() != null)        body.addProperty("python_utf8", in.getPythonUtf8());
        if (in.getLangUtf8() != null)          body.addProperty("lang_utf8", in.getLangUtf8());
        if (in.getUseJupyterLab() != null)     body.addProperty("use_jupyter_lab", in.getUseJupyterLab());
        if (in.getJupyterDir() != null)        body.addProperty("jupyter_dir", in.getJupyterDir());
        if (in.getForce() != null)             body.addProperty("force", in.getForce());
        if (in.getUser() != null)              body.addProperty("user", in.getUser());

        if (in.getArgs() != null && !in.getArgs().isEmpty()) {
            com.google.gson.JsonArray arr = new com.google.gson.JsonArray();
            for (String a : in.getArgs()) arr.add(a);
            body.add("args", arr);
        }
        if (in.getEnv() != null && !in.getEnv().isEmpty()) {
            body.add("env", new com.google.gson.Gson().toJsonTree(in.getEnv()).getAsJsonObject());
        }
        if (in.getVolumeInfo() != null) {
            com.google.gson.JsonObject v = new com.google.gson.JsonObject();
            var vi = in.getVolumeInfo();
            if (vi.createNew != null)  v.addProperty("create_new", vi.createNew);
            if (vi.volumeId != null)   v.addProperty("volume_id", vi.volumeId);
            if (vi.size != null)       v.addProperty("size", vi.size);
            if (vi.mountPath != null)  v.addProperty("mount_path", vi.mountPath);
            if (vi.name != null)       v.addProperty("name", vi.name);
            body.add("volume_info", v);
        }

        VastAIRequest req = client.requestBuilder()
                .put()
                .path("/asks/" + offerId + "/")
                .body(body)  // <-- JSON « propre » sans nulls
                .build();

        return client.execute(req, CreateInstanceResponse.class);
    }

    /** Met l’instance à l’état "running". */
    public void start(long instanceId) {
        changeState(instanceId, "running");
    }

    /** Met l’instance à l’état "stopped". */
    public void stop(long instanceId) {
        changeState(instanceId, "stopped");
    }

    /** Ajoute/écrase un label côté serveur. */
    public void label(long instanceId, String label) {
        VastAIRequest req = client.requestBuilder()
                .put()
                .path("/instances/" + instanceId + "/")
                .body(new StateOrLabel(null, label))
                .build();
        client.execute(req, String.class); // pas besoin de retour typé
    }

    /** Reboot (stop/start) sans perdre la priorité GPU. */
    public void reboot(long instanceId) {
        VastAIRequest req = client.requestBuilder()
                .put()
                .path("/instances/reboot/" + instanceId + "/")
                .build();
        client.execute(req, String.class);
    }

    /** Destruction définitive. */
    public void destroy(long instanceId) {
        VastAIRequest req = client.requestBuilder()
                .delete()
                .path("/instances/" + instanceId + "/")
                .build();
        client.execute(req, String.class);
    }

    /** Détails d’une instance (ssh/jupyter, status, template, etc.). */
    public InstanceDetails show(long instanceId) {
        VastAIRequest req = client.requestBuilder()
                .get()
                .path("/instances/" + instanceId + "/")
                .build();
        return client.execute(req, InstanceDetails.class);
    }

    // --- helpers privés ---

    private void changeState(long instanceId, String state) {
        VastAIRequest req = client.requestBuilder()
                .put()
                .path("/instances/" + instanceId + "/")
                .body(new StateOrLabel(state, null))
                .build();
        client.execute(req, String.class);
    }

    @Override
    public void update() {

    }

    /** petit DTO interne pour PUT /instances/{id}/ */
    private record StateOrLabel(String state, String label) {}
}
