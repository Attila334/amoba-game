package hu.nye.progtech;

import hu.nye.progtech.persistence.HighScoreManager;
import hu.nye.progtech.model.Board;
import hu.nye.progtech.model.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;

class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        LOGGER.info("Az alkalmazás elindult.");
        Scanner scanner = new Scanner(System.in);
        HighScoreManager highScoreManager;

        try {
            highScoreManager = new HighScoreManager();
        } catch (SQLException e) {
            LOGGER.error("Nem sikerült csatlakozni az adatbázishoz!", e);
            System.out.println("Hiba: Az adatbázis nem elérhető. A játék mentés nélkül fut.");
            highScoreManager = null;
        }

        System.out.println("Üdvözöl az Amőba játék!");
        System.out.print("Kérlek, add meg a neved: ");
        String playerName = scanner.nextLine();

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    playGame(scanner, playerName, highScoreManager);
                    break;
                case "2":
                    if (highScoreManager != null) {
                        highScoreManager.printHighScores();
                    } else {
                        System.out.println("Az adatbázis nem elérhető.");
                    }
                    break;
                case "3":
                    running = false;
                    System.out.println("Viszlát!");
                    break;
                default:
                    System.out.println("Érvénytelen választás!");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n--- FŐMENÜ ---");
        System.out.println("1. Új játék indítása");
        System.out.println("2. Dicsőségtábla (High Score)");
        System.out.println("3. Kilépés");
        System.out.print("Válassz menüpontot: ");
    }

    private static void playGame(Scanner scanner, String playerName, HighScoreManager highScoreManager) {
        LOGGER.info("Új játék indult: {}", playerName);
        Board board = new Board(10, 10);
        Random random = new Random();
        char playerSymbol = 'X';
        char computerSymbol = 'O';

        while (true) {
            System.out.println(board);
            boolean validMove = false;
            while (!validMove) {
                System.out.println("Te következel! Adj meg egy sort és egy oszlopot (pl: 0 5):");
                try {
                    String line = scanner.nextLine();
                    String[] parts = line.split(" ");
                    int row = Integer.parseInt(parts[0]);
                    int col = Integer.parseInt(parts[1]);

                    Position pos = new Position(row, col);
                    if (board.placeSymbol(pos, playerSymbol)) {
                        validMove = true;
                    } else {
                        System.out.println("Érvénytelen lépés, próbáld újra!");
                    }
                } catch (Exception e) {
                    System.out.println("Hibás formátum! Használj számokat szóközzel elválasztva.");
                }
            }

            if (board.checkWin(playerSymbol)) {
                System.out.println(board);
                System.out.println("GRATULÁLOK! Nyertél!");
                LOGGER.info("Játékos nyert: {}", playerName);
                if (highScoreManager != null) {
                    highScoreManager.saveWin(playerName);
                    System.out.println("Pontszám mentve.");
                }
                break;
            }

            System.out.println("A gép gondolkodik...");
            boolean computerMoved = false;
            while (!computerMoved) {
                int r = random.nextInt(10);
                int c = random.nextInt(10);
                Position compPos = new Position(r, c);
                if (board.placeSymbol(compPos, computerSymbol)) {
                    computerMoved = true;
                    System.out.println("A gép lépett ide: " + r + " " + c);
                }
            }

            if (board.checkWin(computerSymbol)) {
                System.out.println(board);
                System.out.println("SAJNÁLOM, a gép nyert!");
                LOGGER.info("A gép nyert {} ellen.", playerName);
                break;
            }
        }
    }
}
