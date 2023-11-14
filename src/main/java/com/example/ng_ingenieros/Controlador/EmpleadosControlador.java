package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.Empleados;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.*;

public class EmpleadosControlador {

    @FXML
    private TableView<Empleados> TableEmpleados;
    @FXML
    private TextField txtBusqueda;

    @FXML
    private Button btnAgregarEmp, btnEditarEmp, btnEliminarEmp;

    public void initialize() {
        //configurarTabla();
        cargarDatos();
        btnEditarEmp.setOnAction(this::btnEditarOnAction);
        btnAgregarEmp.setOnAction(this::btnAgregarOnAction);
        btnEliminarEmp.setOnAction(this::eliminardatos);



    }

    private void btnAgregarOnAction(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/agregar_empledos.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ingreso de empleados");

            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btnEditarOnAction(javafx.event.ActionEvent actionEvent) {
        abrirVentanaActualizar();
    }



    private void eliminardatos(javafx.event.ActionEvent actionEvent) {

        eliminarEmpleado();
    }

    public void abrirVentanaActualizar() {
        // Obtener la fila seleccionada
        Empleados empleadoSeleccionado = TableEmpleados.getSelectionModel().getSelectedItem();

        if (empleadoSeleccionado != null) {
            // Crear y mostrar la ventana de actualización con los datos de la fila seleccionada
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/actualizar_empleados.fxml"));
            Parent root;
            try {

                root = loader.load();
                // Obtener el controlador de la ventana de actualización
                actualizar_empleadosControlador actualizarEmpleadosControlador = loader.getController();

                // Pasar los datos de la fila seleccionada al controlador de la ventana de actualización
                actualizarEmpleadosControlador.initialize(empleadoSeleccionado);

                // Mostrar la ventana de actualización
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace(); // Considera manejar la excepción de una manera más robusta
            }


        }
    }




//CODIGO que hace que se muestren doble pero por si llega a servir de algo
    /*private void configurarTabla() {
        TableColumn<Empleados, Integer> idColumn = new TableColumn<>("idempleado");
        TableColumn<Empleados, String> nombreColumn = new TableColumn<>("nombre");
        TableColumn<Empleados, String> apellidoColum = new TableColumn<>("apellido");

        TableColumn<Empleados, String> DuiColum = new TableColumn<>("dui");
        TableColumn<Empleados, String> NitColum = new TableColumn<>("nit");
        TableColumn<Empleados, Double> SueldoDiaColum = new TableColumn<>("sueldo_dia");

        TableColumn<Empleados, Double> SueldoHoraColum = new TableColumn<>("sueldo_horaExt");
        TableColumn<Empleados, Integer> CargoColum = new TableColumn<>("idcargo");
        TableColumn<Empleados, Integer> PlazaColum = new TableColumn<>("idTipoPlaza");

        TableColumn<Empleados, Integer> ProyectoColum = new TableColumn<>("idproyecto");

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        apellidoColum.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        DuiColum.setCellValueFactory(new PropertyValueFactory<>("Dui"));
        NitColum.setCellValueFactory(new PropertyValueFactory<>("NIT"));
        SueldoDiaColum.setCellValueFactory(new PropertyValueFactory<>("sueldoDia"));
        SueldoHoraColum.setCellValueFactory(new PropertyValueFactory<>("sueldoHora"));
        CargoColum.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        PlazaColum.setCellValueFactory(new PropertyValueFactory<>("plaza"));
        ProyectoColum.setCellValueFactory(new PropertyValueFactory<>("proyecto"));

        TableEmpleados.getColumns().addAll(idColumn, nombreColumn, apellidoColum, DuiColum, NitColum, SueldoDiaColum, SueldoHoraColum,CargoColum,  PlazaColum, ProyectoColum);
    }*/

    private void cargarDatos() {
        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select emp.idempleado, emp.nombreCompleto, emp.dui, emp.sueldo_dia, emp.sueldo_horaExt,\n" +
                     "c.cargo, \n" +
                     "tp.tipoPlaza, \n" +
                     "p.nombre_proyecto\n" +
                     "from tbempleados emp\n" +
                     "inner join tbcargos c on c.idcargo = emp.idcargo\n" +
                     "inner join tbtipoPlazas tp on tp.idTipoPlaza = emp.idTipoPlaza\n" +
                     "inner join tbProyectos p on p.idproyecto = emp.idproyecto")) {

            while (rs.next()) {
                int id = rs.getInt("idempleado");
                String nombre = rs.getString("nombreCompleto");


                String dui = rs.getString("dui");

                Double sueldoDia = rs.getDouble("sueldo_dia");

                Double sueldoHora = rs.getDouble("sueldo_horaExt");
                String cargo = rs.getString("cargo");
                String plazo = rs.getString("tipoPlaza");

                String Proyecto = rs.getString("nombre_proyecto");

                TableEmpleados.getItems().add(new Empleados(id, nombre, dui, sueldoDia, sueldoHora, cargo, plazo, Proyecto));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Este es el método para la busqueda de datos a partir de lo que se escriba
    //en el textfield
    private void BuscarDatos() {
        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select emp.idempleado, emp.nombreCompleto, emp.dui, emp.sueldo_dia, emp.sueldo_horaExt,\n" +
                     "c.cargo, \n" +
                     "tp.tipoPlaza, \n" +
                     "p.nombre_proyecto\n" +
                     "from tbempleados emp\n" +
                     "inner join tbcargos c on c.idcargo = emp.idcargo\n" +
                     "inner join tbtipoPlazas tp on tp.idTipoPlaza = emp.idTipoPlaza\n" +
                     "inner join tbProyectos p on p.idproyecto = emp.idproyecto" +
                     " where emp.nombre like '%" + txtBusqueda.getText() + " %'")) {

            while (rs.next()) {
                int id = rs.getInt("idempleado");
                String nombre = rs.getString("nombreCompleto");


                String dui = rs.getString("dui");

                Double sueldoDia = rs.getDouble("sueldo_dia");

                Double sueldoHora = rs.getDouble("sueldo_horaExt");
                String cargo = rs.getString("cargo");
                String plazo = rs.getString("tipoPlaza");

                String Proyecto = rs.getString("nombre_proyecto");

                TableEmpleados.getItems().add(new Empleados(id, nombre, dui, sueldoDia, sueldoHora, cargo, plazo, Proyecto));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    private void eliminarEmpleado() {
        // Obtener el ID del proyecto seleccionado (asumiendo que tienes una variable para almacenar el ID)
        int idEmpleado = obtenerIdEmpleadoSeleccionado();

        try (Connection connection = Conexion.obtenerConexion()) {
            String sql = "DELETE FROM tbempleados WHERE idempleado = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idEmpleado);

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




    private int obtenerIdEmpleadoSeleccionado() {
        Empleados empleadoSeleccionado = TableEmpleados.getSelectionModel().getSelectedItem();

        if (empleadoSeleccionado != null) {
            return empleadoSeleccionado.getId();
        } else {
            return -1; // Retorna un valor que indique que no se ha seleccionado ningún proyecto.
}
    }






}







        /*txtBusqueda.setOnKeyReleased(event -> {
        if (!txtBusqueda.getText().isEmpty()) {
            BuscarDatos();
        }
        });*/



