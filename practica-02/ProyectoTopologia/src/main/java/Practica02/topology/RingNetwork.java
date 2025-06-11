package Practica02.topology;

import Practica02.framework.*;
import java.util.*;
import java.util.concurrent.*;
/*
    En esta topologia, cada nodo esta conectado a su vecino
    siguiente formando unn ciclo "cerrado"
 */
public class RingNetwork implements NetworkTopology {
    private List<Nodo> nodes;
    private List<BlockingQueue<Message>> queues;
    private ExecutorService exec;


    // para este build se construye la red tipo anillo con la cant
    // de nodos especificada y cada uno tiene su propia cola de mensajes
    //y se conecta con el siguiente anillo
    @Override
    public void build(int numNodes) {
        exec = Executors.newFixedThreadPool(numNodes);
        queues = new ArrayList<>(numNodes);
        nodes = new ArrayList<>(numNodes);
        // crear una cola para cada nodos
        for (int i = 0; i < numNodes; i++) {
            BlockingQueue<Message> q = new LinkedBlockingQueue<>();
            queues.add(q);
        }
        // crear nodos y se les asigna su cola correspondiente
        for (int i = 0; i < numNodes; i++) {
            Nodo n = new Nodo(i, queues.get(i));
            n.setTopology(this);
            nodes.add(n);
        }
    }

    @Override
    public void start() {
        for (Nodo n : nodes) exec.submit(n);
    }

    @Override
    public void route(Message m) {
        // envía al siguiente en anillo hasta destino
        int src = m.getSourceId();
        int dest = m.getDestId();
        int next = (src + 1) % nodes.size();// nodo siguiente en el anillo
        if (next == dest) {
            queues.get(dest).offer(m);// entrega directa si el siguiente es el destino
        } else {
            // reenviar al siguiente
            queues.get(next).offer(m);
        }
    }

    @Override
    public void shutdown() {
        exec.shutdownNow();
    }
}
