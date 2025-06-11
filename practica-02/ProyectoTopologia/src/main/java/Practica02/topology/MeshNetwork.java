package Practica02.topology;

import Practica02.framework.*;

import java.util.*;
import java.util.concurrent.*;
/*

    En esta implementacion de una red tipo malla,
    Los nodos estan organizados en una cuadricula 2d, y se conectan
    con sus vecinos ''ortogonales'', es decir; arriba, abajo, izquierda,
    derecha. Usando distancia Manhttan
 */
public class MeshNetwork implements NetworkTopology {
    private Nodo[][] grid;// matriz de la malla de nodos
    private int rows, cols;//dimensiones de la malla
    private ExecutorService exec;
    private Map<Integer, Nodo> nodos = new HashMap<>();
    private Map<Integer, List<Nodo>> vecinos = new HashMap<>();

    @Override
    public void build(int numNodes) {
        rows = cols = (int) Math.ceil(Math.sqrt(numNodes));
        exec = Executors.newFixedThreadPool(numNodes);
        grid = new Nodo[rows][cols];

        int id = 0;
        for (int i = 0; i < rows && id < numNodes; i++) {
            for (int j = 0; j < cols && id < numNodes; j++) {
                BlockingQueue<Message> inbox = new LinkedBlockingQueue<>();
                Nodo nodo = new Nodo(id, inbox);
                nodo.setTopology(this);
                grid[i][j] = nodo;
                nodos.put(id, nodo);
                vecinos.put(id, new ArrayList<>());
                id++;
            }
        }

        // Conectar vecinos ortogonales (arriba, abajo, izquierda, derecha)
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Nodo actual = grid[i][j];
                if (actual == null) continue;
                int idActual = actual.getId();
                List<Nodo> lista = vecinos.get(idActual);

                if (i > 0 && grid[i - 1][j] != null) lista.add(grid[i - 1][j]); // arriba
                if (i < rows - 1 && grid[i + 1][j] != null) lista.add(grid[i + 1][j]); // abajo
                if (j > 0 && grid[i][j - 1] != null) lista.add(grid[i][j - 1]); // izquierda
                if (j < cols - 1 && grid[i][j + 1] != null) lista.add(grid[i][j + 1]); // derecha
            }
        }
    }

    @Override
    public void start() {
        for (Nodo nodo : nodos.values()) {
            exec.submit(nodo);
        }
    }

    @Override
    public void route(Message m) {
        Nodo actual = nodos.get(m.getVisitedCount() == 0 ? m.getSourceId() : m.getLastVisited());
        Nodo destino = nodos.get(m.getDestId());

        if (actual == null || destino == null) {
            System.out.println("Nodo fuente o destino no existe.");
            return;
        }

        if (actual.getId() == destino.getId()) {
            actual.getInbox().offer(m);
            return;
        }

        int[] posActual = buscarPosicion(actual);
        int[] posDestino = buscarPosicion(destino);

        Nodo mejorVecino = null;
        int mejorDistancia = Integer.MAX_VALUE;

        for (Nodo vecino : vecinos.get(actual.getId())) {
            if (m.hasVisited(vecino.getId())) continue;

            int[] posVecino = buscarPosicion(vecino);
            int distancia = manhattan(posVecino, posDestino);

            if (distancia < mejorDistancia) {
                mejorDistancia = distancia;
                mejorVecino = vecino;
            }
        }

        if (mejorVecino != null) {
            mejorVecino.getInbox().offer(m);
        } else {
            System.out.printf("Nodo %d no pudo reenviar mensaje a %d (sin vecinos disponibles)%n",
                    actual.getId(), destino.getId());
        }
    }
    // busca la posicion (fila, columna) de un nodo en la malla
    private int[] buscarPosicion(Nodo nodo) {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                if (grid[i][j] != null && grid[i][j].getId() == nodo.getId())
                    return new int[]{i, j};
        return new int[]{-1, -1};
    }
/*
aqui se calcula la distancia manhattan entre dos posiciones en la malla
 */
    private int manhattan(int[] a, int[] b) {
        return Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]);
    }

    @Override
    public void shutdown() {
        exec.shutdownNow();
    }
}
