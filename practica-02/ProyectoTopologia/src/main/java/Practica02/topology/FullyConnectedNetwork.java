package Practica02.topology;

import Practica02.framework.*;
import java.util.*;
import java.util.concurrent.*;
/*

    Implementacion de una red totalmente conectada, esto quiere decir
    que es capaz de que cada nodo puede comunicarse de manera directa con otro nodo
 */
public class FullyConnectedNetwork implements NetworkTopology {
    private List<Nodo> nodos;
    private Map<Integer, BlockingQueue<Message>> colas;
    private ExecutorService exec;

    @Override
    public void build(int numNodes) {
        exec = Executors.newFixedThreadPool(numNodes);
        colas = new HashMap<>();
        nodos = new ArrayList<>();

        for (int i = 0; i < numNodes; i++) {
            BlockingQueue<Message> q = new LinkedBlockingQueue<>();
            Nodo n = new Nodo(i, q);
            n.setTopology(this);
            nodos.add(n);
            colas.put(i, q);
        }
    }

    @Override
    public void start() {
        nodos.forEach(exec::submit);
    }

    @Override
    public void route(Message m) {
        colas.get(m.getDestId()).offer(m);
    }

    @Override
    public void shutdown() {
        exec.shutdownNow();
    }
}
