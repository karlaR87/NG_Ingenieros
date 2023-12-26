package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.example.ng_ingenieros.Controlador.RecuperacionContraseñaControlador;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.sql.ResultSet;
import java.sql.*;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecuperarContraseñaDosControlador {
    @FXML
    private TextField txtCodigoRecu;
    @FXML
    private Button btnRegresar, btnReenviarCodigo, btnSiguiente;

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

    public static void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


    public void initialize() {
        // Configura el evento de clic para el botón
        btnRegresar.setOnAction(this::btnRegresarOnAction);
        btnReenviarCodigo.setOnAction(this::btnReenviarCodigoOnAction);
        btnSiguiente.setOnAction(this::BtnSiguienteOnAction);

    }

    // Método para abrir una nueva ventana
    private void btnRegresarOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/RecuperacionContraseña.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Recuperar contraseña");


            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void BtnSiguienteOnAction(ActionEvent event) {
        validaciones();
    }
    private static String codigoEnviado;
    private static String correodestinatario;

    public static void setCorreodestinatario(String correo){
        correodestinatario = correo;
    }

    public static void setCodigoEnviado(String codigo) {
        codigoEnviado = codigo;
    }

    public void verificarCodigo() {
        String codigoIngresado = txtCodigoRecu.getText(); // Obtener el texto del TextField

        if (codigoIngresado.equals(codigoEnviado)) {
            System.out.println("El código ingresado es correcto.");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/RecuperarContraseñaTres.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Recuperar contraseña");


                stage.setScene(new Scene(root));
                ((Stage) txtCodigoRecu.getScene().getWindow()).close();
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();

            }
            // Realizar acciones correspondientes...
        } else if (codigoIngresado.equals(codigoAleatorio)) {
            System.out.println("El código ingresado es correcto.");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/RecuperarContraseñaTres.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Recuperar contraseña");


                stage.setScene(new Scene(root));
                ((Stage) txtCodigoRecu.getScene().getWindow()).close();
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();

            }
        } else {
            System.out.println("El código ingresado es incorrecto. Vuelve a intentarlo.");
        }
    }



    public void validaciones() {
        if (NoVacio(txtCodigoRecu.getText())){
            verificarCodigo();
        } else {
            mostrarAlerta("Error de Validación", "Ingresar datos, no pueden haber campos vacíos.");
        }
    }

    public void btnReenviarCodigoOnAction(ActionEvent event){
        enviarcorreo();
    }
    String codigoAleatorio = generarCodigoAleatorio();
    public void enviarcorreo() {

        final String correoRemitente = "liamrh855@gmail.com"; // Cambia esto por tu dirección de correo
        final String passwordCorreoRemitente = "nhgh sort xahs kqks"; // Cambia esto por tu contraseña

        String destinatario = correodestinatario; // Cambia esto por la dirección de correo del destinatario

        // Propiedades de la conexión
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Autenticación
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(correoRemitente, passwordCorreoRemitente);
                    }
                });

        try {
            // Creación del mensaje
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(correoRemitente));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(destinatario));
            message.setSubject("Recuperación obligame Contarseña");
            message.setText("Este es el código para poder recuperar su contraseña " + codigoAleatorio );

            // Envío del mensaje
            Transport.send(message);

            System.out.println("¡Correo enviado!");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    public static String generarCodigoAleatorio() {
        Random random = new Random();
        int codigo = 100000 + random.nextInt(900000); // Generar un número aleatorio de 6 dígitos
        return String.valueOf(codigo);
    }




}
