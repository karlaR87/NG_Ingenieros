package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Asistencia;
import com.example.ng_ingenieros.AsistenciaVista;
import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.Empleados;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class AsistenciaDatosControlador {

    @FXML
    private TableView TBMostrarAsistencia;
    @FXML
    private TextField txtBusqueda;

    public void initialize()
    {
        cargarDatos();
        txtBusqueda.setOnKeyReleased(event -> {

            buscarDatos(txtBusqueda.getText());

        });
    }

    private void cargarDatos() {
        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select a.idAsistencia, emp.nombreCompleto, asis.asistencia, a.hora_entrada, a.hora_salida from tbAsistencia a\n" +
                     "inner join tbempleados emp on emp.idempleado = a.idempleado\n" +
                     "inner join tbAsistenciaMarcar asis on asis.idAsistenciaMarcar = a.idAsistenciaMarcar")) {

            while (rs.next()) {
                int id = rs.getInt("idAsistencia");
                String nombre = rs.getString("nombreCompleto");
                String asistencia = rs.getString("asistencia");
                String fechaentrada = rs.getString("hora_entrada");
                String fechasalida = rs.getString("hora_salida");

                TBMostrarAsistencia.getItems().add(new AsistenciaVista(id, nombre, asistencia, fechaentrada, fechasalida));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buscarDatos(String busqueda) {
        TBMostrarAsistencia.getItems().clear(); // Limpiar los elementos actuales de la tabla

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement("select a.idAsistencia, emp.nombreCompleto, asis.asistencia, a.hora_entrada, a.hora_salida from tbAsistencia a\n" +
                     "inner join tbempleados emp on emp.idempleado = a.idempleado\n" +
                     "inner join tbAsistenciaMarcar asis on asis.idAsistenciaMarcar = a.idAsistenciaMarcar\n" +
                     "where emp.nombreCompleto LIKE ?")) {

            // Preparar el parámetro de búsqueda para la consulta SQL
            String parametroBusqueda = "%" + busqueda + "%";
            stmt.setString(1, parametroBusqueda);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Obtener los datos y agregarlos a la tabla
                int id = rs.getInt("idAsistencia");
                String nombre = rs.getString("nombreCompleto");
                String asistencia = rs.getString("asistencia");
                String fechaentrada = rs.getString("hora_entrada");
                String fechasalida = rs.getString("hora_salida");

                TBMostrarAsistencia.getItems().add(new AsistenciaVista(id, nombre, asistencia, fechaentrada, fechasalida));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
