import creditOffer.Credit;
import database.WritingDataToDatabase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class WritingDataToDatabaseTest {

    private String destinationTable = "user_credits";
    private Credit firstCredit;

    @BeforeEach
    void setUp() throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:d:\\JavaFXDemo\\sqlite\\credits.db");
             Statement stmt = conn.createStatement()) {
            // Отримуємо перший запис
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + destinationTable + " LIMIT 1");
            if (rs.next()) {
                firstCredit = new Credit(
                        rs.getInt("id"),
                        rs.getString("bank"),
                        rs.getString("loanType"),
                        rs.getInt("percentRate"),
                        rs.getInt("loanTerm"),
                        rs.getBoolean("earlyRepayment"),
                        rs.getBoolean("creditLineIncrease"),
                        rs.getInt("creditAmount")
                );
                // Видаляємо перший запис
                stmt.executeUpdate("DELETE FROM " + destinationTable + " WHERE id = " + firstCredit.getId());
            }
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Оновлюємо запис в таблиці після кожного тесту
        if (firstCredit != null) {
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:d:\\JavaFXDemo\\sqlite\\credits.db");
                 PreparedStatement pstmt = conn.prepareStatement(
                         "INSERT OR REPLACE INTO " + destinationTable + " (id, bank, loanType, percentRate, loanTerm, earlyRepayment, creditLineIncrease, creditAmount) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
                pstmt.setInt(1, firstCredit.getId());
                pstmt.setString(2, firstCredit.getBank());
                pstmt.setString(3, firstCredit.getLoanType());
                pstmt.setInt(4, firstCredit.getPercentRate());
                pstmt.setInt(5, firstCredit.getLoanTerm());
                pstmt.setBoolean(6, firstCredit.getEarlyRepayment());
                pstmt.setBoolean(7, firstCredit.getCreditLineIncrease());
                pstmt.setInt(8, firstCredit.getCreditAmount());
                pstmt.executeUpdate();
            }
        }
    }

    @Test
    public void testExecute_SuccessfullyInserted() throws SQLException {
        // Arrange
        int numberCredit = 1;
        String sourceTable = "credit_offers";

        // Act
        WritingDataToDatabase writingDataToDatabase = new WritingDataToDatabase(numberCredit, sourceTable, destinationTable);
        writingDataToDatabase.execute();

        // Assert
        int finalRowCount;
        // Отримання кількості записів у таблиці призначення після вставки
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:d:\\JavaFXDemo\\sqlite\\credits.db");
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery("SELECT COUNT(*) FROM " + destinationTable)) {
            finalRowCount = resultSet.getInt(1);
        }

        // Assert
        assertNotEquals(0, finalRowCount);
    }
}
