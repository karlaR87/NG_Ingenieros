package com.example.ng_ingenieros.Controlador;


import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.CustomAlert;
import com.example.ng_ingenieros.Empleados;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;


import java.awt.*;

import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class agregar_empleadosControlador {

    @FXML
    private TextField txtNombreEmp;
    @FXML
    private TextField txtDuiEmp;
    @FXML
    private TextField txtCorreoEmp;
    @FXML
    private TextField txtCuentaBEmp;
    @FXML
    private TextField txtSueldoEmp;
    @FXML
    private TextField txtPagoHorasExEmp;
    @FXML
    private ComboBox<String> cbCargoEmp;
    @FXML
    private ComboBox<String> cbPlazaEmp;

    @FXML
    private Button btnGuardar1, btnCancelar1;

    private TableView<Empleados> TableEmpleados;

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




    public void initialize(){
        btnGuardar1.setOnAction(this::guardarDatos);
        btnCancelar1.setOnAction(this::cerrarVentana);



        cargarCargosEnCombobox();
        cargarPlazasEnCombobox();

        cbCargoEmp.setPromptText("Seleccione el cargo del empleado");
        cbPlazaEmp.setPromptText("Seleccione la plaza del empleado");
    }

    public void setTableEmpleados(TableView<Empleados> tableEmpleados) {
        this.TableEmpleados = tableEmpleados;
    }

    private void guardarDatos(ActionEvent event) {
        validaciones();
    }

    private void cerrarVentana(javafx.event.ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }


    @FXML

    public void agregarEmpleados(){
        String nombre = txtNombreEmp.getText();
        String dui = txtDuiEmp.getText();
        String correo = txtCorreoEmp.getText();
        String cuentaBancaria = txtCuentaBEmp.getText();
        double sueldoDia = Double.parseDouble(txtSueldoEmp.getText());
        double sueldoHoraExt = Double.parseDouble(txtPagoHorasExEmp.getText());
        int idCargo = obtenerIdCargoSeleccionado(cbCargoEmp);
        int idPlaza = obtenerIdPlazaSeleccionado(cbPlazaEmp);

        try (Connection conn = Conexion.obtenerConexion()) {
            String sql = "INSERT INTO tbempleados (nombreCompleto, dui, correo, sueldo_dia, sueldo_horaExt, numero_cuentabancaria, idcargo, idtipoPlaza) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps =conn.prepareStatement(sql);
            ps.setString(1,nombre);
            ps.setString(2,dui);
            ps.setString(3,correo);
            ps.setString(4,cuentaBancaria);
            ps.setDouble(5,sueldoDia);
            ps.setDouble(6,sueldoHoraExt);
            ps.setInt(7,idCargo);
            ps.setInt(8, idPlaza);
            ps.executeUpdate();
            mostrarAlerta("Inserción de empleados", "El empleado ha sido agregado exitosamente", Alert.AlertType.INFORMATION);

            if (TableEmpleados != null) {
                TableEmpleados.getItems().clear();
                EmpleadosControlador empleadosControlador = new EmpleadosControlador();
                empleadosControlador.setTableEmpleados(TableEmpleados);
                empleadosControlador.cargarDatos();
            }

            ((Stage) txtNombreEmp.getScene().getWindow()).close();


        }catch (SQLException e) {
            mostrarAlerta("Error", "Ha ocurrido un error", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }


    private int obtenerIdCargoSeleccionado(ComboBox<String> cbCargoEmp) {
        int idCargo = -1; // Valor predeterminado en caso de error o no selección

        try (Connection conn = Conexion.obtenerConexion()) {
            String cargoSeleccionado = cbCargoEmp.getValue();

            String sql = "SELECT idcargo FROM tbcargos WHERE cargo = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, cargoSeleccionado);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idCargo = rs.getInt("idcargo");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idCargo;
    }


    private int obtenerIdPlazaSeleccionado(ComboBox<String> cbPlazaEmp) {
        int idPlaza = -1; // Valor predeterminado en caso de error o no selección

        try (Connection conn = Conexion.obtenerConexion()) {
            String PlazaSeleccionada = cbPlazaEmp.getValue();

            String sql = "SELECT idtipoPlaza FROM tbTipoPlazas WHERE tipoPlaza = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, PlazaSeleccionada);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idPlaza = rs.getInt("idtipoPlaza");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idPlaza;
    }


    private void cargarPlazasEnCombobox() {
        // Crear una lista observable para almacenar los datos
        ObservableList<String> data = FXCollections.observableArrayList();

        // Conectar a la base de datos y recuperar los datos
        try (Connection conn = Conexion.obtenerConexion()) { // Reemplaza con tu propia lógica de conexión
            String query = "SELECT tipoPlaza FROM tbtipoPlazas";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Recorrer los resultados y agregarlos a la lista observable
            while (resultSet.next()) {
                String item = resultSet.getString("tipoPlaza"); // Reemplaza con el nombre de la columna de tu tabla
                data.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de errores
        }

        // Asignar los datos al ComboBox
        cbPlazaEmp.setItems(data);
    }



    private void cargarCargosEnCombobox() {
        // Crear una lista observable para almacenar los datos
        ObservableList<String> data = FXCollections.observableArrayList();

        // Conectar a la base de datos y recuperar los datos
        try (Connection conn = Conexion.obtenerConexion()) { // Reemplaza con tu propia lógica de conexión
            String query = "SELECT cargo FROM tbcargos";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Recorrer los resultados y agregarlos a la lista observable
            while (resultSet.next()) {
                String item = resultSet.getString("cargo"); // Reemplaza con el nombre de la columna de tu tabla
                data.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de errores
        }

        // Asignar los datos al ComboBox
        cbCargoEmp.setItems(data);
    }



    //Metodo para mostrar mensajes en la aplicacion de forma más facil y ordenada
    public static void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();


    }

    //Validaciones
    public void validaciones() {
        if (NoVacio(txtNombreEmp.getText()) && NoVacio(txtCorreoEmp.getText()) && NoVacio(txtDuiEmp.getText())&& NoVacio(txtCuentaBEmp.getText())&& NoVacio(txtSueldoEmp.getText()) && cbCargoEmp.getValue() != null && cbPlazaEmp.getValue() != null){
            if (validarDui(txtDuiEmp.getText())){
                if (validarDuiNoExistente(txtDuiEmp.getText())) { // Verifica si el DUI no existe
                    if (validarLetras(txtNombreEmp.getText())){
                if (validarCorreo(txtCorreoEmp.getText())){
                    if (validarNumeroc(txtCuentaBEmp.getText())){
                        if (validarNumeroS(txtSueldoEmp.getText())){
                            if (validarNumeroS(txtPagoHorasExEmp.getText())){
                                agregarEmpleados();
                            }
                            else {
                                CustomAlert customAlert = new CustomAlert();
                                customAlert.mostrarAlertaPersonalizada("Error", "Ingrese solo números en Pago por horas Extra", (Stage) btnGuardar1.getScene().getWindow());
                                return;
                            }
                        }
                        else {

                            CustomAlert customAlert = new CustomAlert();
                            customAlert.mostrarAlertaPersonalizada("Error", "Ingrese solo números en sueldo empleado", (Stage) btnGuardar1.getScene().getWindow());
                            return;
                        }

                    }else {

                        CustomAlert customAlert = new CustomAlert();
                        customAlert.mostrarAlertaPersonalizada("Error", "Ingrese solo números en el numero de cuenta.", (Stage) btnGuardar1.getScene().getWindow());
                        return;
                    }

                } else {
                    CustomAlert customAlert = new CustomAlert();
                    customAlert.mostrarAlertaPersonalizada("Error", "Ingrese un correo válido.", (Stage) btnGuardar1.getScene().getWindow());
                    return;                }


            } else{
                CustomAlert customAlert = new CustomAlert();
                customAlert.mostrarAlertaPersonalizada("Error", "Solo se pueden ingresar letras en el nombre.", (Stage) btnGuardar1.getScene().getWindow());
                return;            }
                }else{
                    CustomAlert customAlert = new CustomAlert();
                    customAlert.mostrarAlertaPersonalizada("Error", "El DUI ya existe en la base de datos.", (Stage) btnGuardar1.getScene().getWindow());
                    return;
                }
            } else {
                CustomAlert customAlert = new CustomAlert();
                customAlert.mostrarAlertaPersonalizada("Error", "Ingrese un DUI válido.", (Stage) btnGuardar1.getScene().getWindow());
                return;
            }

        }else {
            CustomAlert customAlert = new CustomAlert();
            customAlert.mostrarAlertaPersonalizada("Error", "Ingresar datos, no pueden haber campos vacíos.", (Stage) btnGuardar1.getScene().getWindow());
            return;           }

    }
    private boolean validarDuiNoExistente(String dui) {
        Conexion conexion = new Conexion();
        Connection connection = conexion.obtenerConexion();

        String consulta = "SELECT COUNT(*) FROM tbempleados WHERE dui = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(consulta);
            preparedStatement.setString(1, dui);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count == 0; // Retorna true si el DUI no existe en la base de datos
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false; // En caso de error o excepción
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
