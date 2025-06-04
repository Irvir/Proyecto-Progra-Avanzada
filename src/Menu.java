import java.util.Scanner;

public class Menu{

    private Scanner input = new Scanner(System.in);
    private String nombreJ1;
    private String nombreJ2;
    private JuegoBase tableroGrande;

    public void menu(){
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
                System.out.println("Bienvenido Jugador 2 (Hard): "+nombreJ2);
                tableroGrande = new JuegoBase(nombreJ1,"humano",nombreJ2,"cpu-hard");

                break;
        }
        input.nextLine();
        tableroGrande.comenzarJuego(nombreJ1,nombreJ2);

    }

}
