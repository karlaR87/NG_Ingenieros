package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.Empleados;
import com.example.ng_ingenieros.Proyecto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestionProyectosControlador {

    @FXML
    private Label lbProyecto;

    @FXML
    private Label lbLugar;

    @FXML
    private Label lbHoras;

    @FXML
    private Label lbInicio;

    @FXML
    private Label lbFinal;

    @FXML
    private Button btnCancelar;

    @FXML
    private ComboBox<String> cmEstado;
    @FXML
    private ComboBox<String> cmbActividad;

    @FXML
    private TableView<Empleados> tbEmpleados;

    private int idProyecto;

    public void cargarDatosProyecto(Proyecto proyecto) {
        lbProyecto.setText(proyecto.getNombre());
        lbLugar.setText(proyecto.getLugar());
        lbHoras.setText(String.valueOf(proyecto.getHoras()));
        lbInicio.setText(proyecto.getInicio());
        lbFinal.setText(proyecto.getFinal());
        btnCancelar.setOnAction(this::cerrarVentana);



        // Seleccionar el estado actual del proyecto en el ComboBox
        cmEstado.getSelectionModel().select(proyecto.getEstado());

        System.out.println("id es:" +idProyecto);


        tbEmpleados.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                // Obtén el empleado seleccionado
                Empleados empleadoSeleccionado = tbEmpleados.getSelectionModel().getSelectedItem();

                // Verifica si el empleado no es nulo
                if (empleadoSeleccionado != null) {
                    // Llama al método para cargar la actividad en el ComboBox
                    String actividadEmpleado = empleadoSeleccionado.getActividad();
                    cargarActividadEmpleado(actividadEmpleado);
                }
            }
        });

        llenarComboEstado();
        ActividadCombobox();

    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
        cargarEmpleadosAsociados();
    }

    public void setIdProyectoDesdeVentanaPrincipal(int idProyecto) {
        // Este método se llama desde la ventana principal para proporcionar el ID del proyecto
        setIdProyecto(idProyecto);
    }


    private void cargarEmpleadosAsociados() {
        try (Connection conn = Conexion.obtenerConexion()) {
            String query = "SELECT \n" +
                    "    e.idempleado, \n" +
                    "    e.nombreCompleto, \n" +
                    "    e.dui, \n" +
                    "    e.correo, \n" +
                    "    e.sueldo_dia, \n" +
                    "    e.sueldo_horaExt, \n" +
                    "    e.numero_cuentabancaria,\n" +
                    "    c.cargo, \n" +
                    "    a.Actividad\n" +
                    "FROM \n" +
                    "    tbEmpleadosProyectos ep\n" +
                    "INNER JOIN \n" +
                    "    tbempleados e ON ep.idEmpleado = e.idempleado\n" +
                    "INNER JOIN \n" +
                    "    tbcargos c ON e.idcargo = c.idcargo\n" +
                    "INNER JOIN \n" +
                    "    tbActividad a ON e.idactividad = a.idactividad\n" +
                    "LEFT JOIN \n" +
                    "    tbProyectos p ON ep.idProyecto = p.idproyecto\n" +
                    "WHERE \n" +
                    "    p.idproyecto = ? OR p.idproyecto IS NULL;";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setInt(1, idProyecto);
                System.out.println("Cargando empleados asociados al Proyecto con ID: " + idProyecto);

                try (ResultSet rs = preparedStatement.executeQuery()) {
                    // Limpiar la lista actual de empleados antes de agregar nuevos
                    tbEmpleados.getItems().clear();

                    // Recorrer los resultados y agregarlos directamente a la tabla
                    while (rs.next()) {
                        int id = rs.getInt("idempleado");
                        String nombre = rs.getString("nombreCompleto");
                        String dui = rs.getString("dui");
                        String correo = rs.getString("correo");
                        double sueldoDia = rs.getDouble("sueldo_dia");
                        double sueldoHoraExt = rs.getDouble("sueldo_horaExt");
                        String cuenta = rs.getString("numero_cuentabancaria");
                        String cargo = rs.getString("cargo");
                        String actividad = rs.getString("Actividad");

                        System.out.println("ID: " + id + ", Nombre: " + nombre + ", Actividad: " + actividad);

                        tbEmpleados.getItems().add(new Empleados(id, nombre, dui, correo, sueldoDia, sueldoHoraExt, cuenta, cargo, actividad));
                    }

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de errores
        }
    }

    private void cerrarVentana(javafx.event.ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void cargarActividadEmpleado(String actividad) {
        ObservableList<String> items = cmbActividad.getItems();

        // Agrega la actividad al ComboBox si no está presente
        if (!items.contains(actividad)) {
            items.add(actividad);
        }

        // Selecciona la actividad en el ComboBox
        cmbActividad.getSelectionModel().select(actividad);
    }


    private void ActividadCombobox() {
        // Crear una lista observable para almacenar los datos
        ObservableList<String> data = FXCollections.observableArrayList();

        // Conectar a la base de datos y recuperar los datos
        try (Connection conn = Conexion.obtenerConexion()) { // Reemplaza con tu propia lógica de conexión
            String query = "select Actividad from tbActividad"; // Reemplaza con tu consulta SQL
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Recorrer los resultados y agregarlos a la lista observable
            while (resultSet.next()) {
                String item = resultSet.getString("Actividad"); // Reemplaza con el nombre de la columna de tu tabla
                data.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de errores
        }
        cmbActividad.setItems(data);

    }


    public int IdRetornoActividad(String actividad) throws SQLException {
        Connection conectar = null;
        PreparedStatement pst = null;
        ResultSet result = null;
        int idActvidad = -1;

        String SSQL = "SELECT idactividad FROM tbActividad WHERE Actividad = ?";

        try {
            conectar = Conexion.obtenerConexion();
            pst = conectar.prepareStatement(SSQL);
            pst.setString(1, actividad);
            result = pst.executeQuery();

            if (result.next()) {
                idActvidad = result.getInt("idactividad");
            } else {
                System.err.println("El Estado seleccionado no existe en la base de datos.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            // Cerrar recursos
            if (result != null) {
                result.close();
            }
            if (pst != null) {
                pst.close();
            }
            if (conectar != null) {
                conectar.close();
            }
        }

        return idActvidad;
    }
    private void llenarComboEstado() {
        // Crear una lista observable para almacenar los datos
        ObservableList<String> data = FXCollections.observableArrayList();

        // Conectar a la base de datos y recuperar los datos
        try (Connection conn = Conexion.obtenerConexion()) { // Reemplaza con tu propia lógica de conexión
            String query = "select Estado_proyecto from tbEstadoProyectos"; // Reemplaza con tu consulta SQL
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Recorrer los resultados y agregarlos a la lista observable
            while (resultSet.next()) {
                String item = resultSet.getString("Estado_proyecto"); // Reemplaza con el nombre de la columna de tu tabla
                data.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de errores
        }
        cmEstado.setItems(data);

    }


    public int IdRetornoEstado(String Cargo) throws SQLException {
        Connection conectar = null;
        PreparedStatement pst = null;
        ResultSet result = null;
        int idEstado = -1;

        String SSQL = "SELECT idEstadoProyecto FROM tbEstadoProyectos WHERE Estado_proyecto = ?";

        try {
            conectar = Conexion.obtenerConexion();
            pst = conectar.prepareStatement(SSQL);
            pst.setString(1, Cargo);
            result = pst.executeQuery();

            if (result.next()) {
                idEstado = result.getInt("idEstadoProyecto");
            } else {
                System.err.println("El Estado seleccionado no existe en la base de datos.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            // Cerrar recursos
            if (result != null) {
                result.close();
            }
            if (pst != null) {
                pst.close();
            }
            if (conectar != null) {
                conectar.close();
            }
        }

        return idEstado;
    }




}
