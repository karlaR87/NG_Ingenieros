package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.AsistenciaVista;
import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.SalarioEmp;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class SalarioVistaControlador {


    @FXML
    private TableView tbMostrarSalario;
    @FXML
    private TextField txtBusqueda;

    public void initialize()
    {
        cargarDatos();

        txtBusqueda.setOnKeyReleased(event -> {

            BuscarDatos(txtBusqueda.getText());

        });
    }
    private void cargarDatos() {
        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select p.idplanilla,em.nombreCompleto, p.diasRemunerados, p.horasExtTrabajadas, p.totalDevengado, p.AFP, p.seguro_social, p.descuento_Renta, p.salarioFinal\n" +
                     "\n" +
                     "from tbplanillas p\n" +
                     "inner join tbempleados em on em.idempleado = p.idempleado")) {

            while (rs.next()) {
                int id = rs.getInt("idplanilla");
                String nombreemp = rs.getString("nombreCompleto");
                int diasrem = rs.getInt("diasRemunerados");
                String horasextra = rs.getString("horasExtTrabajadas");
                float tot = rs.getFloat("totalDevengado");
                float afp = rs.getFloat("AFP");
                float seguro = rs.getFloat("seguro_social");
                float desc = rs.getFloat("descuento_Renta");
                float sal = rs.getFloat("salarioFinal");


                tbMostrarSalario.getItems().add(new SalarioEmp(id, nombreemp, diasrem, horasextra, tot, afp, seguro, desc, sal));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void BuscarDatos(String busqueda) {

        tbMostrarSalario.getItems().clear();

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement("select p.idplanilla,em.nombreCompleto, p.diasRemunerados, p.horasExtTrabajadas, p.totalDevengado, p.AFP, p.seguro_social, p.descuento_Renta, p.salarioFinal\n" +
                     "\n" +
                     "from tbplanillas p\n" +
                     "inner join tbempleados em on em.idempleado = p.idempleado " +
                     "where em.nombreCompleto LIKE ?")) {

            // Preparar el parámetro de búsqueda para la consulta SQL
            String parametroBusqueda = "%" + busqueda + "%";
            stmt.setString(1, parametroBusqueda);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("idplanilla");
                String nombreemp = rs.getString("nombreCompleto");
                int diasrem = rs.getInt("diasRemunerados");
                String horasextra = rs.getString("horasExtTrabajadas");
                float tot = rs.getFloat("totalDevengado");
                float afp = rs.getFloat("AFP");
                float seguro = rs.getFloat("seguro_social");
                float desc = rs.getFloat("descuento_Renta");
                float sal = rs.getFloat("salarioFinal");


                tbMostrarSalario.getItems().add(new SalarioEmp(id, nombreemp, diasrem, horasextra, tot, afp, seguro, desc, sal));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
