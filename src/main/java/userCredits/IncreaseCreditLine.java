package userCredits;

import command.Command;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Клас для збільшення кредитної лінії користувача
 */
public class IncreaseCreditLine implements Command {
    private final int creditId;
    private final int monthsToIncrease;
    private final String databaseUrl;

    public IncreaseCreditLine(int creditId, int monthsToIncrease, String databaseUrl) {
        this.creditId = creditId;
        this.monthsToIncrease = monthsToIncrease;
        this.databaseUrl = databaseUrl;
    }

    public void execute() {
        // SQL-запит для збільшення кредитної лінії у базі даних
        String updateSql = "UPDATE user_credits SET loanTerm = loanTerm + ? WHERE id = ? AND creditLineIncrease = 1";

        try (Connection conn = DriverManager.getConnection(databaseUrl);
             PreparedStatement pstmt = conn.prepareStatement(updateSql)) {

            // Встановлюємо значення параметрів у SQL-запиті
            pstmt.setInt(1, monthsToIncrease);
            pstmt.setInt(2, creditId);

            // Виконуємо SQL-запит для збільшення кредитної лінії
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Кредитна лінія збільшена на " + monthsToIncrease + " місяців.");
            } else {
                System.out.println("Кредит з id " + creditId + " не було знайдено в базі даних або умова creditLineIncrease = true не виконана..");
            }

        } catch (SQLException e) {
            System.out.println("Помилка збільшення кредитної лінії: " + e.getMessage());
        }
    }
}
