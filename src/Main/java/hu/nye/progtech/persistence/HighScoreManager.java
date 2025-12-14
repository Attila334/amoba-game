package hu.nye.progtech.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HighScoreManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(HighScoreManager.class);

    private static final String DB_URL = "jdbc:h2:./highscore";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    public HighScoreManager() throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS scores " +
                    "(name VARCHAR(255) PRIMARY KEY, wins INT)";
            stmt.execute(sql);
            LOGGER.info("Adatbázis kapcsolat és tábla ellenőrizve.");
        }
    }

    public void saveWin(String playerName) {
        String updateSql = "UPDATE scores SET wins = wins + 1 WHERE name = ?";
        String insertSql = "INSERT INTO scores (name, wins) VALUES (?, 1)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setString(1, playerName);
                int rowsAffected = updateStmt.executeUpdate();

                if (rowsAffected == 0) {
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                        insertStmt.setString(1, playerName);
                        insertStmt.executeUpdate();
                        LOGGER.info("Új játékos mentve: {}", playerName);
                    }
                } else {
                    LOGGER.info("Meglévő játékos pontszáma növelve: {}", playerName);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Hiba a pontszám mentésekor", e);
        }
    }

    public void printHighScores() {
        String sql = "SELECT name, wins FROM scores ORDER BY wins DESC LIMIT 10";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("---------------------------------");
            System.out.println("      TOP 10 JÁTÉKOS");
            System.out.println("---------------------------------");
            System.out.printf("%-20s | %s%n", "Név", "Győzelmek");
            System.out.println("---------------------------------");

            boolean hasData = false;
            while (rs.next()) {
                String name = rs.getString("name");
                int wins = rs.getInt("wins");
                System.out.printf("%-20s | %d%n", name, wins);
                hasData = true;
            }

            if (!hasData) {
                System.out.println("Még nincs mentett eredmény.");
            }
            System.out.println("---------------------------------");

        } catch (SQLException e) {
            LOGGER.error("Hiba a lista lekérdezésekor", e);
        }
    }
}
