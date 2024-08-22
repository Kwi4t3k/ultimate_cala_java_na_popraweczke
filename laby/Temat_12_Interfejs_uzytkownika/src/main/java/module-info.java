module org.example.temat_12_interfejs_uzytkownika {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;


    opens org.example.temat_12_interfejs_uzytkownika to javafx.fxml;
    exports org.example.temat_12_interfejs_uzytkownika;
}