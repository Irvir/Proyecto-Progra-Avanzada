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

}
interface Jugador{
    String getNombre();
    char getSimbolo();
    int hacerJugada(char[][] tablero);
}

class JuegoBase implements Sujeto{
    int moneda;
    Jugador jugador1;
    Jugador jugador2;
    Scanner input = new Scanner(System.in);
    Scanner input2 = new Scanner(System.in);

    char simboloGanador;
    char simboloPerdedor ;
    Tablero tablero;

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
        Random random = new Random();
        moneda = random.nextInt(2);
        ArrayList<Tablero> listTablero= new ArrayList<>();
        if (moneda == 0) {
            System.out.println("¿Qué símbolo ('x' ó 'o') desea el Jugador 1?");
            simboloGanador = input.nextLine().charAt(0);
            simboloPerdedor = (simboloGanador == 'x') ? 'o' : 'x';

        } else {
            if (tipoJ2.equals("humano")) {
                System.out.println("¿Qué símbolo ('x' ó 'o') desea el Jugador 2?");
                simboloGanador = input.nextLine().charAt(0);
                if (simboloGanador == 'x'){
                    simboloPerdedor='o';
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

        int y;
        if (moneda == 0) {
            while (true) {
                System.out.println("Escribe coordenada Jugador 1: ");
                y = input2.nextInt();
                tablero.marcarCasilla(y, simboloPerdedor);
                if (tablero.tableroGanado() != -1) {
                    break;
                }
                tablero.imprimirTablero();
                System.out.println("Escribe coordenada Jugador 2: ");
                y = input2.nextInt();
                tablero.marcarCasilla(y, simboloGanador);

                tablero.imprimirTablero();

            }
        } else {
            if (tipoJ2 == "CPU-easy") {
                while (true) {
                    System.out.println("CPU Jugando: ");
                    CPUFacil cpuFacil = new CPUFacil(simboloGanador);
                    tablero.marcarCasilla(cpuFacil.hacerJugada(tablero.getTablero()), 'x');
                    if (tablero.tableroGanado() != -1) {
                        break;
                    }

                    tablero.imprimirTablero();
                    System.out.println("Escribe coordenada Jugador 1: ");
                    y = input.nextInt();
                    tablero.marcarCasilla(y, simboloPerdedor);
                    tablero.imprimirTablero();

                }
            }
            if (tipoJ2 == "humano"){
                while (true) {
                    System.out.println("ooo");
                    System.out.println("Escribe coordenada Jugador 2: ");
                    y = input2.nextInt();
                    tablero.marcarCasilla(y, simboloGanador);
                    if (tablero.tableroGanado() != -1) {
                        break;
                    }
                    tablero.imprimirTablero();
                    System.out.println("Escribe coordenada Jugador 1: ");
                    y = input2.nextInt();
                    tablero.marcarCasilla(y, simboloPerdedor);
                    if (tablero.tableroGanado() != -1) {
                        break;
                    }
                    tablero.imprimirTablero();



                }


            }


        }


        tablero.imprimirTablero();
        notificarObserver();
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