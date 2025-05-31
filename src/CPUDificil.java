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
        // 1. Intentar ganar
        for (int i = 1; i <= 9; i++) {
            int fila = (i - 1) / 3;
            int col = (i - 1) % 3;
            if (tablero[fila][col] == '-') {
                tablero[fila][col] = 'O';  // Suponiendo que la CPU juega con 'O'
                // Revisar si la CPU gana con este movimiento
                if (
                        (tablero[fila][0] == 'O' && tablero[fila][1] == 'O' && tablero[fila][2] == 'O') ||
                                (tablero[0][col] == 'O' && tablero[1][col] == 'O' && tablero[2][col] == 'O') ||
                                (fila == col && tablero[0][0] == 'O' && tablero[1][1] == 'O' && tablero[2][2] == 'O') ||
                                (fila + col == 2 && tablero[0][2] == 'O' && tablero[1][1] == 'O' && tablero[2][0] == 'O')
                ) {
                    tablero[fila][col] = '-';  // Deshacer simulación
                    System.out.println("CPU (difícil) elige: " + i + " (ganar)");
                    return i;
                }
                tablero[fila][col] = '-';  // Deshacer simulación
            }
        }

        // 2. Bloquear al jugador si puede ganar
        for (int i = 1; i <= 9; i++) {
            int fila = (i - 1) / 3;
            int col = (i - 1) % 3;
            if (tablero[fila][col] == '-') {
                tablero[fila][col] = 'X';  // Suponiendo que el jugador usa 'X'
                // Revisar si el jugador ganaría
                if (
                        (tablero[fila][0] == 'X' && tablero[fila][1] == 'X' && tablero[fila][2] == 'X') ||
                                (tablero[0][col] == 'X' && tablero[1][col] == 'X' && tablero[2][col] == 'X') ||
                                (fila == col && tablero[0][0] == 'X' && tablero[1][1] == 'X' && tablero[2][2] == 'X') ||
                                (fila + col == 2 && tablero[0][2] == 'X' && tablero[1][1] == 'X' && tablero[2][0] == 'X')
                ) {
                    tablero[fila][col] = '-';  // Deshacer simulación
                    System.out.println("CPU (difícil) elige: " + i + " (bloqueo)");
                    return i;
                }
                tablero[fila][col] = '-';  // Deshacer simulación
            }
        }

        // 3. Tomar el centro si está libre
        if (tablero[1][1] == '-') {
            System.out.println("CPU (difícil) elige: 5 (centro)");
            return 5;
        }

        // 4. Tomar las esquinas si hay
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

        // 5. Tomar cualquier casilla libre
        for (int i = 1; i <= 9; i++) {
            int fila = (i - 1) / 3;
            int col = (i - 1) % 3;
            if (tablero[fila][col] == '-') {
                System.out.println("CPU (difícil) elige: " + i + " (aleatorio)");
                return i;
            }
        }

        // 6. No hay jugadas posibles
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
