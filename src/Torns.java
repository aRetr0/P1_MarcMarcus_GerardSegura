import java.io.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Torns<E> {
    private final ArrayList<E> llistatTorns;

    // Constructor per defecte que inicialitza el llistat de torns
    public Torns() {
        llistatTorns = new ArrayList<>();
    }

    // Constructor que carrega els torns des d'un fitxer
    public Torns(String nomFitxer) throws IOException {
        llistatTorns = new ArrayList<>();
        carregarDesDeFitxer(nomFitxer);
        if (llistatTorns.isEmpty()) {
            throw new IOException("No s'ha pogut llegir el fitxer o la llista és buida");
        }
    }

    // Afegeix un torn a l'última posició del llistat
    public void afegirTorn(E torn) {
        llistatTorns.add(torn);
    }

    // Guarda tots els torns de la partida en un fitxer del tipus txt
    public void guardarAFitxer(String nomFitxer) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomFitxer))) {
            for (E torn : llistatTorns) {
                writer.write(torn.toString());
                writer.newLine();
            }
        }
    }

    // Elimina i retorna el primer element del llistat
    public E agafarPrimerTorn() {
        if (llistatTorns.isEmpty()) {
            throw new NoSuchElementException("No hi ha més torns");
        }
        return llistatTorns.removeFirst();
    }

    // Mètode privat que llegeix un fitxer txt i inicialitza el llistat de torns
    private void carregarDesDeFitxer(String nomFitxer) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(nomFitxer))) {
            String linia;
            while ((linia = reader.readLine()) != null) {
                // Suposant que E té un constructor que accepta una cadena
                // Això pot necessitar ser ajustat segons la implementació de E
                E torn = (E) new Object(); // TODO: Placeholder, caldrà ajustar segons E
                llistatTorns.add(torn);
            }
        }
    }
}