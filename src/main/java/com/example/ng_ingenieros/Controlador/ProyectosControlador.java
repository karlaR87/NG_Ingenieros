package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.Controlador.AlertDos;
import com.example.ng_ingenieros.CustomAlert;
import com.example.ng_ingenieros.Empleados;
import com.example.ng_ingenieros.Proyecto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
    @FXML
    private TextField txtBusqueda;

    private ObservableList<Proyecto> listaProyectos = FXCollections.observableArrayList();

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

    public void initialize() {
        // Configura el evento de clic para el botón
        btnEmilinar.setOnAction(this::eliminarProyecto);
        btnEditar.setOnAction(this::editarProyecto);

        btnAgregar.setOnAction(this::Abrir);
        tbProyectos.setItems(listaProyectos);

        configurarTabla();


        tbProyectos.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                // Detecta el doble clic y llama a la función para abrir la ventana
                abrirVentanaDetalle();
            }
        });

        txtBusqueda.setOnKeyReleased(event -> {

            buscarDatos(txtBusqueda.getText());

        });

        cargarDatosProyectos();
    }
    private void configurarTabla() {
        // Configurar la tabla para permitir selección múltiple
        tbProyectos.getSelectionModel().setCellSelectionEnabled(false);
        tbProyectos.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
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
                CustomAlert customAlert = new CustomAlert();
                customAlert.mostrarAlertaPersonalizada("Alerta", "No se puede editar un proyecto finalizado.", (Stage) btnEditar.getScene().getWindow());

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
        // Obtener los proyectos seleccionados
        ObservableList<Proyecto> proyectosSeleccionados = tbProyectos.getSelectionModel().getSelectedItems();

        if (!proyectosSeleccionados.isEmpty()) {
            AlertDos alertDos = new AlertDos();
            // Mostrar una confirmación antes de eliminar utilizando tu clase AlertDos
            boolean confirmacion = alertDos.mostrarAlerta("¿Está seguro de que desea eliminar los proyectos seleccionados?", "Confirmación");

            if (confirmacion) {
                // Elimina los proyectos de la base de datos y de la tabla
                try (Connection conn = Conexion.obtenerConexion();
                     Statement stmt = conn.createStatement()) {
                    for (Proyecto proyectoSeleccionado : proyectosSeleccionados) {
                        String query = "DELETE FROM tbProyectos WHERE idproyecto = " + proyectoSeleccionado.getId();
                        stmt.executeUpdate(query);
                    }
                    cargarDatosProyectos(); // Recarga los datos en la tabla después de eliminar
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            // Muestra un mensaje si no se seleccionan proyectos
            CustomAlert alertDos = new CustomAlert();
            alertDos.mostrarAlertaPersonalizada("Alerta", "Por favor, seleccione al menos un proyecto para eliminar.", (Stage) btnEmilinar.getScene().getWindow());
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

            ProyectosAgregarControlador proyectosAgregarControlador=new ProyectosAgregarControlador();
            proyectosAgregarControlador = loader.getController();
            // Configurar la modalidad (bloquea la ventana principal)
            stage.initModality(Modality.APPLICATION_MODAL);

            // Configurar el estilo para quitar la barra de título
            stage.initStyle(StageStyle.UNDECORATED);

            stage.setScene(new Scene(root));
            stage.showAndWait(); // Mostrar y esperar hasta que se cierre

            refrescar(actionEvent);
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

    public void buscarDatos(String busqueda) {
        tbProyectos.getItems().clear(); // Limpiar los elementos existentes

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement("SELECT P.idproyecto, P.nombre_proyecto, P.lugar_proyecto, P.horas_trabajo, P.fechaInicio, P.FechaFin, E.Estado_proyecto \n" +
                     "FROM tbProyectos P \n" +
                     "JOIN tbEstadoProyectos E ON P.idEstadoProyecto = E.idEstadoProyecto\n" +
                     "WHERE P.idproyecto LIKE ? OR P.nombre_proyecto LIKE ? OR P.lugar_proyecto LIKE ? OR P.horas_trabajo LIKE ? OR P.fechaInicio LIKE ? OR P.FechaFin LIKE ? OR E.Estado_proyecto LIKE ?;\n")) {

            String parametroBusqueda = "%" + busqueda + "%";
            for (int i = 1; i <= 7; i++) {
                stmt.setString(i, parametroBusqueda);
            }

            ResultSet rs = stmt.executeQuery();
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




        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

