package userCredits;

import command.Command;

import java.sql.*;

/**
 * Клас для часткової виплати за кредит
 */
public class PartLoanRepayment implements Command {
    private final int creditId;
    private final int sum;
    private final int months;
    private final String databaseUrl;

    public PartLoanRepayment(int creditId, int months, int sum, String databaseUrl) {
        this.creditId = creditId;
        this.sum = sum;
        this.months = months;
        this.databaseUrl = databaseUrl;
    }

    public void execute() {
        // SQL-запит для отримання суми кредиту та процентної ставки за вказаним id
        String selectSql = "SELECT creditAmount, percentRate FROM user_credits WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(databaseUrl);
             PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {

            // Встановлюємо значення параметра у SQL-запиті
            selectStmt.setInt(1, creditId);

            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    int sumCredit = rs.getInt("creditAmount");
                    int interestRate = rs.getInt("percentRate");
                    // Розраховуємо bonusSumCredit, використовуючи отриману суму кредиту sumCredit та процентну ставку interestRate
                    int bonusSumCredit = (int) (sumCredit * (interestRate / 100.0) * (months / 12.0));

                    // SQL-запит для оновлення суми кредиту з врахуванням часткової виплати
                    String updateSql = "UPDATE user_credits SET creditAmount = creditAmount - ? WHERE id = ?";

                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setInt(1, sum - bonusSumCredit);
                        updateStmt.setInt(2, creditId);

                        int rowsAffected = updateStmt.executeUpdate();

                        if (rowsAffected > 0) {
                            // Повторно виконуємо SQL-запит для отримання оновленої суми кредиту
                            try (ResultSet updatedRs = selectStmt.executeQuery()) {
                                if (updatedRs.next()) {
                                    int updatedCreditAmount = updatedRs.getInt("creditAmount");
                                    System.out.println("Сума вашого кредиту після внесення суми: " + updatedCreditAmount);

                                    // Якщо сума кредиту стала 0, видаляємо запис
                                    if (updatedCreditAmount <= 0) {
                                        String deleteSql = "DELETE FROM user_credits WHERE id = ?";
                                        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                                            deleteStmt.setInt(1, creditId);
                                            int deleteRowsAffected = deleteStmt.executeUpdate();
                                            if (deleteRowsAffected > 0) {
                                                System.out.println("Кредит з id " + creditId + " було видалено, оскільки сума кредиту стала 0.");
                                            } else {
                                                System.out.println("Не вдалося видалити кредит з id " + creditId + ".");
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            System.out.println("Кредит з id " + creditId + " не було знайдено в базі даних.");
                        }
                    } catch (SQLException e) {
                        System.out.println("Помилка часткової виплати за кредит: " + e.getMessage());
                    }

                } else {
                    System.out.println("Кредит з id " + creditId + " не знайдено в базі даних.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
