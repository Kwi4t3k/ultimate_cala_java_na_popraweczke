module org.example.kolokwium_2021 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.kolokwium_2021 to javafx.fxml;
    exports org.example.kolokwium_2021;
}