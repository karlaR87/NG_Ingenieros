package com.example.ng_ingenieros.Controlador;


import com.example.ng_ingenieros.Conexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;


import java.awt.*;
import java.sql.*;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;


public class agregar_empleadosControlador {

    @FXML
    private TextField txtNombreEmp;
    @FXML
    private TextField txtDuiEmp;
    @FXML
    private TextField txtCorreoEmp;
    @FXML
    private TextField txtCuentaBEmp;
    @FXML
    private TextField txtSueldoEmp;
    @FXML
    private TextField txtPagoHorasExEmp;
    @FXML
    private ComboBox<String> cbCargoEmp;
    @FXML
    private ComboBox<String> cbPlazaEmp;

    @FXML
    private Button btnGuardar1, btnCancelar1;




    public void initialize(){
        btnGuardar1.setOnAction(this::guardarDatos);
        btnCancelar1.setOnAction(this::cerrarVentana);

        cargarCargosEnCombobox();
        cargarPlazasEnCombobox();

        cbCargoEmp.setPromptText("Seleccione el cargo del empleado");
        cbPlazaEmp.setPromptText("Seleccione la plaza del empleado");
    }

    private void guardarDatos(javafx.event.ActionEvent actionEvent) {
        //agregarDatos();
        agregarEmpleados();
    }

    private void cerrarVentana(javafx.event.ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    /*public final void agregarDatos(){
        Empleados empleado = new Empleados();
        try{
            String Nombre = txtNombreEmp1.getText();
            String Dui = txtDuiEmp1.getText();
            String Correo = txtCorreoEmp1.getText();
            String sueldoDia = txtSueldoEmp1.getText();
            String sueldoHora = txtPagoHorasExEmp1.getText();
            String cuentaBancaria = txtNumCuenta.getText();
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
    }*/



    // Otros campos y métodos...

    @FXML

    public void agregarEmpleados(){
        String nombre = txtNombreEmp.getText();
        String dui = txtDuiEmp.getText();
        String correo = txtCorreoEmp.getText();
        String cuentaBancaria = txtCuentaBEmp.getText();
        double sueldoDia = Double.parseDouble(txtSueldoEmp.getText());
        double sueldoHoraExt = Double.parseDouble(txtPagoHorasExEmp.getText());
        int idCargo = obtenerIdCargoSeleccionado(cbCargoEmp);
        int idPlaza = obtenerIdPlazaSeleccionado(cbPlazaEmp);

        try (Connection conn = Conexion.obtenerConexion()) {
            String sql = "INSERT INTO tbempleados (nombreCompleto, dui, correo, sueldo_dia, sueldo_horaExt, numero_cuentabancaria, idcargo, idtipoPlaza) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps =conn.prepareStatement(sql);
            ps.setString(1,nombre);
            ps.setString(2,dui);
            ps.setString(3,correo);
            ps.setString(4,cuentaBancaria);
            ps.setDouble(5,sueldoDia);
            ps.setDouble(6,sueldoHoraExt);
            ps.setInt(7,idCargo);
            ps.setInt(8, idPlaza);
            ps.executeUpdate();
            mostrarAlerta("Inserción de empleados", "El empleado ha sido agregado exitosamente", Alert.AlertType.INFORMATION);


        }catch (SQLException e) {
            mostrarAlerta("Error", "Ha ocurrido un error", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
 //arreglar los dos metodos siguientes
    /*private int obtenerIdCargoSeleccionado(ComboBox<String> cbCargoEmp) {
        ObservableList<Integer> data = FXCollections.observableArrayList();

        // Conectar a la base de datos y recuperar los datos
        try (Connection conn = Conexion.obtenerConexion()) { // Reemplaza con tu propia lógica de conexión
            String query = "SELECT idcargo FROM tbcargos";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Recorrer los resultados y agregarlos a la lista observable
            while (resultSet.next()) {
                int item = resultSet.getInt("idcargo"); // Reemplaza con el nombre de la columna de tu tabla
                data.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de errores
        }

        // Asignar los datos al ComboBox
        cbCargoEmp.setItems(data);
    }*/

    public int obtenerIdCargoSeleccionado(ComboBox<String> cbCargoEmp) {
        int idCargo = -1; // Valor predeterminado en caso de error o no selección

        try (Connection conn = Conexion.obtenerConexion()) {
            String cargoSeleccionado = cbCargoEmp.getValue();

            String sql = "SELECT idcargo FROM tbcargos WHERE cargo = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, cargoSeleccionado);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idCargo = rs.getInt("idcargo");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idCargo;
    }


    public int obtenerIdPlazaSeleccionado(ComboBox<String> cbPlazaEmp) {
        int idPlaza = -1; // Valor predeterminado en caso de error o no selección

        try (Connection conn = Conexion.obtenerConexion()) {
            String PlazaSeleccionada = cbPlazaEmp.getValue();

            String sql = "SELECT idtipoPlaza FROM tbTipoPlazas WHERE tipoPlaza = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, PlazaSeleccionada);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idPlaza = rs.getInt("idtipoPlaza");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idPlaza;
    }

    /*private int obtenerIdPlazaSeleccionado(ComboBox<String> cbPlazaEmp) {
        ObservableList<String> data = FXCollections.observableArrayList();

        // Conectar a la base de datos y recuperar los datos
        try (Connection conn = Conexion.obtenerConexion()) { // Reemplaza con tu propia lógica de conexión
            String query = "SELECT tipoPlaza FROM tbtipoPlazas";
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
        cbPlazaEmp.setItems(data);
    }*/







    public final void actualizarDatos(){

    }





    private void cargarPlazasEnCombobox() {
        // Crear una lista observable para almacenar los datos
        ObservableList<String> data = FXCollections.observableArrayList();

        // Conectar a la base de datos y recuperar los datos
        try (Connection conn = Conexion.obtenerConexion()) { // Reemplaza con tu propia lógica de conexión
            String query = "SELECT tipoPlaza FROM tbtipoPlazas";
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
        cbPlazaEmp.setItems(data);
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
        cbCargoEmp.setItems(data);
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
