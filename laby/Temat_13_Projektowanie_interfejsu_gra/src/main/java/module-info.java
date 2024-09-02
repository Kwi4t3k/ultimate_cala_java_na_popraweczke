module org.example.temat_13_projektowanie_interfejsu_gra {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.temat_13_projektowanie_interfejsu_gra to javafx.fxml;
    exports org.example.temat_13_projektowanie_interfejsu_gra;
}