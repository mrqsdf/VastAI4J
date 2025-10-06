package fr.mrqsdf.vastai4j.examples;

import fr.mrqsdf.vastai4j.VastAI;
import fr.mrqsdf.vastai4j.event.*;
import fr.mrqsdf.vastai4j.event.instance.InstancePortsMappedEvent;
import fr.mrqsdf.vastai4j.event.instance.InstanceSshReadyEvent;
import fr.mrqsdf.vastai4j.event.instance.InstanceStateChangeEvent;
import fr.mrqsdf.vastai4j.model.Offer;
import fr.mrqsdf.vastai4j.model.instance.CreateInstanceRequest;
import fr.mrqsdf.vastai4j.model.instance.CreateInstanceResponse;
import fr.mrqsdf.vastai4j.model.instance.RunType;
import fr.mrqsdf.vastai4j.monitor.InstanceMonitor;
import fr.mrqsdf.vastai4j.query.*;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public final class DemoEvents {

    public static final class MyListener implements Listener {

        @EventHandler(priority = EventPriority.LOW)
        public void onState(InstanceStateChangeEvent e) {
            System.out.printf("[STATE] #%d %s -> %s | actual %s -> %s%n",
                    e.instanceId(), e.previousCurState(), e.newCurState(),
                    e.previousActualStatus(), e.newActualStatus());
        }

        @EventHandler(priority = EventPriority.NORMAL)
        public void onSsh(InstanceSshReadyEvent e) {
            System.out.printf("[SSH] #%d ssh %s:%d ready%n",
                    e.instanceId(), e.sshHost(), e.sshPort());
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPorts(InstancePortsMappedEvent e) {
            System.out.printf("[PORTS] #%d ip=%s map=%s%n",
                    e.instanceId(), e.publicIpOrHost(), e.mapping());
        }
    }

    public static void main(String[] args) throws Exception {

        VastAI vast = new VastAI(args[0]);

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



        EventBus bus = vast.eventBus();
        bus.register(new MyListener());

        try (InstanceMonitor mon = new InstanceMonitor(vast, bus)) {
            mon.watch(instanceId, 2, TimeUnit.SECONDS); // poll toutes les 2s
            // démo 60s
            Thread.sleep(60_000L);
        }


        vast.instances().destroy(instanceId);
        System.out.println("Destroyed instance " + instanceId);
    }
}
