import java.io.Serializable;

public interface Jugador extends Serializable {
    String getNombre();
    char getSimbolo();
    int hacerJugada(char[][] tablero);
}
