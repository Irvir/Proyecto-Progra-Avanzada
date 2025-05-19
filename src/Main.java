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
interface Jugador{
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
        moneda = 0;
        if (moneda == 0) {
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
                    System.out.println("Resultado"+v);
                    listTablero.add(x);

                    if (v == 'x' || v == 'o') {
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
                    /*System.out.println("Escribe coordenada Jugador 2: ");
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
                        System.out.println("¡Jugador " + jugador2.getNombre() + " ha ganado el tablero " + x + "!");
                    }

                    if (resultado == 1 || resultado == 2) {
                        System.out.println("¡Jugador " + resultado + " ha ganado!");
                        break;
                    }
                    revisionMetaPlano = metaTablero.tableroGanado();
                    if (revisionMetaPlano == 1 || resultado == 2){
                        System.out.println("¡Jugador " + resultado + " ha ganado!");
                        aux = revisionMetaPlano;
                    }
                     */


                }





            }

        } else{
            /*
                Random random1 = new Random();
                int planoPlanoRandom = random1.nextInt(9);
                if (tipoJ2.equals("CPU-easy")) {
                    while (true) {
                        System.out.println("CPU Jugando: ");
                        CPUFacil cpuFacil = new CPUFacil(simboloGanador);
                        System.out.println("CPU Escogió: Plano: " + planoPlanoRandom + "Cuadrante: " + cpuFacil.hacerJugada(listTablero.get(planoPlanoRandom).getTablero()));
                        listTablero.get(planoPlanoRandom).marcarCasilla(cpuFacil.hacerJugada(tablero.getTablero()), 'x');
                        for (Tablero t : listTablero) {
                            t.imprimirTablero();
                        }

                        notificarObserver();
                        int resultado = listTablero.get(planoPlanoRandom).tableroGanado();
                        if (resultado != -1) {
                            char simbolo = (resultado == 1) ? 'x' : 'o';
                            int fila = planoPlanoRandom / 3;
                            int col = planoPlanoRandom % 3;
                            if (metaTablero.getCasilla(fila, col) == '-') {
                                metaTablero.setCasilla(fila, col,simbolo);
                            }
                            // Revisar si alguien ganó el tablero general
                            if (verificarMetaGanador() != '-') {
                                System.out.println("¡Jugador con símbolo " + simbolo + " ha ganado el tablero general!");
                                break;
                            }
                        }
                        if (listTablero.get(planoPlanoRandom).tableroGanado() != -1) {
                            break;
                        }
                        listTablero.get(planoPlanoRandom).imprimirTablero();
                        System.out.println("Escribe coordenada Jugador 1: ");
                        linea = input.nextLine().split(" ");
                        x = Integer.parseInt(linea[0]);
                        y = Integer.parseInt(linea[1]);
                        listTablero.get(x).marcarCasilla(y, simboloPerdedor);
                        for (Tablero t : listTablero) {
                            t.imprimirTablero();
                        }

                        notificarObserver();
                        resultado = listTablero.get(x).tableroGanado();
                        if (resultado != -1) {
                            char simbolo = (resultado == 1) ? 'x' : 'o';
                            int fila = x / 3;
                            int col = x % 3;
                            if (metaTablero.getCasilla(fila, col)== '-') {
                                metaTablero.setCasilla(fila, col,simbolo);
                            }
                            // Revisar si alguien ganó el tablero general
                            if (verificarMetaGanador() != '-') {
                                System.out.println("¡Jugador con símbolo " + simbolo + " ha ganado el tablero general!");
                                break;
                            }}
                        listTablero.get(x).imprimirTablero();

                    }
                }
                if (tipoJ2.equals("humano")) {
                    while (true) {
                        System.out.println("Escribe coordenada Jugador 2: ");
                        linea = input.nextLine().split(" ");
                        x = Integer.parseInt(linea[0]);
                        y = Integer.parseInt(linea[1]);
                        listTablero.get(x).marcarCasilla(y, simboloGanador);
                        for (Tablero t : listTablero) {
                            t.imprimirTablero();
                        }

                        notificarObserver();
                        int resultado = listTablero.get(x).tableroGanado();
                        if (resultado != -1) {
                            char simbolo = (resultado == 1) ? 'x' : 'o';
                            int fila = x / 3;
                            int col = x % 3;
                            if (metaTablero.getCasilla(fila, col) == '-') {
                                metaTablero.setCasilla(fila, col, simbolo);
                            }
                            // Revisar si alguien ganó el tablero general
                            if (verificarMetaGanador() != '-') {
                                System.out.println("¡Jugador con símbolo " + simbolo + " ha ganado el tablero general!");
                                break;
                            }}
                        if (listTablero.get(x).tableroGanado() != -1) {
                            break;
                        }
                        listTablero.get(x).imprimirTablero();
                        System.out.println("Escribe coordenada Jugador 1: ");
                        linea = input.nextLine().split(" ");
                        x = Integer.parseInt(linea[0]);
                        y = Integer.parseInt(linea[1]);
                        listTablero.get(x).marcarCasilla(y, simboloPerdedor);
                        for (Tablero t : listTablero) {
                            t.imprimirTablero();
                        }

                        notificarObserver();
                        resultado = listTablero.get(x).tableroGanado();
                        if (resultado != -1) {
                            char simbolo = (resultado == 1) ? 'x' : 'o';
                            int fila = x / 3;
                            int col = x % 3;
                            if (metaTablero.getCasilla(fila, col) == '-') {
                                metaTablero.setCasilla(fila, col,simbolo);
                            }
                            // Revisar si alguien ganó el tablero general
                            if (verificarMetaGanador() != '-') {
                                System.out.println("¡Jugador con símbolo " + simbolo + " ha ganado el tablero general!");
                                break;
                            }}
                        if (listTablero.get(x).tableroGanado() != -1) {
                            break;
                        }
                        listTablero.get(x).imprimirTablero();

                    }




                }

            */
            }




    }
    char verificarMetaGanador() {
        // filas
        for (int i = 0; i < 3; i++) {
            if (metaTablero.getCasilla(i,0 ) != '-' && metaTablero.getCasilla(i,0 ) == metaTablero.getCasilla(i,1 )
                    && metaTablero.getCasilla(i,1) == metaTablero.getCasilla(i,2 )) {
                return metaTablero.getCasilla(i,0 );
            }
        }

        // columnas
        for (int j = 0; j < 3; j++) {
            if (metaTablero.getCasilla(0,j ) != '-' && metaTablero.getCasilla(0,j ) == metaTablero.getCasilla(1,j )
                    && metaTablero.getCasilla(1,j ) == metaTablero.getCasilla(2,j )) {
                return metaTablero.getCasilla(0,j );
            }
        }

        // diagonales
        if (metaTablero.getCasilla(0,0 ) != '-' && metaTablero.getCasilla(0,0 )==
                metaTablero.getCasilla(1,1 ) && metaTablero.getCasilla(1,1 ) ==metaTablero.getCasilla(2,2 )) {
            return metaTablero.getCasilla(0,0 );
        }

        if (metaTablero.getCasilla(0,2 ) != '-' &&metaTablero.getCasilla(0,2 ) == metaTablero.getCasilla(1,1 )
                && metaTablero.getCasilla(1,1 ) == metaTablero.getCasilla(2,0 )) {
            return metaTablero.getCasilla(0,2 );
        }

        return '-'; // aún no hay ganador
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
                System.out.println("Registre el Jugador 2");
                nombreJ2 = input.nextLine();
                System.out.println("Bienvenido Jugador 2: "+nombreJ2);
                tableroGrande = new JuegoBase(nombreJ1,"humano",nombreJ2,"humano");
                break;
            case 2:
                nombreJ2 = "CPU";
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