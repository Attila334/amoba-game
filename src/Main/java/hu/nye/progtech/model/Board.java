package hu.nye.progtech.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A játéktáblát reprezentáló osztály.
 * Tárolja a mezőket, kezeli a lépéseket és ellenőrzi a nyerést.
 */
public class Board {

    private static final Logger LOGGER = LoggerFactory.getLogger(Board.class);

    private final int n; // sorok száma
    private final int m; // oszlopok száma
    private final char[][] map; // a tábla 2D tömb reprezentációja

    /**
     * Konstruktor, amely létrehozza az üres táblát.
     *
     * @param n Sorok száma.
     * @param m Oszlopok száma.
     */
    public Board(int n, int m) {
        this.n = n;
        this.m = m;
        this.map = new char[n][m];
        // Tábla feltöltése pontokkal (üres mező jelzése)
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                map[i][j] = '.';
            }
        }
        LOGGER.info("Létrejött egy {}x{} méretű tábla.", n, m);
    }

    /**
     * Megpróbál elhelyezni egy jelet a táblán.
     *
     * @param position A célpozíció (sor, oszlop).
     * @param symbol   A játékos vagy gép jele (pl. 'X', 'O').
     * @return true, ha a lépés sikeres, false, ha érvénytelen.
     */
    public boolean placeSymbol(Position position, char symbol) {
        if (!isValidMove(position)) {
            LOGGER.warn("Érvénytelen lépés kísérlet: {}", position);
            return false;
        }

        map[position.getRow()][position.getCol()] = symbol;
        LOGGER.info("Sikeres lépés: {} jel a {} pozícióra.", symbol, position);
        return true;
    }

    /**
     * Ellenőrzi, hogy egy adott pozícióra szabad-e lépni.
     */
    private boolean isValidMove(Position position) {
        int r = position.getRow();
        int c = position.getCol();

        // 1. Pályán belül van-e?
        if (r < 0 || r >= n || c < 0 || c >= m) {
            return false;
        }

        // 2. Üres-e a mező?
        return map[r][c] == '.';
    }

    /**
     * Ellenőrzi, hogy az adott jelet használó játékos nyert-e.
     * A feladatkiírás szerint 4 egyforma jel kell.
     *
     * @param symbol A vizsgált játékos jele.
     * @return true, ha nyert.
     */
    public boolean checkWin(char symbol) {
        // Végigmegyünk minden mezőn
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                // Ha nem a keresett jel van ott, átugorjuk
                if (map[i][j] != symbol) {
                    continue;
                }

                // Négy irány ellenőrzése:
                // 1. Vízszintes (jobbra)
                if (checkDirection(i, j, 0, 1, symbol)) return true;
                // 2. Függőleges (lefelé)
                if (checkDirection(i, j, 1, 0, symbol)) return true;
                // 3. Átlós (jobbra le)
                if (checkDirection(i, j, 1, 1, symbol)) return true;
                // 4. Átlós (balra le)
                if (checkDirection(i, j, 1, -1, symbol)) return true;
            }
        }
        return false;
    }

    /**
     * Segédmetódus egy adott irány ellenőrzésére.
     * A (row, col) pontból indulva megnézi, hogy van-e 4 db jel az adott irányban.
     */
    private boolean checkDirection(int row, int col, int dRow, int dCol, char symbol) {
        for (int k = 1; k < 4; k++) { // A 0. elem maga a kiindulópont, így még 3-at kell nézni
            int r = row + k * dRow;
            int c = col + k * dCol;

            // Ha kilépünk a tábláról, vagy nem egyezik a jel, akkor nem nyert ebben az irányban
            if (r < 0 || r >= n || c < 0 || c >= m || map[r][c] != symbol) {
                return false;
            }
        }
        return true;
    }

    /**
     * A tábla kirajzolása String formátumban a konzolra.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Fejléc (oszlopok számozása)
        sb.append("  ");
        for (int j = 0; j < m; j++) {
            sb.append(j).append(" ");
        }
        sb.append("\n");

        // Sorok kirajzolása
        for (int i = 0; i < n; i++) {
            sb.append(i).append(" "); // Sor sorszáma
            for (int j = 0; j < m; j++) {
                sb.append(map[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}