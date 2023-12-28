package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.AsistenciaVista;
import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.SalarioEmp;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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

        TableColumn<SalarioEmp, ?> columnaProyecto = (TableColumn<SalarioEmp, ?>) tbMostrarSalario.getColumns().get(11); // El índice 9 representa la columna del proyecto
        columnaProyecto.setVisible(false);
        TableColumn<SalarioEmp, ?> columnaProyecto2 = (TableColumn<SalarioEmp, ?>) tbMostrarSalario.getColumns().get(12); // El índice 10 representa la columna del proyecto
        columnaProyecto2.setVisible(false);

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
        tbMostrarSalario.getItems().clear();
        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("\n" +
                     "select p.idplanilla, em.nombreCompleto, em.dui, em.numero_cuentabancaria, pro.idproyecto, pro.nombre_proyecto, p.diasRemunerados, p.horasExtTrabajadas, \n" +
                     "CAST(p.totalDevengado AS DECIMAL(10, 2)) as totalDevengado, \n" +
                     "CAST(p.AFP AS DECIMAL(10, 2)) as AFP, \n" +
                     "CAST(p.seguro_social AS DECIMAL(10, 2)) as seguro_social, \n" +
                     "CAST(p.descuento_Renta AS DECIMAL(10, 2)) as descuento_Renta, \n" +
                     "CAST(p.salarioFinal AS DECIMAL(10, 2)) as salarioFinal\n" +
                     "from tbplanillas p\n" +
                     "inner join tbEmpleadosProyectos emp on emp.idempleado = p.idempleado\n" +
                     "inner join tbempleados em on em.idempleado = emp.idEmpleado\n" +
                     "inner join tbProyectos pro on pro.idproyecto = emp.idProyecto\n" +
                     "\n" +
                     "order by em.idempleado DESC")) {

            while (rs.next()) {
                int id = rs.getInt("idplanilla");
                String nombreemp = rs.getString("nombreCompleto");
                String dui = rs.getString("dui");
                String cuenta = rs.getString("numero_cuentabancaria");
                int diasrem = rs.getInt("diasRemunerados");
                String horasextra = rs.getString("horasExtTrabajadas");
                float tot = rs.getFloat("totalDevengado");
                float afp = rs.getFloat("AFP");
                float seguro = rs.getFloat("seguro_social");
                float desc = rs.getFloat("descuento_Renta");
                float sal = rs.getFloat("salarioFinal");
                int idproyecto = rs.getInt("idproyecto");
                String nombrepory = rs.getString("nombre_proyecto");

                tbMostrarSalario.getItems().add(new SalarioEmp(id, nombreemp, dui, cuenta, diasrem, horasextra, tot, afp, seguro, desc, sal, idproyecto, nombrepory));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void BuscarDatos(String busqueda) {

        tbMostrarSalario.getItems().clear();

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement("select p.idplanilla, em.nombreCompleto,  em.dui, em.numero_cuentabancaria, pro.idproyecto, pro.nombre_proyecto, p.diasRemunerados, p.horasExtTrabajadas, \n" +
                     "CAST(p.totalDevengado AS DECIMAL(10, 2)) as totalDevengado, \n" +
                     "CAST(p.AFP AS DECIMAL(10, 2)) as AFP, \n" +
                     "CAST(p.seguro_social AS DECIMAL(10, 2)) as seguro_social, \n" +
                     "CAST(p.descuento_Renta AS DECIMAL(10, 2)) as descuento_Renta, \n" +
                     "CAST(p.salarioFinal AS DECIMAL(10, 2)) as salarioFinal\n" +
                     "from tbplanillas p\n" +
                     "inner join tbEmpleadosProyectos emp on emp.idempleado = p.idempleado\n" +
                     "inner join tbempleados em on em.idempleado = emp.idEmpleado\n" +
                     "inner join tbProyectos pro on pro.idproyecto = emp.idProyecto " +
                     "WHERE em.nombreCompleto LIKE ? OR p.diasRemunerados LIKE ? OR p.horasExtTrabajadas LIKE ? OR p.totalDevengado LIKE ? " +
                     "OR p.AFP LIKE ? OR p.seguro_social LIKE ? OR p.descuento_Renta LIKE ? OR p.salarioFinal LIKE ? " +
                     "order by em.idempleado DESC")) {

            // Preparar el parámetro de búsqueda para la consulta SQL
            String parametroBusqueda = "%" + busqueda + "%";
            for (int i = 1; i <= 8; i++) {
                stmt.setString(i, parametroBusqueda);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("idplanilla");
                String nombreemp = rs.getString("nombreCompleto");
                String dui = rs.getString("dui");
                String cuenta = rs.getString("numero_cuentabancaria");
                int diasrem = rs.getInt("diasRemunerados");
                String horasextra = rs.getString("horasExtTrabajadas");
                float tot = rs.getFloat("totalDevengado");
                float afp = rs.getFloat("AFP");
                float seguro = rs.getFloat("seguro_social");
                float desc = rs.getFloat("descuento_Renta");
                float sal = rs.getFloat("salarioFinal");
                int idproyecto = rs.getInt("idproyecto");
                String nombrepory = rs.getString("nombre_proyecto");

                tbMostrarSalario.getItems().add(new SalarioEmp(id, nombreemp, dui, cuenta, diasrem, horasextra, tot, afp, seguro, desc, sal, idproyecto, nombrepory));
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

            tbMostrarSalario.getItems().clear();
            cargarDatos();
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
