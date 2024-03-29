package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.CustomAlert;
import com.example.ng_ingenieros.Empleados;
import javafx.collections.FXCollections;
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

public class EmpleadosAElegirControlador {
    @FXML
    private TableView<Empleados> tbEmpleados;

    @FXML
    private Button btnAgregar; // Botón para guardar empleados

    @FXML
    private TextField txtBusqueda;

    private List<Empleados> empleadosSeleccionados; // Lista para almacenar empleados seleccionados


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
        cargarDatos();
        empleadosSeleccionados = new ArrayList<>(); // Inicializar la lista de empleados seleccionados
        btnAgregar.setOnAction(this::guardarEmpleadosSeleccionados);
        txtBusqueda.setOnKeyReleased(event -> {

            buscarDatos(txtBusqueda.getText());

        });

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
                     "    (ep.idProyecto IS NULL OR p.idEstadoProyecto = 2 OR (p.idEstadoProyecto = 1 AND (a.idactividad IS NULL OR a.idactividad = 2)));")) {


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

    /*public void buscarDatos(String busqueda) {
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
                     "WHERE e.idempleado LIKE ? OR e.nombreCompleto LIKE ? OR e.dui LIKE ? OR e.correo LIKE ? OR e.sueldo_dia LIKE ? OR e.sueldo_horaExt LIKE ? OR e.numero_cuentabancaria LIKE ? OR c.cargo LIKE ? "

                     )) {

            String parametroBusqueda = "%" + busqueda + "%";
            for (int i = 1; i <= 8; i++) {
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




    private EmpleadosAsignadosControlador empleadosAsignadosControlador;

    public List<Empleados> getEmpleadosAElegir() {
        return empleadosSeleccionados;
    }

    private boolean validarIngenierosCargo7(List<Empleados> empleados) {
        // Construir una cadena de IDs de empleados seleccionados para la consulta SQL
        StringBuilder idEmpleados = new StringBuilder();
        for (Empleados empleado : empleados) {
            idEmpleados.append(empleado.getId()).append(",");
        }
        idEmpleados.deleteCharAt(idEmpleados.length() - 1); // Eliminar la última coma

        // Consultar la base de datos para contar la cantidad de ingenieros con idcargo 7 en la selección
        String consultaSQL = "SELECT COUNT(*) AS conteo FROM tbempleados WHERE idempleado IN (" + idEmpleados + ") AND idcargo = 7";

        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(consultaSQL)) {

            if (rs.next()) {
                int conteoIngenierosCargo7 = rs.getInt("conteo");
                return conteoIngenierosCargo7 <= 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false; // En caso de error, considerarlo como que no se cumple la validación
    }


    @FXML
    private void guardarEmpleadosSeleccionados(javafx.event.ActionEvent actionEvent) {
        empleadosSeleccionados.clear();

        ObservableList<Empleados> seleccionados = tbEmpleados.getSelectionModel().getSelectedItems();

        // Validar la cantidad de ingenieros con idcargo 7
        if (!validarIngenierosCargo7(seleccionados)) {
            CustomAlert customAlert = new CustomAlert();
            customAlert.mostrarAlertaPersonalizada("Error", "Solo se permite agregar un ingeniero con ID de cargo 7.", (Stage) btnAgregar.getScene().getWindow());
            return;
        }

        empleadosSeleccionados.addAll(seleccionados);

        // Resto del código...

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
