package fr.mrqsdf.vastia4j.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Builder object representing the payload required to launch a Vast.ai instance.
 */
public class InstanceLaunchRequest {

    private final Map<String, Object> payload = new LinkedHashMap<>();

    private InstanceLaunchRequest() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public Map<String, Object> toPayload() {
        return new LinkedHashMap<>(payload);
    }

    private InstanceLaunchRequest(Builder builder) {
        this.payload.putAll(builder.payload);
    }

    public static final class Builder {
        private final Map<String, Object> payload = new LinkedHashMap<>();

        private Builder() {
        }

        public Builder machineId(long machineId) {
            payload.put("machine_id", machineId);
            return this;
        }

        public Builder image(String image) {
            payload.put("image", image);
            return this;
        }

        public Builder disk(Integer disk) {
            payload.put("disk", disk);
            return this;
        }

        public Builder env(String env) {
            payload.put("env", env);
            return this;
        }

        public Builder label(String label) {
            payload.put("label", label);
            return this;
        }

        public Builder onstart(String onstart) {
            payload.put("onstart", onstart);
            return this;
        }

        public Builder onstartTimeout(Integer timeoutSeconds) {
            payload.put("onstart_timeout", timeoutSeconds);
            return this;
        }

        public Builder sshKey(String sshKey) {
            payload.put("ssh_key", sshKey);
            return this;
        }

        public Builder jupyterPassword(String password) {
            payload.put("jupyter_password", password);
            return this;
        }

        public Builder with(String key, Object value) {
            Objects.requireNonNull(key, "key");
            if (value == null) {
                payload.remove(key);
            } else {
                payload.put(key, value);
            }
            return this;
        }

        public InstanceLaunchRequest build() {
            if (!payload.containsKey("machine_id")) {
                throw new IllegalStateException("machine_id is required to launch an instance");
            }
            if (!payload.containsKey("image")) {
                throw new IllegalStateException("image is required to launch an instance");
            }
            return new InstanceLaunchRequest(this);
        }
    }
}
