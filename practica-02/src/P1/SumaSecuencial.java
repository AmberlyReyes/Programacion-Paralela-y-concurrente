package P1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SumaSecuencial {
    public static void main(String[] args) {
        String archivoGenerado = "AmbersNumbers"; /*archivo con los numeros aletorios generados entre 1 y 10mil*/
        long sumatotal = 0; /*resultado total*/


        ArrayList<Integer> lista = new ArrayList<>();

        try (BufferedReader br = new BufferedReader((new FileReader(archivoGenerado)))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lista.add(Integer.parseInt(linea));
            }
        } catch (IOException e) {
            System.err.println("Error " + e.getMessage());
            return;
        }

        int[] datos = new int[lista.size()];
        for (int i = 0; i < lista.size(); i++) {
            datos[i] = lista.get(i);
        }

// 🔽 Mide solo la parte de la suma
        long tiempoInicio = System.nanoTime();

        for (int i = 0; i < datos.length; i++) {
            sumatotal += datos[i];
        }

        long tiempoFin = System.nanoTime();
        double tiempototal = (tiempoFin - tiempoInicio) / 1_000_000_000.0;

        System.out.println("Suma total: " + sumatotal);
        System.out.println("Tiempo secuencial: " + tiempototal + " s");
    }


}
