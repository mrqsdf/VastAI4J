package fr.mrqsdf.vastia4j.model.instance;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

/**
 * Body pour PUT /api/v0/asks/{id}/ (create instance).
 * Tous les champs sont optionnels sauf le minimum imposé par Vast (ex. disk >= 8).
 * Cf. doc: template_id/template_hash_id, image, disk, env, runtype, onstart, label, price, target_state, etc.
 * Sources: "create instance" + champs détaillés (runtype, args, entrypoint...).
 */
public final class CreateInstanceRequest {
    // --- Template ou image ---
    @SerializedName("template_id")       private Long templateId;         // ex: 89
    @SerializedName("template_hash_id")  private String templateHashId;   // ex: "abc123..."
    @SerializedName("image")             private String image;            // ex: "tensorflow/tensorflow:latest-gpu"

    // --- Ressources/exec ---
    @SerializedName("disk")              private Double disk;             // en GB (min 8)
    @SerializedName("env")               private Map<String, Object> env; // variables d'env et/ou "PORTAL_CONFIG"
    @SerializedName("runtype")           private String runtype;          // "ssh" | "jupyter" | "args" | ...
    @SerializedName("onstart")           private String onstart;          // script de démarrage
    @SerializedName("label")             private String label;            // label libre
    @SerializedName("image_login")       private String imageLogin;       // credentials registry
    @SerializedName("price")             private Double price;            // bid price/h pour interruptible
    @SerializedName("target_state")      private String targetState;      // "running" | "stopped"
    @SerializedName("cancel_unavail")    private Boolean cancelUnavail;   // annuler si démarrage impossible
    @SerializedName("vm")                private Boolean vm;              // VM mode
    @SerializedName("client_id")         private String clientId;         // ex "me"
    @SerializedName("apikey_id")         private String apikeyId;         // audit id
    @SerializedName("args")              private List<String> args;       // args passés à l’entrypoint
    @SerializedName("entrypoint")        private String entrypoint;       // ex: "bash"
    @SerializedName("use_ssh")           private Boolean useSsh;
    @SerializedName("python_utf8")       private Boolean pythonUtf8;
    @SerializedName("lang_utf8")         private Boolean langUtf8;
    @SerializedName("use_jupyter_lab")   private Boolean useJupyterLab;
    @SerializedName("jupyter_dir")       private String jupyterDir;
    @SerializedName("force")             private Boolean force;
    @SerializedName("user")              private String user;

    // --- Volumes (optionnel) ---
    @SerializedName("volume_info")       private VolumeInfo volumeInfo;

    // --- Getters + Builder fluide pour la commodité ---
    public Long getTemplateId() { return templateId; }
    public String getTemplateHashId() { return templateHashId; }
    public String getImage() { return image; }
    public Double getDisk() { return disk; }
    public Map<String, Object> getEnv() { return env; }
    public String getRuntype() { return runtype; }
    public String getOnstart() { return onstart; }
    public String getLabel() { return label; }
    public String getImageLogin() { return imageLogin; }
    public Double getPrice() { return price; }
    public String getTargetState() { return targetState; }
    public Boolean getCancelUnavail() { return cancelUnavail; }
    public Boolean getVm() { return vm; }
    public String getClientId() { return clientId; }
    public String getApikeyId() { return apikeyId; }
    public List<String> getArgs() { return args; }
    public String getEntrypoint() { return entrypoint; }
    public Boolean getUseSsh() { return useSsh; }
    public Boolean getPythonUtf8() { return pythonUtf8; }
    public Boolean getLangUtf8() { return langUtf8; }
    public Boolean getUseJupyterLab() { return useJupyterLab; }
    public String getJupyterDir() { return jupyterDir; }
    public Boolean getForce() { return force; }
    public String getUser() { return user; }
    public VolumeInfo getVolumeInfo() { return volumeInfo; }

    public CreateInstanceRequest templateId(Long v){ this.templateId=v; return this; }
    public CreateInstanceRequest templateHashId(String v){ this.templateHashId=v; return this; }
    public CreateInstanceRequest image(String v){ this.image=v; return this; }
    public CreateInstanceRequest disk(Double v){ this.disk=v; return this; }
    public CreateInstanceRequest env(Map<String,Object> v){ this.env=v; return this; }
    public CreateInstanceRequest runtype(String v){ this.runtype=v; return this; }
    public CreateInstanceRequest onstart(String v){ this.onstart=v; return this; }
    public CreateInstanceRequest label(String v){ this.label=v; return this; }
    public CreateInstanceRequest imageLogin(String v){ this.imageLogin=v; return this; }
    public CreateInstanceRequest price(Double v){ this.price=v; return this; }
    public CreateInstanceRequest targetState(String v){ this.targetState=v; return this; }
    public CreateInstanceRequest cancelUnavail(Boolean v){ this.cancelUnavail=v; return this; }
    public CreateInstanceRequest vm(Boolean v){ this.vm=v; return this; }
    public CreateInstanceRequest clientId(String v){ this.clientId=v; return this; }
    public CreateInstanceRequest apikeyId(String v){ this.apikeyId=v; return this; }
    public CreateInstanceRequest args(List<String> v){ this.args=v; return this; }
    public CreateInstanceRequest entrypoint(String v){ this.entrypoint=v; return this; }
    public CreateInstanceRequest useSsh(Boolean v){ this.useSsh=v; return this; }
    public CreateInstanceRequest pythonUtf8(Boolean v){ this.pythonUtf8=v; return this; }
    public CreateInstanceRequest langUtf8(Boolean v){ this.langUtf8=v; return this; }
    public CreateInstanceRequest useJupyterLab(Boolean v){ this.useJupyterLab=v; return this; }
    public CreateInstanceRequest jupyterDir(String v){ this.jupyterDir=v; return this; }
    public CreateInstanceRequest force(Boolean v){ this.force=v; return this; }
    public CreateInstanceRequest user(String v){ this.user=v; return this; }
    public CreateInstanceRequest volumeInfo(VolumeInfo v){ this.volumeInfo=v; return this; }

    public CreateInstanceRequest runtypeEnum(RunType rt){ this.runtype = rt.json(); return this; }


    /** Sous-volet volume optionnel. */
    public static final class VolumeInfo {
        @SerializedName("create_new") public Boolean createNew;
        @SerializedName("volume_id")  public Long volumeId;
        @SerializedName("size")       public Integer size;
        @SerializedName("mount_path") public String mountPath;
        @SerializedName("name")       public String name;

        public VolumeInfo createNew(Boolean v){ this.createNew=v; return this; }
        public VolumeInfo volumeId(Long v){ this.volumeId=v; return this; }
        public VolumeInfo size(Integer v){ this.size=v; return this; }
        public VolumeInfo mountPath(String v){ this.mountPath=v; return this; }
        public VolumeInfo name(String v){ this.name=v; return this; }

    }
}
