public class Peca implements Itipopeca {
    public static final char PEO = 'P';
    public static final char TORRE = 'T';
    public static final char CAVALL = 'C';
    public static final char ALFIL = 'A';
    public static final char REINA = 'Q';
    public static final char REI = 'K';

    private final char tipus;
    private int columna;
    private int fila;

    public Peca(char tipus, int fila, int columna) {
        checkTipus(tipus); // Validate piece type
        this.tipus = tipus;
        setPosicio(fila, columna); // Validate position
    }

    @Override
    public char getTipus() {
        return this.tipus;
    }

    @Override
    public int getFila() {
        return this.fila;
    }

    @Override
    public int getColumna() {
        return this.columna;
    }

    @Override
    public void setPosicio(int fila, int columna) {
        if (fila < 0 || fila > 7 || columna < 0 || columna > 7) {
            throw new IllegalArgumentException("Posició incorrecta: fila i columna han d'estar entre 0 i 7.");
        }
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public boolean fiJoc() {
        return this.tipus == REI;
    }

    private void checkTipus(char tipus) {
        if (tipus != PEO && tipus != TORRE && tipus != CAVALL && tipus != ALFIL && tipus != REINA && tipus != REI) {
            throw new IllegalArgumentException("Tipus de peça incorrecte: " + tipus);
        }
    }

    @Override
    public String toString() {
        return "Peca [tipus=" + tipus + ", fila=" + fila + ", columna=" + columna + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Peca peca = (Peca) obj;
        return tipus == peca.tipus && columna == peca.columna && fila == peca.fila;
    }

    @Override
    public int hashCode() {
        int result = Character.hashCode(tipus);
        result = 31 * result + Integer.hashCode(columna);
        result = 31 * result + Integer.hashCode(fila);
        return result;
    }
}