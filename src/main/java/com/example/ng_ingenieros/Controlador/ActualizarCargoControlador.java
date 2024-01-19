package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Cargos;
import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.Empleados;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ActualizarCargoControlador {

@FXML
private TextField txtActualizarCargo;

@FXML
private Button btnActualizarCargo, btnCancelar;

private TableView<Cargos> tbCargo;

    @FXML
    private Pane topPane; // Asegúrate de que tienes una referencia a tu AnchorPane principal
    private double xOffset =0;
    private double yOffset =0;
    @FXML
    protected void handleClickAction(MouseEvent event) {
        Stage stage = (Stage) topPane.getScene().getWindow();
        xOffset = stage.getX() - event.getX();
        yOffset = stage.getY() - event.getY();
    }

    @FXML
    protected void handleMovementAction(MouseEvent event) {
        Stage stage = (Stage) topPane.getScene().getWindow();
        stage.setX(event.getScreenX() + xOffset);
        stage.setY(event.getScreenY() +yOffset);
    }



    public void initialize(Cargos cargoSeleccionado) {
        btnCancelar.setOnAction(this::cerrarVentana);

        btnActualizarCargo.setOnAction(actionEvent -> {
            try {
               actualizarDatos(actionEvent);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });


        txtActualizarCargo.setText(cargoSeleccionado.getCargo());
    }

    public void setTableCargos(TableView<Cargos> tbCargo) {
        this.tbCargo = tbCargo;
    }


    private void cerrarVentana(javafx.event.ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }


    private void actualizarDatos(ActionEvent event) throws SQLException {
        validaciones();
    }

    public void actualizarCargos() {

        // Obtener los datos actualizados de los campos
        String CargoN = txtActualizarCargo.getText();


        // Obtener el empleado seleccionado en la tabla
        Cargos cargoSeleccionado = obtenerEmpleadoSeleccionadoDesdeTabla();

        // Realizar la actualización en la base de datos
        try (Connection conn = Conexion.obtenerConexion()) {
            String sql = "UPDATE tbcargos SET cargo=? WHERE idcargo=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, CargoN);
                ps.setInt(2, cargoSeleccionado.getIdCargo()); // Asegúrate de tener un método getIdEmpleado() en tu clase Empleado
                ps.executeUpdate();

                if ( tbCargo != null) {
                    tbCargo.getItems().clear();
                    CrudCargosControlador crudCargosControlador = new CrudCargosControlador();
                    crudCargosControlador.setTableCargos(tbCargo);
                    crudCargosControlador.cargarDatos();
                }

                // Opcional: Cerrar la ventana actual
                ((Stage) txtActualizarCargo.getScene().getWindow()).close();

                agregar_empleadosControlador.mostrarAlerta("Actualización de Datos", "Se han actualizado los datos exitosamente", Alert.AlertType.INFORMATION);
            }
        } catch (SQLException e) {
            agregar_empleadosControlador.mostrarAlerta("Error", "Ha ocurrido un error", Alert.AlertType.ERROR);
            e.printStackTrace();

        }
    }


    private Cargos obtenerEmpleadoSeleccionadoDesdeTabla() {

        return tbCargo.getSelectionModel().getSelectedItem(); // Reemplaza con la lógica real
    }

    public void validaciones(){
        if (validarLetras(txtActualizarCargo.getText())){
            actualizarCargos();
        }
        else {
            mostrarAlerta("Error de Validación", "Solo se pueden ingresar letras en el nombre.");
        }
    }
    public static boolean validarLetras(String input) {
        return input.matches("[a-zA-Z ]+");
    }
    public static void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
