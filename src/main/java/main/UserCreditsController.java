package main;

import creditOffer.Credit;
import creditOffer.CreditOffer;
import database.ReadingDataFromDatabase;
import command.Command;
import command.ResultCommand;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import userCredits.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Клас відповідає за управління вікном з кредитами користувача та взаємодію з користувачем.
 */
public class UserCreditsController {

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
    private Button creditIncreaseButton;
    @FXML
    private Button partLoanRepaymentButton;

    @FXML
    private Button creditLineDeleteButton;

    @FXML
    private TextField creditIncreaseLine;
    @FXML
    private TextField numberCreditIncreaseLine;
    @FXML
    private TextField numberCreditLineDelete;
    @FXML
    private TextField numberCreditPartRepayment;
    @FXML
    private TextField monthCreditPartRepayment;
    @FXML
    private TextField amountCreditPartRepayment;
    private static List<CreditOffer> creditOffers = new ArrayList<>();

    private int eventButton;

    ObservableList<Credit> list;

    private static final Logger logger = Logger.getLogger(UserCreditsController.class);

    /**
     * Ініціалізує вікно з кредитами користувача та завантажує дані.
     */
    public void initialize() {
        // Ініціалізуємо таблицю з даними зчитаними з файлу
        String database;
        database = "jdbc:sqlite:d:\\JavaFXDemo\\sqlite\\credits.db";
        String tableName = "user_credits";
        list = loadDataFromDatabase(database, tableName);

        id.setCellValueFactory(new PropertyValueFactory<Credit,Integer>("id"));
        bank.setCellValueFactory(new PropertyValueFactory<Credit,String>("bank"));
        loanType.setCellValueFactory(new PropertyValueFactory<Credit,String>("loanType"));
        percentRate.setCellValueFactory(new PropertyValueFactory<Credit,Integer>("percentRate"));
        loanTerm.setCellValueFactory(new PropertyValueFactory<Credit,Integer>("loanTerm"));
        earlyRepayment.setCellValueFactory(new PropertyValueFactory<Credit,Boolean>("earlyRepayment"));
        creditLineIncrease.setCellValueFactory(new PropertyValueFactory<Credit,Boolean>("creditLineIncrease"));
        creditAmount.setCellValueFactory(new PropertyValueFactory<Credit,Integer>("creditAmount"));

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

        // Викликати метод для налаштування кнопок
        setupButtons();
    }

    /**
     * Налаштовує обробник подій для кнопок у вікні.
     */
    private void setupButtons() {
        // Додати обробник події для кнопки відкриття вікна кредиту
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

            eventButton = 1;
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
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            eventButton = 2;
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
                logger.info("Перехід на сторінку з додаванням кредитної пропозиції");
            } catch (IOException e) {
                logger.info("Не вдалося відкрити сторінку з додаванням кредитної пропозиції");
                throw new RuntimeException(e);
            }
            Parent root = loader_3.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Credit Offer");
            stage.show();
        });

        creditIncreaseButton.setOnAction(event -> {
            if (!numberCreditIncreaseLine.getText().isEmpty() && !creditIncreaseLine.getText().isEmpty()) {
                String creditIncreaseLineText = creditIncreaseLine.getText();
                int creditIncreaseLine = Integer.parseInt(creditIncreaseLineText);

                String numberCreditIncreaseLineText = numberCreditIncreaseLine.getText();
                int numberCreditIncreaseLine = Integer.parseInt(numberCreditIncreaseLineText);

                ResultCommand readingFileCommand2 = new ReadingDataFromDatabase("jdbc:sqlite:d:\\JavaFXDemo\\sqlite\\credits.db", "user_credits");
                creditOffers = readingFileCommand2.execute();
                Command increaseCreditLineCommand = new IncreaseCreditLine(numberCreditIncreaseLine, creditIncreaseLine, "jdbc:sqlite:d:\\JavaFXDemo\\sqlite\\credits.db");
                increaseCreditLineCommand.execute();
                logger.info("Користувач збільшив кредитну лінію");
                initialize();
            } else {
                initialize();
            }
        });


        creditLineDeleteButton.setOnAction(event -> {
            if (!numberCreditLineDelete.getText().isEmpty()) {
                String creditDeleteLineText = numberCreditLineDelete.getText();
                int creditDelete = Integer.parseInt(creditDeleteLineText);

                ResultCommand readingFileCommand2 = new ReadingDataFromDatabase("jdbc:sqlite:d:\\JavaFXDemo\\sqlite\\credits.db", "user_credits");
                creditOffers = readingFileCommand2.execute();
                Command deleteCreditLineCommand = new DeleteUserCredit(creditDelete, "jdbc:sqlite:d:\\JavaFXDemo\\sqlite\\credits.db");
                deleteCreditLineCommand.execute();
                numberCreditLineDelete.clear();
                logger.info("Користувач закрив кредит");
                initialize();
            } else {
                initialize();
            }
        });

        partLoanRepaymentButton.setOnAction(event -> {
            if (!numberCreditPartRepayment.getText().isEmpty()
                    && !monthCreditPartRepayment.getText().isEmpty()
                    && !amountCreditPartRepayment.getText().isEmpty()) {
                String creditPartRepaymentLineText = numberCreditPartRepayment.getText();
                int numberCredit = Integer.parseInt(creditPartRepaymentLineText);

                String monthPartRepaymentLineText = monthCreditPartRepayment.getText();
                int monthCredit = Integer.parseInt(monthPartRepaymentLineText);

                String amountPartRepaymentLineText = amountCreditPartRepayment.getText();
                int amountCredit = Integer.parseInt(amountPartRepaymentLineText);

                ResultCommand readingFileCommand2 = new ReadingDataFromDatabase("jdbc:sqlite:d:\\JavaFXDemo\\sqlite\\credits.db", "user_credits");
                creditOffers = readingFileCommand2.execute();

                Command partRepaymentCreditLineCommand = new PartLoanRepayment(numberCredit, monthCredit, amountCredit, "jdbc:sqlite:d:\\JavaFXDemo\\sqlite\\credits.db");
                partRepaymentCreditLineCommand.execute();

                numberCreditPartRepayment.clear();
                monthCreditPartRepayment.clear();
                amountCreditPartRepayment.clear();

                logger.info("Користувач вніс часткову виплату за кредит");

                initialize();
            } else {
                initialize();
            }
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
}

