package fr.mrqsdf.vastia4j.service;

import fr.mrqsdf.vastia4j.client.VastAIClient;
import fr.mrqsdf.vastia4j.http.VastAIRequest;
import fr.mrqsdf.vastia4j.model.instance.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Service responsible for managing Vast.ai instances.
 * <p>The implementation maps the high-level operations documented by Vast.ai:</p>
 * <ul>
 *     <li>{@code PUT /api/v0/asks/{offerId}/} to accept an ask and create an instance</li>
 *     <li>{@code PUT /api/v0/instances/{id}/} to change the target state or label</li>
 *     <li>{@code PUT /api/v0/instances/reboot/{id}/} to perform an in-place reboot</li>
 *     <li>{@code DELETE /api/v0/instances/{id}/} to destroy an instance</li>
 *     <li>{@code GET /api/v0/instances/{id}/} to retrieve instance details</li>
 * </ul>
 */
public final class InstanceService implements Service {

    private final VastAIClient client;

    public InstanceService(VastAIClient client) {
        this.client = Objects.requireNonNull(client, "client");
    }

    /**
     * Accepts an ask and creates a new instance.
     *
     * @param offerId identifier of the ask to accept
     * @param in      request payload describing the instance configuration
     * @return the response returned by Vast.ai after provisioning
     */
    public CreateInstanceResponse createInstance(long offerId, CreateInstanceRequest in) {
        com.google.gson.JsonObject body = new com.google.gson.JsonObject();

        // Enforce a version tag when a custom image is provided.
        if (in.getImage() != null) {
            String img = in.getImage();
            if (!img.contains(":")) {
                throw new IllegalArgumentException("Docker image must include a version tag, e.g. 'pytorch/pytorch:latest'.");
            }
            body.addProperty("image", img);
        }

        if (in.getTemplateId() != null) body.addProperty("template_id", in.getTemplateId());
        if (in.getTemplateHashId() != null) body.addProperty("template_hash_id", in.getTemplateHashId());

        // Vast.ai requires at least 8 GB of disk space.
        double disk = (in.getDisk() == null ? 10.0 : in.getDisk());
        if (disk < 8.0) throw new IllegalArgumentException("disk must be >= 8 GB");
        body.addProperty("disk", disk);

        if (in.getRuntype() != null) body.addProperty("runtype", in.getRuntype());
        if (in.getOnstart() != null) body.addProperty("onstart", in.getOnstart());
        if (in.getLabel() != null) body.addProperty("label", in.getLabel());
        if (in.getImageLogin() != null) body.addProperty("image_login", in.getImageLogin());
        if (in.getPrice() != null) body.addProperty("price", in.getPrice());
        if (in.getTargetState() != null) body.addProperty("target_state", in.getTargetState());
        if (in.getCancelUnavail() != null) body.addProperty("cancel_unavail", in.getCancelUnavail());
        if (in.getVm() != null) body.addProperty("vm", in.getVm());
        if (in.getClientId() != null) body.addProperty("client_id", in.getClientId());
        if (in.getApikeyId() != null) body.addProperty("apikey_id", in.getApikeyId());
        if (in.getEntrypoint() != null) body.addProperty("entrypoint", in.getEntrypoint());
        if (in.getUseSsh() != null) body.addProperty("use_ssh", in.getUseSsh());
        if (in.getPythonUtf8() != null) body.addProperty("python_utf8", in.getPythonUtf8());
        if (in.getLangUtf8() != null) body.addProperty("lang_utf8", in.getLangUtf8());
        if (in.getUseJupyterLab() != null) body.addProperty("use_jupyter_lab", in.getUseJupyterLab());
        if (in.getJupyterDir() != null) body.addProperty("jupyter_dir", in.getJupyterDir());
        if (in.getForce() != null) body.addProperty("force", in.getForce());
        if (in.getUser() != null) body.addProperty("user", in.getUser());

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
            if (vi.createNew != null) v.addProperty("create_new", vi.createNew);
            if (vi.volumeId != null) v.addProperty("volume_id", vi.volumeId);
            if (vi.size != null) v.addProperty("size", vi.size);
            if (vi.mountPath != null) v.addProperty("mount_path", vi.mountPath);
            if (vi.name != null) v.addProperty("name", vi.name);
            body.add("volume_info", v);
        }

        VastAIRequest req = client.requestBuilder()
                .put()
                .path("/asks/" + offerId + "/")
                .body(body)
                .build();

        return client.execute(req, CreateInstanceResponse.class);
    }

    /**
     * Transitions an instance to the {@code running} target state.
     */
    public void start(long instanceId) {
        changeState(instanceId, "running");
    }

    /**
     * Transitions an instance to the {@code stopped} target state.
     */
    public void stop(long instanceId) {
        changeState(instanceId, "stopped");
    }

    /**
     * Sets or replaces the server-side label associated with an instance.
     */
    public void label(long instanceId, String label) {
        VastAIRequest req = client.requestBuilder()
                .put()
                .path("/instances/" + instanceId + "/")
                .body(new StateOrLabel(null, label))
                .build();
        client.execute(req, String.class);
    }

    /**
     * Performs an in-place reboot which maintains the GPU queue priority.
     */
    public void reboot(long instanceId) {
        VastAIRequest req = client.requestBuilder()
                .put()
                .path("/instances/reboot/" + instanceId + "/")
                .build();
        client.execute(req, String.class);
    }

    /**
     * Permanently destroys an instance.
     */
    public void destroy(long instanceId) {
        VastAIRequest req = client.requestBuilder()
                .delete()
                .path("/instances/" + instanceId + "/")
                .build();
        client.execute(req, String.class);
    }

    /**
     * Retrieves the details of an instance including SSH, runtype and template metadata.
     */
    public InstanceDetails show(long instanceId) {
        VastAIRequest req = client.requestBuilder()
                .get()
                .path("/instances/" + instanceId + "/")
                .build();
        return client.execute(req, InstanceDetails.class);
    }

    // --- private helpers ---

    private void changeState(long instanceId, String state) {
        VastAIRequest req = client.requestBuilder()
                .put()
                .path("/instances/" + instanceId + "/")
                .body(new StateOrLabel(state, null))
                .build();
        client.execute(req, String.class);
    }

    public List<InstanceSummary> list() {
        VastAIRequest req = client.requestBuilder()
                .get()
                .path("/instances/")
                .build();
        InstancesResponse resp = client.execute(req, InstancesResponse.class);
        return (resp == null || resp.instances() == null)
                ? Collections.emptyList()
                : resp.instances();
    }

    /**
     * Filters the locally retrieved instance list by current state.
     */
    public List<InstanceSummary> listByState(String stateLowercase) {
        List<InstanceSummary> all = list();
        if (stateLowercase == null || stateLowercase.isBlank()) return all;
        String s = stateLowercase.trim().toLowerCase();
        return all.stream()
                .filter(i -> i.curState() != null && i.curState().equalsIgnoreCase(s))
                .collect(Collectors.toList());
    }

    @Override
    public void update() {

    }

    /**
     * Simple DTO used when toggling the state or label of an instance.
     */
    private record StateOrLabel(String state, String label) {
    }
}
