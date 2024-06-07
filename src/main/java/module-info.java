module com.example.javafxdemo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires log4j;

    exports main;
    opens main to javafx.fxml;
    exports creditOffer;
    exports userCredits;
    exports database; // Доданий експорт пакету file
    exports searchCredits; // Доданий експорт пакету searchCredits
    opens creditOffer to javafx.fxml;
}