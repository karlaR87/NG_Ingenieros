package com.example.ng_ingenieros.Controlador;


import com.example.ng_ingenieros.Empleados;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;


import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;



public class agregar_empleadosControlador  implements Initializable {

    @FXML
    private TextField txtNombreEmp, txtDuiEmp, txtSueldoEmp, txtPagoHorasExEmp, txtCorreoEmp, txtCuentaBEmp;

    @FXML
    private Button btnGuardar, btnCancelar;

    @FXML
    private int idCargo;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //btnGuardar.setOnAction(this::agregarDatos);

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
            empleado.setCargo(idCargo);
            int valor = empleado.agregarEmpleados();
            if(valor == 1){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Ingreso de empleados");
                alert.setHeaderText(null);
                alert.setContentText("Se han ingresado los datos correctamente");

                alert.showAndWait();
            }else if(valor == 0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Ingreso de empleados");
                alert.setHeaderText(null);
                alert.setContentText("el valor es 0");

                alert.showAndWait();
            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Ingreso de empleados");
                alert.setHeaderText(null);
                alert.setContentText("Ha ocurrido un error");

                alert.showAndWait();
            }

        }catch (Exception e){

        }
    }



}
