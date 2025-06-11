package Practica02.framework;

import Practica02.topology.*;
/*
    Clase encargada de gestionar la topologia de red y facilita la inteccion
    en la misma.
 */

public class NetworkManager {
    private NetworkTopology topology;//referencia a la topologia actual (en uso)

    public void setupTopology(String tipo, int numNodos) {
        switch (tipo.toLowerCase()) {
            case "bus":
                topology = new BusNetwork(); break;
            case "ring":
                topology = new RingNetwork(); break;
            case "mesh":
                topology = new MeshNetwork(); break;
            case "star":
                topology = new StarNetwork(); break;
            case "hypercube":
                topology = new HypercubeNetwork(); break;
            case "tree":
                topology = new TreeNetwork(); break;
            case "fullyconnected":
                topology = new FullyConnectedNetwork(); break;
            case "switched":
                topology = new SwitchedNetwork(); break;
            default:
                throw new IllegalArgumentException("Topología no reconocida: " + tipo);
        }
        topology.build(numNodos);//con esto construimos la red con la cant de nodos especificada
    }
    //inicia simulacion
    public void startSimulation() {
        if (topology != null) topology.start();
    }
    //Envia el mensaje desde nodo origen hasta destino, segun la topologia indicada
    public void sendMessage(int origen, int destino, String mensaje) {
        Message m = new Message(origen, destino, mensaje);
        topology.route(m);
    }
    //finaliza la simulacion
    public void shutdown() {
        if (topology != null) topology.shutdown();
    }
    //muestra en  consola la topolagia activa
    public void showState() {
        System.out.println("Topología activa: " + topology.getClass().getSimpleName());
    }
}
