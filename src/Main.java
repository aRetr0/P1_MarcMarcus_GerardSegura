import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Main main = new Main();
        main.mostrarMenu();
    }

    private void mostrarMenu() {
        while (true) {
            System.out.println("1. Jugar una partida nova");
            System.out.println("2. Reproduir una partida des d'un fitxer");
            System.out.println("3. Sortir");
            System.out.print("Selecciona una opció: ");
            int opcio = scanner.nextInt();
            scanner.nextLine();

            switch (opcio) {
                case 1:
                    jugarNovaPartida();
                    break;
                case 2:
                    reproduirPartida();
                    break;
                case 3:
                    System.out.println("Sortint del programa...");
                    return;
                default:
                    System.out.println("Opció no vàlida. Torna a intentar-ho.");
            }
        }
    }

    private void jugarNovaPartida() {
        ArrayList<Peca> pecesBlanques = iniciarJocBlancas();
        ArrayList<Peca> pecesNegres = iniciarJocNegras();
        Jugador<Peca> blanc = new Jugador<>(pecesBlanques);
        Jugador<Peca> negre = new Jugador<>(pecesNegres);
        Torns<String> torns = new Torns<>();

        while (true) {
            mostrarTauler(blanc, negre);
            System.out.print("Introdueix el torn del jugador blanc (ex: E2 E4): ");
            String tornBlanc = scanner.nextLine();
            try {
                tornToPosition(tornBlanc, blanc, negre);
                torns.afegirTorn(tornBlanc);
            } catch (FiJocException e) {
                System.out.println(e.getMessage());
                break;
            }

            mostrarTauler(blanc, negre);
            System.out.print("Introdueix el torn del jugador negre (ex: E7 E5): ");
            String tornNegre = scanner.nextLine();
            try {
                tornToPosition(tornNegre, negre, blanc);
                torns.afegirTorn(tornNegre);
            } catch (FiJocException e) {
                System.out.println(e.getMessage());
                break;
            }
        }

        try {
            torns.guardarAFitxer("partida.txt");
        } catch (IOException e) {
            System.out.println("Error al guardar la partida: " + e.getMessage());
        }
    }


    private void reproduirPartida() {
        Torns<String> torns = llegirTorns();
        ArrayList<Peca> pecesBlanques = iniciarJocBlancas();
        ArrayList<Peca> pecesNegres = iniciarJocNegras();
        Jugador<Peca> blanc = new Jugador<>(pecesBlanques);
        Jugador<Peca> negre = new Jugador<>(pecesNegres);

        while (true) {
            mostrarTauler(blanc, negre);
            try {
                String tornBlanc = torns.agafarPrimerTorn();
                tornToPosition(tornBlanc, blanc, negre);
                mostrarTauler(blanc, negre);

                String tornNegre = torns.agafarPrimerTorn();
                tornToPosition(tornNegre, negre, blanc);
                mostrarTauler(blanc, negre);
            } catch (NoSuchElementException e) {
                System.out.println("Partida finalitzada.");
                break;
            }
        }
    }

    private Torns<String> llegirTorns() {
        System.out.print("Introdueix el nom del fitxer: ");
        String nomFitxer = scanner.nextLine();
        try {
            return new Torns<>(nomFitxer);
        } catch (IOException e) {
            System.out.println("Error al llegir el fitxer: " + e.getMessage());
            return llegirTorns();
        }
    }

    private void tornToPosition(String torn, Jugador<Peca> p1, Jugador<Peca> p2) {
        String[] parts = torn.split(" ");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Torn invàlid");
        }
        int columnaAnterior = parts[0].charAt(0) - 'A';
        int filaAnterior = parts[0].charAt(1) - '1';
        int columnaNova = parts[1].charAt(0) - 'A';
        int filaNova = parts[1].charAt(1) - '1';

        p1.mourePeca(columnaAnterior, filaAnterior, columnaNova, filaNova);
        p2.eliminarPecaEnPosicio(columnaNova, filaNova);
    }

    private void mostrarTauler(Jugador<Peca> blanc, Jugador<Peca> negre) {
        char[][] tauler = new char[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tauler[i][j] = '.';
            }
        }

        for (Peca peca : blanc.getPecesVives()) {
            tauler[peca.getFila()][peca.getColumna()] = peca.getTipus();
        }

        for (Peca peca : negre.getPecesVives()) {
            tauler[peca.getFila()][peca.getColumna()] = Character.toLowerCase(peca.getTipus());
        }

        for (int i = 7; i >= 0; i--) {
            System.out.print((i + 1) + "| "); // Coordenada de fila
            for (int j = 0; j < 8; j++) {
                if (i == 0) System.out.print("\u0332" + tauler[i][j] + " ");
                else
                    System.out.print(tauler[i][j] + " ");
            }
            System.out.println();
        }
        System.out.print(" | "); // Espai per a l'alineació de les coordenades de columna
        for (char c = 'A'; c <= 'H'; c++) {
            System.out.print(c + " ");
        }
        System.out.println();
    }

    private ArrayList<Peca> iniciarJocBlancas() {
        ArrayList<Peca> pecesBlanques = new ArrayList<>();
        // Afegir peons blancs
        for (int i = 0; i < 8; i++) {
            pecesBlanques.add(new Peca(Peca.PEO, 1, i));
        }
        // Afegir altres peces blanques
        pecesBlanques.add(new Peca(Peca.TORRE, 0, 0));
        pecesBlanques.add(new Peca(Peca.CAVALL, 0, 1));
        pecesBlanques.add(new Peca(Peca.ALFIL, 0, 2));
        pecesBlanques.add(new Peca(Peca.REINA, 0, 3));
        pecesBlanques.add(new Peca(Peca.REI, 0, 4));
        pecesBlanques.add(new Peca(Peca.ALFIL, 0, 5));
        pecesBlanques.add(new Peca(Peca.CAVALL, 0, 6));
        pecesBlanques.add(new Peca(Peca.TORRE, 0, 7));
        return pecesBlanques;
    }

    private ArrayList<Peca> iniciarJocNegras() {
        ArrayList<Peca> pecesNegres = new ArrayList<>();
        // Afegir peons negres
        for (int i = 0; i < 8; i++) {
            pecesNegres.add(new Peca(Peca.PEO, 6, i));
        }
        // Afegir altres peces negres
        pecesNegres.add(new Peca(Peca.TORRE, 7, 0));
        pecesNegres.add(new Peca(Peca.CAVALL, 7, 1));
        pecesNegres.add(new Peca(Peca.ALFIL, 7, 2));
        pecesNegres.add(new Peca(Peca.REINA, 7, 3));
        pecesNegres.add(new Peca(Peca.REI, 7, 4));
        pecesNegres.add(new Peca(Peca.ALFIL, 7, 5));
        pecesNegres.add(new Peca(Peca.CAVALL, 7, 6));
        pecesNegres.add(new Peca(Peca.TORRE, 7, 7));
        return pecesNegres;
    }
}