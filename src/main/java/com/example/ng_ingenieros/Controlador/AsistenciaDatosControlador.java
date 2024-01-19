package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.AsistenciaVista;
import com.example.ng_ingenieros.Conexion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.*;

public class AsistenciaDatosControlador {

    @FXML
    public TableView TBMostrarAsistencia;
    @FXML
    private TextField txtBusqueda;
    @FXML
    private Button btnActualizar;
    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnRegistrarSalarios, btnVerSalarios, btnRefresh;

    private int idProyectoSeleccionado;

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

    public void recibirIdProyecto(int idProyecto) {
        idProyectoSeleccionado = idProyecto;
        // Llama a un método para cargar los empleados según el ID del proyecto
        cargarDatos();
    }


    public void initialize()
    {
        recibirIdProyecto(idProyectoSeleccionado);

        txtBusqueda.setOnKeyReleased(event -> {

            buscarDatos(txtBusqueda.getText());

        });
        btnActualizar.setOnAction(actionEvent -> {
            try {
                btnActualizarOnAction(actionEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        btnEliminar.setOnAction(actionEvent -> {
            try {
                btnEliminarOnAction(actionEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        btnRegistrarSalarios.setOnAction(actionEvent -> {
            try {
                btnMostrarSalarioOnAction(actionEvent);
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

        btnVerSalarios.setOnAction(actionEvent -> {
            try {
                mostrarVentanaSalarioOnAction(actionEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void setTableAsistencia(TableView<AsistenciaVista> TBMostrarAsistencia) {
        this.TBMostrarAsistencia = TBMostrarAsistencia;
    }
    private void mostrarVentanaSalarioOnAction(javafx.event.ActionEvent actionEvent) throws IOException {
        mostrarVentanaSalario();
    }

    private void btnEliminarOnAction(javafx.event.ActionEvent actionEvent) throws IOException{
        eliminarAsistencia();
    }

    private void btnActualizarOnAction(javafx.event.ActionEvent actionEvent) throws IOException {
        abrirVentanaActualizar();
    }

    private void btnRefreshOnAction(javafx.event.ActionEvent actionEvent) throws IOException {
        cargarDatos();
    }

    private void btnMostrarSalarioOnAction(javafx.event.ActionEvent actionEvent) throws IOException {
        abrirVentanaSalario();
    }

    public void abrirVentanaActualizar() {
        // Obtener la fila seleccionada
        AsistenciaVista empleadoSeleccionado = (AsistenciaVista) TBMostrarAsistencia.getSelectionModel().getSelectedItem();

        if (empleadoSeleccionado != null) {
            // Crear y mostrar la ventana de actualización con los datos de la fila seleccionada
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/Asistencia_actualizar.fxml"));
            Parent root;
            try {

                root = loader.load();
                // Obtener el controlador de la ventana de actualización
                AsistenciaActualizarControlador asistenciaActualizarControlador = loader.getController();

                asistenciaActualizarControlador.setTableAsistencia(TBMostrarAsistencia);
                // Pasar la referencia de TableAsistencia al controlador de la ventana de actualización
                asistenciaActualizarControlador.initialize(empleadoSeleccionado);


                // Mostrar la ventana de actualización
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.initStyle(StageStyle.UNDECORATED);
                stage.show();


            } catch (IOException e) {
                // Manejo de excepciones
            }
        }
    }

    public void abrirVentanaSalario() {
        // Obtener la fila seleccionada
        AsistenciaVista empleadoSeleccionado = (AsistenciaVista) TBMostrarAsistencia.getSelectionModel().getSelectedItem();

        if (empleadoSeleccionado != null) {
            // Crear y mostrar la ventana de actualización con los datos de la fila seleccionada
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/Salarioemp_registrar.fxml"));
            Parent root;
            try {

                root = loader.load();
                // Obtener el controlador de la ventana de actualización
                SalarioEmpleadoControlador asistenciaActualizarControlador = loader.getController();

                asistenciaActualizarControlador.setTableAsistencia(TBMostrarAsistencia);
                asistenciaActualizarControlador.initialize(empleadoSeleccionado);


                // Mostrar la ventana de actualización
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.initStyle(StageStyle.UNDECORATED);
                stage.show();


            } catch (IOException e) {
                // Manejo de excepciones
            }
        }
    }

    public void mostrarVentanaSalario() throws IOException {
        AsistenciaVista empleadoSeleccionado = (AsistenciaVista) TBMostrarAsistencia.getSelectionModel().getSelectedItem();

        if (empleadoSeleccionado != null) {
            // Crear y mostrar la ventana de actualización con los datos de la fila seleccionada
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/SalarioEmp_vista.fxml"));
            Parent root;
            try {

                root = loader.load();
                // Obtener el controlador de la ventana de actualización
                SalarioVistaControlador asistenciaActualizarControlador = loader.getController();
                asistenciaActualizarControlador.recibirIdProyecto(idProyectoSeleccionado);

                // Pasar la referencia de TableAsistencia al controlador de la ventana de actualización
                asistenciaActualizarControlador.initialize(empleadoSeleccionado);


                // Mostrar la ventana de actualización
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.initStyle(StageStyle.UNDECORATED);
                stage.show();


            } catch (IOException e) {
                // Manejo de excepciones
            }
        }


    }

    private void eliminarAsistencia() {
        // Obtener el ID del proyecto seleccionado (asumiendo que tienes una variable para almacenar el ID)
        int idAsistencia = obtenerIdAsistenciaSeleccionada();

        try (Connection connection = Conexion.obtenerConexion()) {
            String sql = "DELETE FROM tbAsistencia WHERE idAsistencia = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idAsistencia);

            int filasAfectadas = statement.executeUpdate();
            if (filasAfectadas > 0) {
                mostrarAlerta("Eliminación de datos","Se eliminaron los datos exitosamente", Alert.AlertType.INFORMATION);

            } else {
                mostrarAlerta("Alerta","No hay ningun elemento seleccionado", Alert.AlertType.WARNING);
            }
            TBMostrarAsistencia.getItems().clear();
            cargarDatos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    private int obtenerIdAsistenciaSeleccionada() {
        AsistenciaVista empleadoSeleccionado = (AsistenciaVista) TBMostrarAsistencia.getSelectionModel().getSelectedItem();

        if (empleadoSeleccionado != null) {
            return empleadoSeleccionado.getId();
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



    public void cargarDatos() {
        TBMostrarAsistencia.getItems().clear();
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement statement = conn.prepareStatement("select aa.idAsistencia, empleado.idempleado, empleado.nombreCompleto, asia.asistencia, aa.hora_entrada, aa.hora_salida, aa.turno, pro.idproyecto, pro.nombre_proyecto from tbAsistencia aa\n" +
                     "inner join tbEmpleadosProyectos id on id.idEmpleado = aa.idempleado\n" +
                     "inner join tbempleados empleado on empleado.idempleado = id.idEmpleado\n" +
                     "inner join tbProyectos pro on pro.idproyecto = id.idProyecto\n" +
                     "inner join tbAsistencia asis on asis.idAsistencia = aa.idAsistencia\n" +
                     "inner join tbAsistenciaMarcar asia on asia.idAsistenciaMarcar = asis.idAsistenciaMarcar " +
                     "WHERE pro.idproyecto = ? order by empleado.idempleado")) {

            // Establece el ID del proyecto en la consulta SQL
            statement.setInt(1, idProyectoSeleccionado);

            ResultSet rs = statement.executeQuery();



            while (rs.next()) {
                int id = rs.getInt("idAsistencia");
                int ide = rs.getInt("idempleado");
                String nombre = rs.getString("nombreCompleto");
                String asistencia = rs.getString("asistencia");
                String fechaentrada = rs.getString("hora_entrada");
                String fechasalida = rs.getString("hora_salida");
                String turno = rs.getString("turno");
                int idproyecto = rs.getInt("idproyecto");
                String nombrepro = rs.getString("nombre_proyecto");

                TBMostrarAsistencia.getItems().add(new AsistenciaVista(id, ide, nombre, asistencia, fechaentrada, fechasalida, turno, idproyecto, nombrepro));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buscarDatos(String busqueda) {
        TBMostrarAsistencia.getItems().clear(); // Limpiar los elementos actuales de la tabla

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement("SELECT aa.idAsistencia, empleado.idempleado, empleado.nombreCompleto, asia.asistencia, aa.hora_entrada, aa.hora_salida, aa.turno, pro.idproyecto, pro.nombre_proyecto " +
                     "FROM tbAsistencia aa " +
                     "INNER JOIN tbEmpleadosProyectos id ON id.idEmpleado = aa.idempleado " +
                     "INNER JOIN tbempleados empleado ON empleado.idempleado = id.idEmpleado " +
                     "INNER JOIN tbProyectos pro ON pro.idproyecto = id.idProyecto " +
                     "INNER JOIN tbAsistencia asis ON asis.idAsistencia = aa.idAsistencia " +
                     "INNER JOIN tbAsistenciaMarcar asia ON asia.idAsistenciaMarcar = asis.idAsistenciaMarcar " +
                     "WHERE id.idproyecto = ? " +
                     "AND (empleado.nombreCompleto LIKE ? OR asia.asistencia LIKE ? OR aa.hora_entrada LIKE ? " +
                     "OR aa.hora_salida LIKE ? OR aa.turno LIKE ? OR pro.nombre_proyecto LIKE ?)")) {

            // Preparar el parámetro de búsqueda para la consulta SQL
            String parametroBusqueda = "%" + busqueda + "%";
            stmt.setInt(1, idProyectoSeleccionado);
            for (int i = 2; i <= 7; i++) {
                stmt.setString(i, parametroBusqueda);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Obtener los datos y agregarlos a la tabla
                int id = rs.getInt("idAsistencia");
                int ide = rs.getInt("idempleado");
                String nombre = rs.getString("nombreCompleto");
                String asistencia = rs.getString("asistencia");
                String fechaentrada = rs.getString("hora_entrada");
                String fechasalida = rs.getString("hora_salida");
                String turno = rs.getString("turno");
                int idproyecto = rs.getInt("idproyecto");
                String nombrepro = rs.getString("nombre_proyecto");

                TBMostrarAsistencia.getItems().add(new AsistenciaVista(id,ide, nombre, asistencia, fechaentrada, fechasalida, turno, idproyecto, nombrepro));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
