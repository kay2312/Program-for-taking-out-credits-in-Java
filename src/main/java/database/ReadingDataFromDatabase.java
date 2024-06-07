package database;

import command.ResultCommand;
import creditOffer.CreditOffer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Клас для зчитування даних з бази даних
 */
public class ReadingDataFromDatabase implements ResultCommand {
    private final String databaseUrl;
    private final String tableName;

    /**
     * Конструктор
     *
     * @param databaseUrl - URL бази даних
     * @param tableName   - назва таблиці
     */
    public ReadingDataFromDatabase(String databaseUrl, String tableName) {
        this.databaseUrl = databaseUrl;
        this.tableName = tableName;
    }

    /**
     * Метод для зчитування даних з бази даних
     *
     * @return - список зі зчитаних даних про кредити
     */
    public List<CreditOffer> execute() {
        List<CreditOffer> creditOffers = new ArrayList<>();
        String selectSql = "SELECT * FROM " + tableName;

        try (Connection conn = DriverManager.getConnection(databaseUrl);
             PreparedStatement pstmt = conn.prepareStatement(selectSql)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String bankName = rs.getString("bank");
                String creditName = rs.getString("loanType");
                int interestRate = rs.getInt("percentRate");
                int loanTerm = rs.getInt("loanTerm");
                boolean earlyRepaymentEnabled = rs.getBoolean("earlyRepayment");
                boolean creditLineIncreaseEnabled = rs.getBoolean("creditLineIncrease");
                int sumCredit = rs.getInt("creditAmount");

                CreditOffer creditOffer = new CreditOffer(id, bankName, creditName, interestRate,
                        loanTerm, earlyRepaymentEnabled, creditLineIncreaseEnabled, sumCredit);
                creditOffers.add(creditOffer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return creditOffers;
    }
}
