import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Serializacion {

    public static void guardarJugador(Jugador jugador) {
        ArrayList<Jugador> jugadores = cargarJugadores();

        boolean jugadorExistente = false;
        int mayor = 0;
        for (int i = 0; i < jugadores.size()-1; i++) {

            Jugador j = jugadores.get(i);
            if (j.getNombre().equals(jugador.getNombre())) {
                if (j instanceof JugadorHumano && jugador instanceof JugadorHumano) {
                    JugadorHumano jHumano = (JugadorHumano) j;
                    JugadorHumano nuevoHumano = (JugadorHumano) jugador;
                    jHumano.setGanadas(nuevoHumano.getGanadas());
                    jHumano.setPerdidas(nuevoHumano.getPerdidas());
                    jHumano.setEmpatadas(nuevoHumano.getEmpatadas());
                }
                jugadorExistente = true;
                break;
            }
        }

        if (!jugadorExistente) {
            jugadores.add(jugador);
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("jugadores.txt"))) {
            out.writeObject(jugadores);
            System.out.println("- Jugador: " + jugador.getNombre() + " serializado correctamente.");
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
            e.printStackTrace();
        }

        // Ordenar la lista por ganadas de mayor a menor
        Collections.sort(jugadores, new Comparator<Jugador>() {
            @Override
            public int compare(Jugador j1, Jugador j2) {
                if (j1 instanceof JugadorHumano && j2 instanceof JugadorHumano) {
                    JugadorHumano h1 = (JugadorHumano) j1;
                    JugadorHumano h2 = (JugadorHumano) j2;
                    return Integer.compare(h2.getGanadas(), h1.getGanadas()); // Orden descendente
                } else {
                    return 0; // O compara de otra forma si hay otros tipos de jugadores
                }
            }
        });



    return jugadores;
    }
}




