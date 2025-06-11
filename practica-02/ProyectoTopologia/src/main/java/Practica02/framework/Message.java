package Practica02.framework;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/*
    Esta clase representa el mensaje enviado entre nodos en la red. Implementa
    Serializable porque  debe permitir la transmision en entornos distribuidos
 */

public class Message implements Serializable {
    private final int sourceId;// id nodo emisor
    private final int destId;//id nodo receptor
    private final String payload;//contenido del mensaje
    private final boolean isAck;//para confirmar si el mensaje es de confirmacion
    private final Set<Integer> visitedNodes = new HashSet<>();
    //nodos por los que pasa el mensaje


    public Message(int sourceId, int destId, String payload) {
        this(sourceId, destId, payload, false);
    }

    public Message(int sourceId, int destId, String payload, boolean isAck) {
        this.sourceId = sourceId;
        this.destId = destId;
        this.payload = payload;
        this.isAck = isAck;
    }

    public int getSourceId() {
        return sourceId;
    }

    public int getDestId() {
        return destId;
    }

    public String getPayload() {
        return payload;
    }

    public boolean isAck() {
        return isAck;
    }

    /*
    Esta parte de 'Visited' se utiliza para verificar si el mensaje ya ha pasado por un nodo
    especifico. Es util para cuando usemos topologias de anillo o malla :)
     */
    public boolean hasVisited(int nodeId) {
        return visitedNodes.contains(nodeId);
    }

    public void markVisited(int nodeId) {
        visitedNodes.add(nodeId);
    }
    public int getVisitedCount() {
        return visitedNodes.size();
    }

    public int getLastVisited() {
        return visitedNodes.stream().reduce((first, second) -> second).orElse(getSourceId());
    }
    //esto es solo para depuracion, realmente no es necesario y puede ser eliminado
    @Override
    public String toString() {
        return String.format("Message[from=%d,to=%d,type=%s,payload=%s]",
                sourceId, destId, isAck ? "ACK" : "DATA", payload);
    }
}
