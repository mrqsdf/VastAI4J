package fr.mrqsdf.vastia4j.model.instance;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * Request payload for {@code PUT /api/v0/asks/{id}/} when creating an instance.
 * Every field is optional apart from the minimal requirements enforced by Vast.ai (for example,
 * the disk size must be at least 8 GB). The fields align with the "Create instance" documentation
 * which describes {@code template_id}, {@code template_hash_id}, {@code image}, {@code disk},
 * {@code env}, {@code runtype}, {@code onstart}, {@code label}, {@code price},
 * {@code target_state}, and more.
 */
public final class CreateInstanceRequest {
    // --- Template or image ---
    @SerializedName("template_id")
    private Long templateId;
    @SerializedName("template_hash_id")
    private String templateHashId;
    @SerializedName("image")
    private String image;

    // --- Runtime configuration ---
    @SerializedName("disk")
    private Double disk;
    @SerializedName("env")
    private Map<String, Object> env;
    @SerializedName("runtype")
    private String runtype;
    @SerializedName("onstart")
    private String onstart;
    @SerializedName("label")
    private String label;
    @SerializedName("image_login")
    private String imageLogin;
    @SerializedName("price")
    private Double price;
    @SerializedName("target_state")
    private String targetState;
    @SerializedName("cancel_unavail")
    private Boolean cancelUnavail;
    @SerializedName("vm")
    private Boolean vm;
    @SerializedName("client_id")
    private String clientId;
    @SerializedName("apikey_id")
    private String apikeyId;
    @SerializedName("args")
    private List<String> args;
    @SerializedName("entrypoint")
    private String entrypoint;
    @SerializedName("use_ssh")
    private Boolean useSsh;
    @SerializedName("python_utf8")
    private Boolean pythonUtf8;
    @SerializedName("lang_utf8")
    private Boolean langUtf8;
    @SerializedName("use_jupyter_lab")
    private Boolean useJupyterLab;
    @SerializedName("jupyter_dir")
    private String jupyterDir;
    @SerializedName("force")
    private Boolean force;
    @SerializedName("user")
    private String user;

    // --- Optional volume section ---
    @SerializedName("volume_info")
    private VolumeInfo volumeInfo;

    // --- Getters and fluent builder helpers ---

    /**
     * @return the template ID
     */
    public Long getTemplateId() {
        return templateId;
    }

    /**
     * @return the template hash ID
     */
    public String getTemplateHashId() {
        return templateHashId;
    }

    /**
     * @return the image UUID or custom image hash ID
     */
    public String getImage() {
        return image;
    }

    /**
     * @return the disk size in GB
     */
    public Double getDisk() {
        return disk;
    }

    /**
     * @return the environment variables to set inside the instance (null if not set)
     */
    public Map<String, Object> getEnv() {
        return env;
    }

    /**
     * @return the runtime type, either {@code docker} or {@code singularity}
     */
    public String getRuntype() {
        return runtype;
    }

    /**
     * @return the command to run when the instance starts (null if not set)
     */
    public String getOnstart() {
        return onstart;
    }

    /**
     * @return the label for the instance (null if not set)
     */
    public String getLabel() {
        return label;
    }

    /**
     * @return the login username for the image (null if not set)
     */
    public String getImageLogin() {
        return imageLogin;
    }

    /**
     * @return the maximum hourly price in USD (null if not set)
     */
    public Double getPrice() {
        return price;
    }

    /**
     * @return the desired target state of the instance (null if not set)
     */
    public String getTargetState() {
        return targetState;
    }

    /**
     * @return whether to cancel the instance creation if no suitable host is found (null if not set)
     */
    public Boolean getCancelUnavail() {
        return cancelUnavail;
    }

    /**
     * @return whether to use a virtual machine (true) or a container (false)
     */
    public Boolean getVm() {
        return vm;
    }

    /**
     * @return the client ID to associate with the instance (null if not set)
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * @return the API key ID to associate with the instance (null if not set)
     */
    public String getApikeyId() {
        return apikeyId;
    }

    /**
     * @return the command-line arguments to pass to the instance (null if not set)
     */
    public List<String> getArgs() {
        return args;
    }

    /**
     * @return the entrypoint command to run inside the instance (null if not set)
     */
    public String getEntrypoint() {
        return entrypoint;
    }

    /**
     * @return whether to enable SSH access (null if not set)
     */
    public Boolean getUseSsh() {
        return useSsh;
    }

    /**
     * @return whether to set the {@code PYTHONUTF8} environment variable to 1 (null if not set)
     */
    public Boolean getPythonUtf8() {
        return pythonUtf8;
    }

    /**
     * @return whether to set the {@code LANG} environment variable to a UTF-8 locale (null if not set)
     */
    public Boolean getLangUtf8() {
        return langUtf8;
    }

    /**
     * @return whether to use JupyterLab instead of classic Jupyter (null if not set)
     */
    public Boolean getUseJupyterLab() {
        return useJupyterLab;
    }

    /**
     * @return the working directory for Jupyter (null if not set)
     */
    public String getJupyterDir() {
        return jupyterDir;
    }

    /**
     * @return whether to force creation even if the image is known to be broken (null if not set)
     */
    public Boolean getForce() {
        return force;
    }

    /**
     * @return the username to use for SSH or Jupyter access (null if not set)
     */
    public String getUser() {
        return user;
    }

    /**
     * @return the volume information (null if not set)
     */
    public VolumeInfo getVolumeInfo() {
        return volumeInfo;
    }

    /**
     * @param v the template ID (for example, {@code 12345})
     * @return the updated CreateInstanceRequest instance
     */
    public CreateInstanceRequest templateId(Long v) {
        this.templateId = v;
        return this;
    }

    /**
     * @param v the template hash ID (for example, {@code tpl_1234567890abcdef})
     * @return the updated CreateInstanceRequest instance
     */
    public CreateInstanceRequest templateHashId(String v) {
        this.templateHashId = v;
        return this;
    }

    /**
     * @param v the image UUID (for example, {@code ubuntu-20.04-x64} or
     *          {@code windows-server-2019-standard-x64}) or a custom image hash ID
     *          (for example, {@code img_1234567890abcdef})
     * @return the updated CreateInstanceRequest instance
     */
    public CreateInstanceRequest image(String v) {
        this.image = v;
        return this;
    }

    /**
     * @param v the disk size in GB (minimum 8)
     * @return the updated CreateInstanceRequest instance
     */
    public CreateInstanceRequest disk(Double v) {
        this.disk = v;
        return this;
    }

    /**
     * @param v the environment variables to set inside the instance (default: null, meaning no extra
     *          environment variables are set)
     * @return the updated CreateInstanceRequest instance
     */
    public CreateInstanceRequest env(Map<String, Object> v) {
        this.env = v;
        return this;
    }

    /**
     * @param v the runtime type, either {@code docker} (default) or {@code singularity}
     * @return the updated CreateInstanceRequest instance
     */
    public CreateInstanceRequest runtype(String v) {
        this.runtype = v;
        return this;
    }

    /**
     * @param v the command to run when the instance starts (default: null, meaning no command is run)
     * @return the updated CreateInstanceRequest instance
     */
    public CreateInstanceRequest onstart(String v) {
        this.onstart = v;
        return this;
    }

    /**
     * @param v the label for the instance (default: null, meaning no label is set)
     * @return the updated CreateInstanceRequest instance
     */
    public CreateInstanceRequest label(String v) {
        this.label = v;
        return this;
    }

    /**
     * @param v the login username for the image (default: null, meaning the default username for the
     *          image is used, typically {@code root} for Linux images and {@code Administrator} for
     *          Windows images)
     * @return the updated CreateInstanceRequest instance
     */
    public CreateInstanceRequest imageLogin(String v) {
        this.imageLogin = v;
        return this;
    }

    /**
     * @param v the maximum hourly price in USD (default: null, meaning the user's default max price)
     * @return the updated CreateInstanceRequest instance
     */
    public CreateInstanceRequest price(Double v) {
        this.price = v;
        return this;
    }

    /**
     * @param v the desired target state of the instance (default: "running")
     * @return the updated CreateInstanceRequest instance
     */
    public CreateInstanceRequest targetState(String v) {
        this.targetState = v;
        return this;
    }

    /**
     * @param v whether to cancel the instance creation if no suitable host is found (default: false,
     *          meaning the request will remain pending until a suitable host becomes available)
     * @return the updated CreateInstanceRequest instance
     */
    public CreateInstanceRequest cancelUnavail(Boolean v) {
        this.cancelUnavail = v;
        return this;
    }

    /**
     * @param v whether to use a virtual machine (true) or a container (false, default)
     * @return the updated CreateInstanceRequest instance
     */
    public CreateInstanceRequest vm(Boolean v) {
        this.vm = v;
        return this;
    }

    /**
     * @param v the client ID to associate with the instance (default: null, meaning the instance is
     *          associated with the user's main account)
     * @return the updated CreateInstanceRequest instance
     */
    public CreateInstanceRequest clientId(String v) {
        this.clientId = v;
        return this;
    }

    /**
     * @param v the API key ID to associate with the instance (default: null, meaning the instance is
     *          associated with the user's main account)
     * @return the updated CreateInstanceRequest instance
     */
    public CreateInstanceRequest apikeyId(String v) {
        this.apikeyId = v;
        return this;
    }

    /**
     * @param v the command-line arguments to pass to the instance (default: null, meaning the image's
     *          default command is used)
     * @return the updated CreateInstanceRequest instance
     */
    public CreateInstanceRequest args(List<String> v) {
        this.args = v;
        return this;
    }

    /**
     * @param v the entrypoint command to run inside the instance (default: null, meaning the image's
     *          default entrypoint is used)
     * @return the updated CreateInstanceRequest instance
     */
    public CreateInstanceRequest entrypoint(String v) {
        this.entrypoint = v;
        return this;
    }

    /**
     * @param v whether to enable SSH access (default: false)
     * @return the updated CreateInstanceRequest instance
     */
    public CreateInstanceRequest useSsh(Boolean v) {
        this.useSsh = v;
        return this;
    }

    /**
     * @param v whether to set the {@code PYTHONUTF8} environment variable to 1 (default: false)
     * @return the updated CreateInstanceRequest instance
     */
    public CreateInstanceRequest pythonUtf8(Boolean v) {
        this.pythonUtf8 = v;
        return this;
    }

    /**
     * @param v whether to set the {@code LANG} environment variable to a UTF-8 locale (default: false)
     * @return the updated CreateInstanceRequest instance
     */

    public CreateInstanceRequest langUtf8(Boolean v) {
        this.langUtf8 = v;
        return this;
    }

    /**
     * @param v whether to use JupyterLab instead of classic Jupyter (default: false)
     * @return the updated CreateInstanceRequest instance
     */
    public CreateInstanceRequest useJupyterLab(Boolean v) {
        this.useJupyterLab = v;
        return this;
    }

    /**
     * @param v the working directory for Jupyter (default: {@code /home/username}, where
     *          {@code username} is {@code root} for Linux images and {@code Administrator} for
     *          Windows images)
     * @return the updated CreateInstanceRequest instance
     */
    public CreateInstanceRequest jupyterDir(String v) {
        this.jupyterDir = v;
        return this;
    }

    /**
     * @param v whether to force creation even if the image is known to be broken (default: false)
     * @return the updated CreateInstanceRequest instance
     */
    public CreateInstanceRequest force(Boolean v) {
        this.force = v;
        return this;
    }

    /**
     * @param v the username to use for SSH or Jupyter access (default: {@code root} for Linux images,
     *          {@code Administrator} for Windows images)
     * @return the updated CreateInstanceRequest instance
     */
    public CreateInstanceRequest user(String v) {
        this.user = v;
        return this;
    }

    /**
     * @param v the volume information (null to omit)
     * @return the updated CreateInstanceRequest instance
     */
    public CreateInstanceRequest volumeInfo(VolumeInfo v) {
        this.volumeInfo = v;
        return this;
    }

    /**
     * Fluent helper to set the {@code runtype} field using a {@link RunType} enum value.
     *
     * @param rt the RunType enum value
     * @return the updated CreateInstanceRequest instance
     */
    public CreateInstanceRequest runtypeEnum(RunType rt) {
        this.runtype = rt.json();
        return this;
    }


    /**
     * Optional nested structure describing volume attachments.
     */
    public static final class VolumeInfo {
        @SerializedName("create_new")
        public Boolean createNew;
        @SerializedName("volume_id")
        public Long volumeId;
        @SerializedName("size")
        public Integer size;
        @SerializedName("mount_path")
        public String mountPath;
        @SerializedName("name")
        public String name;

        /**
         * @param v whether to create a new volume (true) or attach an existing one (false)
         * @return the updated VolumeInfo instance
         */
        public VolumeInfo createNew(Boolean v) {
            this.createNew = v;
            return this;
        }

        /**
         * @param v the ID of an existing volume to attach (required if not creating a new volume)
         * @return the updated VolumeInfo instance
         */
        public VolumeInfo volumeId(Long v) {
            this.volumeId = v;
            return this;
        }

        /**
         * @param v the size of the volume in GB (required if creating a new volume)
         * @return the updated VolumeInfo instance
         */
        public VolumeInfo size(Integer v) {
            this.size = v;
            return this;
        }

        /**
         * @param v the path where the volume should be mounted inside the instance (e.g., {@code /data}).
         * @return the updated VolumeInfo instance
         */
        public VolumeInfo mountPath(String v) {
            this.mountPath = v;
            return this;
        }

        /**
         * @param v the name of the volume (required if creating a new volume)
         * @return the updated VolumeInfo instance
         */
        public VolumeInfo name(String v) {
            this.name = v;
            return this;
        }

    }
}
