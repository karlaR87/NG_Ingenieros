package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrudCargosControlador {
    @FXML
    private TableView tbCargo;
    @FXML
    private Button btnAgregarCargo, btnEditarCargo, btnEliminarCargo, btnAtras;
    @FXML
    private TextField txtBusqueda;

    public void initialize() {
        // Configura el evento de clic para el botón
        btnAgregarCargo.setOnAction(this::btnAgregarCargoOnAction);
        btnEditarCargo.setOnAction(this::btnEditarCargoOnAction);
        cargarDatos();
        txtBusqueda.setOnKeyReleased(event -> {

            buscarDatos(txtBusqueda.getText());

        });

    }
    private void btnAgregarCargoOnAction(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/AgregarCargos.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Cargos");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btnEditarCargoOnAction(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/ActualizarCargo.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Nueva");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarDatos() {
        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select * from tbcargos")) {

            while (rs.next()) {
                int id = rs.getInt("idcargo");
                String nombre = rs.getString("cargo");




                // Agregar los datos a la tabla
                tbCargo.getItems().add(new Cargos(id, nombre));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void buscarDatos(String busqueda) {
        tbCargo.getItems().clear(); // Limpiar los elementos actuales de la tabla

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement("select idcargo, cargo from tbcargos where cargo LIKE ?")) {

            // Preparar el parámetro de búsqueda para la consulta SQL
            String parametroBusqueda = "%" + busqueda + "%";

                stmt.setString(1, parametroBusqueda);


            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Obtener los datos y agregarlos a la tabla
                int id = rs.getInt("idcargo");
                String nombre = rs.getString("cargo");


                tbCargo.getItems().add(new Cargos(id, nombre));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
