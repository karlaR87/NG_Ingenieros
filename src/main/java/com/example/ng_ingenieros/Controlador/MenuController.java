package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.PanelesProyectos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import javafx.scene.control.Button;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML
    public FlowPane panelUsuarios;


    public void mostrarUsuarios() {

        Stage st = new Stage();

        panelUsuarios.getChildren().clear();
        panelUsuarios.setPadding(new Insets(10));
        panelUsuarios.setVgap(10);
        panelUsuarios.setHgap(10);
        ScrollPane scrollPane = new ScrollPane(panelUsuarios);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        VBox root = new VBox(scrollPane); // Agrega el ScrollPane en lugar del panel de usuarios directamente
        Scene scene = new Scene(root, 800, 600);
        st.setScene(scene);


        st.show();


        try (Connection connection = Conexion.obtenerConexion()) {
            String sql = "select pr.idproyecto, pr.nombre_proyecto, pr.lugar_proyecto, pr.horas_trabajo, pr.fechaInicio, pr.FechaFin,\n" +
                    "es.Estado_proyecto from tbProyectos pr\n" +
                    "inner join tbEstadoProyectos es on es.idEstadoProyecto = pr.idEstadoProyecto";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("idproyecto");
                String nombre = resultSet.getString("nombre_proyecto");
                String lugar = resultSet.getString("lugar_proyecto");
                int horasss = resultSet.getInt("horas_trabajo");
                String fechaInicio = resultSet.getString("fechaInicio");
                String fechafin = resultSet.getString("FechaFin");
                String ide = resultSet.getString("Estado_proyecto");

                // Crear un nuevo panel gris para mostrar el usuario
                Pane panelUsuario = new Pane();

                panelUsuario.setMinSize(100, 150);
                CornerRadii cornerRadii = new CornerRadii(10);

                // Crea un relleno de fondo con un gradiente lineal
                Stop[] stops = new Stop[]{new Stop(0, Color.LIGHTGRAY), new Stop(1, Color.DARKGRAY)};
                LinearGradient linearGradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
                BackgroundFill backgroundFill = new BackgroundFill(linearGradient, cornerRadii, Insets.EMPTY);
                Background background = new Background(backgroundFill);

                // Aplica el fondo redondeado al panel de usuarios
                panelUsuario.setBackground(background);

                // Agregar texto de usuario y email al panel
                javafx.scene.control.Label labelUsuario = new javafx.scene.control.Label("Proyecto N° " + id);
                labelUsuario.setFont(javafx.scene.text.Font.font("Times New Roman", FontWeight.BOLD, 27));
                labelUsuario.setAlignment(Pos.TOP_CENTER);

                javafx.scene.control.Label labelEmail = new javafx.scene.control.Label("Nombre del proyecto: " + nombre);
                labelEmail.setFont(javafx.scene.text.Font.font("Times New Roman", FontWeight.NORMAL, 14));

                javafx.scene.control.Label labellugar = new javafx.scene.control.Label("Ubicación: " + lugar);
                labellugar.setFont(javafx.scene.text.Font.font("Times New Roman", FontWeight.NORMAL, 14));

                javafx.scene.control.Label labelUsuario1 = new javafx.scene.control.Label("Horas de trabajo: " + horasss);
                labelUsuario1.setFont(javafx.scene.text.Font.font("Times New Roman", FontWeight.NORMAL, 14));

                javafx.scene.control.Label labelEmail2 = new javafx.scene.control.Label("Fecha de inicio: " + fechaInicio);
                labelEmail2.setFont(javafx.scene.text.Font.font("Times New Roman", FontWeight.NORMAL, 14));

                javafx.scene.control.Label labellugar2 = new javafx.scene.control.Label("Fecha de finalizacion: " + fechafin);
                labellugar2.setFont(javafx.scene.text.Font.font("Times New Roman", FontWeight.NORMAL, 14));

                javafx.scene.control.Label labellugar3 = new javafx.scene.control.Label("Estado: " + ide);
                labellugar3.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 14));

                // Aplicar márgenes para crear espacio
                VBox.setMargin(labelUsuario, new Insets(10)); // Ajusta los valores de los márgenes según tus preferencias
                VBox.setMargin(labelEmail, new Insets(10));   // Ajusta los valores de los márgenes según tus preferencias
                VBox.setMargin(labellugar, new Insets(10));
                VBox.setMargin(labelUsuario1, new Insets(10));
                VBox.setMargin(labelEmail2, new Insets(10));
                VBox.setMargin(labellugar2, new Insets(10));
                VBox.setMargin(labellugar3, new Insets(10));
                VBox vBox = new VBox(labelUsuario, labelEmail, labellugar, labelUsuario1, labelEmail2, labellugar2, labellugar3);//agrega el label con la informacion

                panelUsuario.setOnMouseClicked(event -> {
                    int proyectoSeleccionadoId = id;
                    String proyectoSeleccionadoNombre = nombre;
                    String proyectoSeleccionadoLugar = lugar;
                    int proyectoSeleccionadoHoras = horasss;
                    String proyectoSeleccionadoFechaInicio = fechaInicio;
                    String proyectoSeleccionadoFechaFin = fechafin;
                    String proyectoSeleccionadoEstado = ide;

                    cargarDetallesProyecto(proyectoSeleccionadoId, proyectoSeleccionadoNombre, proyectoSeleccionadoLugar,
                            proyectoSeleccionadoHoras, proyectoSeleccionadoFechaInicio, proyectoSeleccionadoFechaFin,
                            proyectoSeleccionadoEstado);

                });

                panelUsuario.getChildren().add(vBox);


                panelUsuarios.getChildren().add(panelUsuario);








            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cargarDetallesProyecto(int id, String nombre, String lugar, int horas, String fechaInicio,
                                        String fechaFin, String estado) {


        // Crear una nueva ventana (Stage) y una nueva escena (Scene) para mostrar los detalles del proyecto
        Stage detallesStage = new Stage();


        FlowPane panelUsuario = new FlowPane();

        Pane panelVista = new Pane();

        panelVista.setMinSize(200, 300);
        CornerRadii cornerRadii = new CornerRadii(10);

        // Crea un relleno de fondo con un gradiente lineal
        Stop[] stops = new Stop[]{new Stop(0, Color.LIGHTGRAY), new Stop(1, Color.DARKGRAY)};
        LinearGradient linearGradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
        BackgroundFill backgroundFill = new BackgroundFill(linearGradient, cornerRadii, Insets.EMPTY);
        Background background = new Background(backgroundFill);

        panelVista.setBackground(background);


        // Agregar labels con los detalles del proyecto al VBox
        Label labelUsuario = new Label("Proyecto N° " + id);
        labelUsuario.setFont(Font.font("Times New Roman", FontWeight.BOLD, 27));
        labelUsuario.setAlignment(Pos.TOP_CENTER);

        Label labelEmail = new Label("Nombre del proyecto: " + nombre);
        labelEmail.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 14));

        Label labellugar = new Label("Ubicación: " + lugar);
        labellugar.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 14));

        Label labelUsuario1 = new Label("Horas de trabajo: " + horas);
        labelUsuario1.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 14));

        Label labelEmail2 = new Label("Fecha de inicio: " + fechaInicio);
        labelEmail2.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 14));

        Label labellugar2 = new Label("Fecha de finalizacion: " + fechaFin);
        labellugar2.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 14));

        Label labellugar3 = new Label("Estado: " + estado);
        labellugar3.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 14));

        Label labelEspacio = new Label("");



        Label labellugar4 = new Label("NG Ingenieros");
        labellugar4.setFont(Font.font("Times New Roman", FontWeight.BOLD, 10));


        VBox.setMargin(labelUsuario, new Insets(10)); // Ajusta los valores de los márgenes según tus preferencias
        VBox.setMargin(labelEmail, new Insets(10));   // Ajusta los valores de los márgenes según tus preferencias
        VBox.setMargin(labellugar, new Insets(10));
        VBox.setMargin(labelUsuario1, new Insets(10));
        VBox.setMargin(labelEmail2, new Insets(10));
        VBox.setMargin(labellugar2, new Insets(10));
        VBox.setMargin(labellugar3, new Insets(10));
        VBox.setMargin(labelEspacio, new Insets(10));
        VBox.setMargin(labellugar4, new Insets(20));
        VBox vBox = new VBox(labelUsuario, labelEmail, labellugar, labelUsuario1, labelEmail2, labellugar2, labellugar3,labelEspacio, labellugar4);//agrega el label con la informacion



        ScrollPane scroll = new ScrollPane(panelUsuario);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);

        // Crear una nueva escena para el panelUsuario y establecer el VBox como su contenido
        Scene detallesScene = new Scene(scroll, 500, 300);


        panelUsuario.getChildren().add(panelVista);
        panelVista.getChildren().add(vBox);




        // Establecer la escena en la ventana y mostrar la ventana
        detallesStage.setScene(detallesScene);
        detallesStage.show();


        //-----------------------------------------------------------------------
    /*private void eliminarPanelesSeleccionados(FlowPane panelUsuarios) {
        // Obtener todos los paneles seleccionados
        for (Node node : panelUsuarios.getChildren()) {
            if (node instanceof Pane) {
                Pane panelUsuario = (Pane) node;

                // Verificar si el panel está seleccionado (puedes aj ustar esta lógica según tus necesidades)
                if (panelUsuario.getStyle().contains("-fx-background-color: lightgray")) {
                    // Obtener el ID del proyecto del panel
                    int idProyecto = Integer.parseInt(((Label) ((VBox) panelUsuario.getChildren().get(0)).getChildren().get(0)).getText().replace("ID: ", ""));

                    // Eliminar el proyecto de la base de datos
                    eliminarProyecto(idProyecto);

                    // Eliminar el panel del FlowPane
                    panelUsuarios.getChildren().remove(panelUsuario);
                }

            }
        }*/

    }

    @FXML
    private BorderPane NuevoIdParaElCentro;


    @FXML
    private void abrirInicio(ActionEvent event) {


        loadWindow("/com/example/ng_ingenieros/Incio.fxml");
        this.mostrarUsuarios();
    }



    @FXML
    private void abrirProyectos(ActionEvent event) {


        loadWindow("/com/example/ng_ingenieros/Proyectos.fxml");
    }

    @FXML
    private void abrirEmpleados(ActionEvent event) {
        // Cambiar el color de fondo y el color del texto al hacer clic en el botón "Empleados"


        loadWindow("/com/example/ng_ingenieros/Empleados.fxml");


    }

    private void loadWindow(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            NuevoIdParaElCentro.setCenter(root);  // Establecer el root en el centro del BorderPane
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Cargar la ventana "Inicio" en el panel central al iniciar la aplicación

    }


}
