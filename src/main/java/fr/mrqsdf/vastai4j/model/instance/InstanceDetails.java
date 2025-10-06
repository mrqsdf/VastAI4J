package fr.mrqsdf.vastai4j.model.instance;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import java.util.*;

/**
 * Modèle robuste pour GET /api/v0/instances/{id}/
 * - Les champs polymorphes (image_args, extra_env, local_ipaddrs) sont JsonElement / List
 * - Ajoute plusieurs champs usuels visibles dans la réponse (GPU/CPU/Disk/Costs…).
 * - Gson tolère l'absence/présence de champs supplémentaires (ils restent null).
 */
public record InstanceDetails(
        @SerializedName("instances") InstancePayload instances
) {
    public record InstancePayload(
            // Identité / états
            Long id,
            @SerializedName("actual_status") String actualStatus,
            @SerializedName("intended_status") String intendedStatus,
            @SerializedName("cur_state") String curState,
            @SerializedName("next_state") String nextState,
            @SerializedName("verification") String verification,

            // Connexion / IP / ports
            @SerializedName("ssh_idx") String sshIdx,
            @SerializedName("ssh_host") String sshHost,
            @SerializedName("ssh_port") Integer sshPort,
            @SerializedName("public_ipaddr") String publicIpaddr,
            @SerializedName("local_ipaddrs") JsonElement localIpaddrs,
            @SerializedName("machine_dir_ssh_port") Integer machineDirSshPort,
            @SerializedName("ports") Map<String, List<Ports>> ports,
            @SerializedName("direct_port_start") Integer directPortStart,
            @SerializedName("direct_port_end") Integer directPortEnd,

            // Image / Template
            @SerializedName("template_id") Long templateId,
            @SerializedName("template_hash_id") String templateHashId,
            @SerializedName("template_name") String templateName,
            @SerializedName("image_uuid") String imageUuid,
            @SerializedName("image_runtype") String imageRuntype,
            // ⚠️ polymorphe: String OU Array<String>
            @SerializedName("image_args") JsonElement imageArgs,
            // ⚠️ polymorphe: Array<String> OU Array<Array<?,?>>
            @SerializedName("extra_env") java.util.List<JsonElement> extraEnv,
            @SerializedName("onstart") String onstart,

            // UI / accès
            String label,
            @SerializedName("jupyter_token") String jupyterToken,
            @SerializedName("status_msg") String statusMsg,
            @SerializedName("webpage") String webpage,

            // Monitoring / GPU
            @SerializedName("gpu_name") String gpuName,
            @SerializedName("num_gpus") Integer numGpus,
            @SerializedName("gpu_ram") Integer gpuRam,
            @SerializedName("gpu_totalram") Integer gpuTotalram,
            @SerializedName("gpu_frac") Double gpuFrac,
            @SerializedName("gpu_util") Double gpuUtil,
            @SerializedName("gpu_temp") Double gpuTemp,
            @SerializedName("gpu_arch") String gpuArch,
            @SerializedName("cuda_max_good") Double cudaMaxGood,
            @SerializedName("driver_version") String driverVersion,
            @SerializedName("gpu_display_active") Boolean gpuDisplayActive,

            // CPU / réseau / disque
            @SerializedName("cpu_name") String cpuName,
            @SerializedName("cpu_arch") String cpuArch,
            @SerializedName("cpu_cores") Integer cpuCores,
            @SerializedName("cpu_cores_effective") Double cpuCoresEffective,
            @SerializedName("cpu_util") Double cpuUtil,
            @SerializedName("cpu_ram") Long cpuRam,
            @SerializedName("inet_up") Double inetUp,
            @SerializedName("inet_down") Double inetDown,
            @SerializedName("disk_name") String diskName,
            @SerializedName("disk_space") Double diskSpace,
            @SerializedName("disk_usage") Double diskUsage,
            @SerializedName("disk_util") Double diskUtil,
            @SerializedName("disk_bw") Double diskBw,

            // Pricing / coûts (extraits fréquents)
            @SerializedName("dph_base") Double dphBase,
            @SerializedName("dph_total") Double dphTotal,
            @SerializedName("storage_cost") Double storageCost,
            @SerializedName("storage_total_cost") Double storageTotalCost,
            @SerializedName("internet_up_cost_per_tb") Double internetUpCostPerTb,
            @SerializedName("internet_down_cost_per_tb") Double internetDownCostPerTb,
            @SerializedName("inet_up_cost") Double inetUpCost,
            @SerializedName("inet_down_cost") Double inetDownCost,

            // Divers / métadonnées
            @SerializedName("host_id") Long hostId,
            @SerializedName("machine_id") Long machineId,
            @SerializedName("os_version") String osVersion,
            @SerializedName("geolocation") String geolocation,
            @SerializedName("logo") String logo,
            @SerializedName("start_date") Double startDate,
            @SerializedName("end_date") Double endDate,
            @SerializedName("duration") Double duration,
            @SerializedName("uptime_mins") Long uptimeMins,
            @SerializedName("static_ip") Boolean staticIp,
            @SerializedName("external") Boolean external,
            @SerializedName("rentable") Boolean rentable,
            @SerializedName("time_remaining") String timeRemaining,

            Search search
    ) {}

    // ---------- HELPERS DE NORMALISATION ----------

    /** image_args -> List (accepte String ou Array) */
    public static java.util.List<String> imageArgsAsList(JsonElement imageArgs) {
        if (imageArgs == null || imageArgs.isJsonNull()) return java.util.List.of();
        if (imageArgs.isJsonPrimitive()) {
            String s = imageArgs.getAsString().trim();
            if (s.isEmpty()) return java.util.List.of();
            // découpe naïve par espaces (adapte si tu veux gérer les quotes)
            return java.util.Arrays.asList(s.split("\\s+"));
        }
        if (imageArgs.isJsonArray()) {
            java.util.List<String> out = new java.util.ArrayList<>();
            for (JsonElement e : imageArgs.getAsJsonArray()) {
                if (e != null && !e.isJsonNull()) out.add(e.getAsString());
            }
            return out;
        }
        return java.util.List.of(imageArgs.toString());
    }

    public static java.util.Map<String,String> extraEnvAsMap(java.util.List<JsonElement> raw) {
        java.util.Map<String,String> out = new java.util.LinkedHashMap<>();
        if (raw == null) return out;
        for (JsonElement el : raw) {
            if (el == null || el.isJsonNull()) continue;
            if (el.isJsonPrimitive()) {
                String s = el.getAsString();
                int i = s.indexOf('=');
                if (i > 0) out.put(s.substring(0,i), s.substring(i+1));
            } else if (el.isJsonArray()) {
                JsonArray arr = el.getAsJsonArray();
                if (arr.size() >= 2) {
                    String k = asString(arr.get(0));
                    String v = asString(arr.get(1));
                    out.put(k, v);
                }
            } else {
                String s = el.toString();
                int i = s.indexOf('=');
                if (i > 0) out.put(s.substring(0,i), s.substring(i+1));
            }
        }
        return out;
    }

    public static java.util.List<String> localIpaddrsAsList(JsonElement localIpaddrs) {
        if (localIpaddrs == null || localIpaddrs.isJsonNull()) return java.util.List.of();
        if (localIpaddrs.isJsonPrimitive()) {
            // la string peut contenir espaces et sauts de ligne
            String s = localIpaddrs.getAsString().trim();
            if (s.isEmpty()) return java.util.List.of();
            String[] parts = s.split("\\s+");
            java.util.List<String> out = new java.util.ArrayList<>();
            for (String p : parts) if (!p.isBlank()) out.add(p);
            return out;
        }
        if (localIpaddrs.isJsonArray()) {
            java.util.List<String> out = new java.util.ArrayList<>();
            for (JsonElement e : localIpaddrs.getAsJsonArray()) {
                if (e != null && !e.isJsonNull()) out.add(e.getAsString());
            }
            return out;
        }
        return java.util.List.of(localIpaddrs.toString());
    }

    private static String asString(JsonElement e) {
        if (e == null || e.isJsonNull()) return "";
        if (e.isJsonPrimitive()) return e.getAsString();
        return e.toString();
    }
}
