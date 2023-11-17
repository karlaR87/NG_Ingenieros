package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.Empleados;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;




public class actualizar_empleadosControlador {


    private TableView<Empleados> TableEmpleados ;

    @FXML
    private TextField txtNombreEmp2;

    @FXML
    private TextField txtDuiEmp2;
    @FXML
    private TextField txtCorreoEmp2;
    @FXML
    private TextField txtNumCuenta2;
    @FXML
    private TextField txtSueldoEmp2;
    @FXML
    private TextField txtPagoHorasExEmp2;
    @FXML
    private ComboBox<String> cbCargos2;
    @FXML
    private ComboBox<String> cbPlaza2;

    @FXML
    private Button btnActualizarEmp, btnCancelar2;




    public void initialize(Empleados empleadoSeleccionado) {
        btnActualizarEmp.setOnAction(this::actualizarDatos);
        btnCancelar2.setOnAction(this::cerrarVentana);

        txtNombreEmp2.setText(empleadoSeleccionado.getNombre());
        txtDuiEmp2.setText(empleadoSeleccionado.getDui());
        txtCorreoEmp2.setText(empleadoSeleccionado.getCorreo());
        txtNumCuenta2.setText(empleadoSeleccionado.getCuentaBancaria());
        txtSueldoEmp2.setText(String.valueOf(empleadoSeleccionado.getSueldoDia()));
        txtPagoHorasExEmp2.setText(String.valueOf(empleadoSeleccionado.getSueldoHora()));

        // Seleccionar el valor en el ComboBox correspondiente al cargo
        // Seleccionar el valor en el ComboBox correspondiente al cargo
        cbCargos2.setValue(empleadoSeleccionado.getCargo());

        cargarCargosEnCombobox();

        // Seleccionar el valor en el ComboBox correspondiente a la plaza
        cbPlaza2.setValue(empleadoSeleccionado.getPlaza());

        cargarPlazasEnCombobox();

    }





    private void actualizarDatos(javafx.event.ActionEvent actionEvent) {
        actualizarEmpleado();
    }

    private void cerrarVentana(javafx.event.ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }







    public void actualizarEmpleado() {
        // Obtener los datos actualizados de los campos
        String nombreN = txtNombreEmp2.getText();
        String duiN = txtDuiEmp2.getText();
        String correoN = txtCorreoEmp2.getText();
        String cuentaBancariaN = txtNumCuenta2.getText();
        double sueldoDiaN = Double.parseDouble(txtSueldoEmp2.getText());
        double sueldoHoraExtN = Double.parseDouble(txtPagoHorasExEmp2.getText());
        int idCargoN = obtenerIdCargoSeleccionado(cbCargos2);
        int idPlazaN = obtenerIdPlazaSeleccionado(cbPlaza2);

        // Obtener el empleado seleccionado en la tabla
        Empleados empleadoSeleccionado = obtenerEmpleadoSeleccionadoDesdeTabla();

        // Realizar la actualización en la base de datos
        try (Connection conn = Conexion.obtenerConexion()) {
            String sql = "UPDATE tbempleados SET nombreCompleto=?, dui=?, correo=?, sueldoDia=?, sueldoHoraExt=?, numero_cuentabancaria=?, idcargo=?, idtipoPlaza=? WHERE idempleado=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, nombreN);
                ps.setString(2, duiN);
                ps.setString(3, correoN);
                ps.setDouble(4, sueldoDiaN);
                ps.setDouble(5, sueldoHoraExtN);
                ps.setString(6, cuentaBancariaN);
                ps.setInt(7, idCargoN);
                ps.setInt(8, idPlazaN);
                ps.setInt(9, empleadoSeleccionado.getId()); // Asegúrate de tener un método getIdEmpleado() en tu clase Empleado
                ps.executeUpdate();

                agregar_empleadosControlador.mostrarAlerta("Actualización de empleados", "Se han actualizado los datos exitosamente", Alert.AlertType.INFORMATION);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            agregar_empleadosControlador.mostrarAlerta("Error", "Ha ocurrido un error", Alert.AlertType.ERROR);
        }
    }

    // Método para obtener el nombre del cargo a partir del ID
    private void cargarPlazasEnCombobox() {
        // Crear una lista observable para almacenar los datos
        ObservableList<String> data = FXCollections.observableArrayList();

        // Conectar a la base de datos y recuperar los datos
        try (Connection conn = Conexion.obtenerConexion()) { // Reemplaza con tu propia lógica de conexión
            String query = "SELECT tipoPlaza FROM tbtipoPlazas";
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
        cbPlaza2.setItems(data);
    }



    private void cargarCargosEnCombobox() {
        // Crear una lista observable para almacenar los datos
        ObservableList<String> data = FXCollections.observableArrayList();

        // Conectar a la base de datos y recuperar los datos
        try (Connection conn = Conexion.obtenerConexion()) { // Reemplaza con tu propia lógica de conexión
            String query = "SELECT cargo FROM tbcargos";
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
        cbCargos2.setItems(data);
    }

    private Empleados obtenerEmpleadoSeleccionadoDesdeTabla() {

        return TableEmpleados.getSelectionModel().getSelectedItem(); // Reemplaza con la lógica real
    }


    private int obtenerIdCargoSeleccionado(ComboBox<String> cbCargoEmp) {
        int idCargo = -1; // Valor predeterminado en caso de error o no selección

        try (Connection conn = Conexion.obtenerConexion()) {
            String cargoSeleccionado = cbCargoEmp.getValue();

            String sql = "SELECT idcargo FROM tbcargos WHERE cargo = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, cargoSeleccionado);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idCargo = rs.getInt("idcargo");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idCargo;
    }


    private int obtenerIdPlazaSeleccionado(ComboBox<String> cbPlazaEmp) {
        int idPlaza = -1; // Valor predeterminado en caso de error o no selección

        try (Connection conn = Conexion.obtenerConexion()) {
            String PlazaSeleccionada = cbPlazaEmp.getValue();

            String sql = "SELECT idtipoPlaza FROM tbTipoPlazas WHERE tipoPlaza = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, PlazaSeleccionada);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idPlaza = rs.getInt("idtipoPlaza");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idPlaza;
    }


}
