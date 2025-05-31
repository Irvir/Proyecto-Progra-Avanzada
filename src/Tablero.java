import java.util.ArrayList;
class Tablero implements Observer {
    private JuegoBase juego;
    private char[][] tablero = new char[3][3];

    void crearTablero() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tablero[i][j] = '-';
            }
            ;
        }
    }

    void imprimirTablero() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(" " + tablero[i][j]);
                if (j < 2) System.out.print(" |");
            }
            System.out.println();
            if (i < 2) {
                System.out.println("---+---+---");
            }
        }
    }
    //Marca casilla si es que no está marca saca un true, caso contrario saca un falso
    boolean marcarCasilla(int posicion, char simbolo) {
        int i = (posicion - 1) / 3;
        int j = (posicion - 1) % 3;
        if (tablero[i][j] == '-') {
            tablero[i][j] = simbolo;
            return true;
        }
        return false;
    }
    public char getCasilla(int fila, int columna){
        return tablero[fila][columna];
    }
    public void setCasilla(int fila, int columna,char simbolo){
        tablero[fila][columna] = simbolo;
    }
    public char[][] getTablero() {
        return tablero;
    }
    /*
    Retorna
    1: Jugador 1 Gana
    2: Jugador 2 Gana
    0: Empate
    -1: Ninguno de los 2 ganó
     */
    public int tableroGanado() {
        //Filas
        for (int i = 0; i < 3; i++) {
            if (tablero[i][0] != '-' && tablero[i][0] == tablero[i][1] && tablero[i][1] == tablero[i][2]) {
                return tablero[i][0] == 'x' ? 1 : 2;
            }
        }

        // Columnas
        for (int j = 0; j < 3; j++) {
            if (tablero[0][j] != '-' && tablero[0][j] == tablero[1][j] && tablero[1][j] == tablero[2][j]) {
                return tablero[0][j] == 'x' ? 1 : 2;
            }
        }

        // Diagonal principal
        if (tablero[0][0] != '-' && tablero[0][0] == tablero[1][1] && tablero[1][1] == tablero[2][2]) {
            return tablero[0][0] == 'x' ? 1 : 2;
        }

        // Diagonal secundaria
        if (tablero[0][2] != '-' && tablero[0][2] == tablero[1][1] && tablero[1][1] == tablero[2][0]) {
            return tablero[0][2] == 'x' ? 1 : 2;
        }

        // Verificar si el tablero está lleno (empate)
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[i][j] == '-') {
                    return -1; // Aún quedan espacios, sigue el juego
                }
            }
        }

        return 0; // Tablero lleno, empate
    }
    void rellenar(char signo){
        for (int i= 0; i< 3; i++){;
            for (int j= 0;j<3 ; j++){
                tablero[i][j] = signo;
            };
        }
    }


    @Override
    public void actualizar() {
        System.out.println("Tablero actualizado");
        imprimirTablero();
    }

}
