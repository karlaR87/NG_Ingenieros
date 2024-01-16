package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.Empleados;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmpleadosAElegirActualizarControlador {
    @FXML
    private TableView<Empleados> tbEmpleados;

    @FXML
    private Button btnAgregar; // Botón para guardar empleados


    public void initialize() {
        configurarTabla();
        cargarDatos();
        btnAgregar.setOnAction(this::guardarEmpleadosSeleccionados);


    }
    private int idProyecto;

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
        System.out.println("id: " + idProyecto);

    }

    private void configurarTabla() {
        // Configurar la tabla para permitir selección múltiple
        tbEmpleados.getSelectionModel().setCellSelectionEnabled(false);
        tbEmpleados.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
    }

    private void cargarDatos() {
        tbEmpleados.getItems().clear();

        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT \n" +
                     "                         e.idempleado,\n" +
                     "                         e.nombreCompleto,\n" +
                     "                         e.dui,\n" +
                     "                         e.correo,\n" +
                     "                         COALESCE(e.sueldo_dia, 0) AS sueldo_dia,\n" +
                     "                         COALESCE(e.sueldo_horaExt, 0) AS sueldo_horaExt,\n" +
                     "                         e.numero_cuentabancaria,\n" +
                     "                         c.cargo\n" +
                     "                     FROM \n" +
                     "                         tbempleados e\n" +
                     "                     INNER JOIN \n" +
                     "                         tbcargos c ON e.idcargo = c.idcargo\n" +
                     "                     LEFT JOIN \n" +
                     "                         tbEmpleadosProyectos ep ON e.idempleado = ep.idEmpleado\n" +
                     "                     LEFT JOIN \n" +
                     "                         tbProyectos p ON ep.idProyecto = p.idproyecto\n" +
                     "                     LEFT JOIN \n" +
                     "                         tbActividad a ON ep.idactividad = a.idactividad\n" +
                     "                     WHERE\n" +
                     "                         e.idcargo <> 7\n" +
                     "                         AND (ep.idProyecto IS NULL OR p.idEstadoProyecto = 2 OR (p.idEstadoProyecto = 1 AND (a.idactividad IS NULL OR a.idactividad = 2)));")) {

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

            // Columna para el DUI
            TableColumn<Empleados, String> colDui = new TableColumn<>("DUI");
            colDui.setCellValueFactory(new PropertyValueFactory<>("dui"));
            columnas.add(colDui);

            // Columna para el correo
            TableColumn<Empleados, String> colCorreo = new TableColumn<>("Correp");
            colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
            columnas.add(colCorreo);

            // Columna para el sueldo dia
            TableColumn<Empleados, String> colSueldoDia = new TableColumn<>("Sueldo del dia");
            colSueldoDia.setCellValueFactory(new PropertyValueFactory<>("sueldoDia"));
            columnas.add(colSueldoDia);

            // Columna para el sueldo hora
            TableColumn<Empleados, String> ColSueldoHora = new TableColumn<>("Sueldo por Hora extra");
            ColSueldoHora.setCellValueFactory(new PropertyValueFactory<>("sueldoHora"));
            columnas.add(ColSueldoHora);

            // Columna para la cuenta
            TableColumn<Empleados, String> Colcuenta = new TableColumn<>("Cuenta Bancaria");
            Colcuenta.setCellValueFactory(new PropertyValueFactory<>("cuentaBancaria"));
            columnas.add(Colcuenta);

            // Columna para el cargo
            TableColumn<Empleados, String> colCargo = new TableColumn<>("Cargo");
            colCargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
            columnas.add(colCargo);

            while (rs.next()) {
                int id = rs.getInt("idempleado");
                String nombre = rs.getString("nombreCompleto");
                String dui = rs.getString("dui");
                String corre = rs.getString("correo");
                double sueldodia = Double.parseDouble(rs.getString("sueldo_dia"));
                double sueldohora = Double.parseDouble(rs.getString("sueldo_horaExt"));
                String cuenta = rs.getString("numero_cuentabancaria");
                String cargo = rs.getString("cargo");


                tbEmpleados.getItems().add(new Empleados(id, nombre, dui, corre, sueldodia, sueldohora, cuenta,cargo));
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

    private void guardarEmpleadosSeleccionados(javafx.event.ActionEvent actionEvent) {
        // Obtener la lista de empleados seleccionados
        ObservableList<Empleados> empleadosSeleccionados = tbEmpleados.getSelectionModel().getSelectedItems();

        // Obtener los IDs de los empleados seleccionados
        List<Integer> idsEmpleadosSeleccionados = new ArrayList<>();
        for (Empleados empleado : empleadosSeleccionados) {
            idsEmpleadosSeleccionados.add(empleado.getId());
        }

        // Llamar al método en el controlador principal para asociar empleados al proyecto
        asociarEmpleadosAProyecto(idsEmpleadosSeleccionados);

        // Cerrar la ventana actual
        cerrarVentana(actionEvent);
    }

    private void asociarEmpleadosAProyecto(List<Integer> idsEmpleados) {
        if (idsEmpleados.isEmpty()) {
            System.out.println("Ningún empleado seleccionado.");
            return;
        }

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement pstmtInsert = conn.prepareStatement("INSERT INTO tbEmpleadosProyectos (idEmpleado, idProyecto, idActividad) VALUES (?, ?, 1)")) {

            int idProyecto = this.idProyecto;

            // Insertar registros en tbEmpleadosProyectos para asociar empleados al proyecto
            for (int idEmpleado : idsEmpleados) {
                // Asociar empleado al proyecto con idActividad = 1 (activo)
                pstmtInsert.setInt(1, idEmpleado);
                pstmtInsert.setInt(2, idProyecto);
                pstmtInsert.executeUpdate();
            }

            System.out.println("Empleados asociados al proyecto con ID: " + idProyecto);

            // Actualizar la tabla en el hilo de la interfaz gráfica
            Platform.runLater(() -> cargarDatos());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }









}
