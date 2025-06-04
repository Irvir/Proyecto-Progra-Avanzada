import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class JuegoBase implements Sujeto {
    private Tablero metaTablero;
    private GrupoTableros grupoTableros;
    private Jugador jugador1;
    private Jugador jugador2;
    private Scanner input = new Scanner(System.in);
    private char simboloGanador;
    private char simboloPerdedor;
    private Tablero tablero;
    private String nombreJ1;
    private String tipoJ1;
    private String nombreJ2;
    private String tipoJ2;
    private ArrayList<Observer> observers = new ArrayList<>();

    /**
     * @param nombreJ1 Nombre del Jugador
     * @param tipoJ1   Tipo del Jugador
     * @param nombreJ2 Nombre del Jugador
     * @param tipoJ2   Tipo del Jugador
     */
    public JuegoBase(String nombreJ1, String tipoJ1, String nombreJ2, String tipoJ2) {
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
     * @param o método del observer
     */
    @Override
    public void agregarObserver(Observer o) {
        observers.add(o);
    }

    /**
     * @param o método del observer
     */
    @Override
    public void eliminarObserver(Observer o) {
        observers.remove(o);
    }

    //Notifica que ocurra algo
    @Override
    public void notificarObserver() {
        for (Observer o : observers) {
            o.actualizar(jugador1, jugador2);
        }
    }

    /**
     * @param nombreJ1 Nombre Jugador 1
     * @param nombreJ2 Nombre Jugador 2
     */
    void comenzarJuego(String nombreJ1, String nombreJ2) {
        metaTablero = new Tablero();
        metaTablero.crearTablero();
        Random random = new Random();
        int dado1 = random.nextInt(6) + 1;
        int dado2 = random.nextInt(6) + 1;
        int resultadoDado1 = dado1 + dado2;
        System.out.println("Lanzando los dados... RESULTADO JUGADOR 1: " + resultadoDado1);

        dado1 = random.nextInt(6) + 1;
        dado2 = random.nextInt(6) + 1;
        int resultadoDado2 = dado1 + dado2;
        System.out.println("Lanzando los dados... RESULTADO JUGADOR 2: " + resultadoDado2);


        if (resultadoDado1 > resultadoDado2) {
            System.out.println("GANÓ JUGADOR 1");
            System.out.println("¿Qué símbolo ('x' ó 'o') desea el Jugador 1?");
            if (input.hasNextLine()) {
                simboloGanador = input.nextLine().charAt(0);
            } else {
                System.out.println("No hay entrada disponible.");
            }
            simboloPerdedor = (simboloGanador == 'x') ? 'o' : 'x';

        }
        else {
            System.out.println("GANÓ JUGADOR 2");
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

            }
            else {
                System.out.println("Como J2 es: CPU, su signo es 'o'");
                simboloGanador = 'o';
                simboloPerdedor = 'x';
            }
        }

        jugador1 = JugadorFactory.crearJugador(tipoJ1, nombreJ1, simboloGanador);
        jugador2 = JugadorFactory.crearJugador(tipoJ2, nombreJ2, simboloPerdedor);

        ArrayList<Jugador> jugadores = Serializacion.cargarJugadores();

        jugadores.add(jugador1);
        jugadores.add(jugador2);

        System.out.println("Ranking de Jugadores (de mayor a menor ganadas):");
        for (Jugador jugador : jugadores) {
            if (jugador instanceof JugadorHumano) {
                JugadorHumano jHumano = (JugadorHumano) jugador;
                System.out.println("Jugador: " + jHumano.getNombre() + " - Ganadas: " + jHumano.getGanadas());
            }
        }

        tablero = new Tablero();
        tablero.crearTablero();

        String[] linea;
        int x;
        int y;
        System.out.println("FORMATO DE ENTRADA: x (plano) (espacio) y(cuadrante)");
        int aux = 0;
        Serializacion.guardarJugador(jugador1);
        Serializacion.guardarJugador(jugador2);

        x = 0;
        y = 0;
        int turno = 0;
        int yElegida = 0;

        System.out.println(jugador2.getNombre());


        if (resultadoDado1 > resultadoDado2) {
            if (tipoJ2.equals("humano")) {
                while (aux != -1) {
                    // TURNO JUGADOR 1
                    System.out.println("Turno de " + jugador1.getNombre());
                    int tableroDestino;
                    if (turno < 1) {
                        tableroDestino = y;
                    } else {
                        tableroDestino = yElegida;
                    }
                    TableroIndividual tableroDestinoInd = (TableroIndividual) grupoTableros.getTablero(tableroDestino);

                    boolean puedeElegirTablero = (turno < 1) ||
                            tableroDestinoInd.estaCompleto() ||
                            (GrupoTableros.verificarGanador(tableroDestinoInd.getTablero().getTablero()) != '-');

                    if (puedeElegirTablero) {
                        System.out.println("Puedes elegir cualquier tablero. Escribe coordenada (x y) o 'rendirse': ");
                    } else {
                        System.out.println("Debes jugar en el tablero " + tableroDestino + ". Escribe coordenada (casilla) o 'rendirse': ");
                    }

                    String entrada = input.nextLine();
                    if (entrada.equalsIgnoreCase("rendirse")) {
                        if (jugador2 instanceof JugadorHumano) {

                            tablero.actualizar(jugador2,jugador1);

                            jugadores = Serializacion.cargarJugadores();
                            for (Jugador j : jugadores) {
                                System.out.println(j);
                            }

                            TableroIndividual tableroInd = (TableroIndividual) grupoTableros.getTablero(tableroDestino);
                            tableroInd.rellenar(simboloPerdedor);
                            char[][] plano = tableroInd.getTablero().getTablero();
                            char v = GrupoTableros.verificarGanador(plano);

                            System.out.println("¡Jugador " + jugador2.getNombre() + " ha ganado el tablero " + tableroDestino + "!");
                        }
                        System.out.println("¡Jugador 1 se ha rendido! Turno para Jugador 2.");
                        turno++;
                        continue;
                    }

                    linea = entrada.split(" ");
                    if (puedeElegirTablero) {
                        x = Integer.parseInt(linea[0]);
                        yElegida = Integer.parseInt(linea[1]);
                    } else {
                        x = tableroDestino;
                        yElegida = Integer.parseInt(linea[0]);
                    }

                    boolean marcado = grupoTableros.marcarCasilla(x, yElegida, jugador1.getSimbolo());
                    if (!marcado) {
                        System.out.println("Casilla ocupada. Intenta otra vez.");
                        continue;
                    }

                    grupoTableros.imprimir();

                    TableroIndividual tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                    char[][] plano = tableroInd.getTablero().getTablero();
                    char v = GrupoTableros.verificarGanador(plano);
                    System.out.println("Resultado Jugador 1: " + v);

                    if (v == 'x' || v == 'o') {
                        if (jugador1 instanceof JugadorHumano) {
                            tablero.actualizar(jugador1, jugador2);

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
                    if (tableroInd.estaCompleto() && v == '-') {
                        System.out.println("El tablero " + x + " ha quedado en empate.");
                        ((JugadorHumano)jugador1).incrementarEmpatadas();
                        ((JugadorHumano)jugador2).incrementarEmpatadas();
                        Serializacion.guardarJugador(jugador1);
                        Serializacion.guardarJugador(jugador2);
                        jugadores = Serializacion.cargarJugadores();
                        for (Jugador j : jugadores) {
                            System.out.println(j);
                        }


                        tableroInd.rellenar('*');
                    }


                    // TURNO JUGADOR 2
                    tableroDestino = yElegida-1;
                    y = yElegida;

                    tableroDestinoInd = (TableroIndividual) grupoTableros.getTablero(tableroDestino);

                    puedeElegirTablero = (tableroDestinoInd.estaCompleto() ||
                            (GrupoTableros.verificarGanador(tableroDestinoInd.getTablero().getTablero()) != '-'));

                    if (puedeElegirTablero) {
                        System.out.println("Puedes elegir cualquier tablero. Escribe coordenada (x y) o 'rendirse': ");
                    } else {
                        System.out.println("Debes jugar en el tablero " + tableroDestino + ". Escribe coordenada (casilla) o 'rendirse': ");
                    }

                    entrada = input.nextLine();
                    if (entrada.equalsIgnoreCase("rendirse")) {
                        if (jugador1 instanceof JugadorHumano) {

                            tablero.actualizar(jugador1, jugador2);


                            jugadores = Serializacion.cargarJugadores();
                            for (Jugador j : jugadores) {
                                System.out.println(j);
                            }
                            tableroInd = (TableroIndividual) grupoTableros.getTablero(tableroDestino);
                            tableroInd.rellenar(simboloGanador);
                            plano = tableroInd.getTablero().getTablero();
                            v = GrupoTableros.verificarGanador(plano);
                            System.out.println("¡Jugador " + jugador1.getNombre() + " ha ganado el tablero " + tableroDestino + "!");
                        }
                        System.out.println("¡Jugador 2 se ha rendido! Turno para Jugador 1.");
                        turno++;
                        continue;
                    }

                    linea = entrada.split(" ");
                    if (puedeElegirTablero) {
                        x = Integer.parseInt(linea[0]);
                        yElegida = Integer.parseInt(linea[1]);
                    } else {
                        x = tableroDestino;
                        yElegida = Integer.parseInt(linea[0]);
                    }

                    marcado = grupoTableros.marcarCasilla(x, yElegida, jugador2.getSimbolo());
                    if (!marcado) {
                        System.out.println("Casilla ocupada. Intenta otra vez.");
                        continue;
                    }

                    grupoTableros.imprimir();

                    tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                    plano = tableroInd.getTablero().getTablero();
                    v = GrupoTableros.verificarGanador(plano);
                    System.out.println("Resultado Jugador 2: " + v);

                    if (v == 'x' || v == 'o') {
                        if (jugador2 instanceof JugadorHumano) {

                            tablero.actualizar(jugador2,jugador1);


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
                    if (tableroInd.estaCompleto() && v == '-') {
                        System.out.println("El tablero " + x + " ha quedado en empate.");
                        ((JugadorHumano)jugador1).incrementarEmpatadas();
                        ((JugadorHumano)jugador2).incrementarEmpatadas();
                        Serializacion.guardarJugador(jugador1);
                        Serializacion.guardarJugador(jugador2);
                        jugadores = Serializacion.cargarJugadores();
                        for (Jugador j : jugadores) {
                            System.out.println(j);
                        }
                        tableroInd.rellenar('*');
                    }

                    System.out.println("Tablero actual:  " + x + " Posición escogida: " + yElegida);
                    turno++;
                }
            }
            else if (tipoJ2.equals("cpu-easy")) {
                while (aux != -1) {
                    System.out.println("Turno de " + jugador1.getNombre());
                    int tableroDestino;
                    if (turno < 1) {
                        tableroDestino = y;
                    } else {
                        tableroDestino = yElegida;
                    }
                    TableroIndividual tableroDestinoInd = (TableroIndividual) grupoTableros.getTablero(tableroDestino);

                    boolean puedeElegirTablero = (turno < 1) ||
                            tableroDestinoInd.estaCompleto() ||
                            (GrupoTableros.verificarGanador(tableroDestinoInd.getTablero().getTablero()) != '-');

                    if (puedeElegirTablero) {
                        System.out.println("Puedes elegir cualquier tablero. Escribe coordenada (x y) o 'rendirse': ");
                    } else {
                        System.out.println("Debes jugar en el tablero " + tableroDestino + ". Escribe coordenada (casilla) o 'rendirse': ");
                    }

                    String entrada = input.nextLine();
                    if (entrada.equalsIgnoreCase("rendirse")) {
                        if (jugador2 instanceof JugadorHumano) {

                            tablero.actualizar(jugador2, jugador1);


                            jugadores = Serializacion.cargarJugadores();
                            for (Jugador j : jugadores) {
                                System.out.println(j);
                            }
                            TableroIndividual tableroInd = (TableroIndividual) grupoTableros.getTablero(tableroDestino);
                            tableroInd.rellenar(simboloPerdedor);
                            char[][] plano = tableroInd.getTablero().getTablero();
                            char v = GrupoTableros.verificarGanador(plano);
                            System.out.println("¡Jugador " + jugador2.getNombre() + " ha ganado el tablero " + tableroDestino + "!");
                        }
                        System.out.println("¡Jugador 1 se ha rendido! Turno para Jugador 2.");
                        turno++;
                        continue;
                    }

                    linea = entrada.split(" ");
                    if (puedeElegirTablero) {
                        x = Integer.parseInt(linea[0]);
                        yElegida = Integer.parseInt(linea[1]);
                    } else {
                        x = tableroDestino;
                        yElegida = Integer.parseInt(linea[0]);
                    }

                    boolean marcado = grupoTableros.marcarCasilla(x, yElegida, jugador1.getSimbolo());
                    if (!marcado) {
                        System.out.println("Casilla ocupada. Intenta otra vez.");
                        continue;
                    }

                    grupoTableros.imprimir();

                    TableroIndividual tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                    char[][] plano = tableroInd.getTablero().getTablero();
                    char v = GrupoTableros.verificarGanador(plano);
                    System.out.println("Resultado Jugador 1: " + v);

                    if (v == 'x' || v == 'o') {
                        if (jugador1 instanceof JugadorHumano) {
                            tablero.actualizar(jugador1, jugador2);

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
                    if (tableroInd.estaCompleto() && v == '-') {
                        System.out.println("El tablero " + x + " ha quedado en empate.");
                        ((JugadorHumano)jugador1).incrementarEmpatadas();
                        ((CPUFacil)jugador2).incrementarEmpatadas();
                        Serializacion.guardarJugador(jugador1);
                        Serializacion.guardarJugador(jugador2);
                        jugadores = Serializacion.cargarJugadores();
                        for (Jugador j : jugadores) {
                            System.out.println(j);
                        }
                        tableroInd.rellenar('*');
                    }

                    // Turno CPU

                    tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                    char[][] tableroCPU = tableroInd.getTablero().getTablero();
                    y = jugador2.hacerJugada(tableroCPU);
                    x = yElegida;
                    System.out.println("Plano x: " + x + " Posicion: " + y + " Posicion elegida: " + yElegida);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    boolean marcadoCPU = grupoTableros.marcarCasilla(x, y, jugador2.getSimbolo());
                    if (!marcadoCPU) {
                        System.out.println("CPU no pudo marcar. Intenta de nuevo.");
                        continue;
                    }

                    grupoTableros.imprimir();
                    plano = tableroInd.getTablero().getTablero();
                    v = GrupoTableros.verificarGanador(plano);
                    System.out.println("Resultado: " + v);


                    if (v == 'x' || v == 'o') {
                        if (jugador2 instanceof CPUFacil) {
                            tablero.actualizar(jugador2,jugador1);


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
                    if (tableroInd.estaCompleto() && v == '-') {
                        System.out.println("El tablero " + x + " ha quedado en empate.");
                        ((JugadorHumano)jugador1).incrementarEmpatadas();
                        ((CPUDificil)jugador2).incrementarEmpatadas();
                        Serializacion.guardarJugador(jugador1);
                        Serializacion.guardarJugador(jugador2);
                        jugadores = Serializacion.cargarJugadores();
                        for (Jugador j : jugadores) {
                            System.out.println(j);
                        }
                        tableroInd.rellenar('*');
                    }

                    turno++;

                }

            }
            //Si el Jugador1 Gana, pero es CPU - Hard
            else if (tipoJ2.equals("cpu-hard")) {
                while (aux != -1) {
                    System.out.println("Turno de " + jugador1.getNombre());
                    int tableroDestino;
                    if (turno < 1) {
                        tableroDestino = yElegida;
                    } else {
                        tableroDestino = y;
                    }
                    TableroIndividual tableroDestinoInd = (TableroIndividual) grupoTableros.getTablero(tableroDestino);

                    boolean puedeElegirTablero = (turno < 1) ||
                            tableroDestinoInd.estaCompleto() ||
                            (GrupoTableros.verificarGanador(tableroDestinoInd.getTablero().getTablero()) != '-');

                    if (puedeElegirTablero) {
                        System.out.println("Puedes elegir cualquier tablero. Escribe coordenada (x y) o 'rendirse': ");
                    } else {
                        System.out.println("Debes jugar en el tablero " + tableroDestino + ". Escribe coordenada (casilla) o 'rendirse': ");
                    }

                    String entrada = input.nextLine();
                    if (entrada.equalsIgnoreCase("rendirse")) {
                        if (jugador2 instanceof JugadorHumano) {
                            tablero.actualizar(jugador2,jugador1);

                            jugadores = Serializacion.cargarJugadores();
                            for (Jugador j : jugadores) {
                                System.out.println(j);
                            }
                            TableroIndividual tableroInd = (TableroIndividual) grupoTableros.getTablero(tableroDestino);
                            tableroInd.rellenar(simboloPerdedor);
                            char[][] plano = tableroInd.getTablero().getTablero();
                            char v = GrupoTableros.verificarGanador(plano);
                            System.out.println("¡Jugador " + jugador2.getNombre() + " ha ganado el tablero " + tableroDestino + "!");
                        }
                        System.out.println("¡Jugador 1 se ha rendido! Turno para Jugador 2.");
                        turno++;
                        continue;
                    }

                    linea = entrada.split(" ");
                    if (puedeElegirTablero) {
                        x = Integer.parseInt(linea[0]);
                        yElegida = Integer.parseInt(linea[1]);
                    } else {
                        x = tableroDestino;
                        yElegida = Integer.parseInt(linea[0]);
                    }

                    boolean marcado = grupoTableros.marcarCasilla(x, yElegida, jugador1.getSimbolo());
                    if (!marcado) {
                        System.out.println("Casilla ocupada. Intenta otra vez.");
                        continue;
                    }

                    grupoTableros.imprimir();

                    TableroIndividual tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                    char[][] plano = tableroInd.getTablero().getTablero();
                    char v = GrupoTableros.verificarGanador(plano);
                    System.out.println("Resultado Jugador 1: " + v);

                    if (v == 'x' || v == 'o') {
                        if (jugador1 instanceof JugadorHumano) {
                            tablero.actualizar(jugador1,jugador2);

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
                    if (tableroInd.estaCompleto() && v == '-') {
                        System.out.println("El tablero " + x + " ha quedado en empate.");
                        ((JugadorHumano)jugador1).incrementarEmpatadas();
                        ((CPUDificil)jugador2).incrementarEmpatadas();
                        Serializacion.guardarJugador(jugador1);
                        Serializacion.guardarJugador(jugador2);
                        jugadores = Serializacion.cargarJugadores();
                        for (Jugador j : jugadores) {
                            System.out.println(j);
                        }
                        tableroInd.rellenar('*');
                    }


                    //TURNO CPU
                    tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                    char[][] tableroCPU = tableroInd.getTablero().getTablero();
                    y = jugador2.hacerJugada(tableroCPU);
                    x = yElegida;
                    System.out.println("Plano x: " + x + " Posicion: " + y + " Posicion elegida: " + yElegida);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    boolean marcadoCPU = grupoTableros.marcarCasilla(x, y, jugador2.getSimbolo());
                    if (!marcadoCPU) {
                        System.out.println("CPU no pudo marcar. Intenta de nuevo.");
                        continue;
                    }

                    grupoTableros.imprimir();
                    plano = tableroInd.getTablero().getTablero();
                    v = GrupoTableros.verificarGanador(plano);
                    System.out.println("Resultado: " + v);


                    if (v == 'x' || v == 'o') {
                        if (jugador2 instanceof CPUFacil) {
                            tablero.actualizar(jugador2,jugador1);

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
                    if (tableroInd.estaCompleto() && v == '-') {
                        System.out.println("El tablero " + x + " ha quedado en empate.");
                        ((JugadorHumano)jugador1).incrementarEmpatadas();
                        ((CPUDificil)jugador2).incrementarEmpatadas();
                        Serializacion.guardarJugador(jugador1);
                        Serializacion.guardarJugador(jugador2);
                        jugadores = Serializacion.cargarJugadores();
                        for (Jugador j : jugadores) {
                            System.out.println(j);
                        }
                        tableroInd.rellenar('*');
                    }

                    turno++;

                }
            }
        }


        else {

            if (tipoJ2.equals("humano") ){
                while (aux != -1) {
                    // TURNO JUGADOR 2
                    System.out.println("Turno de " + jugador2.getNombre());
                    int tableroDestino;
                    if (turno<1){
                        tableroDestino = y;

                    }else {
                        tableroDestino = yElegida;
                    }
                    TableroIndividual tableroDestinoInd = (TableroIndividual) grupoTableros.getTablero(tableroDestino);

                    boolean puedeElegirTablero = (turno < 1) ||
                            tableroDestinoInd.estaCompleto() ||
                            (GrupoTableros.verificarGanador(tableroDestinoInd.getTablero().getTablero()) != '-');

                    if (puedeElegirTablero) {
                        System.out.println("Puedes elegir cualquier tablero. Escribe coordenada (x y) o 'rendirse': ");
                    } else {
                        System.out.println("Debes jugar en el tablero " + tableroDestino + ". Escribe coordenada (casilla) o 'rendirse': ");
                    }

                    String entrada = input.nextLine();
                    if (entrada.equalsIgnoreCase("rendirse")) {
                        if (jugador1 instanceof JugadorHumano) {
                            tablero.actualizar(jugador1,jugador2);

                            jugadores = Serializacion.cargarJugadores();
                            for (Jugador j : jugadores) {
                                System.out.println(j);
                            }
                            TableroIndividual tableroInd = (TableroIndividual) grupoTableros.getTablero(tableroDestino);
                            tableroInd.rellenar(simboloPerdedor);
                            char[][] plano = tableroInd.getTablero().getTablero();
                            char v = GrupoTableros.verificarGanador(plano);
                            System.out.println("¡Jugador " + jugador1.getNombre() + " ha ganado el tablero " + tableroDestino + "!");
                        }
                        System.out.println("¡Jugador 2 se ha rendido! Turno para Jugador 1.");
                        turno++;
                        continue;
                    }

                    linea = entrada.split(" ");

                    if (puedeElegirTablero) {
                        x = Integer.parseInt(linea[0]);
                        yElegida = Integer.parseInt(linea[1]);
                    } else {
                        x = tableroDestino;
                        yElegida = Integer.parseInt(linea[0]);
                    }

                    boolean marcado = grupoTableros.marcarCasilla(x, yElegida, jugador2.getSimbolo());
                    if (!marcado) {
                        System.out.println("Casilla ocupada. Intenta otra vez.");
                        continue;
                    }

                    grupoTableros.imprimir();

                    TableroIndividual tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                    char[][] plano = tableroInd.getTablero().getTablero();
                    char v = GrupoTableros.verificarGanador(plano);
                    System.out.println("Resultado Jugador 2: " + v);

                    if (v == 'x' || v == 'o') {
                        if (jugador2 instanceof JugadorHumano) {
                            tablero.actualizar(jugador2, jugador1);

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
                    if (tableroInd.estaCompleto() && v == '-') {
                        System.out.println("El tablero " + x + " ha quedado en empate.");
                        ((JugadorHumano)jugador1).incrementarEmpatadas();
                        ((JugadorHumano)jugador2).incrementarEmpatadas();
                        Serializacion.guardarJugador(jugador1);
                        Serializacion.guardarJugador(jugador2);
                        jugadores = Serializacion.cargarJugadores();
                        for (Jugador j : jugadores) {
                            System.out.println(j);
                        }
                        tableroInd.rellenar('*');
                    }


                    // TURNO JUGADOR 1
                    tableroDestino = yElegida - 1;
                    y = yElegida;

                    tableroDestinoInd = (TableroIndividual) grupoTableros.getTablero(tableroDestino);

                    puedeElegirTablero = (tableroDestinoInd.estaCompleto() ||
                            (GrupoTableros.verificarGanador(tableroDestinoInd.getTablero().getTablero()) != '-'));

                    if (puedeElegirTablero) {
                        System.out.println("Puedes elegir cualquier tablero. Escribe coordenada (x y) o 'rendirse': ");
                    } else {
                        System.out.println("Debes jugar en el tablero " + tableroDestino + ". Escribe coordenada (casilla) o 'rendirse': ");
                    }

                    entrada = input.nextLine();
                    if (entrada.equalsIgnoreCase("rendirse")) {

                        if (jugador2 instanceof JugadorHumano) {
                            tablero.actualizar(jugador2,jugador1);
                            jugadores = Serializacion.cargarJugadores();
                            for (Jugador j : jugadores) {
                                System.out.println(j);
                            }
                            tableroInd = (TableroIndividual) grupoTableros.getTablero(tableroDestino);
                            tableroInd.rellenar(simboloGanador);
                            plano = tableroInd.getTablero().getTablero();
                            v = GrupoTableros.verificarGanador(plano);
                            System.out.println("¡Jugador " + jugador2.getNombre() + " ha ganado el tablero " + tableroDestino + "!");
                        }
                        System.out.println("¡Jugador 1 se ha rendido! Turno para Jugador 2.");
                        turno++;
                        continue;
                    }

                    linea = entrada.split(" ");
                    if (puedeElegirTablero) {
                        x = Integer.parseInt(linea[0]);
                        yElegida = Integer.parseInt(linea[1]);
                    } else {
                        x = tableroDestino;
                        yElegida = Integer.parseInt(linea[0]);
                    }

                    marcado = grupoTableros.marcarCasilla(x, yElegida, jugador1.getSimbolo());
                    if (!marcado) {
                        System.out.println("Casilla ocupada. Intenta otra vez.");
                        continue;
                    }

                    grupoTableros.imprimir();

                    tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                    plano = tableroInd.getTablero().getTablero();
                    v = GrupoTableros.verificarGanador(plano);
                    System.out.println("Resultado Jugador 1: " + v);

                    if (v == 'x' || v == 'o') {
                        if (jugador1 instanceof JugadorHumano) {
                            tablero.actualizar(jugador1,jugador2);

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

                    System.out.println("Tablero actual:  " + x + " Posicion escogida: "+ yElegida);
                    turno++;
                    if (tableroInd.estaCompleto() && v == '-') {
                        System.out.println("El tablero " + x + " ha quedado en empate.");
                        ((JugadorHumano)jugador1).incrementarEmpatadas();
                        ((JugadorHumano)jugador2).incrementarEmpatadas();
                        Serializacion.guardarJugador(jugador1);
                        Serializacion.guardarJugador(jugador2);
                        jugadores = Serializacion.cargarJugadores();
                        for (Jugador j : jugadores) {
                            System.out.println(j);
                        }
                        tableroInd.rellenar('*');
                    }

                }


            }
            else if (tipoJ2.equals("cpu-easy")) {
                while (aux != -1) {
                    // TURNO CPU PRIMERO
                    System.out.println("Turno de " + jugador2.getNombre());
                    int tableroDestinoCPU;
                    if (turno < 1) {
                        tableroDestinoCPU = 9;

                    } else {
                        tableroDestinoCPU = yElegida;
                    }

                    TableroIndividual tableroDestinoInd = (TableroIndividual) grupoTableros.getTablero(tableroDestinoCPU-1);
                    char[][] tableroCPU = tableroDestinoInd.getTablero().getTablero();
                    y = jugador2.hacerJugada(tableroCPU);
                    x = tableroDestinoCPU;
                    if (x ==9){
                        x-=1;
                    }
                    System.out.println("Plano x: " + x + " Posición: " + y + " Posición elegida: " + yElegida);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    boolean marcadoCPU = grupoTableros.marcarCasilla(x, y, jugador2.getSimbolo());
                    System.out.println(marcadoCPU);

                    if (!marcadoCPU) {
                        System.out.println("CPU no pudo marcar. Intenta de nuevo.");
                        continue;
                    }

                    grupoTableros.imprimir();
                    char[][] plano = tableroDestinoInd.getTablero().getTablero();
                    char v = GrupoTableros.verificarGanador(plano);
                    System.out.println("Resultado CPU: " + v);

                    if (tableroDestinoInd.estaCompleto() && v == '-') {
                        System.out.println("El tablero " + x + " ha quedado en empate.");
                        ((JugadorHumano)jugador1).incrementarEmpatadas();
                        ((JugadorHumano)jugador2).incrementarEmpatadas();
                        Serializacion.guardarJugador(jugador1);
                        Serializacion.guardarJugador(jugador2);
                        jugadores = Serializacion.cargarJugadores();
                        for (Jugador j : jugadores) {
                            System.out.println(j);
                        }
                        tableroDestinoInd.rellenar('*');
                    }

                    if (v == 'x' || v == 'o') {
                        if (jugador2 instanceof CPUFacil) {
                            tablero.actualizar(jugador2,jugador1);
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



                    // TURNO JUGADOR 1
                    System.out.println("Turno de " + jugador1.getNombre());
                    int tableroDestino;
                    if (turno < 1) {
                        tableroDestino = y;
                    } else {
                        tableroDestino = yElegida;
                    }

                    TableroIndividual tableroDestinoIndJ1 = (TableroIndividual) grupoTableros.getTablero(tableroDestino);

                    boolean puedeElegirTablero =
                            tableroDestinoIndJ1.estaCompleto() ||
                            (GrupoTableros.verificarGanador(tableroDestinoIndJ1.getTablero().getTablero()) != '-');
                    System.out.println( tableroDestinoIndJ1.estaCompleto());
                    System.out.println(GrupoTableros.verificarGanador(tableroDestinoIndJ1.getTablero().getTablero()) != '-');
                    if (puedeElegirTablero) {
                        System.out.println("Puedes elegir cualquier tablero. Escribe coordenada (x y) o 'rendirse': ");
                    } else {
                        System.out.println("Debes jugar en el tablero " + tableroDestino + ". Escribe coordenada (casilla) o 'rendirse': ");
                    }

                    String entrada = input.nextLine();
                    if (entrada.equalsIgnoreCase("rendirse")) {
                        if (jugador2 instanceof JugadorHumano) {
                            tablero.actualizar(jugador2,jugador1);

                            jugadores = Serializacion.cargarJugadores();
                            for (Jugador j : jugadores) {
                                System.out.println(j);
                            }
                            TableroIndividual tableroInd = (TableroIndividual) grupoTableros.getTablero(tableroDestino);
                            tableroInd.rellenar(simboloPerdedor);
                            plano = tableroInd.getTablero().getTablero();
                            v = GrupoTableros.verificarGanador(plano);
                            System.out.println("¡Jugador " + jugador2.getNombre() + " ha ganado el tablero " + tableroDestino + "!");
                        }
                        System.out.println("¡Jugador 1 se ha rendido! Turno para Jugador 2.");
                        turno++;
                        continue;
                    }

                    linea = entrada.split(" ");
                    if (puedeElegirTablero) {
                        x = Integer.parseInt(linea[0]);
                        yElegida = Integer.parseInt(linea[1]);
                    } else {
                        x = tableroDestino;
                        yElegida = Integer.parseInt(linea[0]);
                    }

                    boolean marcado = grupoTableros.marcarCasilla(x, yElegida, jugador1.getSimbolo());
                    if (!marcado) {
                        System.out.println("Casilla ocupada. Intenta otra vez.");
                        continue;
                    }

                    grupoTableros.imprimir();

                    TableroIndividual tableroInd = (TableroIndividual) grupoTableros.getTablero(x);
                    plano = tableroInd.getTablero().getTablero();
                    v = GrupoTableros.verificarGanador(plano);
                    System.out.println("Resultado Jugador 1: " + v);

                    if (v == 'x' || v == 'o') {
                        if (jugador1 instanceof JugadorHumano) {
                            tablero.actualizar(jugador1,jugador2);

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
                    if (tableroInd.estaCompleto() && v == '-') {
                        System.out.println("El tablero " + x + " ha quedado en empate.");
                        ((JugadorHumano)jugador1).incrementarEmpatadas();
                        ((JugadorHumano)jugador2).incrementarEmpatadas();
                        Serializacion.guardarJugador(jugador1);
                        Serializacion.guardarJugador(jugador2);
                        jugadores = Serializacion.cargarJugadores();
                        for (Jugador j : jugadores) {
                            System.out.println(j);
                        }
                        tableroInd.rellenar('*');
                    }


                    turno++;
                }

            }


            else if (tipoJ2.equals("cpu-hard")) {
                while (aux != -1) {
                    //  TURNO DE CPU
                    int tableroDestino;
                    if (turno < 1) {
                        tableroDestino = yElegida;
                    } else {
                        tableroDestino = y;
                    }
                    TableroIndividual tableroDestinoInd = (TableroIndividual) grupoTableros.getTablero(tableroDestino);


                    tableroDestinoInd = (TableroIndividual) grupoTableros.getTablero(x);
                    char[][] tableroCPU = tableroDestinoInd.getTablero().getTablero();
                    y = jugador2.hacerJugada(tableroCPU);
                    x = yElegida;
                    System.out.println("CPU x: " + x + " Posición: " + y + " Posición elegida: " + yElegida);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    boolean marcadoCPU = grupoTableros.marcarCasilla(x, y, jugador2.getSimbolo());
                    if (!marcadoCPU) {
                        System.out.println("CPU no pudo marcar. Intenta de nuevo.");
                        continue;
                    }

                    grupoTableros.imprimir();
                    char[][] plano = tableroDestinoInd.getTablero().getTablero();
                    char v = GrupoTableros.verificarGanador(plano);
                    System.out.println("Resultado CPU: " + v);
                    if (tableroDestinoInd.estaCompleto() && v == '-') {
                        System.out.println("El tablero " + x + " ha quedado en empate.");
                        ((JugadorHumano)jugador1).incrementarEmpatadas();
                        ((JugadorHumano)jugador2).incrementarEmpatadas();
                        Serializacion.guardarJugador(jugador1);
                        Serializacion.guardarJugador(jugador2);
                        jugadores = Serializacion.cargarJugadores();
                        for (Jugador j : jugadores) {
                            System.out.println(j);
                        }
                        tableroDestinoInd.rellenar('*');
                    }


                    if (v == 'x' || v == 'o') {
                        if (jugador2 instanceof CPUDificil) {
                            tablero.actualizar(jugador2,jugador1);
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

                    //TURNO DE JUGADOR HUMANO
                    System.out.println("Turno de " + jugador1.getNombre());
                    if (turno < 1) {
                        tableroDestino = y;
                    } else {
                        tableroDestino = yElegida;
                    }
                    tableroDestinoInd = (TableroIndividual) grupoTableros.getTablero(tableroDestino);

                    boolean puedeElegirTablero =
                            tableroDestinoInd.estaCompleto() ||
                            (GrupoTableros.verificarGanador(tableroDestinoInd.getTablero().getTablero()) != '-');

                    if (puedeElegirTablero) {
                        System.out.println("Puedes elegir cualquier tablero. Escribe coordenada (x y) o 'rendirse': ");
                    } else {
                        System.out.println("Debes jugar en el tablero " + tableroDestino + ". Escribe coordenada (casilla) o 'rendirse': ");
                    }

                    String entrada = input.nextLine();
                    if (entrada.equalsIgnoreCase("rendirse")) {
                        if (jugador2 instanceof JugadorHumano) {
                            tablero.actualizar(jugador2,jugador1);
                            jugadores = Serializacion.cargarJugadores();
                            for (Jugador j : jugadores) {
                                System.out.println(j);
                            }
                            tableroDestinoInd.rellenar(simboloPerdedor);
                            plano = tableroDestinoInd.getTablero().getTablero();
                            v = GrupoTableros.verificarGanador(plano);
                            System.out.println("¡Jugador " + jugador2.getNombre() + " ha ganado el tablero " + tableroDestino + "!");
                        }
                        System.out.println("¡Jugador 1 se ha rendido! Turno para Jugador 2.");
                        turno++;
                        continue;
                    }

                    linea = entrada.split(" ");
                    if (puedeElegirTablero) {
                        x = Integer.parseInt(linea[0]);
                        yElegida = Integer.parseInt(linea[1]);
                    } else {
                        x = tableroDestino;
                        yElegida = Integer.parseInt(linea[0]);
                    }

                    boolean marcado = grupoTableros.marcarCasilla(x, yElegida, jugador1.getSimbolo());
                    if (!marcado) {
                        System.out.println("Casilla ocupada. Intenta otra vez.");
                        continue;
                    }

                    grupoTableros.imprimir();
                    tableroDestinoInd = (TableroIndividual) grupoTableros.getTablero(x);
                    plano = tableroDestinoInd.getTablero().getTablero();
                    v = GrupoTableros.verificarGanador(plano);
                    System.out.println("Resultado Jugador 1: " + v);

                    if (v == 'x' || v == 'o') {
                        if (jugador1 instanceof JugadorHumano) {
                            tablero.actualizar(jugador1,jugador2);
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
                    if (tableroDestinoInd.estaCompleto() && v == '-') {
                        System.out.println("El tablero " + x + " ha quedado en empate.");
                        ((JugadorHumano)jugador1).incrementarEmpatadas();
                        ((JugadorHumano)jugador2).incrementarEmpatadas();
                        Serializacion.guardarJugador(jugador1);
                        Serializacion.guardarJugador(jugador2);
                        jugadores = Serializacion.cargarJugadores();
                        for (Jugador j : jugadores) {
                            System.out.println(j);
                        }
                        tableroDestinoInd.rellenar('*');
                    }


                    turno++;
                }
            }


        }






    }

}
