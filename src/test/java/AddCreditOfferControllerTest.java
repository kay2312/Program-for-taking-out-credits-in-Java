import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.testfx.matcher.control.LabeledMatchers.hasText;

import static org.testfx.api.FxAssert.verifyThat;

public class AddCreditOfferControllerTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/add-credit-offer.fxml"));
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
}