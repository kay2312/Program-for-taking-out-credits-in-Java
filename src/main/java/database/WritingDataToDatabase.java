package database;

import command.Command;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Клас для запису даних про кредити користувача в іншу таблицю бази даних
 */
public class WritingDataToDatabase implements Command {
    private final int numberCredit;
    private final String sourceTable;
    private final String destinationTable;

    /**
     * Конструктор
     *
     * @param numberCredit      - номер кредиту, дані якого ви хочете записати
     * @param sourceTable      - назва таблиці, з якої беруться дані
     * @param destinationTable - назва таблиці, у яку вставляються дані
     */
    public WritingDataToDatabase(int numberCredit, String sourceTable, String destinationTable) {
        this.numberCredit = numberCredit;
        this.sourceTable = sourceTable;
        this.destinationTable = destinationTable;
    }

    public void execute() {
        String selectSql = "SELECT * FROM " + sourceTable + " WHERE id = ?";
        String insertSql = "INSERT INTO " + destinationTable + " (id, bank, loanType, percentRate, loanTerm, earlyRepayment, creditLineIncrease, creditAmount) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"; // SQL-запит для вставки даних у призначення

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:d:\\JavaFXDemo\\sqlite\\credits.db");
             PreparedStatement selectStmt = conn.prepareStatement(selectSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

            selectStmt.setInt(1, numberCredit);
            ResultSet rs = selectStmt.executeQuery(); // Виконання SQL-запиту для вибору даних
            if (rs.next()) {
                // Встановлення значень параметрів у SQL-запиті для вставки даних
                insertStmt.setInt(1, rs.getInt("id"));
                insertStmt.setString(2, rs.getString("bank"));
                insertStmt.setString(3, rs.getString("loanType"));
                insertStmt.setInt(4, rs.getInt("percentRate"));
                insertStmt.setInt(5, rs.getInt("loanTerm"));
                insertStmt.setInt(6, rs.getInt("earlyRepayment"));
                insertStmt.setInt(7, rs.getInt("creditLineIncrease"));
                insertStmt.setInt(8, rs.getInt("creditAmount"));
                insertStmt.executeUpdate(); // Виконання SQL-запиту для вставки даних
                System.out.println("Дані кредиту з id " + numberCredit + " успішно вставлено в таблицю " + destinationTable);
            } else {
                System.out.println("Кредит з id " + numberCredit + " не знайдено в таблиці " + sourceTable);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
