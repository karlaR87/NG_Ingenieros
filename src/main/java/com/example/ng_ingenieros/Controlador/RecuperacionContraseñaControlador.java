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

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import java.util.Properties;
import java.sql.ResultSet;
import java.sql.*;
public class RecuperacionContraseñaControlador {
    @FXML
    private Button btnEnviar;

    @FXML
    private TextField txtCorreoRecu;

    public void initialize() {
        //Configura el evento de clic para el botón
        btnEnviar.setOnAction(this::btnEnviarOnAction);

    }
    private void btnEnviarOnAction(ActionEvent event) {
        ventana();
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

    public  void ventana(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/RecuperarContraseñaDos.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Nueva");
            stage.setScene(new Scene(root));
            stage.show();
            // Opcional: Cerrar la ventana actual
            ((Stage) txtCorreoRecu.getScene().getWindow()).close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void enviarcorreo() {

        final String correoRemitente = "liamrh855@gmail.com"; // Cambia esto por tu dirección de correo
        final String passwordCorreoRemitente = "nhgh sort xahs kqks"; // Cambia esto por tu contraseña

        String destinatario = "20220240@ricaldone.edu.sv"; // Cambia esto por la dirección de correo del destinatario

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
            message.setSubject("Asunto del correo");
            message.setText("Este es el cuerpo del correo.");

            // Envío del mensaje
            Transport.send(message);

            System.out.println("¡Correo enviado!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();


    }
}
