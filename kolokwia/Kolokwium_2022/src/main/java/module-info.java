module org.example.kolokwium_2022 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.kolokwium_2022.Client to javafx.fxml;
    exports org.example.kolokwium_2022.Client;
}