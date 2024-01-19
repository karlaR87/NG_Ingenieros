package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.Empleados;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;


import java.awt.*;

import java.io.IOException;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.stage.StageStyle;

import javax.swing.*;


public class ProyectosAgregarControlador {
    @FXML
    private Button btnCancelar;


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

    public void initialize() throws SQLException {
        // Configura el evento de clic para el botón
        btnCancelar.setOnAction(this::cerrarVentana);
        cmIngeniero.setPromptText("Seleccionar Ingeniero a cargo");


        btnGestionar.setOnAction(this::AbrirGestion);



        btnGuardarProyecto.setOnAction(this::AgregarProyecto);

        llenarComboEstado();
        llenarComboingACargo();



    }







    public void setEmpleados(ObservableList<Empleados> empleados) {
        empleadosProyecto.addAll(empleados);
    }

    public void setEmpleadoProyecto(ObservableList<Empleados> empleadosProyecto) {
        this.empleadosProyecto.setAll(empleadosProyecto);
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
        validaciones(actionEvent);
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




    //Validaciones

    //Validaciones
    public void validaciones(javafx.event.ActionEvent actionEvent) {
        if (NoVacio(txtNombre.getText()) && NoVacio(txtLugar.getText()) && NoVacio(txtHoras.getText())){
                    if (validarNumeroS(txtHoras.getText())){

                            Connection conn = Conexion.obtenerConexion();
                            System.out.println("Conexión establecida correctamente.");

                            String nombreProyecto = txtNombre.getText();
                            String lugarProyecto = txtLugar.getText();
                            int horasTrabajo = Integer.parseInt(txtHoras.getText());
                            Date fechaInicio = Date.valueOf(dateInicio.getValue());
                            Date fechaFin = Date.valueOf(dateFinalizacion.getValue());

                            // Obtener el ID del estado del proyecto


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

                            String queryProcedimiento = "exec AgregarProyectoConEmpleados ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
                            try (CallableStatement cs = conn.prepareCall(queryProcedimiento)) {
                                cs.setString(1, nombreProyecto);
                                cs.setString(2, lugarProyecto);
                                cs.setInt(3, horasTrabajo);
                                cs.setDate(4, fechaInicio);
                                cs.setDate(5, fechaFin);

                                // Itera sobre los empleados y agrega cada uno al procedimiento almacenado
                                for (Empleados empleado : empleadosProyecto) {
                                    cs.setString(6, empleado.getNombre());
                                    cs.setString(7, empleado.getDui());
                                    cs.setString(8, empleado.getCorreo());
                                    cs.setDouble(9, empleado.getSueldoDia());
                                    cs.setDouble(10, empleado.getSueldoHora());
                                    cs.setString(11, empleado.getCuentaBancaria());
                                    cs.setInt(12, empleado.getIdcargo());
                                    cs.setInt(13, idIngeniero);

                                    cs.execute();
                                }


                                // Ejecuta el procedimiento almacenado una vez para todos los empleados
                                mostrarAlerta("Éxito", "Proyecto y empleados asociados agregados con éxito.");
                            } catch (SQLException e) {
                                e.printStackTrace();
                                System.err.println("Error al ejecutar el procedimiento almacenado: " + e.getMessage());
                                // Mostrar mensaje de error
                                mostrarAlerta("Error", "Error al agregar el proyecto y empleados. Consulta la consola para más detalles.");
                            }

                            Node source = (Node) actionEvent.getSource();
                            Stage stage = (Stage) source.getScene().getWindow();
                            stage.close();

                } else{

                        CustomAlert customAlert = new CustomAlert();
                        customAlert.mostrarAlertaPersonalizada("Error", "Solo se pueden ingresar numeros en el la hora.", (Stage) btnGuardarProyecto.getScene().getWindow());
                        return;
                }


        }else {

            CustomAlert customAlert = new CustomAlert();
            customAlert.mostrarAlertaPersonalizada("Error", "Ingresar datos, no pueden haber campos vacíos.", (Stage) btnGuardarProyecto.getScene().getWindow());
            return;
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
