package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import javafx.scene.control.Alert;

import java.sql.ResultSet;
import java.sql.*;
public class RecuperacionContraseñaControlador {
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
    public static void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();


    }
}
