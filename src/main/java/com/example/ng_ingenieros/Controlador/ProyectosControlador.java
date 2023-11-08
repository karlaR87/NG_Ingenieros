package com.example.ng_ingenieros.Controlador;
import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.Proyectos;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.Color;

public class ProyectosControlador {

    @FXML
    private TableView<Proyectos>tbProyectos;


    public void initialize()
    {
        cargarDatosProyectos();
    }

    private void cargarDatosProyectos() {
        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select pro.idproyecto, pro.nombre_proyecto, pro.lugar_proyecto, pro.horas_trabajo, es.Estado_proyecto from tbProyectos pro" +
                     " inner join tbEstadoProyectos es on es.idEstadoProyecto = pro.idEstadoProyecto")) {

            while (rs.next()) {
            int id = rs.getInt("idproyecto");
            String nombre = rs.getString("nombre_proyecto");
            String lugar = rs.getString("lugar_proyecto");

            int horas = rs.getInt("horas_trabajo");
            String estado = rs.getString("Estado_proyecto");


            tbProyectos.getItems().add(new Proyectos(id, nombre, lugar, horas, estado));


        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    /*@FXML codigo para paneles
        private JPanel panel;

        public void initialize() {
            try (Connection conn = Conexion.obtenerConexion();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("select * from tbProyectos")) {

                panel = new JPanel();
                while (rs.next()) {

                    JPanel PanelMostrar = new JPanel();

                    int id = rs.getInt("idproyecto");
                    String nombrep = rs.getString("nombre_proyecto");
                    String ubicacion = rs.getString("lugar_proyecto");
                    int horas = rs.getInt("horas_trabajo");

                    JLabel label1 = new JLabel("Proyecto N° " + id);
                    JLabel label2 = new JLabel("nombre del proyecto: " + nombrep);
                    JLabel label3 = new JLabel("ubicación: " + ubicacion);
                    JLabel label4 = new JLabel("Horas de trabajo: " + horas);

                    PanelMostrar.add(label1);
                    PanelMostrar.add(label2);
                    PanelMostrar.add(label3);
                    PanelMostrar.add(label4);

                    panel.add(PanelMostrar);
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }*/

}
