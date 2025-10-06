package fr.mrqsdf.vastai4j.examples;

import fr.mrqsdf.vastai4j.VastAI;
import fr.mrqsdf.vastai4j.model.Offer;
import fr.mrqsdf.vastai4j.model.instance.*;
import fr.mrqsdf.vastai4j.query.*;
import fr.mrqsdf.vastai4j.service.InstanceService;

import java.util.*;

/**
 * Affiche l'IP publique, les infos SSH et les ports ouverts d'une instance Vast.ai,
 * et imprime des conseils pour se connecter à SSH (22) et à un service interne (ex: 8000).
 */
public final class ShowPorts {

    public static void main(String[] args) {

        String apiKey = args[0];

        VastAI vast = new VastAI(apiKey);
        InstanceService svc = vast.instances();

        OfferQuery q = new OfferQuery()
                .where(OfferField.GPU_NAME, Op.IN, Set.of("RTX 3060"))
                .where(OfferField.STATIC_IP, Op.EQ, false)
                .orderBy(OrderField.SCORE, Direction.DESC)
                .limit(40);

        List<Offer> offers = vast.offers().search(q);
        offers.sort(Comparator.comparing(Offer::dphTotal));

        long offerId = offers.get(0).id();

        CreateInstanceRequest req = new CreateInstanceRequest()
                .templateId(null)  // Set to null when providing a custom image instead.
                .templateHashId("a910281dde7abd591fe1c8a7ee9312d6") // Demo hashId: replace with a valid value for real usage.
                //.image("nvidia/cuda:11.6.2-cudnn8-runtime-ubuntu20.04") // Uncomment to rely on a public image.
                .disk(35.0)                                             // Minimum is 8 GB.
                .runtypeEnum(RunType.SSH)                                // Alternatively use JUPYTER.
                .targetState("running")
                .label("java-sdk-demo");                 // Or "stopped" if you plan to start later.

        CreateInstanceResponse created = vast.instances().createInstance(offerId, req);
        System.out.println("Create success=" + created.success() + " new_contract=" + created.newContract());

        // Instance identifiers usually match the "new_contract" value.
        long instanceId = created.newContract();


        InstanceDetails det = svc.show(instanceId);
        InstanceDetails.InstancePayload i = det.instances();

        String publicIp   = str(i.publicIpaddr());
        String sshHost    = str(i.sshHost());
        Integer sshPort   = i.sshPort();
        String curState   = str(i.curState());
        Integer directLo  = i.directPortStart();
        Integer directHi  = i.directPortEnd();
        Map<String, List<Ports>> openPorts = i.ports() == null ? Map.of() : i.ports();

        System.out.println("=== Vast.ai Instance ===");
        System.out.println("ID            : " + i.id());
        System.out.println("State         : " + curState);
        System.out.println("Public IP     : " + publicIp);
        System.out.println("SSH host:port : " + sshHost + ":" + sshPort);
        System.out.println("Open ports    : " + (openPorts.isEmpty() ? "(none)" : openPorts));
        System.out.println("Direct range  : " + (directLo == null ? "n/a" : directLo) + " - " + (directHi == null ? "n/a" : directHi));

        // === Accès SSH (port 22 interne) ===
        // L'API te donne directement ssh_host/ssh_port. Tu peux te connecter ainsi :
        //   ssh -p <ssh_port> root@<ssh_host>
        System.out.println();
        System.out.println("SSH quick connect:");
        if (sshHost != null && sshPort != null) {
            System.out.println("  ssh -p " + sshPort + " root@" + sshHost);
        } else {
            System.out.println("  (ssh_host/ssh_port indisponibles)");
        }

        // === Accès à un service interne sur 8000 ===
        // RECOMMANDÉ: tunnel SSH. Pas besoin de connaître le port externe mappé.
        //   ssh -N -L 8000:localhost:8000 -p <ssh_port> root@<ssh_host>
        System.out.println();
        System.out.println("Tunnel pour accéder au service interne sur 8000 (recommandé) :");
        if (sshHost != null && sshPort != null) {
            System.out.println("  ssh -N -L 8000:localhost:8000 -p " + sshPort + " root@" + sshHost);
            System.out.println("  puis ouvrez http://localhost:8000 dans votre navigateur.");
        } else {
            System.out.println("  (ssh_host/ssh_port indisponibles)");
        }

        // NOTE: Si tu tiens à utiliser le port externe public direct:
        //  - il est assigné dynamiquement par Vast (mapping externe -> interne).
        //  - l'UI \"IP & Port Info\" montre la table exacte.
        //  - l'API expose public_ipaddr et la liste 'ports', mais n'énumère pas toujours
        //    la correspondance précise pour chaque port interne. Le tunnel SSH est donc
        //    la méthode la plus robuste pour 8000.


        vast.instances().destroy(instanceId);
        System.out.println("Destroyed instance " + instanceId);

    }

    private static String str(String s) { return s == null ? "null" : s; }
}
