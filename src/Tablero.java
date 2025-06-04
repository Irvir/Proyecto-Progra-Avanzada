class Tablero implements Observer {
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
        for (int i = 0; i < 3; i++) {
            if (tablero[i][0] != '-' && tablero[i][0] == tablero[i][1] && tablero[i][1] == tablero[i][2]) {
                return tablero[i][0] == 'x' ? 1 : 2;
            }
        }

        for (int j = 0; j < 3; j++) {
            if (tablero[0][j] != '-' && tablero[0][j] == tablero[1][j] && tablero[1][j] == tablero[2][j]) {
                return tablero[0][j] == 'x' ? 1 : 2;
            }
        }

        if (tablero[0][0] != '-' && tablero[0][0] == tablero[1][1] && tablero[1][1] == tablero[2][2]) {
            return tablero[0][0] == 'x' ? 1 : 2;
        }

        if (tablero[0][2] != '-' && tablero[0][2] == tablero[1][1] && tablero[1][1] == tablero[2][0]) {
            return tablero[0][2] == 'x' ? 1 : 2;
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[i][j] == '-') {
                    return -1;
                }
            }
        }

        return 0;
    }

    @Override

    public void actualizar(Jugador ganador, Jugador perdedor) {

        System.out.println("¡Felicidades! El jugador " + ganador.getNombre() + " ha ganado.");
        //Caso CPU - Facil
        if (ganador instanceof CPUFacil){
            ((CPUFacil)ganador).incrementarGanadas();
            ((JugadorHumano)perdedor).incrementarPerdidas();


        //Caso CPU - Dificil
        }
        if (ganador instanceof CPUDificil){
            ((CPUDificil)ganador).incrementarGanadas();
            ((JugadorHumano)perdedor).incrementarPerdidas();


        }
        //Caso J2 - J1 y viceversa

        if (ganador instanceof JugadorHumano){
            ((JugadorHumano)ganador).incrementarGanadas();
            ((JugadorHumano)perdedor).incrementarPerdidas();

        }
        //Caso J1 - cpu facil
        if (ganador instanceof JugadorHumano&& perdedor instanceof CPUFacil){
            ((JugadorHumano)ganador).incrementarGanadas();
            ((CPUFacil)ganador).incrementarPerdidas();

        }
        if (ganador instanceof JugadorHumano&& perdedor instanceof CPUDificil){
            ((JugadorHumano)ganador).incrementarGanadas();
            ((CPUDificil)ganador).incrementarPerdidas();

        }
        Serializacion.guardarJugador(ganador);
        Serializacion.guardarJugador(perdedor);



    }


}
