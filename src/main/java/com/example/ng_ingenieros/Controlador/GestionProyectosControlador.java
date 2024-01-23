package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.Controlador.AlertDos;
import com.example.ng_ingenieros.CustomAlert;
import com.example.ng_ingenieros.Empleados;
import com.example.ng_ingenieros.Proyecto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

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
    private Button btnCambio;
    @FXML
    private Button btnGuardarProyecto;


    @FXML
    private ComboBox<String> cmEstado;
    @FXML
    private ComboBox<String> cmbActividad;

    @FXML
    private TableView<Empleados> tbEmpleados;

    private int idProyecto;

    private Proyecto proyecto; // Agregar esta línea

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

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

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


        // Agregar evento al botón para realizar el cambio de actividad
        btnCambio.setOnAction(event -> {
            // Verificar si el proyecto está finalizado
            if (verificarProyectoFinalizado(idProyecto)) {
                mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "No se puede cambiar la actividad de un proyecto finalizado.");
                return; // Salir del método si el proyecto está finalizado
            }

            Empleados empleadoSeleccionado = tbEmpleados.getSelectionModel().getSelectedItem();
            if (empleadoSeleccionado != null) {
                String nuevaActividad = cmbActividad.getValue();

                // Verificar si el proyecto no está finalizado antes de realizar el cambio
                if (!verificarProyectoFinalizado(idProyecto)) {
                    // Actualizar la actividad del empleado
                    actualizarActividadEmpleado(empleadoSeleccionado.getId(), nuevaActividad, idProyecto);
                    cargarEmpleadosAsociados(); // Volver a cargar la lista de empleados después de la actualización
                } else {
                    mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "No se puede cambiar la actividad de un proyecto finalizado.");
                }
            }
        });

        btnGuardarProyecto.setOnAction(this::btnGuardarProyectoClick);


        llenarComboEstado();
        ActividadCombobox();

        verificarYDeshabilitarBotones();

        if (verificarProyectoFinalizado(idProyecto)) {
            // Bloquear los botones de agregar y cambiar
            // (Asegúrate de tener las referencias adecuadas a los botones en tu código)
            btnGuardarProyecto.setDisable(true);
            btnCambio.setDisable(true);
            cmEstado.setDisable(true);
            // Puedes agregar más acciones según tus necesidades
        }

    }
    private void verificarYDeshabilitarBotones() {
        String estadoProyecto = obtenerEstadoProyecto(idProyecto);

        // Verificar si el proyecto está finalizado y deshabilitar los botones
        if ("Finalizado".equals(estadoProyecto)) {
            btnCambio.setDisable(true);
            btnGuardarProyecto.setDisable(true);
        } else {
            btnCambio.setDisable(false);
            btnGuardarProyecto.setDisable(false);
        }
    }
    // Método para realizar la actualización de la actividad del empleado en la base de datos
    private void actualizarActividadEmpleado(int idEmpleado, String nuevaActividad, int idProyectoActual) {
        // Verificar si el empleado está activo en otro proyecto
        if (!empleadoActivoEnOtroProyecto(idEmpleado, idProyectoActual)) {
            try (Connection conn = Conexion.obtenerConexion()) {
                // Actualizar la tabla intermedia tbEmpleadosProyectos
                String updateQuery = "UPDATE tbEmpleadosProyectos SET idactividad = ? WHERE idEmpleado = ? AND idProyecto = ?";
                try (PreparedStatement updateStatement = conn.prepareStatement(updateQuery)) {
                    int idActividad = IdRetornoActividad(nuevaActividad);
                    updateStatement.setInt(1, idActividad);
                    updateStatement.setInt(2, idEmpleado);
                    updateStatement.setInt(3, idProyectoActual);
                    updateStatement.executeUpdate();
                }

                // Mensaje de éxito
                JOptionPane.showMessageDialog(null, "Se ha actualizado la actividad del empleado en el proyecto.");
            } catch (SQLException e) {
                e.printStackTrace();
                // Manejo de errores
            }
        } else {
            // El empleado está activo en otro proyecto, muestra un mensaje o realiza alguna acción
            JOptionPane.showMessageDialog(null, "El empleado está activo en otro proyecto. No se puede cambiar la actividad.");
        }
    }

    private boolean empleadoActivoEnOtroProyecto(int idEmpleado, int idProyectoActual) {
        try (Connection conn = Conexion.obtenerConexion()) {
            String query = "SELECT COUNT(*) " +
                    "FROM tbEmpleadosProyectos " +
                    "WHERE idEmpleado = ? AND idProyecto <> ? AND (idactividad = 1 OR idProyecto = ?)";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setInt(1, idEmpleado);
                preparedStatement.setInt(2, idProyectoActual);
                preparedStatement.setInt(3, idProyectoActual);

                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        int count = rs.getInt(1);
                        return count > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de errores
        }
        return false;
    }




    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
        cargarEmpleadosAsociados();
    }

    public void setIdProyectoDesdeVentanaPrincipal(int idProyecto) {
        // Este método se llama desde la ventana principal para proporcionar el ID del proyecto
        setIdProyecto(idProyecto);
    }
    @FXML
    void btnGuardarProyectoClick(javafx.event.ActionEvent event) {
        // Verifica si el proyecto ya está finalizado
        if (verificarProyectoFinalizado(idProyecto)) {
            // Muestra un mensaje indicando que la acción ya se realizó anteriormente
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "El proyecto esta finalizado, no se puede cambiar. ");
            return; // Sale del método si el proyecto ya está finalizado
        }

        // Crea una instancia de tu alerta personalizada
        AlertDos alertDos = new AlertDos();
        // Muestra una confirmación antes de cambiar el estado del proyecto
        boolean confirmacion = alertDos.mostrarAlerta("¿Está seguro de que desea finalizar el proyecto?", "Confirmación");

        if (confirmacion) {
            // Actualizar el estado del proyecto a "Finalizado"
            actualizarEstadoProyecto(idProyecto, "Finalizado");

            // Cambiar el idactividad de los empleados del proyecto a "Inactivo"
            cambiarIdActividadEmpleados(idProyecto);

            // Volver a cargar los empleados asociados después de la actualización
            cargarEmpleadosAsociados();

            // Verificar y deshabilitar botones según el nuevo estado del proyecto
            verificarYDeshabilitarBotones();

            // Mostrar un mensaje de éxito o realizar otras acciones necesarias
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "El proyecto se ha marcado como finalizado.");

            ProyectosControlador pro = new ProyectosControlador();
            pro.cargarDatosProyectos();

            // Verificar si el proyecto está finalizado y bloquear los botones de agregar y cambiar si es necesario
            if (verificarProyectoFinalizado(idProyecto)) {
                // Bloquear los botones de agregar y cambiar
                // (Asegúrate de tener las referencias adecuadas a los botones en tu código)
                btnGuardarProyecto.setDisable(true);
                btnCambio.setDisable(true);
                cmEstado.setDisable(true);
                // Puedes agregar más acciones según tus necesidades
            }
        }
    }


    private boolean verificarProyectoFinalizado(int idProyecto) {
        try (Connection conn = Conexion.obtenerConexion()) {
            String query = "SELECT idEstadoProyecto FROM tbProyectos WHERE idproyecto = ?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setInt(1, idProyecto);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        int idEstado = rs.getInt("idEstadoProyecto");
                        // Compara el id del estado con el correspondiente al estado "Finalizado"
                        return idEstado == IdRetornoEstado("Finalizado");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de errores
        }
        return false;
    }


    private String obtenerEstadoProyecto(int idProyecto) {
        try (Connection conn = Conexion.obtenerConexion()) {
            String query = "SELECT Estado_proyecto FROM tbEstadoProyectos " +
                    "JOIN tbProyectos ON tbEstadoProyectos.idEstadoProyecto = tbProyectos.idEstadoProyecto " +
                    "WHERE tbProyectos.idproyecto = ?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setInt(1, idProyecto);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("Estado_proyecto");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de errores
        }
        return null; // Manejar el caso de error o proyecto no encontrado
    }


    private void cambiarIdActividadEmpleados(int idProyecto) {
        try (Connection conn = Conexion.obtenerConexion()) {
            // Actualizar la tabla intermedia tbEmpleadosProyectos
            String updateQuery = "UPDATE tbEmpleadosProyectos SET idactividad = 2 WHERE idProyecto = ?";
            try (PreparedStatement updateStatement = conn.prepareStatement(updateQuery)) {
                // Establecer el nuevo idActividad para todos los empleados del proyecto
                updateStatement.setInt(1, idProyecto);
                updateStatement.executeUpdate();
            }

            // Volver a cargar los empleados asociados después de la actualización
            cargarEmpleadosAsociados();
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de errores
        }
    }



    private void actualizarEstadoProyecto(int idProyecto, String nuevoEstado) {
        try (Connection conn = Conexion.obtenerConexion()) {
            // Actualizar el estado del proyecto
            String updateQuery = "UPDATE tbProyectos SET idEstadoProyecto = ? WHERE idproyecto = ?";
            try (PreparedStatement updateStatement = conn.prepareStatement(updateQuery)) {
                int idEstado = IdRetornoEstado(nuevoEstado);
                updateStatement.setInt(1, idEstado);
                updateStatement.setInt(2, idProyecto);
                updateStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de errores
        }
    }

    // Método para mostrar una alerta con el tipo, título y mensaje especificados
    private void mostrarAlerta(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void cargarEmpleadosAsociados() {
        try (Connection conn = Conexion.obtenerConexion()) {
            String query = "SELECT \n" +
                    " e.idempleado, \n" +
                    " e.nombreCompleto, \n" +
                    " e.dui, \n" +
                    "  e.correo, \n" +
                    "  e.sueldo_dia, \n" +
                    "  e.sueldo_horaExt, \n" +
                    "  e.numero_cuentabancaria,\n" +
                    "  c.cargo, \n" +
                    "  a.Actividad\n" +
                    "  FROM\n" +
                    "       tbEmpleadosProyectos ep \n" +
                    "                INNER JOIN \n" +
                    "                    tbempleados e ON ep.idEmpleado = e.idempleado\n" +
                    "                INNER JOIN \n" +
                    "                    tbcargos c ON e.idcargo = c.idcargo\n" +
                    "                INNER JOIN \n" +
                    "                    tbActividad a ON ep.idactividad = a.idactividad\n" +
                    "                LEFT JOIN \n" +
                    "                    tbProyectos p ON ep.idProyecto = p.idproyecto\n" +
                    "                WHERE \n" +
                    "                 p.idproyecto = ? OR p.idproyecto IS NULL;";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setInt(1, idProyecto);
                System.out.println("Cargando empleados asociados al Proyecto con ID: " + idProyecto);

                try (ResultSet rs = preparedStatement.executeQuery()) {
                    // Limpiar la lista actual de empleados antes de agregar nuevos
                    tbEmpleados.getItems().clear();

                    // Recorrer los resultados y agregarlos directamente a la tabla
                    while (rs.next()) {
                        int id = rs.getInt("idEmpleado");
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
