package com.example.ng_ingenieros.Controlador;


import com.example.ng_ingenieros.Conexion;

import com.example.ng_ingenieros.Usuarios;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class CrudUsuariosControlador {
    @FXML
    private TableView<Usuarios> TBUsuarios;
    @FXML
    private Button btnEditarUser, btnEliminarUser, btnEditarPass, btnRefresh;
    @FXML
    private TextField txtBusqueda;


    public void initialize() {
        btnEliminarUser.setOnAction(this::eliminardatos);
        btnEditarUser.setOnAction(this::btnEditarOnAction);
        btnRefresh.setOnAction(this::refresh);
        btnEditarPass.setOnAction(this::btnEditarPassOnAction);
        txtBusqueda.setOnKeyReleased(event -> {

            buscarDatos(txtBusqueda.getText());

        });
        cargarDatos();

    }

    public void setTableUsuarios(TableView<Usuarios> TBUsuarios) {
        this.TBUsuarios = TBUsuarios;
    }

    private void btnEditarOnAction(javafx.event.ActionEvent actionEvent) {
        Usuarios usuarioSeleccionado = TBUsuarios.getSelectionModel().getSelectedItem();

        if (usuarioSeleccionado != null) {
            // Crear y mostrar la ventana de actualización con los datos de la fila seleccionada
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/actualizar_Usuarios.fxml"));
            Parent root;
            try {

                root = loader.load();
                // Obtener el controlador de la ventana de actualización
                ActualizarUsuarioControlador actualizarUsuariosControlador = loader.getController();

                actualizarUsuariosControlador.setTableUsuarios(TBUsuarios);
                actualizarUsuariosControlador.initialize(usuarioSeleccionado);


                // Mostrar la ventana de actualización
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();

                actualizarUsuariosControlador.setTableUsuarios(TBUsuarios);
            } catch (IOException e) {
                // Manejo de excepciones
            }
        }
    }

    private void refresh(javafx.event.ActionEvent actionEvent) {
        cargarDatos();

    }

    private void btnEditarPassOnAction(javafx.event.ActionEvent actionEvent) {
        Usuarios contraSeleccionado = TBUsuarios.getSelectionModel().getSelectedItem();

        if (contraSeleccionado != null) {
            // Crear y mostrar la ventana de actualización con los datos de la fila seleccionada
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/Actualizar_Contrausuarios.fxml"));
            Parent root;
            try {

                root = loader.load();
                // Obtener el controlador de la ventana de actualización
                ActualizarContraUserControlador actualizarContraUsuariosControlador = loader.getController();

                actualizarContraUsuariosControlador.setTableUsuarios(TBUsuarios);
                actualizarContraUsuariosControlador.initialize(contraSeleccionado);


                // Mostrar la ventana de actualización
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();

                actualizarContraUsuariosControlador.setTableUsuarios(TBUsuarios);
            } catch (IOException e) {
                // Manejo de excepciones
            }
        }
    }


    private void eliminardatos(javafx.event.ActionEvent actionEvent) {
        eliminarUsuario();

    }


    public void cargarDatos() {
        TBUsuarios.getItems().clear();
        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select u.idUsuario,emp.idempleado, u.nombreUsuario, u.contraseña, nu.usuario, emp.nombreCompleto  from tbusuarios u\n" +
                     "inner join tbNivelesUsuario nu on nu.idNivelUsuario = u.idNivelUsuario\n" +
                     "inner join tbempleados emp on emp.idempleado = u.idempleado ")) {

            while (rs.next()) {
                int idU = rs.getInt("IdUsuario");
                String nombreU = rs.getString("nombreUsuario");
                String contraU = rs.getString("contraseña");
                String nombreEMP = rs.getString("nombreCompleto");
                String NivelU = rs.getString("usuario");
                int idEmp = rs.getInt("idempleado");


                // Agregar los datos a la tabla
                TBUsuarios.getItems().add(new Usuarios(idU, nombreU, contraU, NivelU, nombreEMP, idEmp));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buscarDatos(String busqueda) {
        TBUsuarios.getItems().clear(); // Limpiar los elementos actuales de la tabla

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement("select u.idUsuario, emp.idempleado u.nombreUsuario, u.contraseña, nu.usuario, emp.nombreCompleto  from tbusuarios u\n" +
                     "inner join tbNivelesUsuario nu on nu.idNivelUsuario = u.idNivelUsuario \n" +
                     "inner join tbempleados emp on emp.idempleado = u.idempleado \n" +
                     "WHERE emp.nombreCompleto LIKE ? OR nu.usuario LIKE ? OR emp.idusuario LIKE ? or u.nombreUsuario LIKE ? OR u.contraseña LIKE ?")) {

            // Preparar el parámetro de búsqueda para la consulta SQL
            String parametroBusqueda = "%" + busqueda + "%";
            for (int i = 1; i <= 5; i++) {
                stmt.setString(i, parametroBusqueda);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int idU = rs.getInt("IdUsuario");
                String nombreU = rs.getString("nombreUsuario");
                String contraU = rs.getString("contraseña");
                String nombreEMP = rs.getString("nombreCompleto");
                String NivelU = rs.getString("usuario");
                int idEmp = rs.getInt("idempleado");


                // Agregar los datos a la tabla
                TBUsuarios.getItems().add(new Usuarios(idU, nombreU, contraU, NivelU, nombreEMP, idEmp));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void eliminarUsuario() {
        // Obtener el ID del proyecto seleccionado (asumiendo que tienes una variable para almacenar el ID)
        int idUsuario = obtenerIdUsuarioSeleccionado();
        int idempleado = obtenerIdEmpleadoSeleccionado();
        try (Connection connection = Conexion.obtenerConexion()) {
            String sql = "DELETE FROM tbusuarios WHERE idUsuario = ?\n"+
                    "DELETE FROM tbempleados WHERE idempleado = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idUsuario);
            statement.setInt(2, idempleado);



            int filasAfectadas = statement.executeUpdate();
            if (filasAfectadas > 0) {
                agregar_empleadosControlador.mostrarAlerta("Eliminación de datos", "Se eliminaron los datos exitosamente", Alert.AlertType.INFORMATION);

            } else {
                agregar_empleadosControlador.mostrarAlerta("Alerta", "No se encontro ningun empleado", Alert.AlertType.WARNING);
            }
            TBUsuarios.getItems().clear();
            cargarDatos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }







    private int obtenerIdUsuarioSeleccionado() {
        Usuarios empleadoSeleccionado = TBUsuarios.getSelectionModel().getSelectedItem();

        if (empleadoSeleccionado != null) {
            return empleadoSeleccionado.getIdUsuario();
        } else {
            return -1; // Retorna un valor que indique que no se ha seleccionado ningún proyecto.
        }
    }

    private int obtenerIdEmpleadoSeleccionado() {
        Usuarios empleadoSeleccionado = TBUsuarios.getSelectionModel().getSelectedItem();

        if (empleadoSeleccionado != null) {
            return empleadoSeleccionado.getIdEmpleado();

        } else {
            return -1; // Retorna un valor que indique que no se ha seleccionado ningún proyecto.
        }
    }
}
