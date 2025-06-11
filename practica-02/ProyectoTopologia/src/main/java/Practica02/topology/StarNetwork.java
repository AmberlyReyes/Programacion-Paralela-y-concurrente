package Practica02.topology;

import Practica02.framework.*;
import java.util.*;
import java.util.concurrent.*;
/*

   Esta topología, todos los nodos (hojas) se comunican
   a través de un nodo central (hub).
   El hub actúa como intermediario para todos los mensajes.

 */
public class StarNetwork implements NetworkTopology {
    private List<Nodo> leaves;//lista de nodos hoja
    private Nodo hub;//nodo central
    private BlockingQueue<Message> hubQueue;//cola de mensajes del hub
    private ExecutorService exec;

    @Override
    public void build(int numNodes) {
        exec = Executors.newFixedThreadPool(numNodes);
        hubQueue = new LinkedBlockingQueue<>();
        hub = new Nodo(0, hubQueue);
        hub.setTopology(this);

        leaves = new ArrayList<>();
        for (int i = 1; i < numNodes; i++) {
            Nodo leaf = new Nodo(i, new LinkedBlockingQueue<>());
            leaf.setTopology(this);
            leaves.add(leaf);
        }
    }

    @Override
    public void start() {
        exec.submit(hub);
        leaves.forEach(exec::submit);
    }

    @Override
    public void route(Message m) {
        if (m.getSourceId() == 0) {
            // mensaje del hub a hoja
            leaves.stream()
                    .filter(n -> n.getId() == m.getDestId())
                    .findFirst()
                    .ifPresent(n -> ((LinkedBlockingQueue<Message>)n.inbox).offer(m));
        } else {
            // mensaje de una hoja al hub
            hubQueue.offer(m);
        }
    }

    @Override
    public void shutdown() {
        exec.shutdownNow();
    }
}
