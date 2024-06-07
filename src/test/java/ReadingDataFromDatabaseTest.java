import creditOffer.CreditOffer;
import database.ReadingDataFromDatabase;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReadingDataFromDatabaseTest {

    @Test
    public void testExecute_ReturnsCorrectListOfCreditOffers() throws SQLException {
        // Arrange
        String databaseUrl = "jdbc:sqlite:d:\\JavaFXDemo\\sqlite\\credits.db";
        String tableName = "credit_offers";

        // Виклик методу execute() для отримання списку кредитних пропозицій
        ReadingDataFromDatabase readingDataFromDatabase = new ReadingDataFromDatabase(databaseUrl, tableName);
        List<CreditOffer> actualCreditOffers = readingDataFromDatabase.execute();

        // Перевірка, чи список не є пустим
        assertEquals(false, actualCreditOffers.isEmpty());

    }
}
