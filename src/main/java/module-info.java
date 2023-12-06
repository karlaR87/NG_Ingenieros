module com.example.ng_ingenieros {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.desktop;
    //requires java.mail;


    opens com.example.ng_ingenieros to javafx.fxml;
    exports com.example.ng_ingenieros;
    exports com.example.ng_ingenieros.Controlador;
    opens com.example.ng_ingenieros.Controlador to javafx.fxml;
}