package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.Empleados;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.*;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class EmpleadosControlador {

    @FXML
    public TableView<Empleados> TableEmpleados;
    @FXML
    private TextField txtBusqueda;

    @FXML
    private Button btnAgregarEmp, btnEditarEmp, btnEliminarEmp, btnRefresh;

    public void initialize() {
        //configurarTabla();

        btnEditarEmp.setOnAction(this::btnEditarOnAction);
        btnAgregarEmp.setOnAction(this::btnAgregarOnAction);
        btnEliminarEmp.setOnAction(this::eliminardatos);
        btnRefresh.setOnAction(this::refrescar);

        txtBusqueda.setOnKeyReleased(event -> {

                buscarDatos(txtBusqueda.getText());

        });
        cargarDatos();
    }

    public void setTableEmpleados(TableView<Empleados> tableEmpleados) {
        this.TableEmpleados = tableEmpleados;
    }

    private void btnAgregarOnAction(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/agregar_empledos.fxml"));
            Parent root = loader.load();

            agregar_empleadosControlador agregarEmpleadosControlador = loader.getController();
            agregarEmpleadosControlador.setTableEmpleados(TableEmpleados);


            Stage stage = new Stage();
            stage.setTitle("Ingreso de empleados");

            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btnEditarOnAction(javafx.event.ActionEvent actionEvent) {
        Empleados empleadoSeleccionado = TableEmpleados.getSelectionModel().getSelectedItem();

        if (empleadoSeleccionado != null) {
            // Crear y mostrar la ventana de actualización con los datos de la fila seleccionada
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/actualizar_empleados.fxml"));
            Parent root;
            try {

                root = loader.load();
                // Obtener el controlador de la ventana de actualización
                actualizar_empleadosControlador actualizarEmpleadosControlador = loader.getController();

                actualizarEmpleadosControlador.setTableEmpleados(TableEmpleados);
                actualizarEmpleadosControlador.initialize(empleadoSeleccionado);


                // Mostrar la ventana de actualización
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();

                actualizarEmpleadosControlador.setTableEmpleados(TableEmpleados);
            } catch (IOException e) {
                // Manejo de excepciones
            }
        }
    }



    private void eliminardatos(javafx.event.ActionEvent actionEvent) {

        eliminarEmpleado();

    }

    private void refrescar(javafx.event.ActionEvent actionEvent){
        cargarDatos();
    }




    // Dentro del método cargarDatos() en la clase EmpleadosControlador
    public void cargarDatos() {
        TableEmpleados.getItems().clear();
        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select emp.idempleado, emp.nombreCompleto, emp.dui, emp.sueldo_dia, emp.sueldo_horaExt, c.cargo, tp.tipoPlaza from tbempleados emp\n" +
                     "inner join tbcargos c on c.idcargo = emp.idcargo\n" +
                     "inner join tbtipoPlazas tp on tp.idTipoPlaza = emp.idTipoPlaza")) {

            while (rs.next()) {
                int id = rs.getInt("idempleado");
                String nombre = rs.getString("nombreCompleto");
                String dui = rs.getString("dui");
                Double sueldoDia = rs.getDouble("sueldo_dia");
                Double sueldoHora = rs.getDouble("sueldo_horaExt");
                String cargo = rs.getString("cargo");
                String plaza = rs.getString("tipoPlaza");



                // Agregar los datos a la tabla
                TableEmpleados.getItems().add(new Empleados(id, nombre, dui, sueldoDia, sueldoHora, cargo, plaza));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void buscarDatos(String busqueda) {
        TableEmpleados.getItems().clear(); // Limpiar los elementos actuales de la tabla

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement("SELECT emp.idempleado, emp.nombreCompleto, emp.dui, emp.sueldo_dia, emp.sueldo_horaExt, c.cargo, tp.tipoPlaza " +
                     "FROM tbempleados emp " +
                     "INNER JOIN tbcargos c ON c.idcargo = emp.idcargo " +
                     "INNER JOIN tbtipoPlazas tp ON tp.idTipoPlaza = emp.idTipoPlaza " +
                     "WHERE emp.nombreCompleto LIKE ? OR emp.dui LIKE ? OR emp.sueldo_dia LIKE ? OR emp.sueldo_horaExt LIKE ? OR c.cargo LIKE ? OR tp.tipoPlaza LIKE ?")) {

            // Preparar el parámetro de búsqueda para la consulta SQL
            String parametroBusqueda = "%" + busqueda + "%";
            for (int i = 1; i <= 6; i++) {
                stmt.setString(i, parametroBusqueda);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Obtener los datos y agregarlos a la tabla
                int id = rs.getInt("idempleado");
                String nombre = rs.getString("nombreCompleto");
                String dui = rs.getString("dui");
                Double sueldoDia = rs.getDouble("sueldo_dia");
                Double sueldoHora = rs.getDouble("sueldo_horaExt");
                String cargo = rs.getString("cargo");
                String plaza = rs.getString("tipoPlaza");


                TableEmpleados.getItems().add(new Empleados(id, nombre, dui, sueldoDia, sueldoHora, cargo, plaza));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    private void eliminarEmpleado() {
        // Obtener el ID del proyecto seleccionado (asumiendo que tienes una variable para almacenar el ID)
        int idEmpleado = obtenerIdEmpleadoSeleccionado();

        try (Connection connection = Conexion.obtenerConexion()) {
            String sql = "DELETE FROM tbempleados WHERE idempleado = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idEmpleado);

            int filasAfectadas = statement.executeUpdate();
            if (filasAfectadas > 0) {
                agregar_empleadosControlador.mostrarAlerta("Eliminación de datos","Se eliminaron los datos exitosamente", Alert.AlertType.INFORMATION);

            } else {
                agregar_empleadosControlador.mostrarAlerta("Alerta","No se encontro ningun empleado", Alert.AlertType.WARNING);
            }
            TableEmpleados.getItems().clear();
            cargarDatos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    private int obtenerIdEmpleadoSeleccionado() {
        Empleados empleadoSeleccionado = TableEmpleados.getSelectionModel().getSelectedItem();

        if (empleadoSeleccionado != null) {
            return empleadoSeleccionado.getId();
        } else {
            return -1; // Retorna un valor que indique que no se ha seleccionado ningún proyecto.
}
    }






}







        /*txtBusqueda.setOnKeyReleased(event -> {
        if (!txtBusqueda.getText().isEmpty()) {
            BuscarDatos();
        }
        });*/



