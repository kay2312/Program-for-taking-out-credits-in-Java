import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import userCredits.PartLoanRepayment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PartLoanRepaymentTest {
    private final String databaseUrl = "jdbc:sqlite:d:\\JavaFXDemo\\sqlite\\credits.db";
    private final String tableName = "user_credits";
    private int initialCreditAmount;

    @BeforeEach
    void setUp() throws SQLException {
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             Statement stmt = conn.createStatement()) {
            // Видаляємо запис з id = 1, якщо він існує, щоб уникнути конфлікту
            stmt.executeUpdate("DELETE FROM " + tableName + " WHERE id = 1");

            // Створюємо тестовий запис
            stmt.executeUpdate("INSERT INTO " + tableName + " (id, creditAmount, percentRate) VALUES (1, 1000, 10)");

            // Зчитуємо поточне значення creditAmount
            ResultSet rs = stmt.executeQuery("SELECT creditAmount FROM " + tableName + " WHERE id = 1");
            if (rs.next()) {
                initialCreditAmount = rs.getInt("creditAmount");
            }
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             Statement stmt = conn.createStatement()) {
            // Видаляємо тестовий запис
            stmt.executeUpdate("DELETE FROM " + tableName + " WHERE id = 1");
        }
    }

    @Test
    public void testExecute_SuccessfullyReducedCreditAmount() throws SQLException {
        // Arrange
        int creditId = 1;
        int months = 6;
        int sum = 200;

        // Act
        PartLoanRepayment partLoanRepayment = new PartLoanRepayment(creditId, months, sum, databaseUrl);
        partLoanRepayment.execute();

        // Assert
        int updatedCreditAmount;
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             PreparedStatement pstmt = conn.prepareStatement("SELECT creditAmount FROM " + tableName + " WHERE id = ?")) {
            pstmt.setInt(1, creditId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                updatedCreditAmount = rs.getInt("creditAmount");
                assertTrue(updatedCreditAmount < initialCreditAmount, "Сума кредиту повинна була зменшитися");
            }
        }
    }
}
