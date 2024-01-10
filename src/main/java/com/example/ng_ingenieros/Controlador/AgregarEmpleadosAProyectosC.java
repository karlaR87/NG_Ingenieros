package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.Empleados;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AgregarEmpleadosAProyectosC {
    @FXML
    private TextField txtNombreEmp;
    @FXML
    private TextField txtDuiEmp;
    @FXML
    private TextField txtCorreoEmp;
    @FXML
    private ComboBox<String> cbCargoEmp;
    @FXML
    private ComboBox<String> cbPlaza;
    @FXML
    public TextField txtPagoHorasExEmp;
    @FXML
    public TextField txtNumCuenta;
    @FXML
    public TextField txtSueldoEmp;
    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnGuardar;


    private ObservableList<Empleados> empleados = FXCollections.observableArrayList();



    public void initialize() {
        // Configura el evento de clic para el botón
        btnGuardar.setOnAction(this::Guardar);
        btnCancelar.setOnAction(this::cerrarVentana);

        CargarCargo();

    }



    private void CargarCargo() {
        // Crear una lista observable para almacenar los datos
        ObservableList<String> data = FXCollections.observableArrayList();

        // Conectar a la base de datos y recuperar los datos
        try (Connection conn = Conexion.obtenerConexion()) { // Reemplaza con tu propia lógica de conexión
            String query = "select cargo from tbcargos"; // Reemplaza con tu consulta SQL
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Recorrer los resultados y agregarlos a la lista observable
            while (resultSet.next()) {
                String item = resultSet.getString("cargo"); // Reemplaza con el nombre de la columna de tu tabla
                data.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de errores
        }

        // Asignar los datos al ComboBox
        cbCargoEmp.setItems(data);
    }


    public int IdRetornoCargo(String Cargo) throws SQLException {
        Connection conectar = null;
        PreparedStatement pst = null;
        ResultSet result = null;
        int idCargo = -1;

        String SSQL = "SELECT idcargo FROM tbcargos WHERE cargo = ?";

        try {
            conectar = Conexion.obtenerConexion();
            pst = conectar.prepareStatement(SSQL);
            pst.setString(1, Cargo);
            result = pst.executeQuery();

            if (result.next()) {
                idCargo = result.getInt("idcargo");
            } else {
                System.err.println("El cargo seleccionado no existe en la base de datos.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            // Cerrar recursos
            if (result != null) {
                result.close();
            }
            if (pst != null) {
                pst.close();
            }
            if (conectar != null) {
                conectar.close();
            }
        }

        return idCargo;
    }



    ///GUARDAR EN ARRAY
        @FXML
        private void agregarEmpleado(javafx.event.ActionEvent actionEvent) {
        validaciones();
        // Obtener todos los valores

    }
//...


    private void Guardar(javafx.event.ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private void cerrarVentana(javafx.event.ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        empleados.clear();
        stage.close();
    }


    public ObservableList<Empleados> getPersonas() {
        return empleados;
    }

    public ObservableList<Empleados> getEmpleadosList() {
        return empleados;
    }

    public void validaciones() {
        if (NoVacio(txtNombreEmp.getText()) && NoVacio(txtCorreoEmp.getText()) && NoVacio(txtNumCuenta.getText())&& NoVacio(txtDuiEmp.getText())&& NoVacio(txtPagoHorasExEmp.getText())&& NoVacio(txtSueldoEmp.getText())){
            if (validarLetras(txtNombreEmp.getText())){
                if (validarCorreo(txtCorreoEmp.getText())){
                    if (validarDui(txtDuiEmp.getText())){
                        if (validarNumero(txtPagoHorasExEmp.getText()) && validarNumero(txtSueldoEmp.getText())){
                            String nombre = txtNombreEmp.getText();
                            String dui = txtDuiEmp.getText();
                            String correo = txtCorreoEmp.getText();
                            String cargoNombre = cbCargoEmp.getValue();
                            Double sueldoHora = Double.parseDouble(txtPagoHorasExEmp.getText());
                            String numCuenta = txtNumCuenta.getText();
                            Double sueldo = Double.parseDouble(txtSueldoEmp.getText());

                            try {
                                // Obtener el idcargo correspondiente al nombre seleccionado en el ComboBox
                                int idCargo = IdRetornoCargo(cargoNombre);

                                // Crear una nueva instancia de Empleados y agregar a la lista observable
                                Empleados empleado = new Empleados(nombre, dui, correo, idCargo, sueldoHora, numCuenta, sueldo);
                                empleados.add(empleado);

                                // Imprimir un mensaje en consola con todas las personas
                                System.out.println("Empleado agregado: " + empleado);

                                // Después de utilizar los valores, limpiar los campos
                                txtNombreEmp.clear();
                                txtDuiEmp.clear();
                                txtCorreoEmp.clear();
                                cbCargoEmp.setValue(null);
                                txtPagoHorasExEmp.clear();
                                txtNumCuenta.clear();
                                txtSueldoEmp.clear();
                            } catch (SQLException e) {
                                e.printStackTrace();
                                // Manejar la excepción, por ejemplo, mostrar un mensaje de error
                                mostrarAlerta("Error", "Error al obtener el idcargo: " + e.getMessage());
                            }

                        }else {
                            mostrarAlerta("Error de Validación", "Ingrese solo números.");
                        }

                    }else {
                        mostrarAlerta("Error de Validación", "Ingrese un DUI válido.");
                    }

                }else {
                    mostrarAlerta("Error de Validación", "Ingrese un correo válido.");
                }

            }else {
                mostrarAlerta("Error de Validación", "Solo se pueden ingresar letras en el nombre.");
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
        return input.matches("[a-zA-Z ]+");
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
