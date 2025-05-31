import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

// Implementa la interfaz Sujeto ya que esté va a avisar cuando gane
public class JuegoBase implements Sujeto{
    //Instancia de MetaTablero (El que define si se gana o se pierde)
    private Tablero metaTablero;
    // Instancia de la ClaseTableros que después uso para agregar Tableros Individuales
    private GrupoTableros grupoTableros;
    // Instancia de Jugador 1
    private Jugador jugador1;
    // Instancia de Jugador 2
    private Jugador jugador2;
    // Inputs del usuario
    private Scanner input = new Scanner(System.in);
    private Scanner input2 = new Scanner(System.in);

    //Simbolo con que el marca el que primero Comienza
    private char simboloGanador;
    //Simbolo con que el marca el segundo
    private char simboloPerdedor ;
    //Instancia de tableroIndividual
    private Tablero tablero;
    //Lista para agregarTableros
    private ArrayList<Integer> listTablero = new ArrayList<>();
    //Nombre del Jugador 1
    private String nombreJ1;
    //Tipo del Jugador 1 -> Humano-CPU-easy-CPU-Hard
    private String tipoJ1;
    //Nombre del Jugador 2
    private String nombreJ2;
    //Tipo del Jugador 2 -> Humano-CPU-easy-CPU-Hard
    private String tipoJ2;
    //Arraylist del Patron del diseño Observer
    private ArrayList<Observer> observers = new ArrayList<>();

    /**
     *
     * @param nombreJ1 Nombre del Jugador
     * @param tipoJ1 Tipo del Jugador
     * @param nombreJ2 Nombre del Jugador
     * @param tipoJ2 Tipo del Jugador
     */
    public JuegoBase(String nombreJ1,String  tipoJ1,String nombreJ2,String tipoJ2){
        this.nombreJ1 = nombreJ1;
        this.tipoJ1 = tipoJ1;
        this.nombreJ2 = nombreJ2;
        this.tipoJ2 = tipoJ2;
        this.tablero = new Tablero();
        //Agrega el Observer al Tablero
        agregarObserver(tablero);
        //Crea Instancia de Grupo de Tableros
        grupoTableros = new GrupoTableros();
        //Crea Instancia del Metatablero
        metaTablero = new Tablero();
        metaTablero.crearTablero();

    }

    /**
     *
     * @param o método del observer
     */
    @Override
    public void agregarObserver(Observer o) {
        observers.add(o);
    }
    /**
     *
     * @param o método del observer
     */
    @Override
    public void eliminarObserver(Observer o) {
        observers.remove(o);
    }
    //Notifica que ocurra algo
    @Override
    public void notificarObserver() {
        for (Observer o: observers){
            o.actualizar();
        }
    }

    /**
     *
     * @param nombreJ1 Nombre Jugador 1
     * @param nombreJ2 Nombre Jugador 2
     */
    void comenzarJuego(String nombreJ1,String nombreJ2) {
        //Crea instancia de metaTablero
        metaTablero = new Tablero();
        metaTablero.crearTablero();
        //Crea random que es para los dados para decidir quien va a ganar
        Random random = new Random();
        //Dado 1 va desde el 1 - 6
        int dado1 = random.nextInt(6)+1;
        //Dado 2 va desde el 1 - 6
        int dado2 = random.nextInt(6)+1;
        //El resultadoDado1 es la suma de los dados 1 y 2
        int resultadoDado1 = dado1+dado2;
        System.out.println("Lanzando los dados... RESULTADO JUGADOR 1: "+ resultadoDado1);

        //Dado 1 va desde el 1 - 6
        dado1 = random.nextInt(6)+1;
        //Dado 2 va desde el 1 - 6
        dado2 = random.nextInt(6)+1;
        //El resultadoDado2 es la suma de los dados 1 y 2
        int resultadoDado2 = dado1 + dado2;
        System.out.println("Lanzando los dados... RESULTADO JUGADOR 2: "+ resultadoDado2);

        //Si el resultadoDado1 es mayor a resultadoDado2 gana el Jugador1
        if (resultadoDado1>resultadoDado2) {
            System.out.println("GANÓ JUGADOR 1");
            System.out.println("¿Qué símbolo ('x' ó 'o') desea el Jugador 1?");
            //Escoje el simbolo Ganador
            if (input.hasNextLine()) {
                simboloGanador = input.nextLine().charAt(0);
            } else {
                System.out.println("No hay entrada disponible.");
            }
            //Se escoje el simbolo opuesto
            simboloPerdedor = (simboloGanador == 'x') ? 'o' : 'x';

        }
        //Si gana el DadoResultado2 (Jugador 2)
        else {
            System.out.println("GANÓ JUGADOR 2");
            if (tipoJ2.equals("humano")) {
                System.out.println("¿Qué símbolo ('x' ó 'o') desea el Jugador 2?");
                //Escoje el Jugador2 el simboloGanador
                if (input.hasNextLine()) {
                    simboloGanador = input.nextLine().charAt(0);
                } else {
                    System.out.println("No hay entrada disponible.");
                }
                if (simboloGanador == 'x') {
                    simboloPerdedor = 'o';
                }

            }
            //Si Jugador 2 es un CPU
            else {
                //El signo por defecto de J2 CPU es 'o'
                System.out.println("Como J2 es: CPU, su signo es 'o'");
                simboloGanador = 'o';
                simboloPerdedor = 'x';
            }
        }

        //Llama a Jugador 1 y Jugador 2, para crearlos con JugadorFactory
        jugador1 = JugadorFactory.crearJugador(tipoJ1, nombreJ1, simboloGanador);
        jugador2 = JugadorFactory.crearJugador(tipoJ2, nombreJ2, simboloPerdedor);
        //Crea un nuevo Tablero
        tablero = new Tablero();
        tablero.crearTablero();

        String[] linea;
        int x;
        int y;
        System.out.println("FORMATO DE ENTRADA: x (plano) (espacio) y(cuadrante)");
        int aux=0;
        //Guarda los estados de los jugadores
        Serializacion.guardarJugador(jugador1);
        Serializacion.guardarJugador(jugador2);
        //Los carga
        ArrayList<Jugador> jugadores = Serializacion.cargarJugadores();

        //Si Jugador1 > Jugador 2
        x = 0;
        y = 0;
        int turno = 0;
        if (resultadoDado1>resultadoDado2) {
            //Si jugador es humano
            if (tipoJ2.equals("humano")){
                while (aux != -1) {

                    // TURNO JUGADOR 1
                    System.out.println("Escribe coordenada Jugador 1 o 'rendirse': ");
                    String entrada = input.nextLine();

                    boolean bandera = false;

                    if (entrada.equalsIgnoreCase("rendirse") && turno > 0) {
                        if (jugador2 instanceof JugadorHumano) {
                            ((JugadorHumano) jugador2).incrementarGanadas();
                            Serializacion.guardarJugador(jugador2);
                            jugadores = Serializacion.cargarJugadores();
                            for (Jugador j : jugadores) {
                                System.out.println(j);
                            }
                            TableroIndividual tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                            tableroInd.rellenar(simboloPerdedor);
                            char[][] plano = tableroInd.getTablero().getTablero();
                            char v = GrupoTableros.verificarGanador(plano);
                            System.out.println("¡Jugador " + jugador2.getNombre() + " ha ganado el tablero " + x + "!");
                            bandera = true;
                        }
                        System.out.println("¡Jugador 1 se ha rendido! Turno para Jugador 2.");
                        continue;
                    }

                    linea = entrada.split(" ");
                    x = Integer.parseInt(linea[0]);
                    y = Integer.parseInt(linea[1]);

                    boolean marcado = grupoTableros.marcarCasilla(x, y, jugador1.getSimbolo());
                    if (!marcado) {
                        System.out.println("Casilla ocupada. Intenta otra vez.");
                        continue;
                    }

                    grupoTableros.imprimir();

                    TableroIndividual tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                    char[][] plano = tableroInd.getTablero().getTablero();
                    char v = GrupoTableros.verificarGanador(plano);
                    listTablero.add(x);
                    System.out.println("Resultado Jugador 1: " + v);

                    if (v == 'x' || v == 'o') {
                        if (jugador1 instanceof JugadorHumano) {
                            ((JugadorHumano) jugador1).incrementarGanadas();
                            Serializacion.guardarJugador(jugador1);
                            jugadores = Serializacion.cargarJugadores();
                            for (Jugador j : jugadores) {
                                System.out.println(j);
                            }
                        }
                        System.out.println("¡Jugador " + jugador1.getNombre() + " ha ganado el tablero " + x + "!");
                    }

                    int revisionMetaPlano = grupoTableros.revisarGanador();
                    if (revisionMetaPlano == 1 || revisionMetaPlano == 2) {
                        System.out.println("¡Jugador " + revisionMetaPlano + " ha ganado el meta-juego!");
                        aux = revisionMetaPlano;
                        break;
                    }

                    // TURNO JUGADOR 2
                    // Jugador 2 DEBE jugar en el tablero igual a 'y'
                    int tableroDestino = y;
                    TableroIndividual tableroDestinoInd = (TableroIndividual) grupoTableros.getTablero(tableroDestino);
                    aux = tableroDestinoInd.revisarGanador();
                    // Si el tablero de destino ya está completo o ganado, puede elegir libremente
                    if (aux ==-1 || GrupoTableros.verificarGanador(tableroDestinoInd.getTablero().getTablero()) != ' ') {
                        System.out.println("El tablero " + tableroDestino + " está completo o ganado. Puedes elegir cualquier tablero.");
                        System.out.println("Escribe coordenada Jugador 2 o 'rendirse': ");
                        entrada = input.nextLine();
                        if (entrada.equalsIgnoreCase("rendirse")) {
                            if (jugador1 instanceof JugadorHumano) {
                                ((JugadorHumano) jugador1).incrementarGanadas();
                                Serializacion.guardarJugador(jugador1);
                                jugadores = Serializacion.cargarJugadores();
                                for (Jugador j : jugadores) {
                                    System.out.println(j);
                                }
                                tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                                tableroInd.rellenar(simboloGanador);
                                plano = tableroInd.getTablero().getTablero();
                                v = GrupoTableros.verificarGanador(plano);
                                System.out.println("¡Jugador " + jugador1.getNombre() + " ha ganado el tablero " + x + "!");
                            }
                            System.out.println("¡Jugador 2 se ha rendido! Turno para Jugador 1.");
                            continue;
                        }

                        linea = entrada.split(" ");
                        x = Integer.parseInt(linea[0]);
                        y = Integer.parseInt(linea[1]);
                    } else {
                        System.out.println("Debes jugar en el tablero " + tableroDestino + ". Escribe la casilla: ");
                        entrada = input.nextLine();
                        if (entrada.equalsIgnoreCase("rendirse")) {
                            if (jugador1 instanceof JugadorHumano) {
                                ((JugadorHumano) jugador1).incrementarGanadas();
                                Serializacion.guardarJugador(jugador1);
                                jugadores = Serializacion.cargarJugadores();
                                for (Jugador j : jugadores) {
                                    System.out.println(j);
                                }
                                tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                                tableroInd.rellenar(simboloGanador);
                                plano = tableroInd.getTablero().getTablero();
                                v = GrupoTableros.verificarGanador(plano);
                                System.out.println("¡Jugador " + jugador1.getNombre() + " ha ganado el tablero " + x + "!");
                            }
                            System.out.println("¡Jugador 2 se ha rendido! Turno para Jugador 1.");
                            continue;
                        }

                        linea = entrada.split(" ");
                        x = tableroDestino;  // el tablero está fijo por la jugada anterior
                        y = Integer.parseInt(linea[0]);
                    }

                    marcado = grupoTableros.marcarCasilla(x, y, jugador2.getSimbolo());
                    if (!marcado) {
                        System.out.println("Casilla ocupada. Intenta otra vez.");
                        continue;
                    }

                    grupoTableros.imprimir();

                    tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                    plano = tableroInd.getTablero().getTablero();
                    v = GrupoTableros.verificarGanador(plano);
                    listTablero.add(x);
                    System.out.println("Resultado Jugador 2: " + v);

                    if (v == 'x' || v == 'o') {
                        if (jugador2 instanceof JugadorHumano) {
                            ((JugadorHumano) jugador2).incrementarGanadas();
                            Serializacion.guardarJugador(jugador2);
                            jugadores = Serializacion.cargarJugadores();
                            for (Jugador j : jugadores) {
                                System.out.println(j);
                            }
                        }
                        System.out.println("¡Jugador " + jugador2.getNombre() + " ha ganado el tablero " + x + "!");
                    }

                    revisionMetaPlano = grupoTableros.revisarGanador();
                    if (revisionMetaPlano == 1 || revisionMetaPlano == 2) {
                        System.out.println("¡Jugador " + revisionMetaPlano + " ha ganado el meta-juego!");
                        aux = revisionMetaPlano;
                        break;
                    }

                    turno++;
                }

            }
            //Si ganó el Jugador1 pero el J2 es CPU easy
            else if (tipoJ2.equals("cpu-easy")) {

                while (aux != -1) {
                    boolean bandera = false;
                    System.out.println("Escribe coordenada Jugador 1: ");

                        linea = input.nextLine().split(" ");
                        if (linea.equals("rendirse") && turno > 0){
                            if (jugador1 instanceof JugadorHumano){

                                ((JugadorHumano)jugador2).incrementarGanadas();
                                Serializacion.guardarJugador(jugador2);
                                jugadores = Serializacion.cargarJugadores();
                                for (Jugador j : jugadores) {
                                    System.out.println(j);}
                                TableroIndividual tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                                tableroInd.rellenar(simboloPerdedor);
                                //Plano aux = al plano Individual
                                char[][] plano = tableroInd.getTablero().getTablero();
                                //Comprueba si hay ganador retorna x o y
                                char v = GrupoTableros.verificarGanador(plano);

                                System.out.println("¡Jugador " + jugador2.getNombre() + " ha ganado el tablero " + x + "!");

                                bandera = true;


                            }
                        }
                        if (bandera == true){
                            y = random.nextInt(8);

                        }else{

                            x = y;
                            y = Integer.parseInt(linea[0]);

                        }

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
//-----------------------Turno Jugador 2------------------------------------------------------------------


                        tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                        char[][] tablero = tableroInd.getTablero().getTablero();
                        y= jugador2.hacerJugada(tablero);
                        grupoTableros.marcarCasilla(x,y, jugador2.getSimbolo());
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


                    turno ++;
                }

            }
            //Si el Jugador1 Gana, pero es CPU - Hard
            else if (tipoJ2.equals("cpu-hard")) {
                while (aux != -1) {
                    boolean bandera = false;
                    System.out.println("Escribe coordenada Jugador 1: ");
                    if (input.hasNextLine()) {
                        linea = input.nextLine().split(" ");
                        if (linea.equals("rendirse")&& turno> 0){
                            if (jugador1 instanceof JugadorHumano){
                                ((JugadorHumano)jugador1).incrementarGanadas();
                                Serializacion.guardarJugador(jugador1);
                                jugadores = Serializacion.cargarJugadores();
                                for (Jugador j : jugadores) {
                                    System.out.println(j);}
                                TableroIndividual tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                                tableroInd.rellenar(simboloPerdedor);
                                //Plano aux = al plano Individual
                                char[][] plano = tableroInd.getTablero().getTablero();
                                //Comprueba si hay ganador retorna x o y
                                char v = GrupoTableros.verificarGanador(plano);

                                System.out.println("¡Jugador " + jugador2.getNombre() + " ha ganado el tablero " + x + "!");
                                bandera = true;
                            }
                        }
                        if (bandera == true){
                            y = Integer.parseInt(linea[0]);

                        }else {
                            x = Integer.parseInt(linea[0]);
                            y = Integer.parseInt(linea[1]);

                        }
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
                       if (bandera== true){
                           y= random.nextInt(9)+1;
                           System.out.println("La CPU escogió la posición: " + x);
                       }

                        tableroInd = (TableroIndividual) grupoTableros.getTablero(y);
                        char[][] tablero = tableroInd.getTablero().getTablero();
                        int jugada = jugador2.hacerJugada(tablero);
                        grupoTableros.marcarCasilla(y, jugada, jugador2.getSimbolo());

                        try {
                            Thread.sleep(2000);

                        }catch (InterruptedException e){
                            Thread.currentThread().interrupt();
                        }
                        grupoTableros.imprimir();
                        resultado = grupoTableros.getTablero(x).revisarGanador();


                        tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                        plano = tableroInd.getTablero().getTablero();
                        v = GrupoTableros.verificarGanador(plano);
                        System.out.println("Resultado" + v);
                        listTablero.add(x);

                        if (v == 'x' || v == 'o') {
                            if (jugador2 instanceof CPUDificil) {
                                ((CPUDificil) jugador2).incrementarGanadas();
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
                    turno ++ ;
                }
            }

        }else {
            //Jugador 1 pierde pero es HUMANO
            if (tipoJ2.equals("humano") ){
                while (aux!= -1) {

                    // TURNO JUGADOR 2
                    System.out.println("Escribe coordenada Jugador 2 o 'rendirse': ");
                    String entrada = input.nextLine();

                    //Si escribe rendirse
                    boolean bandera = false;

                    if (entrada.equalsIgnoreCase("rendirse") && turno > 0) {
                        bandera = true;
                        if (jugador1 instanceof JugadorHumano) {
                            //Incrementa Ganadas
                            ((JugadorHumano) jugador1).incrementarGanadas();
                            //Guarda e imprime
                            Serializacion.guardarJugador(jugador1);
                            jugadores = Serializacion.cargarJugadores();
                            for (Jugador j : jugadores) {
                                System.out.println(j);
                            }

                            TableroIndividual tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                            tableroInd.rellenar(simboloPerdedor);
                            //Plano aux = al plano Individual
                            char[][] plano = tableroInd.getTablero().getTablero();
                            //Comprueba si hay ganador retorna x o y
                            char v = GrupoTableros.verificarGanador(plano);

                            System.out.println("¡Jugador " + jugador1.getNombre() + " ha ganado el tablero " + x + "!");


                        }
                        System.out.println("¡Jugador 1 se ha rendido! Turno para Jugador 2.");
                        bandera = true;
                        continue; // salta al turno de Jugador 2
                    }

                    linea = entrada.split(" ");
                    if(turno<1){
                        bandera = true;
                    }
                    if (bandera == true){
                        x = Integer.parseInt(linea[0]);
                        y = Integer.parseInt(linea[1]);
                    }else{
                        x = y;
                        y = Integer.parseInt(linea[0]);
                    }

                    boolean marcado = grupoTableros.marcarCasilla(x, y, jugador1.getSimbolo());
                    if (!marcado) {
                        System.out.println("Casilla ocupada. Intenta otra vez.");
                        continue; // vuelve a intentar Jugador 1
                    }

                    grupoTableros.imprimir();
                    //Llama al tablero x
                    TableroIndividual tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                    //Plano aux = al plano Individual
                    char[][] plano = tableroInd.getTablero().getTablero();
                    //Comprueba si hay ganador retorna x o y
                    char v = GrupoTableros.verificarGanador(plano);
                    listTablero.add(x);
                    System.out.println("Resultado Jugador 1: " + v);

                    if (v == 'x' || v == 'o') {
                        if (jugador1 instanceof JugadorHumano) {
                            ((JugadorHumano) jugador1).incrementarGanadas();
                            Serializacion.guardarJugador(jugador1);
                            jugadores = Serializacion.cargarJugadores();
                            for (Jugador j : jugadores) {
                                System.out.println(j);
                            }
                        }
                        System.out.println("¡Jugador " + jugador1.getNombre() + " ha ganado el tablero " + x + "!");
                    }
                    //Si no pasa el test de que hay un ganador revisa la casilla
                    int revisionMetaPlano = grupoTableros.revisarGanador();
                    if (revisionMetaPlano == 1 || revisionMetaPlano == 2) {
                        System.out.println("¡Jugador " + revisionMetaPlano + " ha ganado el meta-juego!");
                        aux = revisionMetaPlano;
                        break;
                    }

                    //------------------------------------------------ TURNO JUGADOR 1
                    if(turno < 1){
                        bandera = false;
                    }

                    System.out.println("Escribe coordenada Jugador 1 o 'rendirse': ");
                    if (bandera == true){
                        System.out.println("Como el J2 Se rindió tú escoges el plano y la posición");
                    }
                    entrada = input.nextLine();

                    if (entrada.equalsIgnoreCase("rendirse")) {
                        if (jugador1 instanceof JugadorHumano) {
                            ((JugadorHumano) jugador1).incrementarGanadas();
                            Serializacion.guardarJugador(jugador1);
                            jugadores = Serializacion.cargarJugadores();
                            for (Jugador j : jugadores) {
                                System.out.println(j);
                            }
                            tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                            tableroInd.rellenar(simboloPerdedor);
                            //Plano aux = al plano Individual
                            plano = tableroInd.getTablero().getTablero();
                            //Comprueba si hay ganador retorna x o y
                            v = GrupoTableros.verificarGanador(plano);
                            bandera = true;

                            System.out.println("¡Jugador " + jugador1.getNombre() + " ha ganado el tablero " + x + "!");
                        }
                        System.out.println("¡Jugador 2 se ha rendido! Turno para Jugador 1.");
                        continue; // salta al siguiente ciclo (turno del Jugador 1)
                    }

                    linea = entrada.split(" ");

                    //Si se rindió x se escoje, si no se deja como está
                    if (bandera == true){
                        x = Integer.parseInt(linea[0]);
                        y = Integer.parseInt(linea[1]);
                    }else{
                        x = y;
                        y = Integer.parseInt(linea[0]);
                    }

                    marcado = grupoTableros.marcarCasilla(x, y, jugador2.getSimbolo());
                    if (!marcado) {
                        System.out.println("Casilla ocupada. Intenta otra vez.");
                        continue; // vuelve a intentar Jugador 1
                    }

                    grupoTableros.imprimir();

                    tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                    plano = tableroInd.getTablero().getTablero();
                    v = GrupoTableros.verificarGanador(plano);
                    listTablero.add(x);
                    System.out.println("Resultado Jugador 2: " + v);

                    if (v == 'x' || v == 'o') {
                        if (jugador1 instanceof JugadorHumano) {
                            ((JugadorHumano) jugador1).incrementarGanadas();
                            Serializacion.guardarJugador(jugador1);
                            jugadores = Serializacion.cargarJugadores();
                            for (Jugador j : jugadores) {
                                System.out.println(j);
                            }
                        }
                        System.out.println("¡Jugador " + jugador1.getNombre() + " ha ganado el tablero " + x + "!");
                    }

                    revisionMetaPlano = grupoTableros.revisarGanador();
                    if (revisionMetaPlano == 1 || revisionMetaPlano == 2) {
                        System.out.println("¡Jugador " + revisionMetaPlano + " ha ganado el meta-juego!");
                        aux = revisionMetaPlano;
                        break;
                    }
                    turno ++;
                }
            }
            // Jugador 1 pierde, pero es cpu -easy
            else if (tipoJ2.equals("cpu-easy")) {
                while (aux != -1) {
                    boolean bandera = false;

                    // ---------------------- TURNO CPU (JUGADOR 2) PRIMERO ----------------------
                    x = random.nextInt(8);

                    System.out.println("La CPU escogió el plano: "+ x);

                    TableroIndividual tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                    char[][] tablero = tableroInd.getTablero().getTablero();

                    y = jugador2.hacerJugada(tablero);
                    grupoTableros.marcarCasilla(x, y, jugador2.getSimbolo());
                    grupoTableros.imprimir();

                    int resultado = grupoTableros.getTablero(x).revisarGanador();

                    tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                    char[][] plano = tableroInd.getTablero().getTablero();
                    char v = GrupoTableros.verificarGanador(plano);
                    System.out.println("Resultado: " + v);
                    listTablero.add(x);

                    if (v == 'x' || v == 'o') {
                        if (jugador2 instanceof CPUFacil) {
                            ((CPUFacil) jugador2).incrementarGanadas();
                            Serializacion.guardarJugador(jugador2);
                            jugadores = Serializacion.cargarJugadores();
                            for (Jugador j : jugadores) {
                                System.out.println(j);
                            }
                        }
                        System.out.println("¡Jugador " + jugador2.getNombre() + " ha ganado el tablero " + x + "!");
                    }

                    // Revisar si el meta-tablero tiene ganador después de la jugada CPU
                    int revisionMetaPlano = grupoTableros.revisarGanador();
                    if (revisionMetaPlano == 1 || revisionMetaPlano == 2) {
                        System.out.println("¡Jugador " + revisionMetaPlano + " ha ganado el meta-juego!");
                        aux = revisionMetaPlano;
                        break;
                    }


                    // ---------------------- TURNO JUGADOR HUMANO ----------------------
                    System.out.println("Escribe coordenada Jugador 1: ");
                    linea = input.nextLine().split(" ");

                    if (linea[0].equalsIgnoreCase("rendirse") && turno > 0) {
                        if (jugador1 instanceof JugadorHumano) {
                            ((JugadorHumano) jugador2).incrementarGanadas();
                            Serializacion.guardarJugador(jugador2);
                            jugadores = Serializacion.cargarJugadores();
                            for (Jugador j : jugadores) {
                                System.out.println(j);
                            }
                            tableroInd.rellenar(simboloPerdedor);
                            plano = tableroInd.getTablero().getTablero();
                            v = GrupoTableros.verificarGanador(plano);
                            System.out.println("¡Jugador " + jugador2.getNombre() + " ha ganado el tablero " + x + "!");
                            bandera = true;
                        }
                    }

                    if (bandera == true){
                        continue;
                    }else{
                        x = y;
                        y = Integer.parseInt(linea[0]);

                    }

                    boolean marcado = grupoTableros.marcarCasilla(x, y, jugador1.getSimbolo());
                    if (!marcado) {
                        System.out.println("Casilla ocupada. Intenta otra vez.");
                        continue;
                    }

                    grupoTableros.imprimir();

                    resultado = grupoTableros.getTablero(x).revisarGanador();
                    tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                    plano = tableroInd.getTablero().getTablero();
                    v = GrupoTableros.verificarGanador(plano);
                    System.out.println("Resultado: " + v);
                    listTablero.add(x);

                    if (v == 'x' || v == 'o') {
                        if (jugador1 instanceof JugadorHumano) {
                            ((JugadorHumano) jugador1).incrementarGanadas();
                            Serializacion.guardarJugador(jugador1);
                            jugadores = Serializacion.cargarJugadores();
                            for (Jugador j : jugadores) {
                                System.out.println(j);
                            }
                        }
                        System.out.println("¡Jugador " + jugador1.getNombre() + " ha ganado el tablero " + x + "!");
                    }

                    // Revisar si el meta-tablero tiene ganador después de la jugada del jugador
                    revisionMetaPlano = grupoTableros.revisarGanador();
                    if (revisionMetaPlano == 1 || revisionMetaPlano == 2) {
                        System.out.println("¡Jugador " + revisionMetaPlano + " ha ganado el meta-juego!");
                        aux = revisionMetaPlano;
                        break;
                    }

                    turno++;
                }

            }
            // Jugador 1 pierde, pero es cpu - hard

            else if (tipoJ2.equals("cpu-hard")) {
                while (aux != -1) {
                    boolean bandera = false;


                    // --- TURNO CPU ---
                    x = random.nextInt(9);
                    System.out.println("CPU escogió el plano: "+x);
                    TableroIndividual tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                    char[][] tablero = tableroInd.getTablero().getTablero();
                    int jugada = jugador2.hacerJugada(tablero);
                    grupoTableros.marcarCasilla(x, jugada, jugador2.getSimbolo());

                    try {
                        Thread.sleep(2000);

                    }catch (InterruptedException e){
                        Thread.currentThread().interrupt();
                    }
                    grupoTableros.imprimir();

                    int resultado = grupoTableros.getTablero(x).revisarGanador();
                    tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                    char[][] plano = tableroInd.getTablero().getTablero();
                    char v = GrupoTableros.verificarGanador(plano);
                    System.out.println("Resultado" + v);
                    listTablero.add(x);

                    if (v == 'x' || v == 'o') {
                        if (jugador2 instanceof CPUDificil) {
                            ((CPUDificil) jugador2).incrementarGanadas();
                            Serializacion.guardarJugador(jugador2);
                            jugadores = Serializacion.cargarJugadores();
                            for (Jugador j : jugadores) {
                                System.out.println(j);
                            }
                        }
                        System.out.println("¡Jugador " + jugador2.getNombre() + " ha ganado el tablero " + x + "!");
                    }

                    int revisionMetaPlano = grupoTableros.revisarGanador();
                    if (revisionMetaPlano == 1 || revisionMetaPlano == 2) {
                        System.out.println("¡Jugador " + revisionMetaPlano + " ha ganado el meta-juego!");
                        aux = revisionMetaPlano;
                        break;
                    }

                    // --- TURNO JUGADOR HUMANO ---
                    System.out.println("Escribe coordenada Jugador 1: ");
                    if (input.hasNextLine()) {
                        linea = input.nextLine().split(" ");
                        if (linea.equals("rendirse") && turno > 0) {
                            if (jugador1 instanceof JugadorHumano) {
                                ((JugadorHumano) jugador1).incrementarGanadas();
                                Serializacion.guardarJugador(jugador1);
                                jugadores = Serializacion.cargarJugadores();
                                for (Jugador j : jugadores) {
                                    System.out.println(j);
                                }
                                tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                                tableroInd.rellenar(simboloPerdedor);
                                plano = tableroInd.getTablero().getTablero();
                                v = GrupoTableros.verificarGanador(plano);
                                System.out.println("¡Jugador " + jugador2.getNombre() + " ha ganado el tablero " + x + "!");
                                bandera = true;
                            }
                        }

                        if (bandera) {
                            x = y;
                            y = Integer.parseInt(linea[0]);
                        } else {
                            x = Integer.parseInt(linea[0]);
                            y = Integer.parseInt(linea[1]);
                        }

                        boolean marcado = grupoTableros.marcarCasilla(x, y, jugador1.getSimbolo());
                        if (!marcado) {
                            System.out.println("Casilla ocupada. Intenta otra vez.");
                            continue;
                        }

                        grupoTableros.imprimir();

                        resultado = grupoTableros.getTablero(x).revisarGanador();
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

                        revisionMetaPlano = grupoTableros.revisarGanador();
                        if (revisionMetaPlano == 1 || revisionMetaPlano == 2) {
                            System.out.println("¡Jugador " + revisionMetaPlano + " ha ganado el meta-juego!");
                            aux = revisionMetaPlano;
                            break;
                        }

                        if (bandera) {
                            x = random.nextInt(9);
                            System.out.println("La CPU escogió el plano: " + x);
                        }
                    }
                    turno++;
                }

            }

        }






    }

}
