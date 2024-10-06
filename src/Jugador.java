import java.util.ArrayList;

public record Jugador<E extends Itipopeca>(ArrayList<E> pecesVives) {

    public boolean mourePeca(int columnaInicial, int filaInicial, int columnaFinal, int filaFinal) throws FiJocException {
        E peca = buscarPecaEnPosicio(filaInicial, columnaInicial);
        if (peca == null) {
            throw new RuntimeException("No hi ha cap peça a la posició inicial.");
        }

        // Verifica si hi ha una peça a la posició final
        E pecaDestinacio = buscarPecaEnPosicio(filaFinal, columnaFinal);
        if (pecaDestinacio != null && pecaDestinacio.getTipus() == Peca.REI) {
            throw new FiJocException("El rei de l'oponent ha estat capturat. Fi de la partida.");
        }

        // Mou la peça a la nova posició
        peca.setPosicio(filaFinal, columnaFinal);

        return true;
    }

    public void eliminarPecaEnPosicio(int columna, int fila) throws FiJocException {
        E peca = buscarPecaEnPosicio(fila, columna);
        if (peca == null) {
            return;
        }
        if (peca.getTipus() == Peca.REI) {
            throw new FiJocException("El rei de l'oponent ha estat capturat. Fi de la partida.");
        }
        pecesVives.remove(peca);
    }

    private E buscarPecaEnPosicio(int fila, int columna) {
        for (E peca : pecesVives) {
            if (peca.getFila() == fila && peca.getColumna() == columna) {
                return peca;
            }
        }
        return null;
    }

    public boolean hiHaPecaA(int columna, int fila) {
        return buscarPecaEnPosicio(fila, columna) != null;
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