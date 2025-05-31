import java.io.*;
import java.util.ArrayList;

public class Serializacion {

    // Serializa un jugador (agrega o actualiza en archivo)
    public static void guardarJugador(Jugador jugador) {
        ArrayList<Jugador> jugadores = cargarJugadores();
        jugadores.add(jugador); // O puedes actualizar si ya existe
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("jugadores.txt"))) {
            out.writeObject(jugadores);
            System.out.println("- Jugador serializado correctamente.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Carga todos los jugadores desde archivo
    public static ArrayList<Jugador> cargarJugadores() {
        ArrayList<Jugador> jugadores = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream("jugadores.txt"))) {
            jugadores = (ArrayList<Jugador>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // archivo no existe, se devuelve lista vacía
        }
        return jugadores;
    }
}



