package main;

import creditOffer.Credit;
import creditOffer.CreditOffer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Клас відповідає за управління вікном для додавання кредитної пропозиції та взаємодію з користувачем.
 */
public class AddCreditOfferController {

    @FXML
    private TableView<Credit> table;
    @FXML
    private TableColumn<Credit,Integer> id;
    @FXML
    private TableColumn<Credit,String> bank;
    @FXML
    private TableColumn<Credit,String> loanType;
    @FXML
    private TableColumn<Credit,Integer> percentRate;
    @FXML
    private TableColumn<Credit,Integer> loanTerm;
    @FXML
    private TableColumn<Credit,Boolean> earlyRepayment;
    @FXML
    private TableColumn<Credit,Boolean> creditLineIncrease;
    @FXML
    private TableColumn<Credit,Integer> creditAmount;

    @FXML
    private Button openCreditButton; // Оголошення змінної для кнопки
    @FXML
    private Button openMyCreditsButton;
    @FXML
    private Button addCreditOfferButton;
    @FXML
    private Button addCreditButton;
    @FXML
    private Button deleteCreditButton;


    @FXML
    private TextField bankNameTextField;
    @FXML
    private TextField loanTypeTextField;
    @FXML
    private TextField percentRateTextField;
    @FXML
    private CheckBox isEarlyRepaymentCheckBox;
    @FXML
    private TextField creditSumTextField;
    @FXML
    private TextField loanTermTextField;
    @FXML
    private TextField numberCreditToDeleteTextField;

    @FXML
    private CheckBox creditLineIncreaseCheckBox;


    private static List<CreditOffer> creditOffers = new ArrayList<>();

    ObservableList<Credit> list;

    public static int globalInt = 0;
    Connection co;

    private static final Logger logger = Logger.getLogger(AddCreditOfferController.class);

    /**
     * Ініціалізує вікно з додаванням кредитної пропозиції та завантажує дані про наявні кредитні пропозиції.
     */
    public void initialize() {
        if(globalInt == 0) {

            // Ініціалізуємо таблицю з даними зчитаними з файлу
            String database;
            database = "jdbc:sqlite:d:\\JavaFXDemo\\sqlite\\credits.db";
            String tableName = "credit_offers";
            list = loadDataFromDatabase(database, tableName);

            id.setCellValueFactory(new PropertyValueFactory<Credit, Integer>("id"));
            bank.setCellValueFactory(new PropertyValueFactory<Credit, String>("bank"));
            loanType.setCellValueFactory(new PropertyValueFactory<Credit, String>("loanType"));
            percentRate.setCellValueFactory(new PropertyValueFactory<Credit, Integer>("percentRate"));
            loanTerm.setCellValueFactory(new PropertyValueFactory<Credit, Integer>("loanTerm"));
            earlyRepayment.setCellValueFactory(new PropertyValueFactory<Credit, Boolean>("earlyRepayment"));
            creditLineIncrease.setCellValueFactory(new PropertyValueFactory<Credit, Boolean>("creditLineIncrease"));
            creditAmount.setCellValueFactory(new PropertyValueFactory<Credit, Integer>("creditAmount"));

            // Зміна відображення значень тільки при виведенні
            earlyRepayment.setCellFactory(column -> new TableCell<Credit, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item ? "можливе" : "неможливе");
                    }
                }
            });

            creditLineIncrease.setCellFactory(column -> new TableCell<Credit, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item ? "можливе" : "неможливе");
                    }
                }
            });

            table.setItems(list);
        }

        setupButtons();
    }

    /**
     * Налаштовує обробник подій для кнопок у вікні.
     */
    private void setupButtons() {
        openCreditButton.setOnAction(event -> {
            openCreditButton.getScene().getWindow().hide();

            FXMLLoader loader  = new FXMLLoader();
            loader.setLocation(getClass().getResource("credit-offers.fxml"));

            try {
                loader.load();
                logger.info("Перехід на сторінку з кредитними пропозиціями");
            } catch (IOException e) {
                logger.info("Не вдалося вікрити сторінку з кредитними пропозиціями");
                throw new RuntimeException(e);
            }
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Credit Offers");
            stage.show();
        });

        openMyCreditsButton.setOnAction(event -> {
            openMyCreditsButton.getScene().getWindow().hide();

            FXMLLoader loader_2  = new FXMLLoader();
            loader_2.setLocation(getClass().getResource("user-credits.fxml"));

            try {
                loader_2.load();
                logger.info("Перехід на сторінку з кредитами користувача");
            } catch (IOException e) {
                logger.error("Не вдалося відкрити сторінку з кредитами користувача");
                throw new RuntimeException(e);
            }
            Parent root = loader_2.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("User Credits");
            stage.show();
        });

        addCreditOfferButton.setOnAction(event -> {
            addCreditOfferButton.getScene().getWindow().hide();

            FXMLLoader loader_3  = new FXMLLoader();
            loader_3.setLocation(getClass().getResource("add-credit-offer.fxml"));

            try {
                loader_3.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Parent root = loader_3.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Credit Offer");
            stage.show();
        });

        addCreditButton.setOnAction(event -> {
            addCreditOfferToDatabase();

        });

        deleteCreditButton.setOnAction(event -> {
            deleteCreditOfferFromDatabase();
        });

    }

    /**
     * Завантажує дані із бази даних до таблиці.
     * @param databaseUrl - посилання на базу даних.
     * @param tableName - назва таблиці бази даних.
     * @return dataList Завантажені дані у вигляді dataList.
     */
    private ObservableList<Credit> loadDataFromDatabase(String databaseUrl, String tableName) {
        ObservableList<Credit> dataList = FXCollections.observableArrayList();
        String sql = "SELECT id, bank, loanType, percentRate, loanTerm, earlyRepayment, creditLineIncrease, creditAmount FROM " + tableName;

        try (Connection conn = DriverManager.getConnection(databaseUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String bank = rs.getString("bank");
                String loanType = rs.getString("loanType");
                int percentRate = rs.getInt("percentRate");
                int loanTerm = rs.getInt("loanTerm");
                boolean earlyRepayment = rs.getBoolean("earlyRepayment");
                boolean creditLineIncrease = rs.getBoolean("creditLineIncrease");
                int creditAmount = rs.getInt("creditAmount");

                Credit credit = new Credit(id, bank, loanType, percentRate, loanTerm, earlyRepayment, creditLineIncrease, creditAmount);
                dataList.add(credit);
            }
        } catch (SQLException e) {
            logger.error("Не вдалося вивести дані з бази даних!");
            e.printStackTrace();
        }

        return dataList;
    }

    /**
     * Додає кредитну прозицію до бази даних.
     */
    @FXML
    private void addCreditOfferToDatabase() {
        // Перевірка, чи всі поля заповнені
        if (isInputValid()) {
            // Отримуємо дані з текстових полів та чекбоксів
            String bankName = bankNameTextField.getText();
            String loanType = loanTypeTextField.getText();
            int percentRate = Integer.parseInt(percentRateTextField.getText());
            int loanTerm = Integer.parseInt(loanTermTextField.getText());
            boolean earlyRepayment = isEarlyRepaymentCheckBox.isSelected();
            boolean creditLineIncrease = creditLineIncreaseCheckBox.isSelected();
            int creditAmount = Integer.parseInt(creditSumTextField.getText());

            // Підготовка запиту SQL для вставки даних в базу даних
            String sql = "INSERT INTO credit_offers (bank, loanType, percentRate, loanTerm, earlyRepayment, creditLineIncrease, creditAmount) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:d:\\JavaFXDemo\\sqlite\\credits.db");
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                // Встановлюємо значення параметрів запиту SQL
                pstmt.setString(1, bankName);
                pstmt.setString(2, loanType);
                pstmt.setInt(3, percentRate);
                pstmt.setInt(4, loanTerm);
                pstmt.setBoolean(5, earlyRepayment);
                pstmt.setBoolean(6, creditLineIncrease);
                pstmt.setInt(7, creditAmount);

                // Виконуємо запит SQL
                pstmt.executeUpdate();

                // Вивід повідомлення про успішне додавання
                System.out.println("Дані успішно додані в базу даних!");

                // Опціонально: очищаємо поля введення
                clearInputFields();
                initialize();
            } catch (SQLException e) {
                logger.error("Не вдалося додати кредит до бази даних!");
                System.out.println("Помилка при додаванні даних в базу даних: " + e.getMessage());
            }
        } else {
            System.out.println("Будь ласка, заповніть всі поля.");
        }
    }

    /**
     * Видаляє кредитну прозицію із бази даних.
     */
    @FXML
    private void deleteCreditOfferFromDatabase() {
        // Перевірка, чи поле ID кредиту заповнене
        if (!numberCreditToDeleteTextField.getText().isEmpty()) {
            int id = Integer.parseInt(numberCreditToDeleteTextField.getText());

            String sql = "DELETE FROM credit_offers WHERE id = ?";

            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:d:\\JavaFXDemo\\sqlite\\credits.db");
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, id);

                pstmt.executeUpdate();

                System.out.println("Кредитна пропозиція з ID " + id + " була видалена з бази даних.");

                numberCreditToDeleteTextField.clear();

                logger.error("Видалено кредитну пропозицію з бази даних");

                initialize();
            } catch (SQLException e) {
                logger.error("Не вдалося видалити кредит з бази даних!");
                System.out.println("Помилка видалення з бази даних: " + e.getMessage());
            }
        } else {
            System.out.println("Введіть ID для видалення!");
        }
    }


    /**
     * Перевіряє чи всі поля заповнені для додавання кредитної пропозиції
     */
    private boolean isInputValid() {
        return !bankNameTextField.getText().isEmpty() &&
                !loanTypeTextField.getText().isEmpty() &&
                !percentRateTextField.getText().isEmpty() &&
                !loanTermTextField.getText().isEmpty() &&
                !creditSumTextField.getText().isEmpty();
    }

    /**
     * Очищає дані з текстових полів
     */
    private void clearInputFields() {
        bankNameTextField.clear();
        loanTypeTextField.clear();
        percentRateTextField.clear();
        loanTermTextField.clear();
        creditSumTextField.clear();
    }
}