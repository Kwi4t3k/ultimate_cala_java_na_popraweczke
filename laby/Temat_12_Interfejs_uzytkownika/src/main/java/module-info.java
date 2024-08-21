module org.example.temat_12_interfejs_uzytkownika {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.temat_12_interfejs_uzytkownika to javafx.fxml;
    exports org.example.temat_12_interfejs_uzytkownika;
}