import java.io.Serializable;
import java.util.Random;
import java.util.Scanner;

class JugadorHumano implements Jugador, Serializable {
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
}
class CPUFacil implements Jugador,Serializable{
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
            System.out.println("CPU (fácil) elige: " + pos);
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
class CpuDificil implements Jugador,Serializable {
    private String nombre = "CPU";
    private char simbolo;
    private int ganadas, perdidas, empatadas;

    public CpuDificil(char simbolo) {
        this.simbolo = simbolo;
    }
    @Override
    public String getNombre() {
        return nombre;
    }
    @Override
    public char getSimbolo() { return simbolo; }

    @Override
    public int hacerJugada(char[][] tablero) {
        if (tablero[1][1] == '-') {
            System.out.println("CPU (difícil) elige: 5");
            return 5;
        }

        // Fallback aleatorio si centro ocupado
        for (int i = 1; i <= 9; i++) {
            int row = (i - 1) / 3;
            int col = (i - 1) % 3;
            if (tablero[row][col] == '-') {
                System.out.println("CPU (difícil) elige: " + i);
                return i;
            }
        }


        return -1;
    }

    @Override
    public String toString() {
        return "cpu-hard{" +
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
class JugadorFactory{
    public static Jugador crearJugador(String tipo, String nombre, char simbolo) {
        switch (tipo.toLowerCase()) {
            case "humano":
                return new JugadorHumano(nombre,simbolo);
            case "cpu-easy":
                return new CPUFacil(simbolo);
            case "cpu-hard":
                return new CpuDificil(simbolo);
            default:
                throw new IllegalArgumentException("Tipo de jugador inválido");
        }
    }

}
