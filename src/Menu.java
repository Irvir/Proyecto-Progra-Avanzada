import java.util.Scanner;

public class Menu{
    /**
     * input Es la llamada del usuario para digitar la opción del jugador
     */
    private Scanner input = new Scanner(System.in);
    /**
     * nombreJ1 Es el nombre del Jugador1
     */
    private String nombreJ1;
    /**
     * nombreJ2 Es el nombre del Jugador2
     */
    private String nombreJ2;
    /**
     *tableroGrande = Es el MetaTablero dentro del que decide quien gana o no el juego final
     */
    private JuegoBase tableroGrande;

    /**
     * Es el menú que invita al jugador a seleccionar su opción
     */
    public void menu(){
        System.out.println("Bienvenido al Juego seleccione el modo de Juego:");
        System.out.println("Registrese para jugar.");

        // Introducción del Nombre del Jugador1
        nombreJ1 = input.nextLine();

        System.out.println("Bienvenido Jugador 1: " + nombreJ1);
        System.out.println("1. Jugador vs Jugador \n2.Jugador vs CPU (Easy)\n3.Jugador vs CPU(Hard)");

        //Selección del modo del Jugador

        int inputUser = input.nextInt();

        switch (inputUser){

            // Caso HUMANO

            case 1:
                input.nextLine();
                System.out.println("Registre el Jugador 2");

                //Asigna nombre al Jugador 2
                nombreJ2 = input.nextLine();
                System.out.println("Bienvenido Jugador 2: "+nombreJ2);

                //Crea instancia del TableroGrande con un nuevo Juego Base
                tableroGrande = new JuegoBase(nombreJ1,"humano",nombreJ2,"humano");
                break;

            // CASO CPU - FÁCIL

            case 2:

                //Se le asigna automáticamente el nombre de CPU
                nombreJ2 = "CPU";
                System.out.println("Bienvenido Jugador 2 (Easy): "+nombreJ2);

                //Crea instancia del TableroGrande con un nuevo Juego Base
                tableroGrande = new JuegoBase(nombreJ1,"humano",nombreJ2,"cpu-easy");

                break;

            // CASO CPU  DIFÍCIL

            case 3:

                //Se le asigna automáticamente el nombre de CPU
                nombreJ2 = "CPU";
                System.out.println("Bienvenido Jugador 2 (Hard): "+nombreJ2);

                //Crea instancia del TableroGrande con un nuevo Juego Base
                tableroGrande = new JuegoBase(nombreJ1,"humano",nombreJ2,"cpu-hard");

                break;
        }
        input.nextLine();
        tableroGrande.comenzarJuego(nombreJ1,nombreJ2);



    }

}
