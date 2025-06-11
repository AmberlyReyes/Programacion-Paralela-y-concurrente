package P1;
import  java.io.IOException;
import java.io.FileWriter;
import java.util.Random;

public class GeneraArchivo {
    public static void main (String[] args){

        int cant =1_000_000;
        Random rand = new Random();

        try (FileWriter writer = new FileWriter("AmbersNumbers")) {
            for (int i=0; i <cant ; i++){
                int numeroGenerado= rand.nextInt(10_000)+1;
                writer.write(numeroGenerado+"\n");
            }
            System.out.println("generado!");


        } catch (Exception e) {
            System.err.println("Error escribiendo el archivo: " + e.getMessage());
        }

    }
}
