package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Клас, що відповідає за запуск та ініціалізацію головного вікна додатку Credit System.
 * Використовує JavaFX для створення графічного інтерфейсу користувача.
 */
public class Main extends Application {

    private static final Logger logger = Logger.getLogger(Controller.class);
    /**
     * Метод start викликається при запуску додатку і ініціалізує головне вікно.
     * @param stage Об'єкт Stage для головного вікна додатку.
     * @throws IOException Якщо виникає помилка при завантаженні файлу FXML.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("credit-offers.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Credit System");
        stage.setScene(scene);
        logger.info("Початок роботи програми");
        stage.show();
    }

    /**
     * Вхідна точка програми. Запускає додаток.
     */
    public static void main(String[] args) {
        launch();
    }
}
