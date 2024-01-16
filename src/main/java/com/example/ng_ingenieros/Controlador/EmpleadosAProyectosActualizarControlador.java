package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.Empleados;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmpleadosAProyectosActualizarControlador {

    @FXML
    private TextField txtNombreEmp;

    @FXML
    private TextField txtDuiEmp;

    @FXML
    private TextField txtCorreoEmp;

    @FXML
    private ComboBox<String> cbCargoEmp;



    @FXML
    private TextField txtPagoHorasExEmp;

    @FXML
    private TextField txtNumCuenta;

    @FXML
    private TextField txtSueldoEmp;

    @FXML
    private Button btnGuardar;

    private Empleados empleadoParaActualizar;


    public void initialize() {
        btnGuardar.setOnAction(this::guardar);

        CargarCargo();


    }
    public void cargarDatosEmpleado(Empleados empleado) {
        this.empleadoParaActualizar = empleado;

        // Llena los campos del formulario con los datos del empleado para actualizar
        if (empleado != null) {
            txtNombreEmp.setText(empleado.getNombre());
            txtDuiEmp.setText(empleado.getDui());
            txtCorreoEmp.setText(empleado.getCorreo());
            cbCargoEmp.setValue(empleado.getCargo());
            txtPagoHorasExEmp.setText(String.valueOf(empleado.getSueldoHora()));
            txtNumCuenta.setText(empleado.getCuentaBancaria());
            txtSueldoEmp.setText(String.valueOf(empleado.getSueldoDia()));
        }
    }


    @FXML
    private void guardar(ActionEvent event) {
        validaciones(event);

    }
    @FXML
    private void cerrarVentana(javafx.event.ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
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


    public Empleados  getPersonas() {
        return empleadoParaActualizar;
    }


    //Validaciones
    public void validaciones(javafx.event.ActionEvent actionEvent) {
        if (NoVacio(txtNombreEmp.getText()) && NoVacio(txtCorreoEmp.getText()) && NoVacio(txtNumCuenta.getText()) && NoVacio(txtDuiEmp.getText()) && NoVacio(txtPagoHorasExEmp.getText()) && NoVacio(txtSueldoEmp.getText())) {
            if (validarLetras(txtNombreEmp.getText())) {
                if (validarCorreo(txtCorreoEmp.getText())) {
                    if (validarDui(txtDuiEmp.getText())) {
                        if (validarNumero(txtPagoHorasExEmp.getText()) && validarNumero(txtSueldoEmp.getText())) {
                            // Actualiza los datos del empleado con los valores de los campos del formulario
                            if (empleadoParaActualizar != null) {
                                empleadoParaActualizar.setNombre(txtNombreEmp.getText());
                                empleadoParaActualizar.setDui(txtDuiEmp.getText());
                                empleadoParaActualizar.setCorreo(txtCorreoEmp.getText());
                                empleadoParaActualizar.setCargo(cbCargoEmp.getValue());
                                empleadoParaActualizar.setSueldoHora(Double.parseDouble(txtPagoHorasExEmp.getText()));
                                empleadoParaActualizar.setCuentaBancaria(txtNumCuenta.getText());
                                empleadoParaActualizar.setSueldoDia(Double.parseDouble(txtSueldoEmp.getText()));
                            }

                            // Imprimir en la consola los datos actualizados
                            System.out.println("Datos actualizados: " + empleadoParaActualizar.toString());

                            // Mostrar mensaje de confirmación
                            mostrarAlerta("Confirmación", "Datos actualizados correctamente.");
                            Node source = (Node) actionEvent.getSource();
                            Stage stage = (Stage) source.getScene().getWindow();
                            stage.close();

                        } else {
                            mostrarAlerta("Error de Validación", "Ingrese solo números.");
                        }

                    } else {
                        mostrarAlerta("Error de Validación", "Ingrese un DUI válido.");
                    }

                } else {
                    mostrarAlerta("Error de Validación", "Ingrese un correo válido.");
                }

            } else {
                mostrarAlerta("Error de Validación", "Solo se pueden ingresar letras en el nombre.");
            }

        } else {
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
