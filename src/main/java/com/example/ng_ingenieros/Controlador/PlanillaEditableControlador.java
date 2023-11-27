package com.example.ng_ingenieros.Controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlanillaEditableControlador {
    @FXML
    private TextField txtNombre,txtDui,txtNIT,txtCargo,txtSueldo;

    @FXML
    private Button btnCancelar, btnSiguiente;


    //Para cambiar de ventana
    public void initialize() {
        // Configura el evento de clic para el botón

        btnSiguiente.setOnAction(this::BtnSiguienteOnAction);
        btnCancelar.setOnAction(this::btnCancelarOnAction);
    }


    private void BtnSiguienteOnAction(ActionEvent event) {
        validaciones();
    }

    private void btnCancelarOnAction(ActionEvent event) {

    }

    //Validaciones
    public void validaciones() {
        if (NoVacio(txtNombre.getText()) && NoVacio(txtDui.getText()) && NoVacio(txtSueldo.getText()) && NoVacio(txtNIT.getText()) && NoVacio(txtCargo.getText())) {
            if (validarLetras(txtNombre.getText()) && validarLetras(txtCargo.getText())){
                if (validarNumero(txtSueldo.getText()) && validarNumero(txtNIT.getText())) {
                    if (validarDui(txtDui.getText())){

                    }else {
                        mostrarAlerta("Error de Validación", "Ingrese un DUI válido.");
                    }

                }else {
                    mostrarAlerta("Error de Validación", "Ingrese solo números.");
                }

            }else {
                mostrarAlerta("Error de Validación", "Ingrese solo letras.");
            }

        }else {
            mostrarAlerta("Error de validación", "Ingresar datos, no pueden haber campos vacíos.");
        }
    }

    //Validaciones
    public static boolean validarNumero(String input) {
        return input.matches("\\d+");
    }

    public static boolean validarLetras(String input) {
        return input.matches("[a-zA-Z]+");
    }

    public static boolean validarCorreo(String input) {
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public static boolean NoVacio(String input) {
        return !input.trim().isEmpty();
    }
    public static void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    // Función para validar el formato de un DUI (por ejemplo, 12345678-9)
    private boolean validarDui(String dui) {
        // Se puede implementar una lógica más avanzada según el formato real de DUI
        return dui.matches("\\d{8}-\\d{1}");
    }


}
