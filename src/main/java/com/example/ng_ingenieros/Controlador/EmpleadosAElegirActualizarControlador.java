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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
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

    @FXML
    private TextField txtBusqueda;


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
        configurarTabla();

        btnAgregar.setOnAction(this::guardarEmpleadosSeleccionados);
        txtBusqueda.setOnKeyReleased(event -> {

            buscarDatos(txtBusqueda.getText());

        });
        cargarDatos();

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


    /*private void buscarDatos(String busqueda) {
        tbEmpleados.getItems().clear();

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT \n" +
                             "    e.idempleado,\n" +
                             "    e.nombreCompleto,\n" +
                             "    e.dui,\n" +
                             "    e.correo,\n" +
                             "    COALESCE(e.sueldo_dia, 0) AS sueldo_dia,\n" +
                             "    COALESCE(e.sueldo_horaExt, 0) AS sueldo_horaExt,\n" +
                             "    e.numero_cuentabancaria,\n" +
                             "    c.cargo\n" +
                             "FROM \n" +
                             "    tbempleados e\n" +
                             "INNER JOIN \n" +
                             "    tbcargos c ON e.idcargo = c.idcargo\n" +
                             "LEFT JOIN \n" +
                             "    tbEmpleadosProyectos ep ON e.idempleado = ep.idEmpleado\n" +
                             "LEFT JOIN \n" +
                             "    tbProyectos p ON ep.idProyecto = p.idproyecto\n" +
                             "LEFT JOIN \n" +
                             "    tbActividad a ON ep.idactividad = a.idactividad\n" +
                             "WHERE e.idempleado LIKE ? OR e.nombreCompleto LIKE ? OR e.dui LIKE ? OR e.correo LIKE ? OR e.sueldo_dia LIKE ? OR e.sueldo_horaExt LIKE ? OR e.numero_cuentabancaria LIKE ? OR c.cargo LIKE ? ")) {

            // Setear los parámetros para la búsqueda
            String parametroBusqueda = "%" + busqueda + "%";
            for (int i = 1; i <= 8; i++) {
                pstmt.setString(i, parametroBusqueda);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
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

                    tbEmpleados.getItems().add(new Empleados(id, nombre, dui, corre, sueldodia, sueldohora, cuenta, cargo));
                }

                tbEmpleados.refresh(); // Actualizar la vista
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    public void buscarDatos(String busqueda) {
        tbEmpleados.getItems().clear(); // Limpiar los elementos existentes

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement("SELECT \n" +
                     "    e.idempleado,\n" +
                     "    e.nombreCompleto,\n" +
                     "    e.dui,\n" +
                     "    e.correo,\n" +
                     "    COALESCE(e.sueldo_dia, 0) AS sueldo_dia,\n" +
                     "    COALESCE(e.sueldo_horaExt, 0) AS sueldo_horaExt,\n" +
                     "    e.numero_cuentabancaria,\n" +
                     "    c.cargo\n" +
                     "FROM \n" +
                     "    tbempleados e\n" +
                     "INNER JOIN \n" +
                     "    tbcargos c ON e.idcargo = c.idcargo\n" +
                     "LEFT JOIN \n" +
                     "    tbEmpleadosProyectos ep ON e.idempleado = ep.idEmpleado\n" +
                     "LEFT JOIN \n" +
                     "    tbProyectos p ON ep.idProyecto = p.idproyecto\n" +
                     "LEFT JOIN \n" +
                     "    tbActividad a ON ep.idactividad = a.idactividad\n" +
                     "WHERE\n" +
                     "    e.idcargo <> 7\n" +
                     "    AND (ep.idProyecto IS NULL OR p.idEstadoProyecto = 2 OR (p.idEstadoProyecto = 1 AND (a.idactividad IS NULL OR a.idactividad = 2)))" +
                     "    AND (e.nombreCompleto LIKE ? OR e.dui LIKE ? OR e.correo LIKE ?)")) {

            String parametroBusqueda = "%" + busqueda + "%";
            for (int i = 1; i <= 3; i++) {
                stmt.setString(i, parametroBusqueda);
            }

            ResultSet rs = stmt.executeQuery();
            // Agregar datos a la tabla
            while (rs.next()) {
                int id = rs.getInt("idempleado");
                String nombre = rs.getString("nombreCompleto");
                String dui = rs.getString("dui");
                String correo = rs.getString("correo");
                double sueldoDia = Double.parseDouble(rs.getString("sueldo_dia"));
                double sueldoHora = Double.parseDouble(rs.getString("sueldo_horaExt"));
                String cuentaBancaria = rs.getString("numero_cuentabancaria");
                String cargo = rs.getString("cargo");

                tbEmpleados.getItems().add(new Empleados(id, nombre, dui, correo, sueldoDia, sueldoHora, cuentaBancaria, cargo));
            }

            // Crear columnas dinámicamente si es necesario
            if (tbEmpleados.getColumns().isEmpty()) {
                ObservableList<TableColumn<Empleados, ?>> columnas = tbEmpleados.getColumns();

                // Agrega columnas necesarias para Empleados (ajusta según sea necesario)
                TableColumn<Empleados, Integer> colId = new TableColumn<>("ID");
                colId.setCellValueFactory(new PropertyValueFactory<>("id"));
                columnas.add(colId);

                TableColumn<Empleados, String> colNombre = new TableColumn<>("Nombre");
                colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
                columnas.add(colNombre);

                TableColumn<Empleados, String> colDui = new TableColumn<>("DUI");
                colDui.setCellValueFactory(new PropertyValueFactory<>("dui"));
                columnas.add(colDui);

                TableColumn<Empleados, String> colCorreo = new TableColumn<>("Correo");
                colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
                columnas.add(colCorreo);

                TableColumn<Empleados, Double> colSueldoDia = new TableColumn<>("Sueldo del día");
                colSueldoDia.setCellValueFactory(new PropertyValueFactory<>("sueldoDia"));
                columnas.add(colSueldoDia);

                TableColumn<Empleados, Double> colSueldoHora = new TableColumn<>("Sueldo por Hora Extra");
                colSueldoHora.setCellValueFactory(new PropertyValueFactory<>("sueldoHora"));
                columnas.add(colSueldoHora);

                TableColumn<Empleados, String> colCuentaBancaria = new TableColumn<>("Cuenta Bancaria");
                colCuentaBancaria.setCellValueFactory(new PropertyValueFactory<>("cuentaBancaria"));
                columnas.add(colCuentaBancaria);

                TableColumn<Empleados, String> colCargo = new TableColumn<>("Cargo");
                colCargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
                columnas.add(colCargo);
            }
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
