module com.bindr {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.bindr to javafx.fxml;
    exports com.bindr;
}
