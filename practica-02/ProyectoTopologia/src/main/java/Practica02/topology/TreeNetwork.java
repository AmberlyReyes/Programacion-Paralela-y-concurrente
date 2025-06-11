package Practica02.topology;

import Practica02.framework.*;

import java.util.*;
import java.util.concurrent.*;
/*

  Implementación de una red tipo árbol (Tree Network).
  Los nodos se organizan en una estructura de árbol binario,
  donde cada nodo puede tener hasta dos hijos.
  El enrutamiento se realiza de forma recursiva entre padres e hijos, evitando ciclos.

 */
public class TreeNetwork implements NetworkTopology {
    private Map<Integer, Nodo> nodos = new HashMap<>();
    private Map<Integer, BlockingQueue<Message>> colas = new HashMap<>();
    private Map<Integer, List<Integer>> conexiones = new HashMap<>(); // hijos y padre
    private ExecutorService exec;

    @Override
    public void build(int numNodes) {
        exec = Executors.newFixedThreadPool(numNodes);

        // Crear nodos y colas
        for (int i = 0; i < numNodes; i++) {
            BlockingQueue<Message> q = new LinkedBlockingQueue<>();
            Nodo n = new Nodo(i, q);
            n.setTopology(this);
            nodos.put(i, n);
            colas.put(i, q);
            conexiones.put(i, new ArrayList<>());
        }

        // establecer conexiones padre-hijo en forma de arbol binario
        for (int i = 0; i < numNodes; i++) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            if (left < numNodes) {
                conexiones.get(i).add(left);      // hijo izq
                conexiones.get(left).add(i);      // padre
            }
            if (right < numNodes) {
                conexiones.get(i).add(right);     // hijo der
                conexiones.get(right).add(i);     // padre
            }
        }
    }

    @Override
    public void start() {
        for (Nodo n : nodos.values()) {
            exec.submit(n);
        }
    }

    /**
      Enruta un mensaje desde el nodo actual hacia el destino.
      Se evita reenviar a nodos ya visitados para prevenir ciclos.
       Si no hay vecinos disponibles, se imprime un mensaje de error.
      */
    @Override
    public void route(Message m) {
        int actualId = m.getVisitedCount() == 0 ? m.getSourceId() : m.getLastVisited();

        if (actualId == m.getDestId()) {
            colas.get(actualId).offer(m);
            return;
        }

        for (int vecino : conexiones.get(actualId)) {
            if (!m.hasVisited(vecino)) {
                colas.get(vecino).offer(m);
                return;
            }
        }

        System.out.printf("Nodo %d no pudo reenviar mensaje a %d%n", actualId, m.getDestId());
    }

    @Override
    public void shutdown() {
        exec.shutdownNow();
    }
}
