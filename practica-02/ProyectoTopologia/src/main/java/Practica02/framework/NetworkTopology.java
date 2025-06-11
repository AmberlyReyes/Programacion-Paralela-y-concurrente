package Practica02.framework;
/*
    Interfaz que define el contrato para cualquier topología de
    red dentro del framework.
 */
public interface NetworkTopology {
    // para construir la topología: crea nodos y conexiones
    void build(int numNodos);

    //simulación: inicia hilos
    void start();

    // envia (ruta) mensaje entre nodos
    void route(Message m);

   //detiene la simulación y limpia los recursos residuales
    void shutdown();
}
