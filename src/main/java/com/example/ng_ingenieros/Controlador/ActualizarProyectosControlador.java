package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.CustomAlert;
import com.example.ng_ingenieros.Empleados;
import com.example.ng_ingenieros.Proyecto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;


import java.awt.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;


import javafx.stage.StageStyle;

public class ActualizarProyectosControlador {

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtLugar;
    @FXML
    private ComboBox<String> cmIngeniero;
    @FXML
    private TextField txtHoras;
    @FXML
    private ComboBox<String> cmEstado;
    @FXML
    private DatePicker dateInicio;
    @FXML
    private DatePicker dateFinalizacion;

    @FXML
    private Button btnGestionar;

    @FXML
    private Button btnGuardarProyecto;

    @FXML
    private Button btnCancelar;

    private int idProyecto;

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

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
        System.out.println("ID del Proyecto establecido en ActualizarProyectosControlador: " + idProyecto);
    }

    public void initialize() throws SQLException {
        btnCancelar.setOnAction(this::cerrarVentana);
        btnGestionar.setOnAction(this::gestionarEmpleados);
        btnGuardarProyecto.setOnAction(this::btnGuardarProyecto);




        System.out.println("ID del Proyecto recibido: " + idProyecto);

    }


    @FXML
    private void gestionarEmpleados(ActionEvent event) {

        try {

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            // Cargar la vista de GestionEmpleadosActualizar desde su archivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/GestionEmpleadosActualizar.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la nueva ventana
            GestionEmpleadosActualizarControlador gestionEmpleadosActualizarControlador = loader.getController();

            // Establecer el ID del proyecto en el controlador de GestionEmpleadosActualizar
            gestionEmpleadosActualizarControlador.setIdProyecto(idProyecto);
            System.out.println("ID del Proyecto enviado a GestionEmpleadosActualizarControlador: " + idProyecto);


            // Crear un nuevo escenario (Stage)
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Gestión de Empleados Actualizar");

            // Configurar la escena con el nodo raíz (root)
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            // Mostrar la ventana de GestionEmpleadosActualizar
            stage.showAndWait();



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cargarDatosProyecto(Proyecto proyecto) {
        // Configura los campos de texto y otros elementos según la información del proyecto
        txtNombre.setText(proyecto.getNombre());
        txtLugar.setText(proyecto.getLugar());
        txtHoras.setText(String.valueOf(proyecto.getHoras()));

        // Configura las fechas
        dateInicio.setValue(LocalDate.parse(proyecto.getInicio()));
        dateFinalizacion.setValue(LocalDate.parse(proyecto.getFinal()));

    }










    private void llenarComboingACargo() {
        // Crear una lista observable para almacenar los datos
        ObservableList<String> data = FXCollections.observableArrayList();

        // Conectar a la base de datos y recuperar los datos
        try (Connection conn = Conexion.obtenerConexion()) { // Reemplaza con tu propia lógica de conexión
            String query = "SELECT nombreCompleto FROM tbempleados WHERE idcargo = 7"; // Reemplaza con tu consulta SQL
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Recorrer los resultados y agregarlos a la lista observable
            while (resultSet.next()) {
                String item = resultSet.getString("nombreCompleto"); // Reemplaza con el nombre de la columna de tu tabla
                data.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de errores
        }

        // Asignar los datos al ComboBox
        cmIngeniero.setItems(data);
    }


    public int IdRetornoIngAcargo(String inge) throws SQLException {
        Connection conectar = null;
        PreparedStatement pst = null;
        ResultSet result = null;
        int idInge = -1;

        String SSQL = "SELECT e.idempleado \n" +
                "FROM tbempleados e \n" +
                "LEFT JOIN tbEmpleadosProyectos ep ON e.idempleado = ep.idempleado \n" +
                "LEFT JOIN tbProyectos p ON ep.idProyecto = p.idproyecto \n" +
                "WHERE e.idcargo = 7 \n" +
                "  AND (ep.idproyecto IS NULL OR p.idEstadoProyecto = 2) \n" +
                "  AND (ep.idactividad IS NULL OR ep.idactividad = 2);";

        try {
            conectar = Conexion.obtenerConexion();
            pst = conectar.prepareStatement(SSQL);
            result = pst.executeQuery();

            if (result.next()) {
                idInge = result.getInt("idempleado");
            } else {
                System.err.println("El Inge seleccionado no existe en la base de datos.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Imprime la excepción en lugar de mostrar un diálogo de mensaje
        } finally {
            // Cerrar recursos
            if (result != null) {
                result.close();
            }
            if (pst != null) {
                pst.close();
            }
            if (conectar != null) {
                conectar.close();
            }
        }

        return idInge;
    }

    private void cerrarVentana(javafx.event.ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    public static void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    @FXML
    private void btnGuardarProyecto(ActionEvent event) {
        // Obtener los valores de los campos
        String nombre = txtNombre.getText();
        String lugar = txtLugar.getText();
        String horas = txtHoras.getText();
        LocalDate fechaInicio = dateInicio.getValue();
        LocalDate fechaFinalizacion = dateFinalizacion.getValue();

        // Validar que los campos obligatorios no estén vacíos
        if (nombre.isEmpty() || lugar.isEmpty() || horas.isEmpty() || fechaInicio == null || fechaFinalizacion == null) {
            CustomAlert customAlert = new CustomAlert();
            customAlert.mostrarAlertaPersonalizada("Error", "Por favor, complete todos los campos.", (Stage) btnGuardarProyecto.getScene().getWindow());
            return;
        }


        if (!validarNumero(horas)) {
            CustomAlert customAlert = new CustomAlert();
            customAlert.mostrarAlertaPersonalizada("Error", "Ingrese solo números en el campo de Horas a Trabajar.", (Stage) btnGuardarProyecto.getScene().getWindow());
            return;

        }

        if (fechaFinalizacion.isBefore(fechaInicio)) {
            CustomAlert customAlert = new CustomAlert();
            customAlert.mostrarAlertaPersonalizada("Error", "La Fecha de Finalización no puede ser anterior a la Fecha de Inicio.", (Stage) btnGuardarProyecto.getScene().getWindow());
            return;

        }

        try (Connection conn = Conexion.obtenerConexion()) {
            // Crear la sentencia SQL para actualizar el proyecto
            String query = "UPDATE tbProyectos SET nombre_proyecto = ?, lugar_proyecto = ?, horas_trabajo = ?, fechaInicio = ?, FechaFin = ? WHERE idproyecto = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                // Establecer los valores de los parámetros en la sentencia SQL
                pstmt.setString(1, nombre);
                pstmt.setString(2, lugar);
                pstmt.setString(3, horas);
                pstmt.setDate(4, java.sql.Date.valueOf(fechaInicio));
                pstmt.setDate(5, java.sql.Date.valueOf(fechaFinalizacion));
                pstmt.setInt(6, idProyecto);

                // Ejecutar la sentencia SQL de actualización
                int filasAfectadas = pstmt.executeUpdate();

                if (filasAfectadas > 0) {
                    System.out.println("Proyecto actualizado con éxito.");
                } else {
                    System.out.println("No se pudo actualizar el proyecto.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de errores
        }
    }
    public static boolean validarNumero(String input) {
        return input.matches("\\d+");
    }

    public static boolean validarLetras(String input) {
        return input.matches("[a-zA-Z ]+");
    }

}
