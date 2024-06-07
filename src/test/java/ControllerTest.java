import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.TableViewMatchers;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

public class ControllerTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/credit-offers.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testOpenCreditButton() {
        clickOn("#openCreditButton");
        // Перевірка, що вікно "Credit Offers" відкрито
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
    public void testSearchByCreditLineIncrease() {
        clickOn("#creditLineIncreaseCheckBox");
        clickOn("#searchCredits");
        // Перевірка, що таблиця містить кредити зі збільшенням кредитного ліміту
        verifyThat("#table", TableViewMatchers.hasNumRows(5));
    }

    @Test
    public void testSearchByEarlyRepayment() {
        clickOn("#isEarlyRepayment");
        clickOn("#searchCredits");
        verifyThat("#table", TableViewMatchers.hasNumRows(7));
    }

    @Test
    public void testSearchByMaxPercentRate() {
        clickOn("#maxPercentRate").write("8");
        clickOn("#searchCredits");
        verifyThat("#table", TableViewMatchers.hasNumRows(6));
    }

    @Test
    public void testSearchByMinLoanTerm() {
        clickOn("#minLoanTerm").write("50");
        clickOn("#searchCredits");
        // Перевірка, що таблиця містить кредити з мінімальним терміном кредиту 50
        verifyThat("#table", TableViewMatchers.hasNumRows(2));
    }

    @Test
    public void testSearchByMinLoanAmount() {
        clickOn("#minLoanAmount").write("2000000");
        clickOn("#searchCredits");
        verifyThat("#table", TableViewMatchers.hasNumRows(5));
    }
}
