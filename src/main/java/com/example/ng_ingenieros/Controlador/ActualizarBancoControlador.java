package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.*;
import com.example.ng_ingenieros.Controlador.CrudBancosControlador;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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


public class ActualizarBancoControlador {
    @FXML
    private TextField txtActualizarBanco;
    @FXML
    private Button btnActualizarBanco, btnCancelar;

    private TableView<Bancos> tbBanco;

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

    public void setTableBancos(TableView<Bancos> tbBanco) {
        this.tbBanco = tbBanco;
    }
    public void initialize(Bancos bancoSeleccionado) {
        // Configura el evento de clic para el botón
        btnCancelar.setOnAction(this::btnCancelarOnAction);
        btnActualizarBanco.setOnAction(actionEvent -> {
            try {
                btnActualizarBancoOnAction(actionEvent);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        txtActualizarBanco.setText(bancoSeleccionado.getBanco());
    }
    private void btnCancelarOnAction(ActionEvent event){
        ((Stage) txtActualizarBanco.getScene().getWindow()).close();
    }
    private void btnActualizarBancoOnAction(ActionEvent event) throws SQLException {
        validaciones();
    }

    private void validaciones() throws SQLException {
        if (validarLetras(txtActualizarBanco.getText())){
            actualizarbanco();
        }
        else {
            mostrarAlerta("Error de Validación", "Solo se pueden ingresar letras en el nombre.");
        }
    }

    private void actualizarbanco() throws SQLException {
        String CargoN = txtActualizarBanco.getText();
        // Obtener el empleado seleccionado en la tabla
        Bancos cargoSeleccionado = obtenerEmpleadoSeleccionadoDesdeTabla();

        // Realizar la actualización en la base de datos
        try (Connection conn = Conexion.obtenerConexion()) {
            String sql = "UPDATE tbBancos SET banco=? WHERE idBanco=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, CargoN);
                ps.setInt(2, cargoSeleccionado.getIdbanco()); // Asegúrate de tener un método getIdEmpleado() en tu clase Empleado
                ps.executeUpdate();

                agregar_empleadosControlador.mostrarAlerta("Actualización de Datos", "Se han actualizado los datos exitosamente", Alert.AlertType.INFORMATION);
                if (tbBanco != null) {
                    tbBanco.getItems().clear();
                    CrudBancosControlador crudBancosControlador = new CrudBancosControlador();
                    crudBancosControlador.setTableBanco(tbBanco);
                    crudBancosControlador.cargarDatos();

                    ((Stage) txtActualizarBanco.getScene().getWindow()).close();
            }
        } catch (SQLException e) {
            agregar_empleadosControlador.mostrarAlerta("Error", "Ha ocurrido un error", Alert.AlertType.ERROR);
            e.printStackTrace();

        }
    }

}

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private boolean validarLetras(String text) {
        return text.matches("[a-zA-Z ]+");
    }

    private Bancos obtenerEmpleadoSeleccionadoDesdeTabla() {
        return tbBanco.getSelectionModel().getSelectedItem(); // Reemplaza con la lógica real
    }
    }

