package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import java.util.Properties;
import java.sql.ResultSet;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecuperacionContraseñaControlador {
    @FXML
    private Button btnEnviar;

    @FXML
    private TextField txtCorreoRecu;

    public static boolean validarCorreo(String input) {
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public static boolean NoVacio(String input) {
        return !input.trim().isEmpty();
    }


    public void initialize() {
        //Configura el evento de clic para el botón
        btnEnviar.setOnAction(this::btnEnviarOnAction);

    }
    private void btnEnviarOnAction(ActionEvent event) {
        validaciones();
       enviarcorreo();

    }




    public ResultSet RecuperacionContraseña(){
        Conexion conexion = new Conexion();
        Connection connection = conexion.obtenerConexion();
        PreparedStatement ps;
        try{
            String query ="Select correo, contraseña from tbusuarios inner join tbempleados on tbusuarios.idempleado = tbempleados.idempleado where correo = ?";
            ps = connection.prepareStatement(query);
            String correo;
            //ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            return rs;

        }catch (Exception e){
            mostrarAlerta("Error", "Ha ocurrido un error", Alert.AlertType.ERROR);
            return null;

        }
    }


    public void enviarcorreo() {
        String codigoAleatorio = generarCodigoAleatorio();
        final String correoRemitente = "liamrh855@gmail.com"; // Cambia esto por tu dirección de correo
        final String passwordCorreoRemitente = "nhgh sort xahs kqks"; // Cambia esto por tu contraseña

        String destinatario = txtCorreoRecu.getText(); // Cambia esto por la dirección de correo del destinatario

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
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(destinatario));
            message.setSubject("Recuperación Contarseña");
            message.setText("Codigo de verificación: " + codigoAleatorio );

            // Envío del mensaje
            Transport.send(message);

            System.out.println("¡Correo enviado!");
            RecuperarContraseñaDosControlador.setCodigoEnviado(codigoAleatorio);
            RecuperarContraseñaDosControlador.setCorreodestinatario(txtCorreoRecu.getText());
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/RecuperarContraseñaDos.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Nueva");
                stage.setScene(new Scene(root));
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


    public static String generarCodigoAleatorio() {
        Random random = new Random();
        int codigo = 100000 + random.nextInt(900000); // Generar un número aleatorio de 6 dígitos
        return String.valueOf(codigo);
    }

    public void validaciones() {
        if (NoVacio(txtCorreoRecu.getText())){
            if (validarCorreo(txtCorreoRecu.getText())){

            }else {
                mostrarAlerta("Error de Validación", "Ingrese un correo válido.");
            }

        }else {
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
    public static void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();


    }
   /* public class RandomNumberGenerator {
        public static void main(String[] args) {
            // Create an instance of the Random class

            // Generate a random 6-digit number
            int min = 100000; // Minimum value for a 6-digit number
            int max = 999999; // Maximum value for a 6-digit number
            int randomNumber = random.nextInt(max - min + 1) + min;

            // Display the generated random number
            System.out.println("Generated 6-digit random number: " + randomNumber);
        }
    }*/
}
