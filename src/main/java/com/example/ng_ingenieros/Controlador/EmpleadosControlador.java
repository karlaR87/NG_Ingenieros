package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.Controlador.AlertDos;
import com.example.ng_ingenieros.Empleados;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javafx.stage.StageStyle;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.*;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;

public class EmpleadosControlador {

    @FXML
    public TableView<Empleados> TableEmpleados;
    @FXML
    private TextField txtBusqueda;

    @FXML
    private Button btnAgregarEmp, btnEditarEmp, btnEliminarEmp, btnRefresh;

    @FXML
    private Pane topPane; // Asegúrate de que tienes una referencia a tu AnchorPane principal
    private double xOffset =0;
    private double yOffset =0;
    @FXML
    protected void handleClickAction(MouseEvent event) {
        Stage stage = (Stage) topPane.getScene().getWindow();
        xOffset = stage.getX() + (stage.getWidth() / 2) - event.getX();
        yOffset = stage.getY() - event.getY();
    }

    @FXML
    protected void handleMovementAction(MouseEvent event) {
        Stage stage = (Stage) topPane.getScene().getWindow();
        stage.setX(event.getScreenX() + xOffset - (stage.getWidth() / 2));
        stage.setY(event.getScreenY() + yOffset);
    }

    public void initialize() {
        //configurarTabla();

        btnEditarEmp.setOnAction(this::btnEditarOnAction);
        btnAgregarEmp.setOnAction(this::btnAgregarOnAction);
        btnEliminarEmp.setOnAction(this::eliminardatos);
        btnRefresh.setOnAction(this::refrescar);
        configurarTabla();
        txtBusqueda.setOnKeyReleased(event -> {

                buscarDatos(txtBusqueda.getText());

        });
        cargarDatos();
    }
    private void configurarTabla() {
        // Configurar la tabla para permitir selección múltiple
        TableEmpleados.getSelectionModel().setCellSelectionEnabled(false);
        TableEmpleados.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
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
            stage.initStyle(StageStyle.UNDECORATED);
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
                stage.initStyle(StageStyle.UNDECORATED);
                stage.show();

                actualizarEmpleadosControlador.setTableEmpleados(TableEmpleados);
            } catch (IOException e) {
                // Manejo de excepciones
            }
        }
    }



    private void eliminardatos(javafx.event.ActionEvent actionEvent) {
        ObservableList<Empleados> empleadosSeleccionados = TableEmpleados.getSelectionModel().getSelectedItems();

        if (!empleadosSeleccionados.isEmpty()) {
            AlertDos alertDos = new AlertDos();

            // Mostrar una confirmación antes de eliminar utilizando tu clase AlertDos
            boolean confirmacion = alertDos.mostrarAlerta("¿Está seguro de que desea eliminar los empleados seleccionados?", "Confirmación");

            if (confirmacion) {
                // Obtener los IDs de los empleados seleccionados
                List<Integer> idsEmpleados = empleadosSeleccionados.stream().map(Empleados::getId).collect(Collectors.toList());

                // Eliminar los empleados de la base de datos y de la tabla
                idsEmpleados.forEach(this::eliminarEmpleado);

                // Eliminar los empleados directamente desde la tabla
                TableEmpleados.getItems().removeAll(empleadosSeleccionados);
            }
        } else {
        }
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





    private void eliminarEmpleado(int idEmpleado) {
        try (Connection connection = Conexion.obtenerConexion()) {
            String sql = "DELETE FROM tbempleados WHERE idempleado = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, idEmpleado);
                statement.executeUpdate();
            }
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



