import org.junit.jupiter.api.Test;
import userCredits.DeleteUserCredit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class DeleteUserCreditTest {

    @Test
    void testExecute_SuccessfullyDeleted() throws SQLException {
        // Arrange
        int creditId = 123;
        String databaseUrl = "jdbc:sqlite:d:\\JavaFXDemo\\sqlite\\credits.db";
        int initialRowCount;
        int finalRowCount;

        // Отримання початкової кількості записів у базі даних
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery("SELECT COUNT(*) FROM user_credits")) {
            initialRowCount = resultSet.getInt(1);
        }

        // Act
        DeleteUserCredit deleteUserCredit = new DeleteUserCredit(creditId, databaseUrl);
        deleteUserCredit.execute();

        // Отримання кількості записів у базі даних після видалення
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery("SELECT COUNT(*) FROM user_credits")) {
            finalRowCount = resultSet.getInt(1);
        }

        // Assert
        assertEquals(initialRowCount, finalRowCount);
    }
}
