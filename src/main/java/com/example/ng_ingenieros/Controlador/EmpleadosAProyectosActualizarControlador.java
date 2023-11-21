package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.Empleados;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    private ComboBox<String> cbPlaza;

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

        CargarCargo();
        CargarPlaza();

    }
    public void cargarDatosEmpleado(Empleados empleado) {
        this.empleadoParaActualizar = empleado;

        // Llena los campos del formulario con los datos del empleado para actualizar
        if (empleado != null) {
            txtNombreEmp.setText(empleado.getNombre());
            txtDuiEmp.setText(empleado.getDui());
            txtCorreoEmp.setText(empleado.getCorreo());
            cbCargoEmp.setValue(empleado.getCargo());
            cbPlaza.setValue(empleado.getPlaza());
            txtPagoHorasExEmp.setText(String.valueOf(empleado.getSueldoHora()));
            txtNumCuenta.setText(empleado.getCuentaBancaria());
            txtSueldoEmp.setText(String.valueOf(empleado.getSueldoDia()));
        }
    }


    @FXML
    private void guardar(ActionEvent event) {
        // Actualiza los datos del empleado con los valores de los campos del formulario
        if (empleadoParaActualizar != null) {
            empleadoParaActualizar.setNombre(txtNombreEmp.getText());
            empleadoParaActualizar.setDui(txtDuiEmp.getText());
            empleadoParaActualizar.setCorreo(txtCorreoEmp.getText());
            empleadoParaActualizar.setCargo(cbCargoEmp.getValue());
            empleadoParaActualizar.setPlaza(cbPlaza.getValue());
            empleadoParaActualizar.setSueldoHora(Double.parseDouble(txtPagoHorasExEmp.getText()));
            empleadoParaActualizar.setCuentaBancaria(txtNumCuenta.getText());
            empleadoParaActualizar.setSueldoDia(Double.parseDouble(txtSueldoEmp.getText()));


        }


        // Imprimir en la consola los datos actualizados
        System.out.println("Datos actualizados: " + empleadoParaActualizar.toString());

        // Cierra la ventana después de guardar cambios
        cerrarVentana(event);

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

    public Empleados  getPersonas() {
        return empleadoParaActualizar;
    }

}
