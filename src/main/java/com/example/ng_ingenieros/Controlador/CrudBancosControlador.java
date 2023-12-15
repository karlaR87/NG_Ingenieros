package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.ng_ingenieros.Validaciones;

import javax.swing.*;
public class CrudBancosControlador {

    @FXML
    private TableView tbBanco;
    @FXML
    private Button btnAgregarBanco, btnEditarBanco, btnEliminarBanco, btnAtras;
    @FXML
    private TextField txtBusqueda;



    public void initialize() {

        // Configura el evento de clic para el botón
        btnAgregarBanco.setOnAction(this::btnAgregarBancoOnAction);
        btnEditarBanco.setOnAction(this::btnEditarBancoOnAction);
        btnEliminarBanco.setOnAction(this::btnEliminarOnAction);

        cargarDatos();

        txtBusqueda.setOnKeyReleased(event -> {

            buscarDatos(txtBusqueda.getText());

        });

    }

    public void setTableBanco(TableView<Bancos> tbBanco) {
        this.tbBanco = tbBanco;
    }
    private void btnAgregarBancoOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/AgregarBancos.fxml"));
            Parent root = loader.load();


            AgregarBancosControlador agregarBancosControlador = loader.getController();
            agregarBancosControlador.setTableBanco(tbBanco);

            Stage stage = new Stage();
            stage.setTitle("Nueva");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btnEditarBancoOnAction(ActionEvent event){
        Bancos bancoSeleccionado = (Bancos) tbBanco.getSelectionModel().getSelectedItem();

        if (bancoSeleccionado != null) {
            // Crear y mostrar la ventana de actualización con los datos de la fila seleccionada
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/ActualizarBanco.fxml"));
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


    private void eliminarBanco() {
        // Obtener el ID del proyecto seleccionado (asumiendo que tienes una variable para almacenar el ID)
        int idBanco = obtenerIdBancoSeleccionado();

        try (Connection connection = Conexion.obtenerConexion()) {
            String sql = "DELETE FROM tbBancos WHERE idBanco = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idBanco);

            int filasAfectadas = statement.executeUpdate();
            if (filasAfectadas > 0) {
                agregar_empleadosControlador.mostrarAlerta("Eliminación de datos","Se eliminaron los datos exitosamente", Alert.AlertType.INFORMATION);

            } else {
                agregar_empleadosControlador.mostrarAlerta("Alerta","No se encontro ningun empleado", Alert.AlertType.WARNING);
            }
            cargarDatos();
        } catch (SQLException e) {
            e.printStackTrace();
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
