package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.*;
import com.example.ng_ingenieros.Controlador.CrudBancosControlador;
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


public class ActualizarBancoControlador {
    @FXML
    private TextField txtActualizarBanco;
    @FXML
    private Button btnActualizarBanco, btnCancelar;

    private TableView<Bancos> tbBanco;

    public void setTableBancos(TableView<Bancos> tbBanco) {
        this.tbBanco = tbBanco;
    }
    public void initialize(Bancos bancoSeleccionado) {
        // Configura el evento de clic para el botón
        btnCancelar.setOnAction(this::btnCancelarOnAction);
        btnActualizarBanco.setOnAction(this::btnActualizarBancoOnAction);

        txtActualizarBanco.setText(bancoSeleccionado.getBanco());
    }
    private void btnCancelarOnAction(ActionEvent event){
        ((Stage) txtActualizarBanco.getScene().getWindow()).close();
    }
    private void btnActualizarBancoOnAction(ActionEvent event){
        actualizarbanco();
    }
    private void actualizarbanco() {
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
            }
        } catch (SQLException e) {
            agregar_empleadosControlador.mostrarAlerta("Error", "Ha ocurrido un error", Alert.AlertType.ERROR);
            e.printStackTrace();

        }
    }
    private Bancos obtenerEmpleadoSeleccionadoDesdeTabla() {

        return tbBanco.getSelectionModel().getSelectedItem(); // Reemplaza con la lógica real
    }
}

