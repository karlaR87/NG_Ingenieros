package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.Empleados;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class AsistenciaEmpleadosControlador {

    public void initialize()
    {
        cargarDatos();
        configurarSeleccionCheckBox();
        //agregarColumnasCheckbox();
        //agregarColumnasTextField();
        configurarEdicionTextField();
    }

    @FXML
    private TableView TbAsistencia;

    private void configurarEdicionTextField() {
        // Permitir la edición de las celdas de TextField
        TableColumn<Empleados, String> textFieldColumn1 = new TableColumn<>("Hora de entrada");
        textFieldColumn1.setCellValueFactory(cellData -> cellData.getValue().textField1Property());
        textFieldColumn1.setCellFactory(TextFieldTableCell.forTableColumn());
        textFieldColumn1.setOnEditCommit(event -> {
            Empleados empleado = event.getTableView().getItems().get(event.getTablePosition().getRow());
            empleado.setTextField1(event.getNewValue());
        });

        TableColumn<Empleados, String> textFieldColumn2 = new TableColumn<>("Hora de salida");
        textFieldColumn2.setCellValueFactory(cellData -> cellData.getValue().textField2Property());
        textFieldColumn2.setCellFactory(TextFieldTableCell.forTableColumn());
        textFieldColumn2.setOnEditCommit(event -> {
            Empleados empleado = event.getTableView().getItems().get(event.getTablePosition().getRow());
            empleado.setTextField2(event.getNewValue());
        });

        TbAsistencia.getColumns().addAll(textFieldColumn1, textFieldColumn2);
    }

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

    private void configurarSeleccionCheckBox() {
        // Permitir la selección de las celdas de CheckBox
        TableColumn<Empleados, Boolean> checkBoxColumn1 = new TableColumn<>("Asistió");
        checkBoxColumn1.setCellValueFactory(cellData -> cellData.getValue().getCheckBox1Property());
        checkBoxColumn1.setCellFactory(column -> new CheckBoxTableCell<>());


        TbAsistencia.getColumns().addAll(checkBoxColumn1);
    }

    /*private void agregarColumnasCheckbox() {
        // Crear las columnas de checkboxes y agregarlas a la tabla

        String[] nombresColumnas = { "Asistió" };

        // Crear las columnas de checkboxes y agregarlas a la tabla
        for (int i = 0; i < 1; i++) {
            TableColumn<Empleados, Boolean> checkBoxColumn = new TableColumn<>(nombresColumnas[i]);

            switch (i) {
                case 0:
                    checkBoxColumn.setCellValueFactory(cellData -> cellData.getValue().getCheckBox1Property());
                    break;

                default:
                    break;
            }

            checkBoxColumn.setCellFactory(column -> new CheckBoxTableCell<>());

            checkBoxColumn.setPrefWidth(150);

            TbAsistencia.getColumns().add(checkBoxColumn);
        }
    }
    private void agregarColumnasTextField() {
        // Crear las columnas de TextField y agregarlas a la tabla
        String[] nombresColumnasTextField = { "TextField1", "TextField2" };

        for (String nombreColumna : nombresColumnasTextField) {
            TableColumn<Empleados, String> textFieldColumn = new TableColumn<>(nombreColumna);

            // Aquí debes asignar la propiedad adecuada de Empleados según tu lógica
            // Supongamos que tienes dos propiedades en la clase Empleados llamadas textField1Property y textField2Property
            // Asigna las propiedades de la clase Empleados a estas columnas
            switch (nombreColumna) {
                case "TextField1":
                    textFieldColumn.setCellValueFactory(cellData -> cellData.getValue().textField1Property());
                    break;
                case "TextField2":
                    textFieldColumn.setCellValueFactory(cellData -> cellData.getValue().textField2Property());
                    break;
                default:
                    break;
            }

            textFieldColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            textFieldColumn.setPrefWidth(100);
            TbAsistencia.getColumns().add(textFieldColumn);
        }
    }*/
}
