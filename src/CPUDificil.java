import java.io.Serializable;

public class CPUDificil implements Jugador, Serializable {
    private String nombre = "CPU";
    private char simbolo;
    private int ganadas, perdidas, empatadas;

    public CPUDificil(char simbolo) {
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
        for (int i = 1; i <= 9; i++) {
            int fila = (i - 1) / 3;
            int col = (i - 1) % 3;
            if (tablero[fila][col] == '-') {
                //Simbolo por el cual es simbolo
                tablero[fila][col] = simbolo;
                if (
                        (tablero[fila][0] == simbolo && tablero[fila][1] == simbolo && tablero[fila][2] == simbolo) ||
                                (tablero[0][col] == simbolo && tablero[1][col] == simbolo && tablero[2][col] == simbolo) ||
                                (fila == col && tablero[0][0] == simbolo && tablero[1][1] == simbolo && tablero[2][2] == simbolo) ||
                                (fila + col == 2 && tablero[0][2] == simbolo && tablero[1][1] == simbolo && tablero[2][0] == simbolo)
                ) {
                    tablero[fila][col] = '-';
                    System.out.println("CPU (difícil) elige: " + i + " (ganar)");
                    return i;
                }
                tablero[fila][col] = '-';
            }
        }

        char simboloPerdedor = (simbolo == 'o')?'x':'o';
        for (int i = 1; i <= 9; i++) {
            int fila = (i - 1) / 3;
            int col = (i - 1) % 3;
            if (tablero[fila][col] == '-') {
                tablero[fila][col] = simboloPerdedor;

                if (
                        (tablero[fila][0] == simboloPerdedor && tablero[fila][1] == simboloPerdedor && tablero[fila][2] == simboloPerdedor) ||
                                (tablero[0][col] == simbolo && tablero[1][col] == simboloPerdedor && tablero[2][col] == simboloPerdedor) ||
                                (fila == col && tablero[0][0] == simboloPerdedor && tablero[1][1] == simboloPerdedor && tablero[2][2] == simboloPerdedor) ||
                                (fila + col == 2 && tablero[0][2] == simboloPerdedor && tablero[1][1] == simboloPerdedor && tablero[2][0] == simboloPerdedor)
                ) {
                    tablero[fila][col] = '-';
                    System.out.println("CPU (difícil) elige: " + i + " (bloqueo)");
                    return i;
                }
                tablero[fila][col] = '-';
            }
        }

        if (tablero[1][1] == '-') {
            System.out.println("CPU (difícil) elige: 5 (centro)");
            return 5;
        }

        int[] esquinas = {1, 3, 7, 9};
        for (int idx = 0; idx < 4; idx++) {
            int i = esquinas[idx];
            int fila = (i - 1) / 3;
            int col = (i - 1) % 3;
            if (tablero[fila][col] == '-') {
                System.out.println("CPU (difícil) elige: " + i + " (esquina)");
                return i;
            }
        }

        for (int i = 1; i <= 9; i++) {
            int fila = (i - 1) / 3;
            int col = (i - 1) % 3;
            if (tablero[fila][col] == '-') {
                System.out.println("CPU (difícil) elige: " + i + " (aleatorio)");
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
