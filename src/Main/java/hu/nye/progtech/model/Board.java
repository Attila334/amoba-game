package hu.nye.progtech.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Board {

    private static final Logger LOGGER = LoggerFactory.getLogger(Board.class);

    private final int n; 
    private final int m; 
    private final char[][] map; 

    public Board(int n, int m) {
        this.n = n;
        this.m = m;
        this.map = new char[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                map[i][j] = '.';
            }
        }
        LOGGER.info("Létrejött egy {}x{} méretű tábla.", n, m);
    }

    public boolean placeSymbol(Position position, char symbol) {
        if (!isValidMove(position)) {
            LOGGER.warn("Érvénytelen lépés kísérlet: {}", position);
            return false;
        }

        map[position.getRow()][position.getCol()] = symbol;
        LOGGER.info("Sikeres lépés: {} jel a {} pozícióra.", symbol, position);
        return true;
    }

    private boolean isValidMove(Position position) {
        int r = position.getRow();
        int c = position.getCol();

        if (r < 0 || r >= n || c < 0 || c >= m) {
            return false;
        }

        return map[r][c] == '.';
    }

    public boolean checkWin(char symbol) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (map[i][j] != symbol) {
                    continue;
                }
                
                if (checkDirection(i, j, 0, 1, symbol)) return true;
                if (checkDirection(i, j, 1, 0, symbol)) return true;
                if (checkDirection(i, j, 1, 1, symbol)) return true;
                if (checkDirection(i, j, 1, -1, symbol)) return true;
            }
        }
        return false;
    }

    private boolean checkDirection(int row, int col, int dRow, int dCol, char symbol) {
        for (int k = 1; k < 4; k++) {
            int r = row + k * dRow;
            int c = col + k * dCol;
            
            if (r < 0 || r >= n || c < 0 || c >= m || map[r][c] != symbol) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("  ");
        for (int j = 0; j < m; j++) {
            sb.append(j).append(" ");
        }
        sb.append("\n");
        for (int i = 0; i < n; i++) {
            sb.append(i).append(" "); 
            for (int j = 0; j < m; j++) {
                sb.append(map[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
