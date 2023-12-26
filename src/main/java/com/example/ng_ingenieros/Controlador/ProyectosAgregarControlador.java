package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.Empleados;
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
    private ObservableList<Empleados> empleadosProyecto = FXCollections.observableArrayList();
    private EmpleadosAsignadosControlador empleadosAsignadosControlador;



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

    // ... otros métodos ...

    public void setEmpleados(ObservableList<Empleados> empleados) {
        empleadosProyecto.addAll(empleados);
        System.out.println("Datos recibidos en ProyectosAgregarControlador: " + empleadosProyecto);
    }


@FXML
    private void AbrirGestion(ActionEvent actionEvent) {
        try {
            // Cargar el archivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/Empleados_Asignados.fxml"));
            Parent root = loader.load();

            // Acceder al controlador de la nueva ventana
            EmpleadosAsignadosControlador empleadosAsignadosControlador = loader.getController();

            // Pasa la lista actualizada de empleados
            empleadosAsignadosControlador.setEmpleadosProyecto(empleadosProyecto);
            System.out.println("Hashcode del controlador: " + empleadosAsignadosControlador.hashCode());

            // Crear un nuevo Stage
            Stage stage = new Stage();

            // Configurar la modalidad (bloquea la ventana principal)
            stage.initModality(Modality.APPLICATION_MODAL);

            // Quitar la barra de título
            stage.initStyle(StageStyle.UNDECORATED);

            stage.setScene(new Scene(root));
            stage.showAndWait(); // Mostrar y esperar hasta que se cierre
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void AgregarProyecto(ActionEvent actionEvent) {
        String nombre = txtNombre.getText();
        String lugar = txtLugar.getText();
        String horas = txtHoras.getText();
        String dinicio = dateInicio.getValue().toString();
        String dfinal = dateFinalizacion.getValue().toString();
        String estado = cmEstado.getValue();

        System.out.println("Nombre: " + nombre);
        System.out.println("Lugar: " + lugar);
        System.out.println("Horas: " + horas);
        System.out.println("Fecha Inicio: " + dinicio);
        System.out.println("Fecha Finalización: " + dfinal);
        System.out.println("Estado: " + estado);

        try {
            // Obtener el ID del estado seleccionado
            int idEstadoProyecto = IdRetornoEstado(estado);

            Connection conn = Conexion.obtenerConexion();
            String query = "EXEC AgregarProyectoConEmpleados " +
                    "@nombreProyecto = ?, " +
                    "@lugarProyecto = ?, " +
                    "@horasTrabajo = ?, " +
                    "@fechaInicio = ?, " +
                    "@fechaFin = ?, " +
                    "@idEstadoProyecto = ?, " +
                    "@nombreEmpleado = ?, " +
                    "@duiEmpleado = ?, " +
                    "@correoEmpleado = ?, " +
                    "@sueldoDia = ?, " +
                    "@sueldoHoraExt = ?, " +
                    "@numeroCuentaBancaria = ?, " +
                    "@idCargo = ?";
            CallableStatement statement = conn.prepareCall(query);

            // Configurar los parámetros del proyecto
            statement.setString("nombreProyecto", nombre);
            statement.setString("lugarProyecto", lugar);
            statement.setInt("horasTrabajo", Integer.parseInt(horas));
            statement.setString("fechaInicio", dinicio);
            statement.setString("fechaFin", dfinal);
            statement.setInt("idEstadoProyecto", idEstadoProyecto);

            // Obtener la lista de empleados asociados al proyecto
            for (Empleados empleado : empleadosProyecto) {
                // Obtener el ID del ingeniero seleccionado
                int idIngeniero = IdRetornoIngAcargo(empleado.getNombre());

                // Agregar parámetros del empleado al procedimiento almacenado
                statement.setString("nombreEmpleado", empleado.getNombre());
                statement.setString("duiEmpleado", empleado.getDui());
                statement.setString("correoEmpleado", empleado.getCorreo());
                statement.setDouble("sueldoDia", empleado.getSueldoDia());
                statement.setDouble("sueldoHoraExt", empleado.getSueldoHora());
                statement.setString("numeroCuentaBancaria", empleado.getCuentaBancaria());
                statement.setInt("idCargo", idIngeniero);

                // Ejecutar el procedimiento almacenado para agregar el proyecto con empleados
                statement.executeUpdate();

                // Mensajes de depuración
                System.out.println("Empleado agregado al proyecto: " + empleado.getNombre());
            }

            // Mensajes de depuración
            System.out.println("Proyecto agregado con empleados exitosamente.");

            // Puedes limpiar los campos después de la inserción si es necesario
            // ...

            // Limpiar la lista de empleados después de agregar el proyecto
            empleadosProyecto.clear();

            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Mensaje de depuración en caso de error
            System.err.println("Error al agregar proyecto con empleados: " + e.getMessage());
        }
    }



    private void cerrarVentana(javafx.event.ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        // Eliminar la instancia actual del controlador
        empleadosAsignadosControlador = null;

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

        String SSQL = "SELECT nombreCompleto FROM tbempleados WHERE idcargo = 7";

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
