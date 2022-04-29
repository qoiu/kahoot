module com.example.kahoot {
    requires javafx.controls;
    requires javafx.fxml;
    requires telegrambots;
    requires telegrambots.meta;
    requires kotlin.stdlib;
    requires java.sql;
    requires java.validation;
    requires annotations;
    requires org.xerial.sqlitejdbc;


    opens com.example.kahoot to javafx.fxml;
    exports com.example.kahoot;
    exports com.example.kahoot.domain.model;
    exports com.example.kahoot.presentation;
    opens com.example.kahoot.presentation to javafx.fxml;
}