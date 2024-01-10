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
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private EmpleadosAsignadosControlador empleadosAsignadosController;


    public void initialize() throws SQLException {
        // Configura el evento de clic para el botón
        btnCancelar.setOnAction(this::cerrarVentana);
        cmIngeniero.setPromptText("Seleccionar Ingeniero a cargo");
        cmEstado.setPromptText("Seleccionar el estado");


        btnGestionar.setOnAction(this::AbrirGestion);



        btnGuardarProyecto.setOnAction(this::AgregarProyecto);

        llenarComboEstado();
        llenarComboingACargo();


    }



    public void setEmpleados(ObservableList<Empleados> empleados) {
        empleadosProyecto.addAll(empleados);
    }


    @FXML
    private void AbrirGestion(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/Empleados_Asignados.fxml"));
            Parent root = loader.load();

            // Acceder al controlador de la nueva ventana
            EmpleadosAsignadosControlador empleadosAsignadosControlador = loader.getController();

            // Pasa la lista actualizada de empleados
            empleadosAsignadosControlador.setEmpleadosProyecto(empleadosProyecto);

            // Crear un nuevo Stage
            Stage stage = new Stage();

            // Configurar la modalidad (bloquea la ventana principal)
            stage.initModality(Modality.APPLICATION_MODAL);

            // Quitar la barra de título
            stage.initStyle(StageStyle.UNDECORATED);

            stage.setScene(new Scene(root));
            stage.showAndWait(); // Mostrar y esperar hasta que se cierre

            ObservableList<Empleados> empleadosActualizados = empleadosAsignadosControlador.getEmpleadosProyecto();
            System.out.println("Datos recibidos en ProyectosAgregarControlador: " + empleadosActualizados);

            // Actualiza la lista de empleados en ProyectosAgregarControlador
            empleadosProyecto.clear();
            empleadosProyecto.addAll(empleadosActualizados);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void AgregarProyecto(ActionEvent actionEvent) {
        validaciones();
    }


    public void setEmpleadosProyecto(ObservableList<Empleados> empleados) {
        empleadosProyecto.clear();
        empleadosProyecto.addAll(empleados);
    }



    private void cerrarVentana(javafx.event.ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    //LLENAR


    private void llenarComboEstado() {
        // Crear una lista observable para almacenar los datos
        ObservableList<String> data = FXCollections.observableArrayList();

        // Conectar a la base de datos y recuperar los datos
        try (Connection conn = Conexion.obtenerConexion()) { // Reemplaza con tu propia lógica de conexión
            String query = "select Estado_proyecto from tbEstadoProyectos"; // Reemplaza con tu consulta SQL
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Recorrer los resultados y agregarlos a la lista observable
            while (resultSet.next()) {
                String item = resultSet.getString("Estado_proyecto"); // Reemplaza con el nombre de la columna de tu tabla
                data.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de errores
        }

        // Asignar los datos al ComboBox
        cmEstado.setItems(data);
    }


    public int IdRetornoEstado(String Cargo) throws SQLException {
        Connection conectar = null;
        PreparedStatement pst = null;
        ResultSet result = null;
        int idEstado = -1;

        String SSQL = "SELECT idEstadoProyecto FROM tbEstadoProyectos WHERE Estado_proyecto = ?";

        try {
            conectar = Conexion.obtenerConexion();
            pst = conectar.prepareStatement(SSQL);
            pst.setString(1, Cargo);
            result = pst.executeQuery();

            if (result.next()) {
                idEstado = result.getInt("idEstadoProyecto");
            } else {
                System.err.println("El Estado seleccionado no existe en la base de datos.");
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

        return idEstado;
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

        String SSQL = "SELECT idempleado FROM tbempleados WHERE nombreCompleto = ? AND idcargo = 7 AND idproyecto IS NULL";

        try {
            conectar = Conexion.obtenerConexion();
            pst = conectar.prepareStatement(SSQL);
            pst.setString(1, inge);
            result = pst.executeQuery();

            if (result.next()) {
                idInge = result.getInt("idempleado");
            } else {
                System.err.println("El Inge seleccionado no existe en la base de datos.");
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

        return idInge;
    }



    //Validaciones

    //Validaciones
    public void validaciones() {
        if (NoVacio(txtNombre.getText()) && NoVacio(txtLugar.getText()) && NoVacio(txtHoras.getText())){
                if (validarLetras(txtNombre.getText())){
                    if (validarNumeroS(txtHoras.getText())){
                        Connection conn = Conexion.obtenerConexion();
                        System.out.println("Conexión establecida correctamente.");

                        String nombreProyecto = txtNombre.getText();
                        String lugarProyecto = txtLugar.getText();
                        int horasTrabajo = Integer.parseInt(txtHoras.getText());
                        Date fechaInicio = Date.valueOf(dateInicio.getValue());
                        Date fechaFin = Date.valueOf(dateFinalizacion.getValue());

                        // Obtener el ID del estado del proyecto
                        int idEstadoProyecto = -1;
                        try {
                            idEstadoProyecto = IdRetornoEstado(cmEstado.getValue());
                        } catch (SQLException e) {
                            e.printStackTrace();
                            System.err.println("Error al obtener el ID del estado del proyecto: " + e.getMessage());
                            return;
                        }

                        // Verifica si hay empleados a agregar
                        if (empleadosProyecto.isEmpty()) {
                            System.err.println("Error: No hay empleados para agregar al proyecto.");
                            return;
                        }

                        // Obtener el nombre del ingeniero seleccionado en el ComboBox
                        String nombreIngeniero = cmIngeniero.getValue();

                        // Obtener el ID del ingeniero a cargo
                        int idIngeniero = -1;
                        try {
                            idIngeniero = IdRetornoIngAcargo(nombreIngeniero);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            System.err.println("Error al obtener el ID del ingeniero a cargo: " + e.getMessage());
                            return;
                        }

                        //  información sobre los empleados a agregar
                        System.out.println("Número de empleados a agregar: " + empleadosProyecto.size());

                        String queryProcedimiento = "exec AgregarProyectoConEmpleados ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
                        try (CallableStatement cs = conn.prepareCall(queryProcedimiento)) {
                            cs.setString(1, nombreProyecto);
                            cs.setString(2, lugarProyecto);
                            cs.setInt(3, horasTrabajo);
                            cs.setDate(4, fechaInicio);
                            cs.setDate(5, fechaFin);
                            cs.setInt(6, idEstadoProyecto);

                            // Itera sobre los empleados y agrega cada uno al procedimiento almacenado
                            for (Empleados empleado : empleadosProyecto) {
                                cs.setString(7, empleado.getNombre());
                                cs.setString(8, empleado.getDui());
                                cs.setString(9, empleado.getCorreo());
                                cs.setDouble(10, empleado.getSueldoDia());
                                cs.setDouble(11, empleado.getSueldoHora());
                                cs.setString(12, empleado.getCuentaBancaria());
                                cs.setInt(13, empleado.getIdcargo());
                                cs.setInt(14, idIngeniero); // Agregar el ID del ingeniero como último parámetro

                                cs.execute();
                            }

                            // Ejecuta el procedimiento almacenado una vez para todos los empleados
                            System.out.println("Proyecto y empleados asociados agregados con éxito.");
                        } catch (SQLException e) {
                            e.printStackTrace();
                            System.err.println("Error al ejecutar el procedimiento almacenado: " + e.getMessage());
                            System.err.println("Error SQL: " + e.getSQLState());
                            System.err.println("Código de error: " + e.getErrorCode());
                            System.err.println("Mensaje: " + e.getMessage());
                        }

                } else{
                    mostrarAlerta("Error de Validación", "Solo se pueden ingresar numeros en el nombre.");
                }

            } else {
                mostrarAlerta("Error de Validación", "Ingrese un DUI válido.");

            }

        }else {
            mostrarAlerta("Error de validación", "Ingresar datos, no pueden haber campos vacíos.");
        }

    }

    //Validaciones
    public static boolean validarNumero(String input) {
        return input.matches("\\d+");
    }

    public static boolean validarNumeroc(String input) {
        return input.matches("[0-9\\-]+");
    }
    public static boolean validarNumeroS(String input) {
        return input.matches("[0-9.]+");    }

    public static boolean validarLetras(String input) {
        return input.matches("[a-zA-Z ]+");
    }

    public static boolean validarCorreo(String input) {
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

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

    // Función para validar el formato de un DUI (por ejemplo, 12345678-9)
    private boolean validarDui(String dui) {
        // Se puede implementar una lógica más avanzada según el formato real de DUI
        return dui.matches("\\d{8}-\\d{1}");
    }



}
