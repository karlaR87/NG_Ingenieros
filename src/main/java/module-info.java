module com.example.ng_ingenieros {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.ng_ingenieros to javafx.fxml;
    exports com.example.ng_ingenieros;
}