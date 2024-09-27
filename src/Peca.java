public class Peca implements Itipopeca {
    public final char PEO = 'P';
    public final char TORRE = 'T';
    public final char CAVALL = 'C';
    public final char ALFIL = 'A';
    public final char REINA = 'Q';
    public final char REI = 'K';

    private int columna;
    private int fila;
    private char tipus;

    public Peca(char tipus, int fila, int columna) {
        this.tipus = tipus;
        this.fila = fila;
        this.columna = columna;
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
    public void setPosicio(int fila, int columna) throws RuntimeException {
        if (fila < 0 || fila > 7 || columna < 0 || columna > 7){
            throw new RuntimeException("Posició incorrecta");
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
            throw new RuntimeException("Tipus de peça incorrecte");
        }
    }

    public String toString() {
        return "Peca [tipus=" + tipus + ", fila=" + fila + ", columna=" + columna + "]";
    }

    public boolean equals(Peca p) {
        return this.tipus == p.tipus && this.fila == p.fila && this.columna == p.columna;
    }
}
