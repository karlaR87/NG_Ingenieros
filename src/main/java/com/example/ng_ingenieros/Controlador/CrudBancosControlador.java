package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.*;
import com.example.ng_ingenieros.Controlador.AlertDos;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.*;

public class CrudBancosControlador {

    @FXML
    private TableView tbBanco;
    @FXML
    private Button btnAgregarBanco, btnEditarBanco, btnEliminarBanco, btnRefresh;
    @FXML
    private TextField txtBusqueda;


    @FXML
    private Button btnClose;

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

    @FXML
    protected void HandleCloseAction(ActionEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }


    public void initialize() {

        // Configura el evento de clic para el botón
        btnAgregarBanco.setOnAction(this::btnAgregarBancoOnAction);
        btnEditarBanco.setOnAction(this::btnEditarBancoOnAction);
        btnEliminarBanco.setOnAction(this::btnEliminarOnAction);
        btnRefresh.setOnAction(this::btnRefreshOnAction);

        cargarDatos();
        configurarTabla();
        txtBusqueda.setOnKeyReleased(event -> {

            buscarDatos(txtBusqueda.getText());

        });

    }
    private void configurarTabla() {
        // Configurar la tabla para permitir selección múltiple
        tbBanco.getSelectionModel().setCellSelectionEnabled(false);
        tbBanco.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
    }
    public void setTableBanco(TableView<Bancos> tbBanco) {
        this.tbBanco = tbBanco;
    }
    private void btnAgregarBancoOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/AgregarBancos.fxml"));
            Parent root = loader.load();




            Stage stage = new Stage();
            stage.setTitle("Nueva");
            stage.setScene(new Scene(root));
            // Configurar la modalidad (bloquea la ventana principal)
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btnEditarBancoOnAction(ActionEvent event){
        Bancos bancoSeleccionado = (Bancos) tbBanco.getSelectionModel().getSelectedItem();

        if (bancoSeleccionado != null) {
            // Crear y mostrar la ventana de actualización con los datos de la fila seleccionada
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ActualizarBanco.fxml"));
            Parent root;
            try {

                root = loader.load();
                // Obtener el controlador de la ventana de actualización
                ActualizarBancoControlador actualizarBancoControlador = loader.getController();

                // Pasar la referencia de TableEmpleados al controlador de la ventana de actualización
                actualizarBancoControlador.setTableBancos(tbBanco);
                actualizarBancoControlador.initialize(bancoSeleccionado);

                // Mostrar la ventana de actualización
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                // Configurar la modalidad (bloquea la ventana principal)
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.show();
            } catch (IOException e) {
                // Manejo de excepciones
                e.printStackTrace();
            }
        }
    }

    private void btnEliminarOnAction(ActionEvent event){
        eliminarBanco();

    }

    private void btnRefreshOnAction(ActionEvent event){
        cargarDatos();
    }


    private void eliminarBanco() {
        // Obtener los bancos seleccionados
        ObservableList<Bancos> bancosSeleccionados = tbBanco.getSelectionModel().getSelectedItems();

        if (!bancosSeleccionados.isEmpty()) {

            AlertDos alertDos = new AlertDos();
            // Mostrar una confirmación antes de eliminar utilizando tu clase AlertDos
            boolean confirmacion = alertDos.mostrarAlerta("¿Está seguro de que desea eliminar los bancos seleccionados?", "Confirmación");

            if (confirmacion) {
                // Elimina los bancos de la base de datos y de la tabla
                try (Connection conn = Conexion.obtenerConexion();
                     Statement stmt = conn.createStatement()) {
                    for (Bancos bancoSeleccionado : bancosSeleccionados) {
                        String query = "DELETE FROM tbBancos WHERE idBanco = " + bancoSeleccionado.getIdbanco();
                        stmt.executeUpdate(query);
                    }
                    cargarDatos(); // Recarga los datos en la tabla después de eliminar
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // Muestra un mensaje si no se seleccionan bancos
            CustomAlert customAlert = new CustomAlert();
            customAlert.mostrarAlertaPersonalizada("Alerta", "Por favor, seleccione al menos un banco para eliminar.", (Stage) btnEliminarBanco.getScene().getWindow());
        }
    }


    private int obtenerIdBancoSeleccionado() {
        Bancos bancoSeleccionado = (Bancos) tbBanco.getSelectionModel().getSelectedItem();

        if (bancoSeleccionado != null) {
            return bancoSeleccionado.getIdbanco();
        } else {
            return -1; // Retorna un valor que indique que no se ha seleccionado ningún proyecto.
        }
    }

    public void cargarDatos() {
        tbBanco.getItems().clear();

        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select * from tbBancos")) {

            while (rs.next()) {
                int id = rs.getInt("idBanco");
                String nombre = rs.getString("banco");




                // Agregar los datos a la tabla
                tbBanco.getItems().add(new Bancos(id, nombre));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void buscarDatos(String busqueda) {
        tbBanco.getItems().clear(); // Limpiar los elementos actuales de la tabla

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement("select idBanco, banco from tbBancos where banco LIKE ?")) {

            // Preparar el parámetro de búsqueda para la consulta SQL
            String parametroBusqueda = "%" + busqueda + "%";

            stmt.setString(1, parametroBusqueda);


            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Obtener los datos y agregarlos a la tabla
                int id = rs.getInt("idBanco");
                String nombre = rs.getString("banco");


                tbBanco.getItems().add(new Bancos(id, nombre));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
