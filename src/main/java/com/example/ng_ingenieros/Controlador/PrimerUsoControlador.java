package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PrimerUsoControlador {
    @FXML
    private PasswordField txtContraseña, txtConfirmarContraseña;

    @FXML
    private TextField txtUser, txtCorreo;

    @FXML
    private Button btnAceptar;

    @FXML
    private Label lbAdvertencia;

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

    //Validaciones
    public static boolean validarNumero(String input) {
        return input.matches("\\d+");
    }

    public static boolean validarLetras(String input) {
        return input.matches("[a-zA-Z]+");
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
        String contraseña = txtContraseña.getText();
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
    private static String nombreemp;
    public static void setnombre(String nombre) {
        nombreemp = nombre;
    }
    private int obteneridempleado() {
        int idempleado = -1; // Valor predeterminado en caso de error o no selección

        try (Connection conn = Conexion.obtenerConexion()) {
            String sql = "SELECT idempleado FROM tbempleados WHERE nombreCompleto = '" + nombreemp+"';" ;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {


                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idempleado = rs.getInt("idempleado");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idempleado;
    }


    public void initialize() {
        // Configura el evento de clic para el botón
        btnAceptar.setOnAction(this::btnAceptarOnAction);
    }

    // Método para abrir una nueva ventana
    private void btnAceptarOnAction(ActionEvent event) {
        validaciones();

    }

    // Metodo para registrar datos en labase de datos
    public void registrardatos(){
        Conexion conexion = new Conexion();
        Connection connection = conexion.obtenerConexion();
        contraseña();

        String user = txtUser.getText();
        String contra = contraseñaEncriptada;
        String confirmarCon = txtConfirmarContraseña.getText();
        int idempledo = obteneridempleado();
        //hora crea un String para hacer la insercion
        String Insercion = "insert into tbusuarios(nombreUsuario, contraseña, idNivelUsuario, idempleado) values(?,?,2,"+idempledo+");";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Insercion);
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, contra);

            preparedStatement.executeUpdate();
            contraseña();

            if (txtConfirmarContraseña.getText().equals(txtContraseña.getText())) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/Login.fxml"));
                    Parent root = loader.load();

                    Stage stage = new Stage();
                    stage.initStyle(StageStyle.UNDECORATED);

                    stage.setScene(new Scene(root));
                    stage.show();

                    // Opcional: Cerrar la ventana actual
                    ((Stage) txtUser.getScene().getWindow()).close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                mostrarAlerta("Error", "Las contraseñas no coinciden");
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }



    public void validaciones() {
        if (NoVacio(txtUser.getText()) && NoVacio(txtContraseña.getText())){
           if (validarLongitud(txtUser.getText(), 8, 20)){
                    registrardatos();


           }    else {
               mostrarAlerta("Error de Validación", "La longitud de los campos debe estar entre 8 y 20 caracteres.");
           }

        } else {
            mostrarAlerta("Error de Validación", "Ingresar datos, no pueden haber campos vacíos.");
        }
    }

    public static void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Función para validar la longitud de un texto
    private boolean validarLongitud(String texto, int minimo, int maximo) {
        int longitud = texto.length();
        return (longitud >= minimo && longitud <= maximo);
    }
}
