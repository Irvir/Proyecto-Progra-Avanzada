import java.util.ArrayList;
class Tablero implements Observer {
    JuegoBase juego;
    char[][] tablero = new char[3][3];

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

    boolean marcarCasilla(int posicion, char simbolo) {
        int i = (posicion - 1) / 3;
        int j = (posicion - 1) % 3;
        if (tablero[i][j] == '-') {
            tablero[i][j] = simbolo;
            return true;
        }
        return false;
    }

    public char[][] getTablero() {
        return tablero;
    }

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

        // Revisar diagonal principal
        if (tablero[0][0] != '-' && tablero[0][0] == tablero[1][1] && tablero[1][1] == tablero[2][2]) {
            return tablero[0][0] == 'x' ? 1 : 2;
        }
        // Revisar diagonal secundaria
        if (tablero[0][2] != '-' && tablero[0][2] == tablero[1][1] && tablero[1][1] == tablero[2][0]) {
            return tablero[0][2] == 'x' ? 1 : 2;
        }

        // Si no hay ganador
        return -1;
    }

    @Override
    public void actualizar() {
        System.out.println("Tablero actualizado");
        imprimirTablero();
    }
}
class TableroIndividual implements ComponenteTablero{
    Tablero tablero;

    @Override
    public void imprimir() {
        System.out.println(tablero);
    }

    @Override
    public int revisarGanador() {
        tablero.tableroGanado();
        return -1;
    }
}
class GrupoTableros implements ComponenteTablero{
    private ArrayList<ComponenteTablero> listaTablero = new ArrayList<>();
    public GrupoTableros(ComponenteTablero t){
        listaTablero.add(t);
    }

    @Override
    public void imprimir() {
        System.out.println(listaTablero);
    }

    @Override
    public int revisarGanador() {
        for (ComponenteTablero tablero: listaTablero){
            int resultado = tablero.revisarGanador();
            if (resultado!=-1)return resultado;
        }
        return -1;
    }
}