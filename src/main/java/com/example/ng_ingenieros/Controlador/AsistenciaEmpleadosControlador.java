package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.Empleados;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class AsistenciaEmpleadosControlador {

    public void initialize()
    {
        cargarDatos();
        agregarColumnasCheckbox();
    }

    @FXML
    private TableView TbAsistencia;

    private void cargarDatos() {
        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select emp.idempleado, emp.nombreCompleto from tbempleados emp")) {

            while (rs.next()) {
                int id = rs.getInt("idempleado");
                String nombre = rs.getString("nombreCompleto");




                TbAsistencia.getItems().add(new Empleados(id, nombre));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void agregarColumnasCheckbox() {
        // Crear las columnas de checkboxes y agregarlas a la tabla

        String[] nombresColumnas = { "Lunes", "Martes", "Miércoles", "Jueves", "Viernes" };

        // Crear las columnas de checkboxes y agregarlas a la tabla
        for (int i = 0; i < 5; i++) {
            TableColumn<Empleados, Boolean> checkBoxColumn = new TableColumn<>(nombresColumnas[i]);

            switch (i) {
                case 0:
                    checkBoxColumn.setCellValueFactory(cellData -> cellData.getValue().getCheckBox1Property());
                    break;
                case 1:
                    checkBoxColumn.setCellValueFactory(cellData -> cellData.getValue().getCheckBox2Property());
                    break;
                case 2:
                    checkBoxColumn.setCellValueFactory(cellData -> cellData.getValue().getCheckBox3Property());
                    break;
                case 3:
                    checkBoxColumn.setCellValueFactory(cellData -> cellData.getValue().getCheckBox4Property());
                    break;
                case 4:
                    checkBoxColumn.setCellValueFactory(cellData -> cellData.getValue().getCheckBox5Property());
                    break;
                default:
                    break;
            }

            checkBoxColumn.setCellFactory(column -> new CheckBoxTableCell<>());

            checkBoxColumn.setPrefWidth(150);

            TbAsistencia.getColumns().add(checkBoxColumn);
        }
    }
}
