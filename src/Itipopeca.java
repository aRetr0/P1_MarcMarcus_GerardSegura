public interface Itipopeca {
    // NO MODIFICAR
    // mètodes abstractes: aqui detallem les capçaleres,
    // les classes que implementen la intefície, hauran d'implementar aquests mètodes
    public abstract char getTipus();
    public abstract int getFila();
    public abstract int getColumna();
    public abstract void setPosicio(int fila, int columna) throws RuntimeException;
    // si la posició no és correcta, cal llançar una excepció
    public abstract boolean fiJoc();
}
