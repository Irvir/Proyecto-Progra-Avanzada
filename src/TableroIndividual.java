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
    //Devuelve x o o, si nadie ha ganado '-'
    public int revisarGanador() {
        char[][] casillas = tablero.getTablero();
        for (int i = 0; i < 3; i++) {
            if (casillas[i][0] != '-' && casillas[i][0] == casillas[i][1] && casillas[i][1] == casillas[i][2]) {
                return casillas[i][0];
            }
        }

        for (int j = 0; j < 3; j++) {
            if (casillas[0][j] != '-' && casillas[0][j] == casillas[1][j] && casillas[1][j] == casillas[2][j]) {
                return casillas[0][j];
            }
        }

        if (casillas[0][0] != '-' && casillas[0][0] == casillas[1][1] && casillas[1][1] == casillas[2][2]) {
            return casillas[0][0];
        }

        if (casillas[0][2] != '-' && casillas[0][2] == casillas[1][1] && casillas[1][1] == casillas[2][0]) {
            return casillas[0][2];
        }

        return '-';
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
    //false si no hay casillas libres, true si está lleno
    public boolean estaCompleto() {
        char[][] casillas = tablero.getTablero();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (casillas[i][j] == '-') {
                    return false;
                }
            }
        }
        return true;
    }






}
