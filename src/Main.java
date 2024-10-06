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

            if (!scanner.hasNextInt()) {
                System.out.println("Entrada no vàlida. Torna a intentar-ho.");
                scanner.next(); // neteja el input invalid
                continue;
            }

            int opcio = scanner.nextInt();
            scanner.nextLine(); // consumeix la línia en blanc

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
        Jugador<Peca> jugadorBlanc = new Jugador<>(pecesBlanques);
        Jugador<Peca> jugadorNegre = new Jugador<>(pecesNegres);
        Torns torns = new Torns();
        boolean fiPartida = false;
        boolean tornBlanc = true; // Comença el jugador blanc

        while (!fiPartida) {
            mostrarTauler(jugadorBlanc, jugadorNegre);

            Jugador<Peca> jugadorActual = tornBlanc ? jugadorBlanc : jugadorNegre;
            Jugador<Peca> oponent = tornBlanc ? jugadorNegre : jugadorBlanc;
            String colorJugador = tornBlanc ? "blanc" : "negre";

            System.out.print("Introdueix el torn del jugador " + colorJugador + " (ex: E2 E4) o 'fi' per acabar: ");
            String torn = scanner.nextLine();

            if (torn.equalsIgnoreCase("fi")) {
                System.out.println("Partida finalitzada per decisió del jugador.");
                fiPartida = true;
            } else {
                try {
                    if (tornToPosition(torn, jugadorActual, oponent)) {
                        torns.afegirTorn(torn); // Afegir el torn a l'objecte Torns
                        tornBlanc = !tornBlanc; // Canvia el torn
                    }
                } catch (FiJocException e) {
                    torns.afegirTorn(torn); // Afegir l'últim torn abans de finalitzar
                    System.out.println(e.getMessage());
                    mostrarTauler(jugadorBlanc, jugadorNegre); // Mostrar el tauler final
                    fiPartida = true;
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }

        try {
            torns.guardarAFitxer("partida.txt");
            System.out.println("Partida guardada a 'partida.txt'");
        } catch (IOException e) {
            System.out.println("Error al guardar la partida: " + e.getMessage());
        }
    }

    private boolean tornToPosition(String torn, Jugador<Peca> p1, Jugador<Peca> p2) throws RuntimeException {
        String[] parts = torn.split(" ");
        if (parts.length != 2) {
            System.out.println("Format de torn invàlid. Utilitza el format 'E2 E4'.");
            return false;
        }

        int columnaAnterior = Character.toUpperCase(parts[0].charAt(0)) - 'A';
        int filaAnterior = Character.getNumericValue(parts[0].charAt(1)) - 1;
        int columnaNova = Character.toUpperCase(parts[1].charAt(0)) - 'A';
        int filaNova = Character.getNumericValue(parts[1].charAt(1)) - 1;

        if (columnaAnterior < 0 || columnaAnterior > 7 || filaAnterior < 0 || filaAnterior > 7 || columnaNova < 0 || columnaNova > 7 || filaNova < 0 || filaNova > 7) {
            System.out.println("Posició fora del tauler. Intenta-ho de nou.");
            return false;
        }

        // Verifica si hi ha una peça a la posició inicial
        if (!p1.hiHaPecaA(columnaAnterior, filaAnterior)) {
            System.out.println("No hi ha cap peça a la posició inicial.");
            return false;
        }

        boolean movimentReeixit = p1.mourePeca(columnaAnterior, filaAnterior, columnaNova, filaNova);
        if (movimentReeixit) {
            if (p2.hiHaPecaA(columnaNova, filaNova)) {
                p2.eliminarPecaEnPosicio(columnaNova, filaNova);
            }
            return true;
        } else {
            System.out.println("Moviment no vàlid per a aquesta peça.");
            return false;
        }
    }

    private void reproduirPartida() {
        Torns torns = llegirTorns();
        ArrayList<Peca> pecesBlanques = iniciarJocBlancas();
        ArrayList<Peca> pecesNegres = iniciarJocNegras();
        Jugador<Peca> blanc = new Jugador<>(pecesBlanques);
        Jugador<Peca> negre = new Jugador<>(pecesNegres);

        while (true) {
            mostrarTauler(blanc, negre);
            System.out.println();
            try {
                String tornBlanc = torns.agafarPrimerTorn();
                tornToPosition(tornBlanc, blanc, negre);
                mostrarTauler(blanc, negre);

                String tornNegre = torns.agafarPrimerTorn();
                tornToPosition(tornNegre, negre, blanc);
                mostrarTauler(blanc, negre);
                System.out.println();
            } catch (NoSuchElementException e) {
                System.out.println("Partida finalitzada.");
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Torn invàlid: " + e.getMessage());
                continue;
            }
        }
    }

    private Torns llegirTorns() {
        System.out.print("Introdueix el nom del fitxer: ");
        String nomFitxer = scanner.nextLine();
        try {
            return new Torns(nomFitxer);
        } catch (IOException e) {
            System.out.println("Error al llegir el fitxer: " + e.getMessage());
            return llegirTorns();
        }
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
                if (i == 0) {
                    System.out.print("\u001B[4m" + tauler[i][j] + "\u001B[0m "); // Underline the character
                } else {
                    System.out.print(tauler[i][j] + " ");
                }
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