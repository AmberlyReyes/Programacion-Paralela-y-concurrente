package Practica02.topology;

//import Practica02.framework.*;
import Practica02.framework.Message;
import Practica02.framework.Nodo;
import Practica02.framework.NetworkTopology;

import java.util.*;
import java.util.concurrent.*;
/*
    En esta topologia todos comparten un canal de comunicacion comun (el bus)
 */
public class BusNetwork implements NetworkTopology {
    private List<Nodo> nodes;
    private BlockingQueue<Message> bus;//canal de comunicacion
    private ExecutorService exec;//pool de hilos para ejecutar nodos


    //construye la red del tipo de bus con la cantidad de nodos especificada
    @Override
    public void build(int numNodes) {
        bus = new LinkedBlockingQueue<>();
        nodes = new ArrayList<>(numNodes);
        exec = Executors.newFixedThreadPool(numNodes);
        for (int i = 0; i < numNodes; i++) {
            Nodo node = new Nodo(i, bus);
            node.setTopology(this);
            nodes.add(node);
        }
    }

    @Override
    public void start() {
        for (Nodo n : nodes) {
            exec.submit(n);
        }
    }

    @Override
    public void route(Message m) {
        // en bus todos ven todos, así que simplemente se añade al bus
        bus.offer(m);
    }

    @Override
    public void shutdown() {
        exec.shutdownNow();
    }
}
