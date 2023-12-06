package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.Empleados;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class EmpleadosAElegirControlador {
    @FXML
    private TableView<Empleados> tbEmpleados;

    public void initialize() {


        cargarDatos();
    }

    private void cargarDatos() {
        tbEmpleados.getItems().clear();

        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT idempleado, nombreCompleto FROM tbempleados WHERE idempleado NOT IN (SELECT idempleado FROM tbEmpleadosProyectos WHERE idProyecto IS NOT NULL) OR idempleado IN (SELECT idempleado FROM tbEmpleadosProyectos ep INNER JOIN tbProyectos p ON ep.idProyecto = p.idproyecto WHERE p.idEstadoProyecto = 2)")) {

            // Crear columnas dinámicamente
            ObservableList<TableColumn<Empleados, ?>> columnas = tbEmpleados.getColumns();
            columnas.clear();

            // Columna para el ID
            TableColumn<Empleados, Integer> colId = new TableColumn<>("ID");
            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            columnas.add(colId);

            // Columna para el nombre
            TableColumn<Empleados, String> colNombre = new TableColumn<>("Nombre");
            colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            columnas.add(colNombre);

            // Columna para la selección
            TableColumn<Empleados, Boolean> colSeleccion = new TableColumn<>("Select");
            colSeleccion.setCellValueFactory(new PropertyValueFactory<>("seleccionado"));
            colSeleccion.setCellFactory(CheckBoxTableCell.forTableColumn(colSeleccion));
            columnas.add(colSeleccion);

            while (rs.next()) {
                int id = rs.getInt("idempleado");
                String nombre = rs.getString("nombreCompleto");
                boolean seleccionado = false;  // Puedes obtener este valor de la base de datos si es relevante para tu aplicación.

                tbEmpleados.getItems().add(new Empleados(id, nombre, seleccionado));
            }

            tbEmpleados.refresh(); // Actualizar la vista
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void cerrarVentana(javafx.event.ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
