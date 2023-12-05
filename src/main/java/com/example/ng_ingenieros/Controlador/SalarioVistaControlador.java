package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.AsistenciaVista;
import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.SalarioEmp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.*;

public class SalarioVistaControlador {


    @FXML
    private TableView tbMostrarSalario;
    @FXML
    private TextField txtBusqueda;

    @FXML
    private Button btnEliminar;

    public void initialize()
    {
        cargarDatos();

        txtBusqueda.setOnKeyReleased(event -> {

            BuscarDatos(txtBusqueda.getText());

        });

        btnEliminar.setOnAction(actionEvent -> {
            try {
                btnEliminarOnAction(actionEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void btnEliminarOnAction(javafx.event.ActionEvent actionEvent) throws IOException {
        eliminarSalario();
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


    private void eliminarSalario() {
        // Obtener el ID del proyecto seleccionado (asumiendo que tienes una variable para almacenar el ID)
        int idAsistencia = obtenerIdSalarioSeleccionado();

        try (Connection connection = Conexion.obtenerConexion()) {
            String sql = "DELETE FROM tbplanillas WHERE idplanilla = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idAsistencia);

            int filasAfectadas = statement.executeUpdate();
            if (filasAfectadas > 0) {
                mostrarAlerta("Eliminación de datos","Se eliminaron los datos exitosamente", Alert.AlertType.INFORMATION);

            } else {
                mostrarAlerta("Alerta","No hay ningun elemento seleccionado", Alert.AlertType.WARNING);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    private int obtenerIdSalarioSeleccionado() {
        SalarioEmp empleadoSeleccionado = (SalarioEmp) tbMostrarSalario.getSelectionModel().getSelectedItem();

        if (empleadoSeleccionado != null) {
            return empleadoSeleccionado.getIdSalario();
        } else {
            return -1; // Retorna un valor que indique que no se ha seleccionado ningún proyecto.
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
