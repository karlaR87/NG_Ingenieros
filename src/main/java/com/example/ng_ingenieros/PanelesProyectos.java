package com.example.ng_ingenieros;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PanelesProyectos {

    public void mostrarUsuarios(FlowPane panelUsuarios, TextField campoBusqueda) {
        panelUsuarios.getChildren().clear();


        try (Connection connection = Conexion.obtenerConexion()) {
            String sql = "SELECT * FROM tbProyectos";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("idproyecto");
                String nombre = resultSet.getString("nombre_proyecto");
                String lugar = resultSet.getString("lugar_proyecto");
                int horasss = resultSet.getInt("horas_trabajo");
                String fechaInicio = resultSet.getString("fechaInicio");
                String fechafin = resultSet.getString("FechaFin");
                int ide = resultSet.getInt("idEstadoProyecto");

                // Crear un nuevo panel gris para mostrar el usuario
                Pane panelUsuario = new Pane();

                panelUsuario.setMinSize(200, 150);
                CornerRadii cornerRadii = new CornerRadii(10);

                // Crea un relleno de fondo con un gradiente lineal
                Stop[] stops = new Stop[]{new Stop(0, Color.LIGHTGRAY), new Stop(1, Color.DARKGRAY)};
                LinearGradient linearGradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
                BackgroundFill backgroundFill = new BackgroundFill(linearGradient, cornerRadii, Insets.EMPTY);
                Background background = new Background(backgroundFill);

                // Aplica el fondo redondeado al panel de usuarios
                panelUsuario.setBackground(background);

                // Agregar texto de usuario y email al panel
                Label labelUsuario = new Label("Proyecto N° " + id);
                labelUsuario.setFont(Font.font("Times New Roman", FontWeight.BOLD, 27));
                labelUsuario.setAlignment(Pos.TOP_CENTER);

                Label labelEmail = new Label("Nombre del proyecto: " + nombre);
                labelEmail.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 14));

                Label labellugar = new Label("Ubicación: " + lugar);
                labellugar.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 14));

                Label labelUsuario1 = new Label("Horas de trabajo: " + horasss);
                labelUsuario1.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 14));

                Label labelEmail2 = new Label("Fecha de inicio: " + fechaInicio);
                labelEmail2.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 14));

                Label labellugar2 = new Label("Fecha de finalizacion: " + fechafin);
                labellugar2.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 14));

                Label labellugar3 = new Label("Estado: " + ide);
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
                    int proyectoSeleccionadoEstado = ide;

                    cargarDetallesProyecto(proyectoSeleccionadoId, proyectoSeleccionadoNombre, proyectoSeleccionadoLugar,
                            proyectoSeleccionadoHoras, proyectoSeleccionadoFechaInicio, proyectoSeleccionadoFechaFin,
                            proyectoSeleccionadoEstado);

                });

                panelUsuario.getChildren().add(vBox);


                panelUsuarios.getChildren().add(panelUsuario);



                campoBusqueda.textProperty().addListener((observable, oldValue, newValue) -> {
                    String textoBusqueda = newValue.toLowerCase();

                    for (Node node : panelUsuarios.getChildren()) {
                        if (node instanceof Pane) {
                            Pane panel = (Pane) node;
                            for (Node labelNode : panel.getChildren()) {
                                if (labelNode instanceof VBox) {
                                    VBox vBoxPanel = (VBox) labelNode;
                                    for (Node label : vBoxPanel.getChildren()) {
                                        if (label instanceof Label) {
                                            String textoLabel = ((Label) label).getText().toLowerCase();
                                            panel.setVisible(textoLabel.contains(textoBusqueda));

                                            break; // Evita procesar más labels en el VBox
                                        }
                                    }
                                }
                            }
                        }
                    }

                });





            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cargarDetallesProyecto(int id, String nombre, String lugar, int horas, String fechaInicio,
                                        String fechaFin, int estado) {


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

}
