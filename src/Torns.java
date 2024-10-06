import java.io.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Torns {
    private final ArrayList<String> llistatTorns;

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
    public void afegirTorn(String torn) {
        llistatTorns.add(torn);
    }

    // Guarda tots els torns de la partida en un fitxer del tipus txt
    public void guardarAFitxer(String nomFitxer) throws IOException {
        if (nomFitxer == null || nomFitxer.isEmpty()) {
            throw new IllegalArgumentException("El nom del fitxer no pot ser nul o buit");
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomFitxer))) {
            for (String torn : llistatTorns) {
                writer.write(torn);
                writer.newLine();
            }
            System.out.println("Torns guardats al fitxer: " + llistatTorns); // Debug statement
        }
    }

    // Elimina i retorna el primer element del llistat
    public String agafarPrimerTorn() {
        if (llistatTorns.isEmpty()) {
            throw new NoSuchElementException("No hi ha més torns");
        }
        return llistatTorns.removeFirst();
    }

    // Mètode privat que llegeix un fitxer txt i inicialitza el llistat de torns
    private void carregarDesDeFitxer(String nomFitxer) throws IOException {
        if (nomFitxer == null || nomFitxer.isEmpty()) {
            throw new IllegalArgumentException("El nom del fitxer no pot ser nul o buit");
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(nomFitxer))) {
            String linia;
            while ((linia = reader.readLine()) != null) {
                llistatTorns.add(linia);
            }
        }
    }
}