import java.util.ArrayList;

public class Jugador<E extends Itipopeca> {

    private final ArrayList<E> pecesVives;

    // Constructor que inicialitza l'objecte amb una llista predefinida de peces
    public Jugador(ArrayList<E> pecesVives) {
        this.pecesVives = pecesVives;
    }

    // Getter que retorna la llista de peces vives
    public ArrayList<E> getPecesVives() {
        return pecesVives;
    }

    // Mètode per moure una peça d'una posició a una altra
    public void mourePeca(int columnaAnterior, int filaAnterior, int columnaNova, int filaNova) {
        E peca = buscarEnPosicio(filaAnterior, columnaAnterior);
        if (peca == null) {
            throw new RuntimeException("Peca no trobada a la posició donada");
        }
        peca.setPosicio(filaNova, columnaNova);
    }

    // Mètode per eliminar una peça d'una posició donada
    public boolean eliminarPecaEnPosicio(int columna, int fila) throws FiJocException {
        E peca = buscarEnPosicio(fila, columna);
        if (peca == null) {
            return false;
        }
        if (peca.fiJoc()) {
            throw new FiJocException();
        }
        return pecesVives.remove(peca);
    }

    // Mètode privat que retorna la peça a una posició donada
    private E buscarEnPosicio(int fila, int columna) {
        for (E peca : pecesVives) {
            if (peca.getFila() == fila && peca.getColumna() == columna) {
                return peca;
            }
        }
        return null;
    }
}