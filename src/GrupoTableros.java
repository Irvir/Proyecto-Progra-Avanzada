import java.util.ArrayList;

public class GrupoTableros implements ComponenteTablero{
    private ArrayList<ComponenteTablero> listaTablero = new ArrayList<>();
    //rellena el Grupo de tablero con 9
    public GrupoTableros(){
        for (int i=0;i<9;i++){
            listaTablero.add(new TableroIndividual());
        }
    }
    public ComponenteTablero getTablero(int index){
        return listaTablero.get(index);
    }

    @Override
    public void imprimir() {
        for (int i = 0; i < 9; i++) {
            System.out.println("Tablero " + i + ":");
            listaTablero.get(i).imprimir();
            System.out.println();
        }
    }
    @Override
    public boolean marcarCasilla(int x,int posicion, char simbolo) {
        int i = (posicion - 1) / 3;
        int j = (posicion - 1) % 3;
        TableroIndividual t = (TableroIndividual) listaTablero.get(x);

        if (t.getTablero().getCasilla(i,j ) == '-') {
            t.getTablero().setCasilla(i, j, simbolo);
            return true;
        }
        return false;
    }

    @Override
    public int revisarGanador() {
        int[][] meta = new int[3][3];

        // Inicializar meta con -1
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                meta[i][j] = -1;
            }
        }

        // Guardar los resultados de los tableros individuales
        for (int i = 0; i < 9; i++) {
            TableroIndividual t = (TableroIndividual) listaTablero.get(i);
            meta[i / 3][i % 3] = t.getTablero().tableroGanado();
        }

        // Verificar filas, columnas y diagonales
        return verificarMatriz(meta);
    }
    private int verificarMatriz(int[][] meta) {

        for (int i = 0; i < 3; i++) {
            if (meta[i][0] != -1 && meta[i][0] == meta[i][1] && meta[i][1] == meta[i][2]) return meta[i][0];
            if (meta[0][i] != -1 && meta[0][i] == meta[1][i] && meta[1][i] == meta[2][i]) return meta[0][i];
        }

        if (meta[0][0] != -1 && meta[0][0] == meta[1][1] && meta[1][1] == meta[2][2]) return meta[0][0];
        if (meta[0][2] != -1 && meta[0][2] == meta[1][1] && meta[1][1] == meta[2][0]) return meta[0][2];

        return -1; // Nadie ha ganado todavía
    }
    //Retorna la fila con el ganador: ex-> retorna x si gano alguien, o 'o' si J2 ganó
    public static char verificarGanador(char[][] plano) {
        // Revisar filas
        for (int i = 0; i < 3; i++) {
            if (plano[i][0] != '-' && plano[i][0] == plano[i][1] && plano[i][1] == plano[i][2]) {
                return plano[i][0];
            }
        }

        // Revisar columnas
        for (int j = 0; j < 3; j++) {
            if (plano[0][j] != '-' && plano[0][j] == plano[1][j] && plano[1][j] == plano[2][j]) {
                return plano[0][j];
            }
        }

        // Revisar diagonal principal
        if (plano[0][0] != '-' && plano[0][0] == plano[1][1] && plano[1][1] == plano[2][2]) {
            return plano[0][0];
        }

        // Revisar diagonal secundaria
        if (plano[0][2] != '-' && plano[0][2] == plano[1][1] && plano[1][1] == plano[2][0]) {
            return plano[0][2];
        }

        // Si no hay ganador
        return '-';
    }

}
