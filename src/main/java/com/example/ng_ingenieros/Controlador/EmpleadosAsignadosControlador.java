package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.CustomAlert;
import com.example.ng_ingenieros.Empleados;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class EmpleadosAsignadosControlador {

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnAgregar2;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnActualizar;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnAgregar_ListEmpleados;

    @FXML
    private TextField txtBusqueda;


    @FXML
    private TableView<Empleados> tbEmpleados;

    private ObservableList<Empleados> empleados = FXCollections.observableArrayList();

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
        btnCancelar.setOnAction(this::cerrarVentana);
        btnAgregar2.setOnAction(this::abrirAgregar);
        btnEliminar.setOnAction(this::eliminarEmpleado);
        btnActualizar.setOnAction(this::abrirActualizar);
        btnAgregar_ListEmpleados.setOnAction(this::AgregarList);
        btnGuardar.setOnAction(this::AbrirGestion);
        tbEmpleados.setItems(empleados);

        txtBusqueda.textProperty().addListener((observable, oldValue, newValue) -> buscarDatos(null));



    }

    private boolean empleadoExisteEnBaseDeDatos(Empleados empleado) {
        // Lógica para verificar si el empleado existe en la base de datos
        String consultaSQL = "SELECT COUNT(*) AS CantidadEmpleados FROM tbempleados WHERE dui = ?";

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(consultaSQL)) {
            pstmt.setString(1, empleado.getDui());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int cantidadEmpleados = rs.getInt("CantidadEmpleados");
                    return cantidadEmpleados > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @FXML
    private void AbrirGestion(ActionEvent actionEvent) {
        try {
            // Validar que no haya más de 1 ingeniero con idcargo 7 en la lista de empleados
            if (!validarIngenierosCargo7(empleados)) {
                CustomAlert customAlert = new CustomAlert();
                customAlert.mostrarAlertaPersonalizada("Error", "Solo se permite agregar un ingeniero con ID de cargo 7.", (Stage) btnGuardar.getScene().getWindow());
                return;
            }

            // Cargar el archivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/ProyectosAgregar.fxml"));
            Parent root = loader.load();

            // Acceso al controlador de la nueva ventana
            ProyectosAgregarControlador proyectosAgregarControlador = loader.getController();

            // Pasar la lista de empleados al controlador de "ProyectosAgregarControlador"
            proyectosAgregarControlador.setEmpleadosProyecto(empleados);

            System.out.println("Datos enviados a ProyectosAgregarControlador.");

            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setEmpleadoselect(List<Empleados> nuevosEmpleados) {
        empleados.addAll(nuevosEmpleados);
        tbEmpleados.setItems(empleados);

    }



    private boolean validarIngenierosCargo7(List<Empleados> empleados) {
        // Construir una cadena de IDs de empleados seleccionados para la consulta SQL
        StringBuilder idEmpleados = new StringBuilder();
        for (Empleados empleado : empleados) {
            idEmpleados.append(empleado.getId()).append(",");
        }
        idEmpleados.deleteCharAt(idEmpleados.length() - 1); // Eliminar la última coma

        // Consultar la base de datos para contar la cantidad de ingenieros con idcargo 7 en la selección
        String consultaSQL = "SELECT COUNT(*) AS conteo FROM tbempleados WHERE idempleado IN (" + idEmpleados + ") AND idcargo = 7";

        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(consultaSQL)) {

            if (rs.next()) {
                int conteoIngenierosCargo7 = rs.getInt("conteo");
                return conteoIngenierosCargo7 <= 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false; // En caso de error, considerarlo como que no se cumple la validación
    }
    public List<Empleados> getEmpleadosAElegirProyecto() {
        return empleados;
    }



    private void abrirActualizar(ActionEvent event) {
        // Obtiene el empleado seleccionado
        Empleados empleadoSeleccionado = tbEmpleados.getSelectionModel().getSelectedItem();

        if (empleadoSeleccionado != null) {
            // Verifica si el empleado ya está en el sistema
                if (empleadoExisteEnBaseDeDatos(empleadoSeleccionado)) {
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Alerta", "No se puede actualizar. El empleado ya está en la base de datos.");
                }else {
                    try {
                        // Carga la nueva ventana
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/EmpleadosAProyectosActualizar.fxml"));
                        Parent root = loader.load();
                        Stage stageNueva = new Stage();
                        // Configurar la modalidad (bloquea la ventana principal)
                        stageNueva.initModality(Modality.APPLICATION_MODAL);
                        stageNueva.initStyle(StageStyle.UNDECORATED);

                        // Accede al controlador de la nueva ventana
                        EmpleadosAProyectosActualizarControlador actualizarController = loader.getController();

                        // Llama al método para cargar los datos del empleado seleccionado
                        actualizarController.cargarDatosEmpleado(empleadoSeleccionado);

                        stageNueva.setScene(new Scene(root));
                        // Muestra la nueva ventana
                        stageNueva.showAndWait();

                        // Actualiza los atributos del empleado existente en lugar de agregar uno nuevo
                        empleadoSeleccionado.setNombre(actualizarController.getPersonas().getNombre());
                        empleadoSeleccionado.setDui(actualizarController.getPersonas().getDui());
                        empleadoSeleccionado.setCorreo(actualizarController.getPersonas().getCorreo());
                        empleadoSeleccionado.setCargo(actualizarController.getPersonas().getCargo());
                        empleadoSeleccionado.setPlaza(actualizarController.getPersonas().getPlaza());
                        empleadoSeleccionado.setSueldoHora(actualizarController.getPersonas().getSueldoHora());
                        empleadoSeleccionado.setCuentaBancaria(actualizarController.getPersonas().getCuentaBancaria());
                        empleadoSeleccionado.setSueldoDia(actualizarController.getPersonas().getSueldoDia());

                        // Actualiza la tabla con las personas
                        tbEmpleados.refresh();
                        tbEmpleados.setItems(empleados);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


        } else {
            System.out.println("No hay empleado seleccionado.");
        }
    }
    private void mostrarAlerta(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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

    @FXML
    private void AgregarList(ActionEvent event) {
        try {
            // Carga la nueva ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/EmpleadosAElegir.fxml"));
            Parent root = loader.load();
            Stage stageNueva = new Stage();
            // Configurar la modalidad (bloquea la ventana principal)
            stageNueva.initModality(Modality.APPLICATION_MODAL);
            stageNueva.initStyle(StageStyle.UNDECORATED);


            // Accede al controlador de la nueva ventana
            EmpleadosAElegirControlador empleadosAElegir = loader.getController();

            stageNueva.setScene(new Scene(root));
            // Muestra la nueva ventana
            stageNueva.showAndWait();

            // Agrega las personas desde agregarPersonasController a la lista
            empleados.addAll(empleadosAElegir.getEmpleadosAElegir());

            // Actualiza la tabla con las personas
            tbEmpleados.setItems(empleados);

            // Cierra la ventana de AgregarPersonasController
            stageNueva.close();
        }

     catch (IOException e) {
        e.printStackTrace();

        }
    }


    private void cerrarVentana(javafx.event.ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void setEmpleadosProyecto(ObservableList<Empleados> empleadosProyecto) {
        empleados.setAll(empleadosProyecto);
        tbEmpleados.setItems(empleados);

        System.out.println("Datos recibidos de empleados nuevo en EmpleadosAsignadosControlador: " + empleadosProyecto);
        System.out.println("Número de empleados recibidos: " + empleados.size());
    }

    private void buscarDatos(ActionEvent event) {
        // Obtén el texto ingresado en el campo de búsqueda
        String busqueda = txtBusqueda.getText().toLowerCase();

        // Crea una nueva lista para almacenar los empleados que coincidan con la búsqueda
        ObservableList<Empleados> empleadosFiltrados = FXCollections.observableArrayList();

        // Itera sobre la lista original y agrega los empleados que coincidan con la búsqueda
        for (Empleados empleado : empleados) {
            if (contieneTexto(empleado.getNombre(), busqueda) ||
                    contieneTexto(empleado.getDui(), busqueda) ||
                    contieneTexto(empleado.getCorreo(), busqueda) ||
                    contieneTexto(empleado.getCargo(), busqueda) ||
                    contieneTexto(empleado.getPlaza(), busqueda) ||
                    contieneTexto(String.valueOf(empleado.getSueldoHora()), busqueda) ||
                    contieneTexto(String.valueOf(empleado.getCuentaBancaria()), busqueda) ||
                    contieneTexto(String.valueOf(empleado.getSueldoDia()), busqueda)) {

                empleadosFiltrados.add(empleado);
            }
        }

        // Actualiza la tabla con los empleados filtrados
        tbEmpleados.setItems(empleadosFiltrados);
    }

    private boolean contieneTexto(String texto, String busqueda) {
        return texto != null && texto.toLowerCase().contains(busqueda);
    }
    public ObservableList<Empleados> getEmpleadosProyecto() {
        return empleados;
    }
}
