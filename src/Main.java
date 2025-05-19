import java.io.Serializable;
import java.util.*;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;

interface Observer{
    void actualizar();

}
interface Sujeto{
    void agregarObserver(Observer o);
    void eliminarObserver(Observer o);
    void notificarObserver();
}
interface ComponenteTablero{
    void imprimir();
    int revisarGanador();
    boolean marcarCasilla(int x, int y, char simbolo);

}
interface Jugador extends Serializable {
    String getNombre();
    char getSimbolo();
    int hacerJugada(char[][] tablero);
}

class JuegoBase implements Sujeto{
    Tablero metaTablero;
    GrupoTableros grupoTableros;

    int moneda;
    Jugador jugador1;
    Jugador jugador2;
    Scanner input = new Scanner(System.in);
    Scanner input2 = new Scanner(System.in);

    char simboloGanador;
    char simboloPerdedor ;
    Tablero tablero;
    ArrayList<Integer> listTablero = new ArrayList<>();
    String nombreJ1;
    String tipoJ1;
    String nombreJ2;
    String tipoJ2;

    ArrayList<Observer> observers = new ArrayList<>();

    public JuegoBase(String nombreJ1,String  tipoJ1,String nombreJ2,String tipoJ2){
        this.nombreJ1 = nombreJ1;
        this.tipoJ1 = tipoJ1;
        this.nombreJ2 = nombreJ2;
        this.tipoJ2 = tipoJ2;
        this.tablero = new Tablero();
        agregarObserver(tablero);
        grupoTableros = new GrupoTableros();
        metaTablero = new Tablero();
        metaTablero.crearTablero();

    }

    @Override
    public void agregarObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void eliminarObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notificarObserver() {
        for (Observer o: observers){
            o.actualizar();
        }
    }

    void comenzarJuego(String nombreJ1,String nombreJ2) {

        metaTablero = new Tablero();
        metaTablero.crearTablero();

        Random random = new Random();
        moneda = random.nextInt(2);

        if (moneda == 0) {
            System.out.println("¿Qué símbolo ('x' ó 'o') desea el Jugador 1?");
            if (input.hasNextLine()) {
                simboloGanador = input.nextLine().charAt(0);
            } else {
                System.out.println("No hay entrada disponible.");
            }
            simboloPerdedor = (simboloGanador == 'x') ? 'o' : 'x';

        } else {
            if (tipoJ2.equals("humano")) {
                System.out.println("¿Qué símbolo ('x' ó 'o') desea el Jugador 2?");
                if (input.hasNextLine()) {
                    simboloGanador = input.nextLine().charAt(0);
                } else {
                    System.out.println("No hay entrada disponible.");
                }
                if (simboloGanador == 'x') {
                    simboloPerdedor = 'o';
                }

            } else {
                System.out.println("Como J2 es: CPU, su signo es 'o'");
                simboloGanador = 'o';
                simboloPerdedor = 'x';
            }
        }


        jugador1 = JugadorFactory.crearJugador(tipoJ1, nombreJ1, simboloGanador);
        jugador2 = JugadorFactory.crearJugador(tipoJ2, nombreJ2, simboloPerdedor);

        tablero = new Tablero();
        tablero.crearTablero();
        /*
        x = Plano
        y = Cuadrante [1-9]
         */
        String[] linea;
        int x;
        int y;
        System.out.println("FORMATO DE ENTRADA: x (plano) (espacio) y(cuadrante)");
        int aux=0;
        Serializacion.guardarJugador(jugador1);
        Serializacion.guardarJugador(jugador2);
        ArrayList<Jugador> jugadores = Serializacion.cargarJugadores();

        for (Jugador j : jugadores) {
            System.out.println(j);}

        if (moneda == 0) {
            if (tipoJ2.equals("humano"))while (aux != -1) {
                System.out.println("Escribe coordenada Jugador 1: ");
                if (input.hasNextLine()) {
                    linea = input.nextLine().split(" ");
                    x = Integer.parseInt(linea[0]);
                    y = Integer.parseInt(linea[1]);
                    boolean marcado = grupoTableros.marcarCasilla(x, y, jugador1.getSimbolo());

                    if (!marcado) {
                        System.out.println("Casilla ocupada. Intenta otra vez.");
                        continue;
                    }

                    grupoTableros.imprimir();

                    // Revisión del tablero individual
                    int resultado = grupoTableros.getTablero(x).revisarGanador();

                    TableroIndividual tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                    char[][] plano = tableroInd.getTablero().getTablero();
                    char v = GrupoTableros.verificarGanador(plano);
                    System.out.println("Resultado"+v);
                    listTablero.add(x);

                    if (v == 'x' || v == 'o') {
                        if (jugador1 instanceof JugadorHumano){

                            ((JugadorHumano)jugador1).incrementarGanadas();
                            Serializacion.guardarJugador(jugador1);
                             jugadores = Serializacion.cargarJugadores();
                            for (Jugador j : jugadores) {
                                System.out.println(j);}

                        }

                        System.out.println("¡Jugador " + jugador1.getNombre() + " ha ganado el tablero " + x + "!");
                    }

                    // Revisar inmediatamente si esto hace que gane el meta-tablero
                    int revisionMetaPlano = grupoTableros.revisarGanador();
                    if (revisionMetaPlano == 1 || revisionMetaPlano == 2) {
                        System.out.println("¡Jugador " + revisionMetaPlano + " ha ganado el meta-juego!");
                        aux = revisionMetaPlano;  // Esto detiene el bucle
                        break;
                    }




// --------------------------------------------------------------------------------------------------
                    System.out.println("Escribe coordenada Jugador 2: ");
                    linea = input.nextLine().split(" ");
                    x = Integer.parseInt(linea[0]);
                    y = Integer.parseInt(linea[1]);

                    marcado = grupoTableros.marcarCasilla(x, y, jugador2.getSimbolo());

                    if (!marcado) {
                        System.out.println("Casilla ocupada. Intenta otra vez.");
                        continue;
                    }

                    grupoTableros.imprimir();
                    resultado = grupoTableros.getTablero(x).revisarGanador();
                    System.out.println("Resultado 2: " +resultado +"Tablero 2: " +x);

                    tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                    plano = tableroInd.getTablero().getTablero();
                    v = GrupoTableros.verificarGanador(plano);
                    System.out.println("Resultado"+v);
                    listTablero.add(x);

                    if (v == 'x' || v == 'o') {
                        if (jugador2 instanceof JugadorHumano){
                            ((JugadorHumano)jugador2).incrementarGanadas();
                            Serializacion.guardarJugador(jugador2);
                             jugadores = Serializacion.cargarJugadores();
                            for (Jugador j : jugadores) {
                                System.out.println(j);}
                        }
                        System.out.println("¡Jugador " + jugador2.getNombre() + " ha ganado el tablero " + x + "!");
                    }

                    if (resultado == 1 || resultado == 2) {
                        System.out.println("¡Jugador " + resultado + " ha ganado!");
                        break;
                    }
                    revisionMetaPlano = grupoTableros.revisarGanador();
                    if (revisionMetaPlano == 1 || revisionMetaPlano == 2){
                        System.out.println("¡Jugador " + revisionMetaPlano + " ha ganado!");
                        aux = revisionMetaPlano;
                    }



                }
            }
            else if (tipoJ2.equals("cpu-easy")) {
                while (aux != -1) {
                    System.out.println("Escribe coordenada Jugador 1: ");
                    if (input.hasNextLine()) {
                        linea = input.nextLine().split(" ");
                        x = Integer.parseInt(linea[0]);
                        y = Integer.parseInt(linea[1]);
                        boolean marcado = grupoTableros.marcarCasilla(x, y, jugador1.getSimbolo());

                        if (!marcado) {
                            System.out.println("Casilla ocupada. Intenta otra vez.");
                            continue;
                        }

                        grupoTableros.imprimir();

                        // Revisión del tablero individual
                        int resultado = grupoTableros.getTablero(x).revisarGanador();


                        TableroIndividual tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                        char[][] plano = tableroInd.getTablero().getTablero();
                        char v = GrupoTableros.verificarGanador(plano);
                        System.out.println("Resultado" + v);
                        listTablero.add(x);

                        if (v == 'x' || v == 'o') {
                            if (jugador1 instanceof JugadorHumano) {
                                ((JugadorHumano) jugador1).incrementarGanadas();
                                Serializacion.guardarJugador(jugador1);
                                 jugadores = Serializacion.cargarJugadores();
                                for (Jugador j : jugadores) {
                                    System.out.println(j);}
                            }
                            System.out.println("¡Jugador " + jugador1.getNombre() + " ha ganado el tablero " + x + "!");
                        }

                        // Revisar inmediatamente si esto hace que gane el meta-tablero
                        int revisionMetaPlano = grupoTableros.revisarGanador();
                        if (revisionMetaPlano == 1 || revisionMetaPlano == 2) {
                            System.out.println("¡Jugador " + revisionMetaPlano + " ha ganado el meta-juego!");
                            aux = revisionMetaPlano;  // Esto detiene el bucle
                            break;
                        }
//-----------------------------------------------------------------------------------------
                        System.out.println("La CPU escogiió el plano: ");
                        int planoRandom = random.nextInt(9);
                        int posicionRandom = random.nextInt(9)+1;
                        tableroInd = (TableroIndividual) grupoTableros.getTablero(planoRandom);
                        char[][] tablero = tableroInd.getTablero().getTablero();
                        int jugada = jugador2.hacerJugada(tablero);
                        grupoTableros.marcarCasilla(planoRandom, jugada, jugador2.getSimbolo());
                        grupoTableros.imprimir();
                        resultado = grupoTableros.getTablero(x).revisarGanador();


                        tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                        plano = tableroInd.getTablero().getTablero();
                        v = GrupoTableros.verificarGanador(plano);
                        System.out.println("Resultado" + v);
                        listTablero.add(x);

                        if (v == 'x' || v == 'o') {
                            if (jugador2 instanceof CPUFacil) {
                                ((CPUFacil) jugador2).incrementarGanadas();
                                Serializacion.guardarJugador(jugador2);
                                 jugadores = Serializacion.cargarJugadores();
                                for (Jugador j : jugadores) {
                                    System.out.println(j);}
                            }
                            System.out.println("¡Jugador " + jugador1.getNombre() + " ha ganado el tablero " + x + "!");
                        }

                        // Revisar inmediatamente si esto hace que gane el meta-tablero
                        revisionMetaPlano = grupoTableros.revisarGanador();
                        if (revisionMetaPlano == 1 || revisionMetaPlano == 2) {
                            System.out.println("¡Jugador " + revisionMetaPlano + " ha ganado el meta-juego!");
                            aux = revisionMetaPlano;  // Esto detiene el bucle
                            break;
                        }

                    }
                }
            }
            else if (tipoJ2.equals("cpu-hard")) {
                while (aux != -1) {

                    System.out.println("Escribe coordenada Jugador 1: ");
                    if (input.hasNextLine()) {
                        linea = input.nextLine().split(" ");
                        x = Integer.parseInt(linea[0]);
                        y = Integer.parseInt(linea[1]);
                        boolean marcado = grupoTableros.marcarCasilla(x, y, jugador1.getSimbolo());

                        if (!marcado) {
                            System.out.println("Casilla ocupada. Intenta otra vez.");
                            continue;
                        }

                        grupoTableros.imprimir();

                        // Revisión del tablero individual
                        int resultado = grupoTableros.getTablero(x).revisarGanador();

                        TableroIndividual tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                        char[][] plano = tableroInd.getTablero().getTablero();
                        char v = GrupoTableros.verificarGanador(plano);
                        System.out.println("Resultado" + v);
                        listTablero.add(x);

                        if (v == 'x' || v == 'o') {
                            if (jugador1 instanceof JugadorHumano) {
                                ((JugadorHumano) jugador1).incrementarGanadas();
                            }
                            System.out.println("¡Jugador " + jugador1.getNombre() + " ha ganado el tablero " + x + "!");
                        }

                        // Revisar inmediatamente si esto hace que gane el meta-tablero
                        int revisionMetaPlano = grupoTableros.revisarGanador();
                        if (revisionMetaPlano == 1 || revisionMetaPlano == 2) {
                            System.out.println("¡Jugador " + revisionMetaPlano + " ha ganado el meta-juego!");
                            aux = revisionMetaPlano;  // Esto detiene el bucle
                            break;
                        }
//-------------------------------------------------------------------------------------------------------------

                        int planoRandom = random.nextInt(9);
                        System.out.println("La CPU escogió el plano: " + planoRandom);

                        int posicionRandom = random.nextInt(9)+1;
                        tableroInd = (TableroIndividual) grupoTableros.getTablero(planoRandom);
                        char[][] tablero = tableroInd.getTablero().getTablero();
                        int jugada = jugador2.hacerJugada(tablero);
                        grupoTableros.marcarCasilla(planoRandom, jugada, jugador2.getSimbolo());
                        grupoTableros.imprimir();
                        resultado = grupoTableros.getTablero(x).revisarGanador();


                        tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                        plano = tableroInd.getTablero().getTablero();
                        v = GrupoTableros.verificarGanador(plano);
                        System.out.println("Resultado" + v);
                        listTablero.add(x);

                        if (v == 'x' || v == 'o') {
                            if (jugador2 instanceof CpuDificil) {
                                ((CpuDificil) jugador2).incrementarGanadas();
                                Serializacion.guardarJugador(jugador2);
                                 jugadores = Serializacion.cargarJugadores();
                                for (Jugador j : jugadores) {
                                    System.out.println(j);}
                            }
                            System.out.println("¡Jugador " + jugador2.getNombre() + " ha ganado el tablero " + x + "!");
                        }

                        // Revisar inmediatamente si esto hace que gane el meta-tablero
                        revisionMetaPlano = grupoTableros.revisarGanador();
                        if (revisionMetaPlano == 1 || revisionMetaPlano == 2) {
                            System.out.println("¡Jugador " + revisionMetaPlano + " ha ganado el meta-juego!");
                            aux = revisionMetaPlano;  // Esto detiene el bucle
                            break;
                        }


                        //
                    }
                }

            }


        }else {
            //Caso humano
            if (tipoJ2.equals("humano") ){
                while (aux != -1) {
                    System.out.println("Escribe coordenada Jugador 2: ");
                    if (input.hasNextLine()) {
                        linea = input.nextLine().split(" ");
                        x = Integer.parseInt(linea[0]);
                        y = Integer.parseInt(linea[1]);
                        boolean marcado = grupoTableros.marcarCasilla(x, y, jugador2.getSimbolo());

                        if (!marcado) {
                            System.out.println("Casilla ocupada. Intenta otra vez.");
                            continue;
                        }

                        grupoTableros.imprimir();

                        // Revisión del tablero individual
                        int resultado = grupoTableros.getTablero(x).revisarGanador();

                        TableroIndividual tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                        char[][] plano = tableroInd.getTablero().getTablero();
                        char v = GrupoTableros.verificarGanador(plano);
                        System.out.println("Resultado" + v);
                        listTablero.add(x);

                        if (v == 'x' || v == 'o') {
                            if (jugador2 instanceof JugadorHumano) {
                                ((JugadorHumano) jugador1).incrementarGanadas();
                                Serializacion.guardarJugador(jugador2);
                                 jugadores = Serializacion.cargarJugadores();
                                for (Jugador j : jugadores) {
                                    System.out.println(j);}
                            }
                            System.out.println("¡Jugador " + jugador1.getNombre() + " ha ganado el tablero " + x + "!");
                        }

                        // Revisar inmediatamente si esto hace que gane el meta-tablero
                        int revisionMetaPlano = grupoTableros.revisarGanador();
                        if (revisionMetaPlano == 1 || revisionMetaPlano == 2) {
                            System.out.println("¡Jugador " + revisionMetaPlano + " ha ganado el meta-juego!");
                            aux = revisionMetaPlano;  // Esto detiene el bucle
                            break;
                        }


                        // --------------------------------------------------------------------------------------------------
                        System.out.println("Escribe coordenada Jugador 1: ");
                        linea = input.nextLine().split(" ");
                        x = Integer.parseInt(linea[0]);
                        y = Integer.parseInt(linea[1]);

                        marcado = grupoTableros.marcarCasilla(x, y, jugador1.getSimbolo());

                        if (!marcado) {
                            System.out.println("Casilla ocupada. Intenta otra vez.");
                            continue;
                        }

                        grupoTableros.imprimir();
                        resultado = grupoTableros.getTablero(x).revisarGanador();
                        System.out.println("Resultado 2: " + resultado + "Tablero 2: " + x);

                        tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                        plano = tableroInd.getTablero().getTablero();
                        v = GrupoTableros.verificarGanador(plano);
                        System.out.println("Resultado" + v);
                        listTablero.add(x);

                        if (v == 'x' || v == 'o') {
                            if (jugador1 instanceof JugadorHumano) {
                                ((JugadorHumano) jugador1).incrementarGanadas();
                                Serializacion.guardarJugador(jugador1);
                                 jugadores = Serializacion.cargarJugadores();
                                for (Jugador j : jugadores) {
                                    System.out.println(j);}
                            }
                            System.out.println("¡Jugador " + jugador2.getNombre() + " ha ganado el tablero " + x + "!");
                        }

                        if (resultado == 1 || resultado == 2) {
                            System.out.println("¡Jugador " + resultado + " ha ganado!");
                            break;
                        }
                        revisionMetaPlano = grupoTableros.revisarGanador();
                        if (revisionMetaPlano == 1 || revisionMetaPlano == 2) {
                            System.out.println("¡Jugador " + revisionMetaPlano + " ha ganado!");
                            aux = revisionMetaPlano;
                        }


                    }


                }
            }
//-------------------------------------------------------------
            else if (tipoJ2.equals("cpu-easy")) {


                while (aux != -1) {
                    int planoRandom = random.nextInt(9);
                    System.out.println("La CPU escogió el plano: " + planoRandom);

                    int posicionRandom = random.nextInt(9)+1;
                    TableroIndividual tableroInd = (TableroIndividual) grupoTableros.getTablero(planoRandom);
                    char[][] tablero = tableroInd.getTablero().getTablero();
                    int jugada = jugador2.hacerJugada(tablero);
                    grupoTableros.marcarCasilla(planoRandom, jugada, jugador2.getSimbolo());
                    grupoTableros.imprimir();

                    char[][] plano = tableroInd.getTablero().getTablero();
                    char v = GrupoTableros.verificarGanador(plano);
                    System.out.println("Resultado" + v);
                    listTablero.add(planoRandom);

                    if (v == 'x' || v == 'o') {
                        if (jugador2 instanceof CPUFacil) {
                            ((CPUFacil) jugador2).incrementarGanadas();
                            Serializacion.guardarJugador(jugador2);
                             jugadores = Serializacion.cargarJugadores();
                            for (Jugador j : jugadores) {
                                System.out.println(j);}
                        }
                        System.out.println("¡Jugador " + jugador2.getNombre() + " ha ganado el tablero " + planoRandom + "!");
                    }

                    // Revisar inmediatamente si esto hace que gane el meta-tablero
                    int revisionMetaPlano = grupoTableros.revisarGanador();
                    if (revisionMetaPlano == 1 || revisionMetaPlano == 2) {
                        System.out.println("¡Jugador " + revisionMetaPlano + " ha ganado el meta-juego!");
                        aux = revisionMetaPlano;  // Esto detiene el bucle
                        break;
                    }


                    //
                    System.out.println("Escribe coordenada Jugador 1: ");
                    if (input.hasNextLine()) {
                        linea = input.nextLine().split(" ");
                        x = Integer.parseInt(linea[0]);
                        y = Integer.parseInt(linea[1]);
                        boolean marcado = grupoTableros.marcarCasilla(x, y, jugador1.getSimbolo());

                        if (!marcado) {
                            System.out.println("Casilla ocupada. Intenta otra vez.");
                            continue;
                        }

                        grupoTableros.imprimir();

                        // Revisión del tablero individual
                        int resultado = grupoTableros.getTablero(x).revisarGanador();

                        tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                        plano = tableroInd.getTablero().getTablero();
                        v = GrupoTableros.verificarGanador(plano);
                        System.out.println("Resultado" + v);
                        listTablero.add(x);

                        if (v == 'x' || v == 'o') {
                            if (jugador1 instanceof JugadorHumano) {
                                ((JugadorHumano) jugador1).incrementarGanadas();
                            }
                            System.out.println("¡Jugador " + jugador1.getNombre() + " ha ganado el tablero " + x + "!");
                        }

                        // Revisar inmediatamente si esto hace que gane el meta-tablero
                        revisionMetaPlano = grupoTableros.revisarGanador();
                        if (revisionMetaPlano == 1 || revisionMetaPlano == 2) {
                            System.out.println("¡Jugador " + revisionMetaPlano + " ha ganado el meta-juego!");
                            aux = revisionMetaPlano;  // Esto detiene el bucle
                            break;
                        }
                    }
                }


            }
            else if (tipoJ2.equals("cpu-hard")) {
                while (aux != -1) {
                    int planoRandom = random.nextInt(9);
                    System.out.println("La CPU escogió el plano: " + planoRandom);

                    int posicionRandom = random.nextInt(9)+1;
                    TableroIndividual tableroInd = (TableroIndividual) grupoTableros.getTablero(planoRandom);
                    char[][] tablero = tableroInd.getTablero().getTablero();
                    int jugada = jugador2.hacerJugada(tablero);
                    grupoTableros.marcarCasilla(planoRandom, jugada, jugador2.getSimbolo());
                    grupoTableros.imprimir();

                    char[][] plano = tableroInd.getTablero().getTablero();
                    char v = GrupoTableros.verificarGanador(plano);
                    System.out.println("Resultado" + v);
                    listTablero.add(planoRandom);

                    if (v == 'x' || v == 'o') {
                        if (jugador2 instanceof CPUFacil) {
                            ((CPUFacil) jugador2).incrementarGanadas();
                            Serializacion.guardarJugador(jugador2);
                             jugadores = Serializacion.cargarJugadores();
                            for (Jugador j : jugadores) {
                                System.out.println(j);}
                        }
                        System.out.println("¡Jugador " + jugador2.getNombre() + " ha ganado el tablero " + planoRandom + "!");
                    }

                    // Revisar inmediatamente si esto hace que gane el meta-tablero
                    int revisionMetaPlano = grupoTableros.revisarGanador();
                    if (revisionMetaPlano == 1 || revisionMetaPlano == 2) {
                        System.out.println("¡Jugador " + revisionMetaPlano + " ha ganado el meta-juego!");
                        aux = revisionMetaPlano;  // Esto detiene el bucle
                        break;
                    }



                    //
                    System.out.println("Escribe coordenada Jugador 1: ");
                    if (input.hasNextLine()) {
                        linea = input.nextLine().split(" ");
                        x = Integer.parseInt(linea[0]);
                        y = Integer.parseInt(linea[1]);
                        boolean marcado = grupoTableros.marcarCasilla(x, y, jugador1.getSimbolo());

                        if (!marcado) {
                            System.out.println("Casilla ocupada. Intenta otra vez.");
                            continue;
                        }

                        grupoTableros.imprimir();

                        // Revisión del tablero individual
                        int resultado = grupoTableros.getTablero(x).revisarGanador();

                        tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                        plano = tableroInd.getTablero().getTablero();
                        v = GrupoTableros.verificarGanador(plano);
                        System.out.println("Resultado" + v);
                        listTablero.add(x);

                        if (v == 'x' || v == 'o') {
                            if (jugador1 instanceof JugadorHumano) {
                                ((JugadorHumano) jugador1).incrementarGanadas();
                            }
                            System.out.println("¡Jugador " + jugador1.getNombre() + " ha ganado el tablero " + x + "!");
                        }

                        // Revisar inmediatamente si esto hace que gane el meta-tablero
                        revisionMetaPlano = grupoTableros.revisarGanador();
                        if (revisionMetaPlano == 1 || revisionMetaPlano == 2) {
                            System.out.println("¡Jugador " + revisionMetaPlano + " ha ganado el meta-juego!");
                            aux = revisionMetaPlano;  // Esto detiene el bucle
                            break;
                        }
                    }
                }


            }

        }






    }

}

class Menu{
    Scanner input = new Scanner(System.in);
    String nombreJ1;
    String nombreJ2;
    JuegoBase tableroGrande;

    void menu(){
        System.out.println("Bienvenido al Juego seleccione el modo de Juego:");
        System.out.println("Registrese para jugar.");
        nombreJ1 = input.nextLine();

        System.out.println("Bienvenido Jugador 1: " + nombreJ1);
        System.out.println("1. Jugador vs Jugador \n2.Jugador vs CPU (Easy)\n3.Jugador vs CPU(Hard)");

        int inputUser = input.nextInt();
        switch (inputUser){
            case 1:
                input.nextLine();
                System.out.println("Registre el Jugador 2");

                nombreJ2 = input.nextLine();
                System.out.println("Bienvenido Jugador 2: "+nombreJ2);
                tableroGrande = new JuegoBase(nombreJ1,"humano",nombreJ2,"humano");
                break;
            case 2:
                nombreJ2 = "CPU";
                System.out.println("Bienvenido Jugador 2 (Easy): "+nombreJ2);

                tableroGrande = new JuegoBase(nombreJ1,"humano",nombreJ2,"cpu-easy");

                break;
            case 3:
                nombreJ2 = "CPU";
                tableroGrande = new JuegoBase(nombreJ1,"humano",nombreJ2,"cpu-hard");

                break;
        }
        input.nextLine();
        tableroGrande.comenzarJuego(nombreJ1,nombreJ2);



    }

}
public class Main {
    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.menu();
    }
}