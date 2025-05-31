public class JugadorFactory{
    public static Jugador crearJugador(String tipo, String nombre, char simbolo) {
        switch (tipo.toLowerCase()) {
            case "humano":
                return new JugadorHumano(nombre,simbolo);
            case "cpu-easy":
                return new CPUFacil(simbolo);
            case "cpu-hard":
                return new CPUDificil(simbolo);
            default:
                throw new IllegalArgumentException("Tipo de jugador inválido");
        }
    }

}
