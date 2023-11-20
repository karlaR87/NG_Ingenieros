package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Empleados;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class EmpleadosAsignadosControlador {

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnAgregar2;

    @FXML
    private Button btnEliminar;


    @FXML
    private TableView<Empleados> tbEmpleados;

    private ObservableList<Empleados> empleados = FXCollections.observableArrayList();


    public void initialize() {
        // Configura el evento de clic para el botón
        btnCancelar.setOnAction(this::cerrarVentana);
        btnAgregar2.setOnAction(this::abrirAgregar);
        btnEliminar.setOnAction(this::eliminarEmpleado);

    }

    private void eliminarEmpleado(ActionEvent event) {
        // Obtiene la fila seleccionada
        Empleados empleadoSeleccionado = tbEmpleados.getSelectionModel().getSelectedItem();

        // Verifica si hay una fila seleccionada
        if (empleadoSeleccionado != null) {
            // Elimina el empleado de la lista
            empleados.remove(empleadoSeleccionado);

            // Actualiza la tabla con las personas
            tbEmpleados.setItems(empleados);
        } else {
            // Muestra un mensaje si no hay una fila seleccionada
            System.out.println("No hay empleado seleccionado para eliminar.");
        }
    }
    @FXML
    private void abrirAgregar(ActionEvent event) {
        try {
            // Carga la nueva ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/AgregarEmpleadosAProyectos.fxml"));
            Parent root = loader.load();
            Stage stageNueva = new Stage();
            // Configurar la modalidad (bloquea la ventana principal)
            stageNueva.initModality(Modality.APPLICATION_MODAL);
            stageNueva.initStyle(StageStyle.UNDECORATED);

            // Accede al controlador de la nueva ventana
            AgregarEmpleadosAProyectosC agregarEmpleadosAProyectosC = loader.getController();

            stageNueva.setScene(new Scene(root));
            // Muestra la nueva ventana
            stageNueva.showAndWait();



            // Agrega las personas desde agregarPersonasController a la lista
            empleados.addAll(agregarEmpleadosAProyectosC.getPersonas());

            // Actualiza la tabla con las personas
            tbEmpleados.setItems(empleados);

            // Cierra la ventana de AgregarPersonasController
            stageNueva.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void cerrarVentana(javafx.event.ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }





}
