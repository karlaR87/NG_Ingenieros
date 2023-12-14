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

import com.example.ng_ingenieros.Validaciones;

import javax.swing.*;
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
        btnEliminarCargo.setOnAction(this::btnEliminarOnAction);

        cargarDatos();

        txtBusqueda.setOnKeyReleased(event -> {

            buscarDatos(txtBusqueda.getText());

        });

    }

    public void setTableCargos(TableView<Cargos> tbCargo) {
        this.tbCargo = tbCargo;
    }
    private void btnAgregarCargoOnAction(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/AgregarCargos.fxml"));
            Parent root = loader.load();



            AgregarCargosControlador agregarCargosControlador = loader.getController();
            agregarCargosControlador.setTableCargos(tbCargo);

            Stage stage = new Stage();
            stage.setTitle("Cargos");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btnEditarCargoOnAction(ActionEvent event){
        Cargos cargoSeleccionado = (Cargos) tbCargo.getSelectionModel().getSelectedItem();

        if (cargoSeleccionado != null) {
            // Crear y mostrar la ventana de actualización con los datos de la fila seleccionada
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/ActualizarCargo.fxml"));
            Parent root;
            try {

                root = loader.load();
                // Obtener el controlador de la ventana de actualización
                ActualizarCargoControlador actualizar_CargoControlador = loader.getController();

                // Pasar la referencia de TableEmpleados al controlador de la ventana de actualización
                actualizar_CargoControlador.setTableCargos(tbCargo);
                actualizar_CargoControlador.initialize(cargoSeleccionado);


                // Mostrar la ventana de actualización
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();


            } catch (IOException e) {
                // Manejo de excepciones
                e.printStackTrace();
            }
        }
    }

    private void btnEliminarOnAction(ActionEvent event){
        eliminarCargo();

    }

    private void eliminarCargo() {
        // Obtener el ID del proyecto seleccionado (asumiendo que tienes una variable para almacenar el ID)
        int idCargo = obtenerIdCargoSeleccionado();

        try (Connection connection = Conexion.obtenerConexion()) {
            String sql = "DELETE FROM tbcargos WHERE idcargo = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idCargo);

            int filasAfectadas = statement.executeUpdate();
            if (filasAfectadas > 0) {
                agregar_empleadosControlador.mostrarAlerta("Eliminación de datos","Se eliminaron los datos exitosamente", Alert.AlertType.INFORMATION);

            } else {
                agregar_empleadosControlador.mostrarAlerta("Alerta","No se encontro ningun empleado", Alert.AlertType.WARNING);
            }
            cargarDatos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    private int obtenerIdCargoSeleccionado() {
        Cargos cargoSeleccionado = (Cargos) tbCargo.getSelectionModel().getSelectedItem();

        if (cargoSeleccionado != null) {
            return cargoSeleccionado.getIdCargo();
        } else {
            return -1; // Retorna un valor que indique que no se ha seleccionado ningún proyecto.
        }
    }

    public void cargarDatos() {
        tbCargo.getItems().clear();


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
