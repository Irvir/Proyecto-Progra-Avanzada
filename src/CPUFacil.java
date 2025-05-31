import java.io.Serializable;
import java.util.Random;

class CPUFacil implements Jugador, Serializable {
    private String nombre = "CPU";
    private char simbolo;
    Random random = new Random();
    private int ganadas, perdidas, empatadas;
    public CPUFacil(char simbolo){
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
        int pos;
        do {
            pos = random.nextInt(9)+1 ;
        }while (!esValida(pos,tablero));
        {;
            System.out.println("CPU (fácil) elige POSICIÓN: " + pos);
            return pos;

        }
    }
    private boolean esValida(int pos, char[][] tablero) {
        int i = (pos - 1) / 3;
        int j = (pos - 1) % 3;
        return tablero[i][j] == '-';
    }

    @Override
    public String toString() {
        return "cpu-easy{" +
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
}
