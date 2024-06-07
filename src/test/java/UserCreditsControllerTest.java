import creditOffer.Credit;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import org.testfx.matcher.control.TextInputControlMatchers;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class UserCreditsControllerTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/user-credits.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testOpenCreditButton() {
        clickOn("#openCreditButton");
        verifyThat("#titleLabel", hasText("Взяти кредит"));
    }

    @Test
    public void testOpenMyCreditsButton() {
        clickOn("#openMyCreditsButton");
        verifyThat("#titleLabel", hasText("Мої кредити"));
    }

    @Test
    public void testAddCreditOfferButton() {
        clickOn("#addCreditOfferButton");
        verifyThat("#titleLabel", hasText("Додати кредит"));
    }

    @Test
    public void testCreditIncreaseButton() {
        TableView<Credit> tableView = lookup("#table").query();
        int initialCount = tableView.getItems().size();

        // Зберігаємо початкове значення кредитної лінії
        int initialCreditAmount = tableView.getItems().stream()
                .filter(credit -> credit.getId() == 3)
                .mapToInt(Credit::getLoanTerm)
                .findFirst().orElse(0);

        clickOn("#numberCreditIncreaseLine").write("3");
        clickOn("#creditIncreaseLine").write("2");

        clickOn("#creditIncreaseButton");

        waitForFxEvents();

        assertTrue(isCreditLineIncreased(initialCreditAmount, tableView));
    }

    private boolean isCreditLineIncreased(int initialCreditAmount, TableView<Credit> tableView) {
        int newCreditAmount = tableView.getItems().stream()
                .filter(credit -> credit.getId() == 3)
                .mapToInt(Credit::getCreditAmount)
                .findFirst().orElse(0);
        return newCreditAmount > initialCreditAmount;
    }
}
