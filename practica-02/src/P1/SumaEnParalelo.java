package P1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SumaEnParalelo {

    static class Sumador extends Thread {
        private int[] data;
        private int Inicial, Final;
        private long resultado = 0;

        public Sumador(int[] data, int Inicial, int Final) {
            this.data = data;
            this.Inicial = Inicial;
            this.Final = Final;
        }

        public void run() {
            for (int i = Inicial; i < Final; i++) {
                resultado += data[i];
            }
        }

        public long getResultado() {
            return resultado;
        }
    }

    public static void main(String[] args) {
        String archivo = "AmbersNumbers";
        ArrayList<Integer> lista = new ArrayList<>();

        // Lee los datos del archivo  fuera de la medición de tiempo paralela)
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lista.add(Integer.parseInt(linea));
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            return;
        }

        int[] data = new int[lista.size()];
        for (int i = 0; i < lista.size(); i++) {
            data[i] = lista.get(i);
        }

        int[] numHilosToTest = {2, 4, 8, 16, 32}; // prueba :p

        System.out.println(" ♥ Rendimiento de Suma Paralela ♥");
        for (int numHilos : numHilosToTest) {
            Sumador[] hilos = new Sumador[numHilos];
            int tamano = data.length / numHilos;

            long inicio = System.nanoTime(); // Inicia la medición de tiempo para la ejecución paralela

            // Crea y lanza los hilos
            for (int i = 0; i < numHilos; i++) {
                int ini = i * tamano;
                int fin = (i == numHilos - 1) ? data.length : ini + tamano;
                hilos[i] = new Sumador(data, ini, fin);
                hilos[i].start();
            }

            long sumaTotal = 0;
            try {
                for (int i = 0; i < numHilos; i++) {
                    hilos[i].join();
                    sumaTotal += hilos[i].getResultado();
                }
            } catch (InterruptedException e) {
                System.err.println("Error esperando hilos: " + e.getMessage());
            }

            long fin = System.nanoTime(); // Finaliza la medición de tiempo para la ejecución paralela

            double tiempoSegundos = (fin - inicio) / 1_000_000_000.0;

            System.out.println("Suma Paralela con " + numHilos + " hilos: " + sumaTotal);
            System.out.println("Tiempo paralelo con " + numHilos + " hilos: " + tiempoSegundos + " segundos\n");
        }
    }
}