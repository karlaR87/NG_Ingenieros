package com.example.ng_ingenieros.Controlador;


import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.Empleados;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;


import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;


public class agregar_empleadosControlador {

    @FXML
    private TextField txtNombreEmp, txtDuiEmp, txtSueldoEmp, txtPagoHorasExEmp, txtCorreoEmp, txtCuentaBEmp;

    @FXML
    private Button btnGuardar1, btnCancelar1;

    @FXML
    private ComboBox<String> cbCargos, cbPlaza ;


    public void initialize(){
        btnGuardar1.setOnAction(this::guardarDatos);
        btnCancelar1.setOnAction(this::cerrarVentana);

        cargarCargosEnCombobox();
        cargarPlazasEnCombobox();

        cbCargos.setPromptText("Seleccione el cargo del empleado");
        cbPlaza.setPromptText("Seleccione el tipo de plaza del empleado");
    }

    private void guardarDatos(javafx.event.ActionEvent actionEvent) {
        agregarDatos();
    }

    private void cerrarVentana(javafx.event.ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    public final void agregarDatos(){
        Empleados empleado = new Empleados();
        try{
            String Nombre = txtNombreEmp.getText().toString();
            String Dui = txtDuiEmp.getText().toString();
            String Correo = txtCorreoEmp.getText().toString();
            String sueldoDia = txtSueldoEmp.getText().toString();
            String sueldoHora = txtPagoHorasExEmp.getText().toString();
            String cuentaBancaria = txtCuentaBEmp.getText().toString();
            empleado.setNombre(Nombre);
            empleado.setDui(Dui);
            empleado.setCorreo(Correo);
            empleado.setSueldoDia(Double.parseDouble(sueldoDia));
            empleado.setSueldoHora(Double.parseDouble(sueldoHora));
            empleado.setCuentaBancaria(cuentaBancaria);
            empleado.setIdcargo(empleado.getIdcargo());
            empleado.setIdplaza(empleado.getIdplaza());
            int valor = empleado.agregarEmpleados();
            if(valor == 1){

                mostrarAlerta("Ingreso de empleados","Se han ingresado los datos exitosamente",Alert.AlertType.INFORMATION);
            }else if(valor == 0){
                mostrarAlerta("Ingreso de empleados","El valor es 0",Alert.AlertType.WARNING);
            }else{
                mostrarAlerta("Ingreso de empleados","Error al ingresar los datos",Alert.AlertType.ERROR);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public final void actualizarDatos(){

    }





    private void cargarPlazasEnCombobox() {
        // Crear una lista observable para almacenar los datos
        ObservableList<String> data = FXCollections.observableArrayList();

        // Conectar a la base de datos y recuperar los datos
        try (Connection conn = Conexion.obtenerConexion()) { // Reemplaza con tu propia lógica de conexión
            String query = "SELECT cargo FROM tbtipoPlazas";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Recorrer los resultados y agregarlos a la lista observable
            while (resultSet.next()) {
                String item = resultSet.getString("tipoPlaza"); // Reemplaza con el nombre de la columna de tu tabla
                data.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de errores
        }

        // Asignar los datos al ComboBox
        cbPlaza.setItems(data);
    }



    private void cargarCargosEnCombobox() {
        // Crear una lista observable para almacenar los datos
        ObservableList<String> data = FXCollections.observableArrayList();

        // Conectar a la base de datos y recuperar los datos
        try (Connection conn = Conexion.obtenerConexion()) { // Reemplaza con tu propia lógica de conexión
            String query = "SELECT cargo FROM tbcargos";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Recorrer los resultados y agregarlos a la lista observable
            while (resultSet.next()) {
                String item = resultSet.getString("cargo"); // Reemplaza con el nombre de la columna de tu tabla
                data.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de errores
        }

        // Asignar los datos al ComboBox
        cbCargos.setItems(data);
    }
    //Metodo para mostraa mensajes en la aplicacion de forma más facil y ordenada
    public static void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();


    }


}
