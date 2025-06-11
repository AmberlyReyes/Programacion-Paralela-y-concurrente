package Practica02;

import Practica02.framework.NetworkManager;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        NetworkManager manager = new NetworkManager();

        String[] topologias = {"bus", "ring", "mesh", "star", "hypercube", "tree", "fullyconnected", "switched"};

        int seleccion = -1;
        while (true) {
            System.out.println(" 🩷 Seleccione una topologia para probar :)");
            System.out.println("(1) bus, (2) ring, (3) mesh, (4) star, (5) hypercube, (6) tree, (7) fullyConnected, (8) switched");
            System.out.print(" # --> : ");
            try {
                seleccion = scanner.nextInt();
                if (seleccion < 1 || seleccion > 8) {
                    System.out.println("Digite un valor dentro del rango establecido :) .\n");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Intentalo con una opción valida :).\n");
                scanner.nextLine(); // limpiar entrada inválida
            }
        }

        int nodos = 0;
        while (true) {
            System.out.print("Número de nodos: ");
            try {
                nodos = scanner.nextInt();
                if (nodos < 2) {
                    System.out.println(" Debe haber al menos 2 nodos.\n");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println(" Entrada inválida. Ingrese un número entero.\n");
                scanner.nextLine(); // limpiar entrada inválida
            }
        }

        String tipoTopologia = topologias[seleccion - 1];
        manager.setupTopology(tipoTopologia, nodos);
        manager.showState();
        manager.startSimulation();

        scanner.nextLine(); // limpiar línea pendiente

        while (true) {
            System.out.print("ID del nodo origen (o -1 para salir): ");
            int origen;
            try {
                origen = scanner.nextInt();
                if (origen == -1) break;
                System.out.print("ID del nodo destino: ");
                int destino = scanner.nextInt();
                scanner.nextLine(); // limpiar salto

                System.out.print("Mensaje a enviar: ");
                String mensaje = scanner.nextLine();

                manager.sendMessage(origen, destino, mensaje);
                Thread.sleep(500);
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Intente nuevamente.\n");
                scanner.nextLine(); // limpiar entrada inválida
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        manager.shutdown();
        System.out.println("Simulación terminada ;D ");
    }
}
