package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.AsistenciaVista;
import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.SalarioEmp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.*;


public class SalarioVistaControlador {


    @FXML
    private TableView tbMostrarSalario;
    @FXML
    private TextField txtBusqueda, txtNombreProyecto;

    @FXML
    private Button btnEliminar, btnGenerarPlanillaReporte, btnConsolidarPago, btnRefresh;
    @FXML
    private Label lblIdProyecto;

    private int idProyectoSeleccionado;

    public void recibirIdProyecto(int idProyecto) {
        idProyectoSeleccionado = idProyecto;
        // Llama a un método para cargar los empleados según el ID del proyecto
        cargarDatos();
    }

    @FXML
    private Button btnClose;

    @FXML
    private Pane topPane; // Asegúrate de que tienes una referencia a tu AnchorPane principal
    private double xOffset =0;
    private double yOffset =0;
    @FXML
    protected void handleClickAction(MouseEvent event) {
        Stage stage = (Stage) topPane.getScene().getWindow();
        xOffset = stage.getX() - event.getX();
        yOffset = stage.getY() - event.getY();
    }

    @FXML
    protected void handleMovementAction(MouseEvent event) {
        Stage stage = (Stage) topPane.getScene().getWindow();
        stage.setX(event.getScreenX() + xOffset);
        stage.setY(event.getScreenY() + yOffset);
    }

    @FXML
    protected void HandleCloseAction(ActionEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    public void initialize(AsistenciaVista empleadoSeleccionado)
    {

        lblIdProyecto.setText(String.valueOf(empleadoSeleccionado.getIdproyecto()));

        recibirIdProyecto(idProyectoSeleccionado);

        TableColumn<SalarioEmp, ?> columnaProyecto = (TableColumn<SalarioEmp, ?>) tbMostrarSalario.getColumns().get(8); // El índice 9 representa la columna del proyecto
        columnaProyecto.setVisible(false);

        tbMostrarSalario.setOnMouseClicked(event -> {
            SalarioEmp salarioSeleccionado = (SalarioEmp) tbMostrarSalario.getSelectionModel().getSelectedItem();
            if (salarioSeleccionado != null) {
                // Obtener el nombre del proyecto y establecerlo en el TextField
                String nombreProyecto = salarioSeleccionado.getNombreProyecto();
                txtNombreProyecto.setText(nombreProyecto);
            }
        });


        txtBusqueda.setOnKeyReleased(event -> {

            BuscarDatos(txtBusqueda.getText());

        });

        btnEliminar.setOnAction(actionEvent -> {
            try {
                btnEliminarOnAction(actionEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


        btnRefresh.setOnAction(actionEvent -> {
            try {
                btnRefreshOnAction(actionEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        btnGenerarPlanillaReporte.setOnAction(this::generarReporte);
        btnConsolidarPago.setOnAction(this::generarReportePagos);
    }

    private void generarReporte(ActionEvent actionEvent) {
        try {
            // Ruta del informe


            // Obtener el archivo de informe como flujo de entrada
            InputStream inputStream = getClass().getResourceAsStream("/ReportePlan.jasper");

            if (inputStream != null) {
                // Parámetros del informe
                String nombreProyecto = txtNombreProyecto.getText();
                java.util.Map<String, Object> parametros = new java.util.HashMap<>();
                parametros.put("nombreProyecto", nombreProyecto);


                // Llenar el informe y generar la vista
                JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros, Conexion.obtenerConexion());
                JasperViewer.viewReport(jasperPrint, false);
            } else {
                System.out.println("inputStream es igual a " + inputStream);
                System.out.println("No se pudo cargar el archivo del informe");
            }
        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    private void generarReportePagos(javafx.event.ActionEvent actionEvent) {
        try {
               // Ruta relativa al archivo .jasper
            InputStream inputStream = getClass().getResourceAsStream("/PagosConsolidados.jasper");

            if (inputStream != null) {
                JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, null, Conexion.obtenerConexion());
                JasperViewer.viewReport(jasperPrint, false);
            } else {
                System.out.println("inputStream es igual a " + inputStream);
                System.out.println("No se pudo cargar el archivo del informe");

            }
        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    private void btnEliminarOnAction(javafx.event.ActionEvent actionEvent) throws IOException {
        eliminarSalario();
    }

    private void btnRefreshOnAction(javafx.event.ActionEvent actionEvent) throws IOException {
        cargarDatos();
    }

    private void cargarDatos() {
        tbMostrarSalario.getItems().clear();
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement statement = conn.prepareStatement("\n" +
                     "select p.idplanilla, em.nombreCompleto, em.dui, em.numero_cuentabancaria, pro.idproyecto, pro.nombre_proyecto, p.diasRemunerados, p.horasExtTrabajadas, \n" +
                     "CAST(p.totalDevengado AS DECIMAL(10, 2)) as totalDevengado, \n" +
                     "CAST(p.AFP AS DECIMAL(10, 2)) as AFP, \n" +
                     "CAST(p.seguro_social AS DECIMAL(10, 2)) as seguro_social, \n" +
                     "CAST(p.descuento_Renta AS DECIMAL(10, 2)) as descuento_Renta, \n" +
                     "CAST(p.salarioFinal AS DECIMAL(10, 2)) as salarioFinal\n" +
                     "from tbplanillas p\n" +
                     "inner join tbEmpleadosProyectos emp on emp.idempleado = p.idempleado\n" +
                     "inner join tbempleados em on em.idempleado = emp.idEmpleado\n" +
                     "inner join tbProyectos pro on pro.idproyecto = emp.idProyecto\n" +
                     "where pro.idproyecto = ?\n" +
                     "order by em.idempleado ASC")) {

            // Establece el ID del proyecto en la consulta SQL
            statement.setInt(1, idProyectoSeleccionado);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("idplanilla");
                String nombreemp = rs.getString("nombreCompleto");
                String dui = rs.getString("dui");
                String cuenta = rs.getString("numero_cuentabancaria");
                int diasrem = rs.getInt("diasRemunerados");
                String horasextra = rs.getString("horasExtTrabajadas");
                float tot = rs.getFloat("totalDevengado");
                float afp = rs.getFloat("AFP");
                float seguro = rs.getFloat("seguro_social");
                float desc = rs.getFloat("descuento_Renta");
                float sal = rs.getFloat("salarioFinal");
                int idproyecto = rs.getInt("idproyecto");
                String nombrepory = rs.getString("nombre_proyecto");

                tbMostrarSalario.getItems().add(new SalarioEmp(id, nombreemp, dui, cuenta, diasrem, horasextra, tot, afp, seguro, desc, sal, idproyecto, nombrepory));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void BuscarDatos(String busqueda) {

        tbMostrarSalario.getItems().clear();

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement("select p.idplanilla, em.nombreCompleto,  em.dui, em.numero_cuentabancaria, pro.idproyecto, pro.nombre_proyecto, p.diasRemunerados, p.horasExtTrabajadas, \n" +
                     "CAST(p.totalDevengado AS DECIMAL(10, 2)) as totalDevengado, \n" +
                     "CAST(p.AFP AS DECIMAL(10, 2)) as AFP, \n" +
                     "CAST(p.seguro_social AS DECIMAL(10, 2)) as seguro_social, \n" +
                     "CAST(p.descuento_Renta AS DECIMAL(10, 2)) as descuento_Renta, \n" +
                     "CAST(p.salarioFinal AS DECIMAL(10, 2)) as salarioFinal\n" +
                     "from tbplanillas p\n" +
                     "inner join tbEmpleadosProyectos emp on emp.idempleado = p.idempleado\n" +
                     "inner join tbempleados em on em.idempleado = emp.idEmpleado\n" +
                     "inner join tbProyectos pro on pro.idproyecto = emp.idProyecto " +
                     "WHERE pro.idproyecto = ? " +
                     "AND(em.nombreCompleto LIKE ? OR p.diasRemunerados LIKE ? OR p.horasExtTrabajadas LIKE ? OR p.totalDevengado LIKE ? " +
                     "OR p.AFP LIKE ? OR p.seguro_social LIKE ? OR p.descuento_Renta LIKE ? OR p.salarioFinal LIKE ?) " +
                     "order by em.idempleado ASC")) {

            // Preparar el parámetro de búsqueda para la consulta SQL
            String parametroBusqueda = "%" + busqueda + "%";
            stmt.setInt(1, idProyectoSeleccionado);
            for (int i = 2; i <= 9; i++) {
                stmt.setString(i, parametroBusqueda);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("idplanilla");
                String nombreemp = rs.getString("nombreCompleto");
                String dui = rs.getString("dui");
                String cuenta = rs.getString("numero_cuentabancaria");
                int diasrem = rs.getInt("diasRemunerados");
                String horasextra = rs.getString("horasExtTrabajadas");
                float tot = rs.getFloat("totalDevengado");
                float afp = rs.getFloat("AFP");
                float seguro = rs.getFloat("seguro_social");
                float desc = rs.getFloat("descuento_Renta");
                float sal = rs.getFloat("salarioFinal");
                int idproyecto = rs.getInt("idproyecto");
                String nombrepory = rs.getString("nombre_proyecto");

                tbMostrarSalario.getItems().add(new SalarioEmp(id, nombreemp, dui, cuenta, diasrem, horasextra, tot, afp, seguro, desc, sal, idproyecto, nombrepory));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void eliminarSalario() {
        // Obtener el ID del proyecto seleccionado (asumiendo que tienes una variable para almacenar el ID)
        int idAsistencia = obtenerIdSalarioSeleccionado();

        try (Connection connection = Conexion.obtenerConexion()) {
            String sql = "DELETE FROM tbplanillas WHERE idplanilla = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idAsistencia);

            int filasAfectadas = statement.executeUpdate();
            if (filasAfectadas > 0) {
                mostrarAlerta("Eliminación de datos","Se eliminaron los datos exitosamente", Alert.AlertType.INFORMATION);

            } else {
                mostrarAlerta("Alerta","No hay ningun elemento seleccionado", Alert.AlertType.WARNING);
            }

            tbMostrarSalario.getItems().clear();
            cargarDatos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    private int obtenerIdSalarioSeleccionado() {
        SalarioEmp empleadoSeleccionado = (SalarioEmp) tbMostrarSalario.getSelectionModel().getSelectedItem();

        if (empleadoSeleccionado != null) {
            return empleadoSeleccionado.getIdSalario();
        } else {
            return -1; // Retorna un valor que indique que no se ha seleccionado ningún proyecto.
        }
    }


    public static void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();


    }

    public void setTableAsistencia(TableView<SalarioEmp> tbMostrarSalario) {
        this.tbMostrarSalario = tbMostrarSalario;
    }
}
