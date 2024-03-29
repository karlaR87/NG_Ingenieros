package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.CustomAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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


public class RegistrarseSegundoControlador {
    @FXML
    private PasswordField txtContraseña;
    @FXML
    private PasswordField txtConfirmaContra;
    @FXML
    private TextField txtUsuario;
    @FXML
    private Button btnRegistrarse, btnRegresar;
    @FXML
    private Label lbAdvertencia;
    @FXML
    private ComboBox cmbNivel;

    @FXML
    private Button btnClose;

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

    @FXML
    protected void HandleCloseAction(ActionEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
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

    public static void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
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




    public void initialize(){
        // Configura el evento de clic para el botón
        btnRegistrarse.setOnAction(this::btnRegistrarseOnAction);
        btnRegresar.setOnAction(this::btnRegresarOnAction);
        cargarnivelCombobox();
        cmbNivel.setPromptText("Seleccione el nivel del usuario");
    }


    private void btnRegistrarseOnAction(ActionEvent event) {
        validaciones();
    }

    private void btnRegresarOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/Registrarse.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Registrarse");


            // Configurar la modalidad (bloquea la ventana principal)
            //stage.initModality(Modality.APPLICATION_MODAL);

            // Configurar el estilo para quitar la barra de título
            stage.initStyle(StageStyle.UNDECORATED);

            stage.setScene(new Scene(root));
            ((Stage) txtUsuario.getScene().getWindow()).close();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
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

    public void registrardatos(){
        Conexion conexion = new Conexion();
        Connection connection = conexion.obtenerConexion();
        contraseña();

        String user = txtUsuario.getText();
        String contra = contraseñaEncriptada;
        String confirmarCon = txtConfirmaContra.getText();
        int idnivel = obteneridnivelusuario(cmbNivel);
        int idempledo = obteneridempleado();
        //hora crea un String para hacer la insercion
        String Insercion = "insert into tbusuarios(nombreUsuario, contraseña, idNivelUsuario, idempleado) values(?,?,?,"+idempledo+");";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Insercion);
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, contra);
            preparedStatement.setInt(3, idnivel);
            preparedStatement.executeUpdate();
            contraseña();

            if (txtConfirmaContra.getText().equals(txtContraseña.getText())) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/Login.fxml"));
                    Parent root = loader.load();

                    Stage stage = new Stage();
                    stage.initStyle(StageStyle.UNDECORATED);

                    stage.setScene(new Scene(root));
                    stage.show();

                    // Opcional: Cerrar la ventana actual
                    ((Stage) txtUsuario.getScene().getWindow()).close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                CustomAlert customAlert = new CustomAlert();
                customAlert.mostrarAlertaPersonalizada("Error", "Las contraseñas no coinciden", (Stage) btnRegistrarse.getScene().getWindow());
                return;            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }


    private void cargarnivelCombobox() {
        // Crear una lista observable para almacenar los datos
        ObservableList<String> data = FXCollections.observableArrayList();

        // Conectar a la base de datos y recuperar los datos
        Conexion conexion = new Conexion();
        Connection connection = conexion.obtenerConexion();
        try {

            // Reemplaza con tu propia lógica de conexión
            String query = "SELECT idNivelUsuario, usuario FROM tbNivelesUsuario";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Recorrer los resultados y agregarlos a la lista observable
            while (resultSet.next()) {
                String item = resultSet.getString("usuario"); // Reemplaza con el nombre de la columna de tu tabla
                data.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de errores
        }

        // Asignar los datos al ComboBox
        cmbNivel.setItems(data);
    }
    private int obteneridnivelusuario(ComboBox<String> cbCargoEmp) {
        int idCargo = -1; // Valor predeterminado en caso de error o no selección

        try (Connection conn = Conexion.obtenerConexion()) {
            String cargoSeleccionado = cbCargoEmp.getValue();

            String sql = "SELECT idNivelUsuario FROM tbNivelesUsuario WHERE usuario = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, cargoSeleccionado);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idCargo = rs.getInt("idNivelUsuario");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idCargo;
    }

    public void validaciones() {
        if (NoVacio(txtContraseña.getText()) && NoVacio(txtUsuario.getText()) && cmbNivel.getValue() != null) {
            if (validarLongitud(txtUsuario.getText(), 3, 25)) {
                if (validarLongitud(txtContraseña.getText(), 8, 20)) {
                    registrardatos();
                } else {
                    CustomAlert customAlert = new CustomAlert();
                    customAlert.mostrarAlertaPersonalizada("Error", "La longitud de la contraseña debe estar entre 8 y 20 caracteres.", (Stage) btnRegistrarse.getScene().getWindow());
                    return;
                }
            } else {
                CustomAlert customAlert = new CustomAlert();
                customAlert.mostrarAlertaPersonalizada("Error", "El nombre de usuario debe de estar entre 3 y 25 carcateres. ", (Stage) btnRegistrarse.getScene().getWindow());
                return;
            }
        } else {
            CustomAlert customAlert = new CustomAlert();
            customAlert.mostrarAlertaPersonalizada("Error", "Ingresar datos y seleccionar un nivel, no pueden haber campos vacíos.", (Stage) btnRegistrarse.getScene().getWindow());
        }
    }


    // Función para validar la longitud de un texto
    private boolean validarLongitud(String texto, int minimo, int maximo) {
        int longitud = texto.length();
        return (longitud >= minimo && longitud <= maximo);
    }



}
