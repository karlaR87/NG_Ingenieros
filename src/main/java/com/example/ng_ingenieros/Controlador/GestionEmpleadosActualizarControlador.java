package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.Empleados;
import com.example.ng_ingenieros.Proyecto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GestionEmpleadosActualizarControlador {
    @FXML
    private TableView<Empleados> tbEmpleados;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnCancelar;


    private int idProyecto;

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
        btnEliminar.setOnAction(this::eliminarEmpleado);
        btnCancelar.setOnAction(this::cerrarVentana);

        cargarEmpleadosAsociados();
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
                    "    tbActividad a ON e.idactividad = a.idactividad\n" +
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
                        String corre = rs.getString("correo");
                        double sueldodia = Double.parseDouble(rs.getString("sueldo_dia"));
                        double sueldohora = Double.parseDouble(rs.getString("sueldo_horaExt"));
                        String cuenta = rs.getString("numero_cuentabancaria");
                        String cargo = rs.getString("cargo");
                        String Actividad = rs.getString("Actividad");



                        tbEmpleados.getItems().add(new Empleados(id, nombre, dui, corre,sueldodia, sueldohora, cuenta, cargo, Actividad));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de errores
        }
    }
    public void eliminarEmpleado(ActionEvent event) {
        Empleados empleadoSeleccionado = tbEmpleados.getSelectionModel().getSelectedItem();

        if (empleadoSeleccionado != null) {
            // Obtener el ID del empleado y del proyecto
            int idEmpleado = empleadoSeleccionado.getId();
            int idProyecto = this.idProyecto;

            // Lógica para eliminar el empleado de la relación con el proyecto y actualizar idActividad a 2
            eliminarEmpleadoDeProyecto(idEmpleado, idProyecto);

            // Volver a cargar los empleados asociados después de la eliminación
            cargarEmpleadosAsociados();
        }
    }

    private void eliminarEmpleadoDeProyecto(int idEmpleado, int idProyecto) {
        try (Connection conn = Conexion.obtenerConexion()) {
            // Obtener el idActividad actual del empleado
            int idActividadActual = obtenerIdActividadActual(idEmpleado);

            // Si el idActividadActual es diferente de 2 (inactivo), actualizarlo a 2
            if (idActividadActual != 2) {
                actualizarIdActividadEmpleado(idEmpleado, 2);
            }

            // Eliminar la relación entre el empleado y el proyecto
            String query = "DELETE FROM tbEmpleadosProyectos WHERE idEmpleado = ? AND idProyecto = ?;";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setInt(1, idEmpleado);
                preparedStatement.setInt(2, idProyecto);

                // Ejecutar la sentencia de eliminación
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de errores
        }
    }

    private int obtenerIdActividadActual(int idEmpleado) {
        int idActividadActual = -1;

        try (Connection conn = Conexion.obtenerConexion()) {
            String query = "SELECT idActividad FROM tbempleados WHERE idempleado = ?;";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setInt(1, idEmpleado);

                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        idActividadActual = rs.getInt("idActividad");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de errores
        }

        return idActividadActual;
    }

    private void actualizarIdActividadEmpleado(int idEmpleado, int nuevoIdActividad) {
        try (Connection conn = Conexion.obtenerConexion()) {
            String query = "UPDATE tbempleados SET idActividad = ? WHERE idempleado = ?;";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setInt(1, nuevoIdActividad);
                preparedStatement.setInt(2, idEmpleado);

                // Ejecutar la sentencia de actualización
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de errores
        }
    }

}

