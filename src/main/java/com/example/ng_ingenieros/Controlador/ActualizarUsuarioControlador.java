package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.CustomAlert;
import com.example.ng_ingenieros.Empleados;
import com.example.ng_ingenieros.Usuarios;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ActualizarUsuarioControlador {
    private TableView<Usuarios> TBUsuarios;

    @FXML
    private Button btnActualizarUser, btnCancelar;

    @FXML
    private TextField txtNombreUser, txtUsername, txtContraUser;

    @FXML
    private ComboBox<String> cmbNivelesUser;

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

    public void initialize(Usuarios usuarioSeleccionado){
        btnActualizarUser.setOnAction(this::actualizarDatos);
        btnCancelar.setOnAction(this::cerrarVentana);

        txtNombreUser.setText(usuarioSeleccionado.getNombreEmp());
        txtUsername.setText(usuarioSeleccionado.getNombreUser());
        txtContraUser.setText(usuarioSeleccionado.getContraUser());

        cmbNivelesUser.setValue(usuarioSeleccionado.getNivelUser());
        cargarNivelesUserEnCombobox();
    }

    public void setTableUsuarios(TableView<Usuarios> TBUsuarios) {
        this.TBUsuarios = TBUsuarios;
    }

    private void actualizarDatos(javafx.event.ActionEvent actionEvent) {
        //validaciones();
        actualizarDatos();
    }

    private void cerrarVentana(javafx.event.ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

   public void actualizarDatos(){
        String NombreUE = txtUsername.getText();
        String contraUsu = txtContraUser.getText();
        int nivelusuario = obtenerIdCargoSeleccionado(cmbNivelesUser);


        Usuarios empleadoSeleccionado = obtenerEmpleadoSeleccionadoDesdeTabla();

        try (Connection conn = Conexion.obtenerConexion()) {
            String sql = "UPDATE tbusuarios SET nombreUsuario=?, contraseña = ?, idNivelUsuario = ?  WHERE idUsuario =?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, NombreUE);
                ps.setString(2, contraUsu);
                ps.setInt(3, nivelusuario);
                ps.setInt(4, empleadoSeleccionado.getIdUsuario()); // Asegúrate de tener un método getIdEmpleado() en tu clase Empleado
                ps.executeUpdate();

                agregar_empleadosControlador.mostrarAlerta("Actualización de empleados", "Se han actualizado los datos exitosamente", Alert.AlertType.INFORMATION);

                if (TBUsuarios != null) {
                    TBUsuarios.getItems().clear();
                    CrudUsuariosControlador empleadosControlador = new CrudUsuariosControlador();
                    empleadosControlador.setTableUsuarios(TBUsuarios);
                    empleadosControlador.cargarDatos();
                }

                ((Stage) txtUsername.getScene().getWindow()).close();
            }
        } catch (SQLException e) {
            CustomAlert customAlert = new CustomAlert();
            customAlert.mostrarAlertaPersonalizada("Error", "Ha ocurrido un error", (Stage) btnActualizarUser.getScene().getWindow());
            e.printStackTrace();


        }
    }


    private void cargarNivelesUserEnCombobox() {
        // Crear una lista observable para almacenar los datos
        ObservableList<String> data = FXCollections.observableArrayList();

        // Conectar a la base de datos y recuperar los datos
        try (Connection conn = Conexion.obtenerConexion()) { // Reemplaza con tu propia lógica de conexión
            String query = "SELECT usuario FROM tbNivelesUsuario";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Recorrer los resultados y agregarlos a la lista observable
            while (resultSet.next()) {
                String item = resultSet.getString("usuario"); // Reemplaza con el nombre de la columna de tu tabla
                data.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de errores
        }

        // Asignar los datos al ComboBox
        cmbNivelesUser.setItems(data);
    }

    private Usuarios obtenerEmpleadoSeleccionadoDesdeTabla() {

        return TBUsuarios.getSelectionModel().getSelectedItem(); // Reemplaza con la lógica real
    }


    private int obtenerIdCargoSeleccionado(ComboBox<String> cmbNivelesUser) {
        int idCargo = -1; // Valor predeterminado en caso de error o no selección

        try (Connection conn = Conexion.obtenerConexion()) {
            String cargoSeleccionado = cmbNivelesUser.getValue();

            String sql = "SELECT idNivelUsuario FROM tbNivelesUsuario WHERE usuario = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, cargoSeleccionado);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idCargo = rs.getInt("idNivelUsuario");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idCargo;
    }
}
