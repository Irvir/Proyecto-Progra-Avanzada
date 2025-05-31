
public interface Sujeto{
    void agregarObserver(Observer o);
    void eliminarObserver(Observer o);
    void notificarObserver();
}