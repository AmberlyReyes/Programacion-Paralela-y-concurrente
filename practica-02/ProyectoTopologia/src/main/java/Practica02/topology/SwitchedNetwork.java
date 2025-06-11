package Practica02.topology;

import Practica02.framework.*;
import java.util.*;
import java.util.concurrent.*;
/*

    En esta topología, todos los mensajes pasan por un "switch"
    central que actúa como intermediario y redirige los
    mensajes al nodo destino correspondiente.

 */
public class SwitchedNetwork implements NetworkTopology  {
    private List<Nodo> nodos;
    private Map<Integer, BlockingQueue<Message>> colas;
    private ExecutorService exec;
    private BlockingQueue<Message> switchQueue;// cola central del switch

    @Override
    public void build(int numNodes) {
        exec = Executors.newFixedThreadPool(numNodes + 1); // +1 para el "switch"
        colas = new HashMap<>();
        nodos = new ArrayList<>();
        switchQueue = new LinkedBlockingQueue<>();

        for (int i = 0; i < numNodes; i++) {
            BlockingQueue<Message> q = new LinkedBlockingQueue<>();
            Nodo n = new Nodo(i, q);
            n.setTopology(this);
            nodos.add(n);
            colas.put(i, q);
        }

        // Switch actúa como "repartidor"
        exec.submit(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Message m = switchQueue.take();
                    colas.get(m.getDestId()).offer(m);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    @Override
    public void start() {
        nodos.forEach(exec::submit);
    }

    @Override
    public void route(Message m) {
        switchQueue.offer(m);
    }

    @Override
    public void shutdown() {
        exec.shutdownNow();
    }
}
