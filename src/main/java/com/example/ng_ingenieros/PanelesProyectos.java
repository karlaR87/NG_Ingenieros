package com.example.ng_ingenieros;
import javafx.fxml.FXML;
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

    @FXML
    private VBox contenedorPrincipal;

    public void initialize()
    {
        cargarDatosDesdeDB();
    }

    private void cargarDatosDesdeDB() {
        // Conexión a la base de datos
        contenedorPrincipal.setStyle("-fx-background-color: #616E73");

        try {
            Connection conn = Conexion.obtenerConexion();

            String sql = "SELECT *\n" +
                    "FROM (\n" +
                    "    SELECT p.idproyecto, p.nombre_proyecto, p.lugar_proyecto, p.horas_trabajo, p.fechaInicio, p.FechaFin, pe.Estado_proyecto\n" +
                    "    FROM tbProyectos p\n" +
                    "\tInner join tbEstadoProyectos pe on pe.idEstadoProyecto = p.idEstadoProyecto\n" +
                    "    WHERE Estado_proyecto = 'Finalizado'\n" +
                    "\n" +
                    "    UNION ALL\n" +
                    "\n" +
                    "     SELECT p.idproyecto, p.nombre_proyecto, p.lugar_proyecto, p.horas_trabajo, p.fechaInicio, p.FechaFin, pe.Estado_proyecto\n" +
                    "    FROM tbProyectos p\n" +
                    "\tInner join tbEstadoProyectos pe on pe.idEstadoProyecto = p.idEstadoProyecto\n" +
                    "    WHERE Estado_proyecto = 'En Ejecución'\n" +
                    ") AS Proyectos\n" +
                    "ORDER BY Estado_proyecto, CASE WHEN Estado_proyecto = 'En Ejecución' THEN fechaInicio END DESC;";

            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("idproyecto");
                String nombreProyecto = resultSet.getString("nombre_proyecto");
                String lugarProyecto = resultSet.getString("lugar_proyecto");
                int horasTrabajo = resultSet.getInt("horas_trabajo");
                String fechaInicio = resultSet.getString("fechaInicio");
                String fechaFIIn = resultSet.getString("FechaFin");
                String estado = resultSet.getString("Estado_proyecto");

                // Crear un Panel para cada conjunto de datos
                Pane panel = new Pane();
                panel.setStyle("-fx-background-color: #333333; -fx-padding: 10px; -fx-spacing: 5px;");

                // Crear etiquetas para mostrar los datos
                Label labelID = new Label("Proyecto N° " + id);
                Label labelProyecto = new Label("Proyecto: " + nombreProyecto);
                Label labelLugar = new Label("Lugar: " + lugarProyecto);
                Label labelHoras = new Label("Horas de Trabajo: " + horasTrabajo);
                Label labelfechai = new Label("Fecha de inicio: " + fechaInicio);
                Label labelfechaf = new Label("Fecha de finalización: " + fechaFIIn);
                Label labelestado = new Label("Estado del proyecto: " + estado);

                // Establecer color de texto
                labelID.setTextFill(Color.WHITE);
                labelProyecto.setTextFill(Color.WHITE);
                labelLugar.setTextFill(Color.WHITE);
                labelHoras.setTextFill(Color.WHITE);
                labelfechai.setTextFill(Color.WHITE);
                labelfechaf.setTextFill(Color.WHITE);
                labelestado.setTextFill(Color.WHITE);

                // Posicionar etiquetas dentro del Panel
                labelID.setLayoutY(10);
                labelProyecto.setLayoutY(40);
                labelLugar.setLayoutY(70);
                labelHoras.setLayoutY(100);
                labelfechai.setLayoutY(130);
                labelfechaf.setLayoutY(160);
                labelestado.setLayoutY(190);

                // Agregar etiquetas al Panel
                panel.getChildren().addAll(labelID, labelProyecto, labelLugar, labelHoras, labelfechai, labelfechaf, labelestado);

                // Crear un VBox para cada Panel y agregar el Panel al VBox
                VBox vboxDato = new VBox(panel);
                vboxDato.setStyle("-fx-padding: 10px; -fx-spacing: 5px;");

                // Agregar el VBox al VBox principal
                contenedorPrincipal.getChildren().add(vboxDato);


                panel.setOnMouseClicked(event -> {
                    int proyectoSeleccionadoId = id;
                    String proyectoSeleccionadoNombre = nombreProyecto;
                    String proyectoSeleccionadoLugar = lugarProyecto;
                    int proyectoSeleccionadoHoras = horasTrabajo;
                    String proyectoSeleccionadoFechaInicio = fechaInicio;
                    String proyectoSeleccionadoFechaFin = fechaFIIn;
                    String proyectoSeleccionadoEstado = estado;

                    cargarDetallesProyecto(proyectoSeleccionadoId, proyectoSeleccionadoNombre, proyectoSeleccionadoLugar,
                            proyectoSeleccionadoHoras, proyectoSeleccionadoFechaInicio, proyectoSeleccionadoFechaFin,
                            proyectoSeleccionadoEstado);

                });
            }

            resultSet.close();
            statement.close();
            Conexion.cerrarConexion();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //ventana nueva
    private void cargarDetallesProyecto(int id, String nombre, String lugar, int horas, String fechaIn, String fechaFi, String est) {


        Stage stage = new Stage();

        FlowPane panelMos = new FlowPane();

        Pane panel = new Pane();
        panel.setStyle("-fx-background-color: #333333; -fx-padding: 10px; -fx-spacing: 5px;");
        panel.setMinSize(200, 300);


        Label labelID = new Label("Proyecto N° " + id);
        labelID.setFont(Font.font("Times New Roman", FontWeight.BOLD, 30));

        Label labelProyecto = new Label("Proyecto: " + nombre);
        labelProyecto.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 14));

        Label labelLugar = new Label("Lugar: " + lugar);
        labelLugar.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 14));

        Label labelHoras = new Label("Horas de Trabajo: " + horas);
        labelHoras.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 14));

        Label labelfechai = new Label("Fecha de inicio: " + fechaIn);
        labelfechai.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 14));

        Label labelfechaf = new Label("Fecha de finalización: " + fechaFi);
        labelfechaf.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 14));

        Label labelestado = new Label("Estado del proyecto: " + est);
        labelestado.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 14));

        // Establecer color de texto
        labelID.setTextFill(Color.WHITE);
        labelProyecto.setTextFill(Color.WHITE);
        labelLugar.setTextFill(Color.WHITE);
        labelHoras.setTextFill(Color.WHITE);
        labelfechai.setTextFill(Color.WHITE);
        labelfechaf.setTextFill(Color.WHITE);
        labelestado.setTextFill(Color.WHITE);

        // Posicionar etiquetas dentro del Panel
        labelID.setLayoutY(10);
        labelProyecto.setLayoutY(70);
        labelLugar.setLayoutY(100);
        labelHoras.setLayoutY(130);
        labelfechai.setLayoutY(160);
        labelfechaf.setLayoutY(190);
        labelestado.setLayoutY(220);

        // Agregar etiquetas al Panel
        panel.getChildren().addAll(labelID, labelProyecto, labelLugar, labelHoras, labelfechai, labelfechaf, labelestado);

        // Crear un VBox para cada Panel y agregar el Panel al VBox
        VBox vboxDato = new VBox(panel);
        vboxDato.setStyle("-fx-padding: 10px; -fx-spacing: 5px;");

        // Agregar el VBox al VBox principal

        panelMos.getChildren().add(panel);
        panel.getChildren().add(vboxDato);

        ScrollPane scroll = new ScrollPane(panelMos);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);

        Scene detallesScene = new Scene(scroll, 500, 300);
        stage.setScene(detallesScene);
        stage.show();
    }




}
