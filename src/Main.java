import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Main main = new Main();
        main.mostrarMenu();
        main.scanner.close(); // Close the Scanner resource
    }

    private void mostrarMenu() {
        while (true) {
            mostrarOpcionsMenu();
            int opcio = llegirOpcioMenu();
            if (opcio == -1) continue;

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

    private void mostrarOpcionsMenu() {
        System.out.println("1. Jugar una partida nova");
        System.out.println("2. Reproduir una partida des d'un fitxer");
        System.out.println("3. Sortir");
        System.out.print("Selecciona una opció: ");
    }

    private int llegirOpcioMenu() {
        if (!scanner.hasNextInt()) {
            System.out.println("Entrada no vàlida. Torna a intentar-ho.");
            scanner.next(); // Clear invalid input
            return -1;
        }
        int opcio = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        return opcio;
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
            fiPartida = processarTorn(jugadorBlanc, jugadorNegre, torns, tornBlanc);
            tornBlanc = !tornBlanc; // Canvia el torn
        }

        guardarPartida(torns);
    }

    private boolean processarTorn(Jugador<Peca> jugadorBlanc, Jugador<Peca> jugadorNegre, Torns torns, boolean tornBlanc) {
        Jugador<Peca> jugadorActual = tornBlanc ? jugadorBlanc : jugadorNegre;
        Jugador<Peca> oponent = tornBlanc ? jugadorNegre : jugadorBlanc;
        String colorJugador = tornBlanc ? "blanc" : "negre";

        System.out.print("Introdueix el torn del jugador " + colorJugador + " (ex: E2 E4) o 'fi' per acabar: ");
        String torn = scanner.nextLine();

        if (torn.equalsIgnoreCase("fi")) {
            System.out.println("Partida finalitzada per decisió del jugador.");
            return true;
        } else {
            try {
                if (tornToPosition(torn, jugadorActual, oponent)) {
                    torns.afegirTorn(torn); // Afegir el torn a l'objecte Torns
                    return false;
                }
            } catch (FiJocException e) {
                torns.afegirTorn(torn); // Afegir l'últim torn abans de finalitzar
                System.out.println(e.getMessage());
                mostrarTauler(jugadorBlanc, jugadorNegre); // Mostrar el tauler final
                return true;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        return false;
    }

    private void guardarPartida(Torns torns) {
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

        for (Peca peca : blanc.pecesVives()) {
            tauler[peca.getFila()][peca.getColumna()] = peca.getTipus();
        }

        for (Peca peca : negre.pecesVives()) {
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
        return iniciarJoc("blanc");
    }

    private ArrayList<Peca> iniciarJocNegras() {
        return iniciarJoc("negre");
    }

    private ArrayList<Peca> iniciarJoc(String color) {
        ArrayList<Peca> peces = new ArrayList<>();
        int filaPeons = color.equals("blanc") ? 1 : 6;
        int filaAltres = color.equals("blanc") ? 0 : 7;

        // Afegir peons
        for (int i = 0; i < 8; i++) {
            peces.add(new Peca(Peca.PEO, filaPeons, i));
        }
        // Afegir altres peces
        peces.add(new Peca(Peca.TORRE, filaAltres, 0));
        peces.add(new Peca(Peca.CAVALL, filaAltres, 1));
        peces.add(new Peca(Peca.ALFIL, filaAltres, 2));
        peces.add(new Peca(Peca.REINA, filaAltres, 3));
        peces.add(new Peca(Peca.REI, filaAltres, 4));
        peces.add(new Peca(Peca.ALFIL, filaAltres, 5));
        peces.add(new Peca(Peca.CAVALL, filaAltres, 6));
        peces.add(new Peca(Peca.TORRE, filaAltres, 7));

        return peces;
    }
}