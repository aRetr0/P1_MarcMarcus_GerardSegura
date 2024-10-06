import java.util.ArrayList;

public class Jugador<E extends Itipopeca> {

    private final ArrayList<E> pecesVives;

    public Jugador(ArrayList<E> pecesVives) {
        this.pecesVives = pecesVives;
    }

    public ArrayList<E> getPecesVives() {
        return pecesVives;
    }

    public boolean mourePeca(int columnaAnterior, int filaAnterior, int columnaNova, int filaNova) throws FiJocException {
        Peca peca = (Peca) buscarEnPosicio(filaAnterior, columnaAnterior);
        if (peca == null) {
            throw new RuntimeException("No hi ha cap peça a la posició inicial.");
        }

        // Verifica si hi ha una peça a la posició nova
        Peca pecaDestinacio = (Peca) buscarEnPosicio(filaNova, columnaNova);
        if (pecaDestinacio != null && pecaDestinacio.getTipus() == Peca.REI) {
            throw new FiJocException("El rei de l'oponent ha estat capturat. Fi de la partida.");
        }
        // Mou la peça a la nova posició
        peca.setPosicio(filaNova, columnaNova);

        return true;
    }

    public boolean eliminarPecaEnPosicio(int columna, int fila) throws FiJocException {
        E peca = buscarEnPosicio(fila, columna);
        if (peca == null) {
            return false;
        }
        if (peca.getTipus() == Peca.REI) {
            throw new FiJocException("El rei de l'oponent ha estat capturat. Fi de la partida.");
        }
        return pecesVives.remove(peca);
    }

    protected E buscarEnPosicio(int fila, int columna) {
        for (E peca : pecesVives) {
            if (peca.getFila() == fila && peca.getColumna() == columna) {
                return peca;
            }
        }
        return null;
    }

    public boolean hiHaPecaA(int columna, int fila) {
        return buscarEnPosicio(fila, columna) != null;
    }

    public boolean teRei() {
        for (E peca : pecesVives) {
            if (peca.getTipus() == Peca.REI) {
                return true;
            }
        }
        return false;
    }
}