package Practica02.framework;

import java.util.concurrent.BlockingQueue;
/*

    Clase que representa un nodo dentro de una topologia de red.
    Cada nodo corre un hilo propio y puede enviar y recibir mensajes.
 */
public class Nodo implements Runnable {
    private final int id;//identificador unico del nodo
    public final BlockingQueue<Message> inbox;//cola de mensajes entrantes
    private NetworkTopology topology;// referencia a la topologia en uso

    public Nodo(int id, BlockingQueue<Message> inbox) {
        this.id = id;
        this.inbox = inbox;
    }

    public int getId() {
        return id;
    }

    public BlockingQueue<Message> getInbox() {
        return inbox;
    }

    public void setTopology(NetworkTopology topology) {
        this.topology = topology;
    }

    public void send(int destId, String payload) {
        if (topology == null) {
            throw new IllegalStateException("Topology not set for node " + id);
        }
        Message m = new Message(this.id, destId, payload);
        topology.route(m);
    }

    @Override
    public void run() {
        int mensajesProcesados = 0;
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Message m = inbox.take();

                // Evita reenviar mensajes que ya pasaron por aquí
                if (m.hasVisited(id)) continue;
                m.markVisited(id);

                if (m.isAck()) {// si es una confirmacion (ACK) y es para el nodo en cuestion, lo imprime
                    if (m.getDestId() == id) {
                        System.out.printf(":) Nodo %d recibió ACK de Nodo %d%n", id, m.getSourceId());
                    }
                    // No reenviar ACKs
                    continue;
                }

                if (m.getDestId() == id) {
                    mensajesProcesados++;
                    System.out.printf("Nodo %d procesó mensaje #%d: %s%n",
                            id, mensajesProcesados, m.getPayload());

                    // Enviar ACK de vuelta al emisor
                    Message ack = new Message(id, m.getSourceId(),
                            "ACK de Nodo " + id, true);
                    topology.route(ack);

                } else {
                    //reenvia el mensaje si no es el destinatario
                    System.out.printf("Nodo %d reenvía mensaje a %d: %s%n",
                            id, m.getDestId(), m.getPayload());
                    topology.route(m);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.printf(" Nodo %d interrumpido :c . Total mensajes procesados: %d%n",
                    id, mensajesProcesados);
        }
    }
}
