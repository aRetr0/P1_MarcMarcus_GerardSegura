import java.util.ArrayList;

public class Torns<E> {
    private ArrayList<E> llistatTorns;

    public Torns() {
        // TODO
        llistatTorns = new ArrayList<>();
    }

    public Torns(String nomFitxer) {
        // TODO

    }

    public void afegirTorn(E torn) {
        // TODO
        llistatTorns.add(torn);
    }

    public E agafarPrimerTorn() {
        // TODO
        E primerTorn;
        primerTorn = llistatTorns.get(0);
        return primerTorn;
    }

    public void guardarAFitxer(String nomFitxer) {
        // TODO

    }

    private void carregarDesDeFitxer(String nomFitxer) {
        // TODO
    }
}
