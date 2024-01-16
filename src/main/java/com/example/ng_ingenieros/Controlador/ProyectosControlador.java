package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.Empleados;
import com.example.ng_ingenieros.Proyecto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ProyectosControlador {
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnEmilinar;
    @FXML
    private Button btnEditar;

    @FXML
    private TableView<Proyecto> tbProyectos;

    private ObservableList<Proyecto> listaProyectos = FXCollections.observableArrayList();

    public void initialize() {
        // Configura el evento de clic para el botón
        btnEmilinar.setOnAction(this::eliminarProyecto);
        btnEditar.setOnAction(this::editarProyecto);

        btnAgregar.setOnAction(this::Abrir);
        tbProyectos.setItems(listaProyectos);

        tbProyectos.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                // Detecta el doble clic y llama a la función para abrir la ventana
                abrirVentanaDetalle();
            }
        });

        cargarDatosProyectos();
    }

    private void abrirVentanaDetalle() {
        Proyecto proyectoSeleccionado = tbProyectos.getSelectionModel().getSelectedItem();
        if (proyectoSeleccionado != null) {
            try {
                // Cargar el archivo FXML de la ventana de detalles
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/GestionProyectos.fxml"));
                Parent root = loader.load();

                // Obtener el controlador de la nueva ventana
                GestionProyectosControlador detallesControlador = loader.getController();

                // Pasar los datos del proyecto seleccionado al controlador de detalles
                detallesControlador.cargarDatosProyecto(proyectoSeleccionado);

                // Pasar el ID del proyecto a través del nuevo método
                detallesControlador.setIdProyectoDesdeVentanaPrincipal(proyectoSeleccionado.getId());

                // Crear un nuevo Stage
                Stage stage = new Stage();
                stage.setTitle("Detalles del Proyecto");

                // Configurar la modalidad (bloquea la ventana principal)
                stage.initModality(Modality.APPLICATION_MODAL);

                // Configurar el estilo para quitar la barra de título
                stage.initStyle(StageStyle.UNDECORATED);

                stage.setScene(new Scene(root));
                stage.showAndWait(); // Mostrar y esperar hasta que se cierre
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public void editarProyecto(ActionEvent actionEvent) {
        Proyecto proyectoSeleccionado = tbProyectos.getSelectionModel().getSelectedItem();
        if (proyectoSeleccionado != null) {
            if ("Finalizado".equals(proyectoSeleccionado.getEstado())) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Alerta", "No se puede editar un proyecto finalizado.");
            } else {
                try {
                    // Cargar el archivo FXML de ActualizarProyectos.fxml
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/ActualizarProyectos.fxml"));
                    Parent root = loader.load();

                    // Obtener el controlador de la nueva ventana
                    ActualizarProyectosControlador actualizarProyectosControlador = loader.getController();

                    // Llamar a un método en el controlador de ActualizarProyectos para cargar la información del proyecto
                    actualizarProyectosControlador.cargarDatosProyecto(proyectoSeleccionado);


                    actualizarProyectosControlador.setIdProyecto(proyectoSeleccionado.getId());

                    // Crear un nuevo Stage
                    Stage stage = new Stage();
                    stage.setTitle("Actualizar Proyecto");

                    // Configurar la modalidad (bloquea la ventana principal)
                    stage.initModality(Modality.APPLICATION_MODAL);

                    // Configurar el estilo para quitar la barra de título
                    stage.initStyle(StageStyle.UNDECORATED);
                    // Imprimir el ID del proyecto para verificar
                    System.out.println("ID del Proyecto antes de abrir la ventana de actualización: " + proyectoSeleccionado.getId());
                    stage.setScene(new Scene(root));
                    stage.showAndWait(); // Mostrar y esperar hasta que se cierre
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void mostrarAlerta(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    private void eliminarProyecto(ActionEvent actionEvent) {
        Proyecto proyectoSeleccionado = tbProyectos.getSelectionModel().getSelectedItem();
        if (proyectoSeleccionado != null) {


            // Elimina el proyecto de la base de datos y de la tabla
            try (Connection conn = Conexion.obtenerConexion();
                 Statement stmt = conn.createStatement()) {
                String query = "DELETE FROM tbProyectos WHERE idproyecto = " + proyectoSeleccionado.getId();
                stmt.executeUpdate(query);
                cargarDatosProyectos(); // Recarga los datos en la tabla después de eliminar
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void Abrir(ActionEvent actionEvent) {
        try {
            // Cargar el archivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/ProyectosAgregar.fxml"));
            Parent root = loader.load();

            // Crear un nuevo Stage
            Stage stage = new Stage();
            stage.setTitle("Nueva Ventana");

            // Configurar la modalidad (bloquea la ventana principal)
            stage.initModality(Modality.APPLICATION_MODAL);

            // Configurar el estilo para quitar la barra de título
            stage.initStyle(StageStyle.UNDECORATED);

            stage.setScene(new Scene(root));
            stage.showAndWait(); // Mostrar y esperar hasta que se cierre

            // La ventana se desbloqueará una vez que se cierre la ventana secundaria
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refrescar(javafx.event.ActionEvent actionEvent){
        cargarDatosProyectos();
    }




    // Dentro del método cargarDatos() en la clase EmpleadosControlador
    public void cargarDatosProyectos() {
        tbProyectos.getItems().clear(); // Limpiar los elementos existentes

        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT P.idproyecto, P.nombre_proyecto, P.lugar_proyecto, P.horas_trabajo, P.fechaInicio, P.FechaFin, E.Estado_proyecto FROM tbProyectos P JOIN tbEstadoProyectos E ON P.idEstadoProyecto = E.idEstadoProyecto;")) {

            // Crear columnas dinámicamente si es necesario
            if (tbProyectos.getColumns().isEmpty()) {
                ObservableList<TableColumn<Proyecto, ?>> columnas = tbProyectos.getColumns();

                // Agrega columnas necesarias para Proyecto (ajusta según sea necesario)
                TableColumn<Proyecto, Integer> colId = new TableColumn<>("ID");
                colId.setCellValueFactory(new PropertyValueFactory<>("id"));
                columnas.add(colId);

                TableColumn<Proyecto, String> colNombre = new TableColumn<>("Nombre");
                colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
                columnas.add(colNombre);

                TableColumn<Proyecto, String> colLugar = new TableColumn<>("Lugar");
                colLugar.setCellValueFactory(new PropertyValueFactory<>("lugar"));
                columnas.add(colLugar);

                TableColumn<Proyecto, Integer> colHorasTrabajo = new TableColumn<>("Horas de trabajo");
                colHorasTrabajo.setCellValueFactory(new PropertyValueFactory<>("horas"));
                columnas.add(colHorasTrabajo);

                TableColumn<Proyecto, String> colFechaInicio = new TableColumn<>("Fecha de inicio");
                colFechaInicio.setCellValueFactory(new PropertyValueFactory<>("inicio"));
                columnas.add(colFechaInicio);

                TableColumn<Proyecto, String> colFechaFin = new TableColumn<>("Fecha de fin");
                colFechaFin.setCellValueFactory(new PropertyValueFactory<>("Final"));
                columnas.add(colFechaFin);

                TableColumn<Proyecto, String> colEstado = new TableColumn<>("Estado");
                colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
                columnas.add(colEstado);
            }

            // Agregar datos a la tabla
            while (rs.next()) {
                int id = rs.getInt("idproyecto");
                String nombre = rs.getString("nombre_proyecto");
                String lugarProyecto = rs.getString("lugar_proyecto");
                int horasTrabajo = rs.getInt("horas_trabajo");
                String fechaInicio = rs.getString("fechaInicio");
                String fechaFin = rs.getString("FechaFin");
                String estadoProyecto = rs.getString("Estado_proyecto");

                tbProyectos.getItems().add(new Proyecto(id, nombre, lugarProyecto, horasTrabajo, fechaInicio, fechaFin, estadoProyecto));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
