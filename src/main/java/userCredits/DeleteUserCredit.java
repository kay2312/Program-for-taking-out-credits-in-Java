package userCredits;

import command.Command;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Клас для видалення кредиту користувача
 */
public class DeleteUserCredit implements Command {
    private final int creditId;
    private final String databaseUrl;

    public DeleteUserCredit(int creditId, String databaseUrl) {
        this.creditId = creditId;
        this.databaseUrl = databaseUrl;
    }

    public void execute() {
        // SQL-запит для видалення запису з бази даних, якщо earlyRepayment = true
        String deleteSql = "DELETE FROM user_credits WHERE id = ? AND earlyRepayment = 1";

        try (Connection conn = DriverManager.getConnection(databaseUrl);
             PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {

            pstmt.setInt(1, creditId);

            // Виконуємо SQL-запит для видалення запису
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Кредит з id " + creditId + " успішно видалено з бази даних.");
            } else {
                System.out.println("Кредит з id " + creditId + " не було знайдено в базі даних або умова earlyRepayment = true не виконана.");
            }

        } catch (SQLException e) {
            System.out.println("Помилка видалення кредиту з бази даних: " + e.getMessage());
        }
    }
}
