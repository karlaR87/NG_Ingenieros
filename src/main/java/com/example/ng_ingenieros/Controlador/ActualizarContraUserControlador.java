package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.Usuarios;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class ActualizarContraUserControlador {

    @FXML
    private Button btnCancelar, btnActualizarContra;
    @FXML
    private TextField txtContraNueva, txtConfirmarContraN;
    @FXML
    Label lbNombre;

    private TableView<Usuarios> TBUsuarios;

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
        stage.setY(event.getScreenY() +yOffset);
    }

    public static boolean validarNumero(String input) {
        return input.matches("\\d+");
    }

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


    private static String contraseñaEncriptada;

    public void contraseña()  {
        String contraseña = txtContraNueva.getText();
        contraseñaEncriptada = encriptarContraseña(contraseña);
        System.out.println("Contraseña original: " + contraseña);
        System.out.println("Contraseña encriptada: " + contraseñaEncriptada);
    }

    public static String encriptarContraseña(String contraseña) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(contraseña.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexHash = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexHash.append('0');
                }
                hexHash.append(hex);
            }

            return hexHash.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void initialize(Usuarios contraSeleccionado){
        lbNombre.setText(contraSeleccionado.getNombreUser());

        btnCancelar.setOnAction(this::cerrarVentana);
        btnActualizarContra.setOnAction(this::actualizarDatos);
    }

    public void setTableUsuarios(TableView<Usuarios> TBUsuarios) {
        this.TBUsuarios = TBUsuarios;
    }

    private void cerrarVentana(javafx.event.ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private void actualizarDatos(javafx.event.ActionEvent actionEvent) {
        validaciones();

    }

    public void actualizarContra(){
        contraseña();
        String PassN = contraseñaEncriptada;
        String PassN2 = txtConfirmarContraN.getText();



        Usuarios empleadoSeleccionado = obtenerEmpleadoSeleccionadoDesdeTabla();

        try (Connection conn = Conexion.obtenerConexion()) {
            String sql = "UPDATE tbusuarios SET contraseña = ?  WHERE idUsuario =?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, PassN);
                ps.setInt(2, empleadoSeleccionado.getIdUsuario()); // Asegúrate de tener un método getIdEmpleado() en tu clase Empleado
                ps.executeUpdate();
                contraseña();

                if(txtConfirmarContraN.getText().equals(txtContraNueva.getText())){
                    try{
                        agregar_empleadosControlador.mostrarAlerta("Actualización de empleados", "Se han actualizado los datos exitosamente", Alert.AlertType.INFORMATION);

                        if (TBUsuarios != null) {
                            TBUsuarios.getItems().clear();
                            CrudUsuariosControlador empleadosControlador = new CrudUsuariosControlador();
                            empleadosControlador.setTableUsuarios(TBUsuarios);
                            empleadosControlador.cargarDatos();
                        }

                        ((Stage) txtContraNueva.getScene().getWindow()).close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else{
                    agregar_empleadosControlador.mostrarAlerta("Error", "Las contraseñas no coinciden", Alert.AlertType.ERROR);
                }
            }
        } catch (SQLException e) {
            agregar_empleadosControlador.mostrarAlerta("Error", "Ha ocurrido un error", Alert.AlertType.ERROR);
            e.printStackTrace();

        }
    }


    public void validaciones() {
        if (NoVacio(txtContraNueva.getText()) && NoVacio(txtConfirmarContraN.getText())){
            actualizarContra();

        } else  {
            agregar_empleadosControlador.mostrarAlerta("Error de Validación", "Ingresar datos, no pueden haber campos vacíos.");
        }
    }

    // Función para validar la longitud de un texto


    private Usuarios obtenerEmpleadoSeleccionadoDesdeTabla() {

        return TBUsuarios.getSelectionModel().getSelectedItem(); // Reemplaza con la lógica real
    }
}


