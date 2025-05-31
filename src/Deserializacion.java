import java.util.ArrayList;

public class Deserializacion {
    public static void main(String[] args) {
        ArrayList<Jugador> jugadores = Serializacion.cargarJugadores();
        for (Jugador j : jugadores) {
            System.out.println(j.toString());
        }
    }
}
