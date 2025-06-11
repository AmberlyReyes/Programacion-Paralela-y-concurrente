package Practica02.topology;

import Practica02.framework.*;
import java.util.*;
import java.util.concurrent.*;
/*
    Esta topologia permite que los nodos esten conectados
    de acuerdo a las dimensiones de un hipercubo binario.
    Cada nodo tiene conexiones con otros nodos que difieren
    en un solo bit en su ID binario.
 */
public class HypercubeNetwork implements NetworkTopology {
    private List<Nodo> nodos;
    private Map<Integer, BlockingQueue<Message>> colas;
    private ExecutorService exec;

    @Override
    public void build(int numNodes) {
        //dimension minima del hipercubo
        int dim = (int) Math.ceil(Math.log(numNodes) / Math.log(2));
        //total de nodos posibles en esa dimension (2^dim)
        int totalNodes = 1 << dim;

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
