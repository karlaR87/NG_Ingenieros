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

import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


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
    private TextField txtPagoHorasExEmp;
    @FXML
    private TextField txtNumCuenta;
    @FXML
    private TextField txtSueldoEmp;
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
        CargarPlaza();

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

    private void CargarPlaza() {
        // Crear una lista observable para almacenar los datos
        ObservableList<String> data = FXCollections.observableArrayList();

        // Conectar a la base de datos y recuperar los datos
        try (Connection conn = Conexion.obtenerConexion()) { // Reemplaza con tu propia lógica de conexión
            String query = "select tipoPlaza from tbtipoPlazas"; // Reemplaza con tu consulta SQL
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Recorrer los resultados y agregarlos a la lista observable
            while (resultSet.next()) {
                String item = resultSet.getString("tipoPlaza"); // Reemplaza con el nombre de la columna de tu tabla
                data.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de errores
        }

        // Asignar los datos al ComboBox
        cbPlaza.setItems(data);
    }


    ///GUARDAR EN ARRAY
    @FXML
    private void agregarEmpleado(javafx.event.ActionEvent actionEvent){
        //obtener todos los valores

        String nombre = txtNombreEmp.getText();
        String dui = txtDuiEmp.getText();
        String correo = txtCorreoEmp.getText();
        String cargo = cbCargoEmp.getValue();
        String plaza = cbPlaza.getValue();
        Double sueldoHora = Double.parseDouble(txtPagoHorasExEmp.getText());
        String numCuenta = txtNumCuenta.getText();
        Double sueldo = Double.parseDouble(txtSueldoEmp.getText());

// Crea una nueva instancia de Empleados y agrega a la lista observable
        Empleados empleado = new Empleados(nombre, dui,correo,cargo,plaza,sueldoHora,numCuenta,sueldo);
        empleados.add(empleado);
// Imprime un mensaje en consola con todas las personas
        System.out.println("Personas agregadas: " + empleado);
// Después de utilizar los valores, limpiar los campos
        txtNombreEmp.clear();
        txtDuiEmp.clear();
        txtCorreoEmp.clear();
        cbCargoEmp.setValue(null);
        cbPlaza.setValue(null);
        txtPagoHorasExEmp.clear();
        txtNumCuenta.clear();
        txtSueldoEmp.clear();


    }

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


}
