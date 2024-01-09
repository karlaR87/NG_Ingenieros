package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.Empleados;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmpleadosAElegirControlador {
    @FXML
    private TableView<Empleados> tbEmpleados;

    @FXML
    private Button btnAgregar; // Botón para guardar empleados

    private List<Empleados> empleadosSeleccionados; // Lista para almacenar empleados seleccionados

    public void initialize() {
        configurarTabla();
        cargarDatos();
        empleadosSeleccionados = new ArrayList<>(); // Inicializar la lista de empleados seleccionados
        btnAgregar.setOnAction(this::guardarEmpleadosSeleccionados);

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
             ResultSet rs = stmt.executeQuery("  SELECT e.idempleado, e.nombreCompleto, e.dui, e.correo, e.sueldo_dia, e.sueldo_horaExt, e.numero_cuentabancaria, e.idcargo FROM tbempleados e WHERE e.idempleado NOT IN (SELECT idempleado FROM tbEmpleadosProyectos WHERE idProyecto IS NOT NULL) OR (e.idempleado IN (SELECT ep.idempleado FROM tbEmpleadosProyectos ep INNER JOIN tbProyectos p ON ep.idProyecto = p.idproyecto WHERE p.idEstadoProyecto = 2) OR e.idactividad = 2);")) {

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
                String cargo = rs.getString("idcargo");


                tbEmpleados.getItems().add(new Empleados(id, nombre, dui, corre, sueldodia, sueldohora, cuenta,cargo));
            }

            tbEmpleados.refresh(); // Actualizar la vista
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private EmpleadosAsignadosControlador empleadosAsignadosControlador;

    public List<Empleados> getEmpleadosAElegir() {
        return empleadosSeleccionados;
    }
    private void guardarEmpleadosSeleccionados(javafx.event.ActionEvent actionEvent) {
        empleadosSeleccionados.clear();

        ObservableList<Empleados> seleccionados = tbEmpleados.getSelectionModel().getSelectedItems();
        empleadosSeleccionados.addAll(seleccionados);

        // Imprimir mensajes de depuración
        System.out.println("Empleados a elegir seleccionados:");
        for (Empleados empleado : empleadosSeleccionados) {
            System.out.println("ID: " + empleado.getId() + ", Nombre: " + empleado.getNombre());
        }

        // Mandar datos a la ventana principal
        if (empleadosAsignadosControlador != null) {
            empleadosAsignadosControlador.setEmpleadoselect(empleadosSeleccionados);


            System.out.println("Información enviada a EmpleadosAsignadosControlador:");
            for (Empleados empleado : empleadosSeleccionados) {
                System.out.println("ID: " + empleado.getId() + ", Nombre: " + empleado.getNombre());
            }


        }
        System.out.println("Cerrando ventana EmpleadosAElegir.");

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }


    @FXML
    private void cerrarVentana(javafx.event.ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
