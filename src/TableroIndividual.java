public class TableroIndividual implements ComponenteTablero{
    Tablero tablero;
    public TableroIndividual(){
        tablero = new Tablero();
        tablero.crearTablero();
    }


    @Override
    public void imprimir() {
        tablero.imprimirTablero();
    }

    void rellenar(char signo){
        for (int i= 0; i< 3; i++){;
            for (int j= 0;j<3 ; j++){
                tablero.getTablero()[i][j] = signo;
            };
        }
    }
    @Override
    public int revisarGanador() {
        int[][] meta = new int[3][3];

        // Revisa filas
        for (int i = 0; i < 3; i++) {
            if (meta[i][0] != -1 && meta[i][0] == meta[i][1] && meta[i][1] == meta[i][2]) {
                return meta[i][0];
            }
        }

        // Revisa columnas
        for (int j = 0; j < 3; j++) {
            if (meta[0][j] != -1 && meta[0][j] == meta[1][j] && meta[1][j] == meta[2][j]) {
                return meta[0][j];
            }
        }

        // Diagonales
        if (meta[0][0] != -1 && meta[0][0] == meta[1][1] && meta[1][1] == meta[2][2]) {
            return meta[0][0];
        }
        if (meta[0][2] != -1 && meta[0][2] == meta[1][1] && meta[1][1] == meta[2][0]) {
            return meta[0][2];
        }

        return -1; // Nadie ha ganado todavía
    }
    public boolean marcar(int posicion, char simbolo){
        return tablero.marcarCasilla(posicion,simbolo);
    }

    public Tablero getTablero() {
        return tablero;
    }

    @Override
    public boolean marcarCasilla(int x, int posicion, char simbolo) {
        int i = (posicion - 1) / 3;
        int j = (posicion - 1) % 3;
        if (tablero.getTablero()[i][j] == '-') {
            tablero.getTablero()[i][j] = simbolo;

            return true;
        }
        return false;
    }





}
