import java.io.Serializable;

public class JugadorHumano implements Jugador, Serializable {
    private String nombre;
    private char simbolo;

    private int ganadas, perdidas, empatadas;
    public JugadorHumano(String nombre, char simbolo){
        this.nombre = nombre;
        this.simbolo = simbolo;

    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public char getSimbolo() {
        return simbolo;
    }

    @Override
    public int hacerJugada(char[][] tablero) {
        return 1;

    }

    @Override
    public String toString() {
        return "JugadorHumano{" +
                "nombre='" + nombre + '\'' +
                ", simbolo=" + simbolo +
                ", ganadas=" + ganadas +
                ", perdidas=" + perdidas +
                ", empatadas=" + empatadas +
                '}';
    }
    public void incrementarGanadas() {
        ganadas++;
    }
    public void incrementarPerdidas(){perdidas++;}
    public void incrementarEmpatadas(){perdidas++;}

    public int getGanadas() {
        return ganadas;
    }
    
    public int getEmpatadas() {
        return empatadas;
    }

    public void setGanadas(int ganadas) {
        this.ganadas = ganadas;
    }

    public void setPerdidas(int perdidas) {
        this.perdidas = perdidas;
    }

    public void setEmpatadas(int empatadas) {
        this.empatadas = empatadas;
    }

    public int getPerdidas() {
        return perdidas;
    }
}
