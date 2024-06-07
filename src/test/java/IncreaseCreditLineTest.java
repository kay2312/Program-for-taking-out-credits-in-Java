import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import userCredits.IncreaseCreditLine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IncreaseCreditLineTest {
    private final String databaseUrl = "jdbc:sqlite:d:\\JavaFXDemo\\sqlite\\credits.db";
    private final String tableName = "user_credits";
    private int initialLoanTerm;

    @BeforeEach
    void setUp() throws SQLException {
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             Statement stmt = conn.createStatement()) {
            // Створюємо тестовий запис
            stmt.executeUpdate("INSERT INTO " + tableName + " (id, loanTerm, creditLineIncrease) VALUES (123, 12, 1)");

            // Зчитуємо поточне значення loanTerm
            ResultSet rs = stmt.executeQuery("SELECT loanTerm FROM " + tableName + " WHERE id = 123");
            if (rs.next()) {
                initialLoanTerm = rs.getInt("loanTerm");
            }
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             Statement stmt = conn.createStatement()) {
            // Видаляємо тестовий запис
            stmt.executeUpdate("DELETE FROM " + tableName + " WHERE id = 123");
        }
    }

    @Test
    public void testExecute_SuccessfullyIncreased() throws SQLException {
        // Arrange
        int creditId = 123;
        int monthsToIncrease = 6;

        // Act
        IncreaseCreditLine increaseCreditLine = new IncreaseCreditLine(creditId, monthsToIncrease, databaseUrl);
        increaseCreditLine.execute();

        // Assert
        int updatedLoanTerm;
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             PreparedStatement pstmt = conn.prepareStatement("SELECT loanTerm FROM " + tableName + " WHERE id = ?")) {
            pstmt.setInt(1, creditId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                updatedLoanTerm = rs.getInt("loanTerm");
                assertTrue(updatedLoanTerm > initialLoanTerm, "Loan term should have been increased");
            }
        }
    }
}
