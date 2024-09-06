module org.example.kolos_2023_odczytpliku_zapispliku_java_fx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens org.example.kolos_2023_odczytpliku_zapispliku_java_fx to javafx.fxml;
    exports org.example.kolos_2023_odczytpliku_zapispliku_java_fx;
}