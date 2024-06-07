package main;

import creditOffer.Credit;
import database.WritingDataToDatabase;
import creditOffer.CreditOffer;
import database.ReadingDataFromDatabase;
import command.Command;
import command.ResultCommand;
import searchCredits.*;

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
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Клас відповідає за управління головним вікном додатку та взаємодію з користувачем.
 */
public class Controller {

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
    private Button searchCredits;
    @FXML
    private Button takeCredits;

    @FXML
    private TextField maxPercentRate;
    @FXML
    private TextField minLoanTerm;
    @FXML
    private TextField minLoanAmount;
    @FXML
    private TextField takeCredit;

    private int eventButton;

    @FXML
    private CheckBox creditLineIncreaseCheckBox;

    @FXML
    private CheckBox isEarlyRepayment;

    @FXML
    private CheckBox checkTakeCredit;

    private static List<CreditOffer> creditOffers = new ArrayList<>();

    public ObservableList<Credit> list;

    public static int globalInt = 0;
    Connection co;

    private static final Logger logger = Logger.getLogger(Controller.class);

    /**
     * Ініціалізує головне вікно та завантажує дані при запуску програми.
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
        // Викликати метод для налаштування кнопок
        setupButtons();
    }

    /**
     * Налаштовує обробник подій для кнопок у головному вікні.
     */
    private void setupButtons() {
        openCreditButton.setOnAction(event -> {
            openCreditButton.getScene().getWindow().hide();

            FXMLLoader loader  = new FXMLLoader();
            loader.setLocation(getClass().getResource("credit-offers.fxml"));

            try {
                loader.load();
            } catch (IOException e) {
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
                logger.info("Перехід на сторінку з додаванням кредитної пропозиції");
            } catch (IOException e) {
                logger.error("Не вдалося відкрити сторінку з додаванням кредитної пропозиції");
                throw new RuntimeException(e);
            }
            Parent root = loader_3.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("User Credits");
            stage.show();
        });

        searchCredits.setOnAction(event -> {
            checkBoxAction();
        });

        takeCredits.setOnAction(event -> {

            takeCreditTextField();
        });
    }

    /**
     * Перевіряє чи введно номер кредиту.
     */
    public void takeCreditTextField() {
        if (!takeCredit.getText().isEmpty() && checkTakeCredit.isSelected()) {
            doIfTakeCredit();
            takeCredit.clear();
            checkTakeCredit.setSelected(false); // Зняття прапорця з `checkTakeCredit`
        } else {
            globalInt = 0;
            initialize();
        }

    }

    /**
     * Обробляє вибір чекбоксів та викликає пошук кредитів за заданими параметрами.
     */
    public void checkBoxAction() {
        if (creditLineIncreaseCheckBox.isSelected()
                || isEarlyRepayment.isSelected()
                || !maxPercentRate.getText().isEmpty()
                || !minLoanTerm.getText().isEmpty()
                || !minLoanAmount.getText().isEmpty()) {
            doSomethingIfCheckBoxSelected();
        } else {
            globalInt = 0;
            initialize();
        }

    }

    /**
     * Додає кредиту до бази даних
     */
    private void doIfTakeCredit() {
        ResultCommand readingFileCommand = new ReadingDataFromDatabase("jdbc:sqlite:d:\\JavaFXDemo\\sqlite\\credits.db", "credit_offers");
        creditOffers = readingFileCommand.execute();
        if (!takeCredit.getText().isEmpty()) {
            String takeCredittext = takeCredit.getText();
            int takeCredit = Integer.parseInt(takeCredittext);
            Command writingToFileCommand = new WritingDataToDatabase(takeCredit, "credit_offers",
                    "user_credits");
            writingToFileCommand.execute();
            System.out.println("Вітаю ви взяли кредит!");
            loadDataFromList(creditOffers);

            // Відображення спливаючого вікна
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Кредит взято");
            alert.setHeaderText(null);
            alert.setContentText("Ви взяли кредит з номером: " + takeCredit);
            alert.showAndWait();

            logger.info("Користувач взяв кредит");
        }
    }

    /**
     * Пошук кредитів
     */
    private void doSomethingIfCheckBoxSelected() {
        ResultCommand readingFileCommand = new ReadingDataFromDatabase("jdbc:sqlite:d:\\JavaFXDemo\\sqlite\\credits.db", "credit_offers");
        creditOffers = readingFileCommand.execute();
        if (creditLineIncreaseCheckBox.isSelected()) {
            ResultCommand searchByIsCreditLineIncreaseEnabledCommand =
                    new SearchByIsCreditLineIncreaseEnabled(creditOffers);
            creditOffers = searchByIsCreditLineIncreaseEnabledCommand.execute();
        }
        if (isEarlyRepayment.isSelected()) {
            ResultCommand searchByIsEarlyRepaymentEnabledCommand =
                    new SearchByIsEarlyRepaymentEnabled(creditOffers);
            creditOffers = searchByIsEarlyRepaymentEnabledCommand.execute();
        }
        if (!maxPercentRate.getText().isEmpty()) {
            String maxPercentRateText = maxPercentRate.getText();
            int maxPercentRateValue = Integer.parseInt(maxPercentRateText);
            ResultCommand searchByMaxInterestRateCommand =
                    new SearchByMaxInterestRate(creditOffers, maxPercentRateValue);
            creditOffers = searchByMaxInterestRateCommand.execute();
        }
        if (!minLoanTerm.getText().isEmpty()) {
            String minLoanTermText = minLoanTerm.getText();
            int minLoanTerm = Integer.parseInt(minLoanTermText);
            ResultCommand searchByMinLoanTermCommand =
                    new SearchByMinLoanTerm(creditOffers, minLoanTerm);
            creditOffers = searchByMinLoanTermCommand.execute();
        }
        if (!minLoanAmount.getText().isEmpty()) {
            String minLoanAmountText = minLoanAmount.getText();
            int minLoanAmount = Integer.parseInt(minLoanAmountText);
            ResultCommand searchByMinLoanAmountCommand =
                    new SearchByMinLoanAmount(creditOffers, minLoanAmount);
            creditOffers = searchByMinLoanAmountCommand.execute();
        }

        logger.info("Пошук кредитів за параметрами");

        loadDataFromList(creditOffers);
        // Тут виконайте необхідні дії, коли чекбокс вибраний
        System.out.println("Checkbox is selected");
    }

    /**
     * Завантажує дані зі списку кредитів до таблиці.
     * @param creditList Список кредитів.
     * @return ObservableList<Credit> Завантажені дані у вигляді ObservableList.
     */
    private ObservableList<Credit> loadDataFromList(List<CreditOffer> creditList) {
        ObservableList<Credit> dataList = FXCollections.observableArrayList();
        for (CreditOffer credit : creditList) {
            int id = credit.getId();
            String bank = credit.getBankName();
            String loanType = credit.getCreditName();
            int percentRate = credit.getInterestRate();
            int loanTerm = credit.getLoanTerm();
            boolean earlyRepayment = credit.getEarlyRepaymentEnabled();
            boolean creditLineIncrease = credit.getCreditLineIncreaseEnabled();
            int creditAmount = credit.getSumCredit();
            Credit newCredit = new Credit(id, bank, loanType, percentRate, loanTerm, earlyRepayment, creditLineIncrease, creditAmount);
            dataList.add(newCredit);
        }
        checkGoodBox(dataList);
        return dataList;
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
     * Оновлює дані.
     */
    private void checkGoodBox(ObservableList<Credit> dataList){
        list = dataList;
        id.setCellValueFactory(new PropertyValueFactory<Credit,Integer>("id"));
        bank.setCellValueFactory(new PropertyValueFactory<Credit,String>("bank"));
        loanType.setCellValueFactory(new PropertyValueFactory<Credit,String>("loanType"));
        percentRate.setCellValueFactory(new PropertyValueFactory<Credit,Integer>("percentRate"));
        loanTerm.setCellValueFactory(new PropertyValueFactory<Credit,Integer>("loanTerm"));
        earlyRepayment.setCellValueFactory(new PropertyValueFactory<Credit,Boolean>("earlyRepayment"));
        creditLineIncrease.setCellValueFactory(new PropertyValueFactory<Credit,Boolean>("creditLineIncrease"));
        creditAmount.setCellValueFactory(new PropertyValueFactory<Credit,Integer>("creditAmount"));

        table.setItems(list);
    }
}