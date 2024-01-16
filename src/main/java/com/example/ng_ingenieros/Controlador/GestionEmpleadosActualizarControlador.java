package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.Empleados;
import com.example.ng_ingenieros.Proyecto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class GestionEmpleadosActualizarControlador {
    @FXML
    private TableView<Empleados> tbEmpleados;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnAgregar2;

    @FXML
    private Button btnRefresh;
    private int idProyecto;

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
        btnEliminar.setOnAction(this::eliminarEmpleado);
        btnCancelar.setOnAction(this::cerrarVentana);
        btnAgregar2.setOnAction(this::abrirVentanaEmpleadosAElegir);
        btnRefresh.setOnAction(this::refrescar);
// Permitir selección múltiple con SHIFT y CTRL
        tbEmpleados.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        cargarEmpleadosAsociados();
    }
    private void refrescar(javafx.event.ActionEvent actionEvent){
        cargarEmpleadosAsociados();
    }


    private void abrirVentanaEmpleadosAElegir(ActionEvent event) {
        try {
            // Cargar el archivo FXML de la ventana EmpleadosAElegirActualizar
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/EmpleadosAElegirActualizar.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la ventana EmpleadosAElegirActualizar
            EmpleadosAElegirActualizarControlador empleadosAElegirControlador = loader.getController();
            // Pasar el idProyecto al controlador de la segunda ventana
            empleadosAElegirControlador.setIdProyecto(idProyecto);

            // Crear una nueva escena y mostrar la ventana
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); // Hacer que la ventana sea modal
            stage.showAndWait(); // Mostrar la ventana y esperar hasta que se cierre

        } catch (Exception e) {
            e.printStackTrace();
            // Manejo de errores
        }
    }


    private void cerrarVentana(javafx.event.ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }


    private void cargarEmpleadosAsociados() {
        try (Connection conn = Conexion.obtenerConexion()) {
            String query = "SELECT \n" +
                    "    e.idempleado, \n" +
                    "    e.nombreCompleto, \n" +
                    "    e.dui, \n" +
                    "    e.correo, \n" +
                    "    e.sueldo_dia, \n" +
                    "    e.sueldo_horaExt, \n" +
                    "    e.numero_cuentabancaria,\n" +
                    "    c.cargo, \n" +
                    "    a.Actividad\n" +
                    "FROM \n" +
                    "    tbEmpleadosProyectos ep\n" +
                    "INNER JOIN \n" +
                    "    tbempleados e ON ep.idEmpleado = e.idempleado\n" +
                    "INNER JOIN \n" +
                    "    tbcargos c ON e.idcargo = c.idcargo\n" +
                    "INNER JOIN \n" +
                    "    tbActividad a ON ep.idactividad = a.idactividad\n" +
                    "LEFT JOIN \n" +
                    "    tbProyectos p ON ep.idProyecto = p.idproyecto\n" +
                    "WHERE \n" +
                    "    p.idproyecto = ? OR p.idproyecto IS NULL;";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setInt(1, idProyecto);
                System.out.println("Cargando empleados asociados al Proyecto con ID: " + idProyecto);

                try (ResultSet rs = preparedStatement.executeQuery()) {
                    // Limpiar la lista actual de empleados antes de agregar nuevos
                    tbEmpleados.getItems().clear();

                    // Recorrer los resultados y agregarlos directamente a la tabla
                    while (rs.next()) {
                        int id = rs.getInt("idempleado");
                        String nombre = rs.getString("nombreCompleto");
                        String dui = rs.getString("dui");
                        String correo = rs.getString("correo");
                        double sueldodia = Double.parseDouble(rs.getString("sueldo_dia"));
                        double sueldohora = Double.parseDouble(rs.getString("sueldo_horaExt"));
                        String cuenta = rs.getString("numero_cuentabancaria");
                        String cargo = rs.getString("cargo");
                        String actividad = rs.getString("Actividad");

                        tbEmpleados.getItems().add(new Empleados(id, nombre, dui, correo, sueldodia, sueldohora, cuenta, cargo, actividad));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de errores
        }
    }

    public void eliminarEmpleado(ActionEvent event) {
        ObservableList<Empleados> empleadosSeleccionados = tbEmpleados.getSelectionModel().getSelectedItems();

        if (empleadosSeleccionados.isEmpty()) {
            System.out.println("Ningún empleado seleccionado.");
            return;
        }

        // Mostrar una confirmación antes de eliminar
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("Eliminar Empleados");
        alert.setContentText("¿Está seguro de que desea eliminar los empleados seleccionados?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            empleadosSeleccionados.forEach(empleado -> eliminarRelacionEmpleadoProyecto(empleado.getId(), idProyecto));
            cargarEmpleadosAsociados();
        }
    }

    private void eliminarRelacionEmpleadoProyecto(int idEmpleado, int idProyecto) {
        try (Connection conn = Conexion.obtenerConexion()) {
            String query = "DELETE FROM tbEmpleadosProyectos WHERE idEmpleado = ? AND idProyecto = ?;";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setInt(1, idEmpleado);
                preparedStatement.setInt(2, idProyecto);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de errores
        }
    }

}

