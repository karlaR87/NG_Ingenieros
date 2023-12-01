package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.w3c.dom.Text;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ProyectosAgregarControlador {
    @FXML
    private Button btnCancelar;

    @FXML
    private ComboBox<String> cmEstado;
    @FXML
    private ComboBox<String> cmIngeniero;

    @FXML
    private Button btnGestionar;
    @FXML
    private Button btnGuardarProyecto;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtLugar;

    @FXML
    private TextField txtHoras;

    @FXML
    private DatePicker dateInicio;

    @FXML
    private DatePicker dateFinalizacion;

    public void initialize() throws SQLException {
        // Configura el evento de clic para el botón
        btnCancelar.setOnAction(this::cerrarVentana);
        cmIngeniero.setPromptText("Seleccionar Ingeniero a cargo");
        cmEstado.setPromptText("Seleccionar el estado");


        btnGestionar.setOnAction(this::AbrirGestion);
        btnGuardarProyecto.setOnAction(this::AgregarProyecto);

        llenarComboEstado(cmEstado);
        llenarComboingACargo(cmIngeniero);


    }



    private void AbrirGestion(ActionEvent actionEvent) {
        try {
            // Cargar el archivo FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/Empleados_Asignados.fxml"));
            Parent root = loader.load();

            // Crear un nuevo Stage
            Stage stage = new Stage();

            // Configurar la modalidad (bloquea la ventana principal)
            stage.initModality(Modality.APPLICATION_MODAL);

            //  quitar la barra de título
            stage.initStyle(StageStyle.UNDECORATED);

            stage.setScene(new Scene(root));
            stage.showAndWait(); // Mostrar y esperar hasta que se cierre

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void AgregarProyecto (ActionEvent actionEvent) {
        String nombre = txtNombre.getText();
        String lugar = txtLugar.getText();
        String horas = txtHoras.getText();
        String dinicio = dateInicio.getValue().toString();  // Obtener la fecha de inicio
        String dfinal = dateFinalizacion.getValue().toString();  // Obtener la fecha de finalización
        String estado = cmEstado.getValue();  // Obtener el estado del proyecto

        try {
            // Obtener el ID del estado seleccionado
            int idEstadoProyecto = IdRetornoEstado(estado);

            Connection conn = Conexion.obtenerConexion();
            String query = "INSERT INTO tbProyectos (nombre_proyecto, lugar_proyecto, horas_trabajo, fechaInicio, FechaFin, idEstadoProyecto)\n" +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, nombre);
            statement.setString(2, lugar);
            statement.setInt(3, Integer.parseInt(horas));  // Convertir a entero
            statement.setString(4, dinicio);
            statement.setString(5, dfinal);
            statement.setInt(6, idEstadoProyecto);

            statement.executeUpdate();

            // Puedes limpiar los campos después de la inserción si es necesario
            txtNombre.clear();
            txtLugar.clear();
            txtHoras.clear();
            dateInicio.setValue(null);
            dateFinalizacion.setValue(null);
            cmEstado.setValue(null);

            statement.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private void cerrarVentana(javafx.event.ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    //LLENAR
    public void llenarComboEstado(ComboBox<String> Estado) throws SQLException {
        ObservableList<String> items = FXCollections.observableArrayList();
        Connection conectar = null;
        PreparedStatement pst = null;
        ResultSet result = null;

        String SSQL = "select Estado_proyecto from tbEstadoProyectos";

        try {
            conectar = Conexion.obtenerConexion();
            pst = conectar.prepareStatement(SSQL);
            result = pst.executeQuery();

            while (result.next()) {
                String nombre = result.getString("Estado_proyecto");
                items.add(nombre);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
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

        // Asignar los datos al ComboBox
        Estado.setItems(items);
    }

    public int IdRetornoEstado(String id) throws SQLException {
        Connection conectar = null;
        PreparedStatement pst = null;
        ResultSet result = null;
        int gen = -1;



        String SSQL = "SELECT idEstadoProyecto FROM tbEstadoProyectos WHERE Estado_proyecto = ?";



        try {
            conectar = Conexion.obtenerConexion();
            pst = conectar.prepareStatement(SSQL);
            pst.setString(1, id);
            result = pst.executeQuery();



            if (result.next()) {
                gen = result.getInt("idEstadoProyecto");
            }



        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
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



        return gen;
    }

    public void llenarComboingACargo(ComboBox<String> Estado) throws SQLException {
        ObservableList<String> items = FXCollections.observableArrayList();
        Connection conectar = null;
        PreparedStatement pst = null;
        ResultSet result = null;

        String SSQL = "SELECT nombreCompleto FROM tbempleados WHERE idcargo = 7 AND idproyecto IS NULL";

        try {
            conectar = Conexion.obtenerConexion();
            pst = conectar.prepareStatement(SSQL);
            result = pst.executeQuery();

            while (result.next()) {
                String nombre = result.getString("nombreCompleto");
                items.add(nombre);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
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

        // Asignar los datos al ComboBox
        Estado.setItems(items);
    }

    public int IdRetornoIngAcargo(String nombreEmpleado) throws SQLException {
        Connection conectar = null;
        PreparedStatement pst = null;
        ResultSet result = null;
        int gen = -1;

        String SSQL = "SELECT idempleado FROM tbempleados WHERE nombreCompleto = ? AND idcargo = 7 AND idproyecto IS NULL";

        try {
            conectar = Conexion.obtenerConexion();
            pst = conectar.prepareStatement(SSQL);
            pst.setString(1, nombreEmpleado);
            result = pst.executeQuery();

            if (result.next()) {
                gen = result.getInt("idempleado");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
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

        return gen;
    }

    //Validaciones
    public void validaciones() {
        if (NoVacio(txtNombre.getText()) && NoVacio(txtHoras.getText()) && NoVacio(txtLugar.getText())){

        }else {
            mostrarAlerta("Error de validación", "Ingresar datos, no pueden haber campos vacíos.");
        }


    }


    //Validaciones
    public static boolean NoVacio(String input) {
        return !input.trim().isEmpty();
    }
    public static void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


}
