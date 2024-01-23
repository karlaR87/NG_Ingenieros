package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.*;
import com.example.ng_ingenieros.Controlador.AlertDos;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
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
    private Button btnAgregarCargo, btnEditarCargo, btnEliminarCargo, btnRefresh;
    @FXML
    private TextField txtBusqueda;

    @FXML
    private Button btnClose;

    @FXML
    private Pane topPane; // Asegúrate de que tienes una referencia a tu AnchorPane principal
    private double xOffset =0;
    private double yOffset =0;
    @FXML
    protected void handleClickAction(MouseEvent event) {
        Stage stage = (Stage) topPane.getScene().getWindow();
        xOffset = stage.getX() + (stage.getWidth() / 2) - event.getX();
        yOffset = stage.getY() - event.getY();
    }

    @FXML
    protected void handleMovementAction(MouseEvent event) {
        Stage stage = (Stage) topPane.getScene().getWindow();
        stage.setX(event.getScreenX() + xOffset - (stage.getWidth() / 2));
        stage.setY(event.getScreenY() + yOffset);
    }

    @FXML
    protected void HandleCloseAction(ActionEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
    public void initialize() {
        // Configura el evento de clic para el botón
        btnAgregarCargo.setOnAction(this::btnAgregarCargoOnAction);
        btnEditarCargo.setOnAction(this::btnEditarCargoOnAction);
        btnEliminarCargo.setOnAction(this::btnEliminarOnAction);
        btnRefresh.setOnAction(this::btnRefreshOnAction);

        cargarDatos();
        configurarTabla();
        txtBusqueda.setOnKeyReleased(event -> {

            buscarDatos(txtBusqueda.getText());

        });

    }
    private void configurarTabla() {
        // Configurar la tabla para permitir selección múltiple
        tbCargo.getSelectionModel().setCellSelectionEnabled(false);
        tbCargo.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
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
            // Configurar la modalidad (bloquea la ventana principal)
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
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
                // Configurar la modalidad (bloquea la ventana principal)
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.UNDECORATED);
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

    private void btnRefreshOnAction(ActionEvent event){
        cargarDatos();
    }

    private void eliminarCargo() {
        // Obtener los cargos seleccionados
        ObservableList<Cargos> cargosSeleccionados = tbCargo.getSelectionModel().getSelectedItems();

        if (!cargosSeleccionados.isEmpty()) {
            AlertDos alertDos = new AlertDos();
            // Mostrar una confirmación antes de eliminar utilizando tu clase AlertDos
            boolean confirmacion = alertDos.mostrarAlerta("¿Está seguro de que desea eliminar los cargos seleccionados?", "Confirmación");

            if (confirmacion) {
                // Elimina los cargos de la base de datos y de la tabla
                try (Connection connection = Conexion.obtenerConexion();
                     Statement stmt = connection.createStatement()) {
                    for (Cargos cargoSeleccionado : cargosSeleccionados) {
                        String query = "DELETE FROM tbcargos WHERE idcargo = " + cargoSeleccionado.getIdCargo();
                        stmt.executeUpdate(query);
                    }
                    cargarDatos(); // Recarga los datos en la tabla después de eliminar
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // Muestra un mensaje si no se seleccionan cargos
            CustomAlert customAlert = new CustomAlert();
            customAlert.mostrarAlertaPersonalizada("Alerta", "Por favor, seleccione al menos un cargo para eliminar.", (Stage) btnEliminarCargo.getScene().getWindow());
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
