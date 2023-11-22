package com.example.ng_ingenieros;

import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.jfoenix.controls.JFXTextField;



public class Validaciones {

    //Este era con una libreria pero XD

    /*
@FXML public void Validaciones(){

    //No dejar campos vacios
    RequiredFieldValidator vacioValidacion = new RequiredFieldValidator();
    vacioValidacion.setMessage("Este campo es obligatorio");

    //Solo numeros jaksjs
    NumberValidator numeroValidacion = new NumberValidator();
    numeroValidacion.setMessage("Solo se admiten números");

    //Validaion para una direion de correo válida
    RegexValidator correoValidacion = new RegexValidator();
    correoValidacion.setRegexPattern( "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    correoValidacion.setMessage("Ingresa una dirección de correo válida");

}

     */


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

    // Puedes agregar más métodos de validación según sea necesario

    public static void validarNumeros(TextField textField) {
        if (validarNumero(textField.getText())) {
            mostrarAlerta("Validación Exitosa", "Número válido.");
        } else {
            mostrarAlerta("Error de Validación", "Ingrese solo números.");
        }
    }

    public static void validarLetras(TextField textField) {
        if (validarLetras(textField.getText())) {
            mostrarAlerta("Validación Exitosa", "Letras válidas.");
        } else {
            mostrarAlerta("Error de Validación", "Ingrese solo letras.");
        }
    }

    public static void validarCorreo(TextField textField) {
        if (validarCorreo(textField.getText())) {
            mostrarAlerta("Validación Exitosa", "Correo válido.");
        } else {
            mostrarAlerta("Error de Validación", "Ingrese un correo válido.");
        }
    }

    public static void NoVacio(TextField textField) {
        if (NoVacio(textField.getText())) {
            mostrarAlerta("Validación Exitosa", "Campo no vacío.");
        } else {
            mostrarAlerta("Error de Validación", "Este campo no puede estar vacío.");
        }
    }





}
