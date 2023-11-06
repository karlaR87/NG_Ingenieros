package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.Empleados;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class EmpleadosControlador {

    @FXML
    private TableView<Empleados>TableEmpleados;

    public void initialize() {
        //configurarTabla();
        cargarDatos();
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
             ResultSet rs = stmt.executeQuery("select emp.idempleado, emp.nombre,emp.apellido, emp.dui, emp.nit, emp.sueldo_dia, emp.sueldo_horaExt,\n" +
                     "c.cargo, \n" +
                     "tp.tipoPlaza, \n" +
                     "p.nombre_proyecto\n" +
                     "from tbempleados emp\n" +
                     "inner join tbcargos c on c.idcargo = emp.idcargo\n" +
                     "inner join tbtipoPlazas tp on tp.idTipoPlaza = emp.idTipoPlaza\n" +
                     "inner join tbProyectos p on p.idproyecto = emp.idproyecto")) {

            while (rs.next()) {
                int id = rs.getInt("idempleado");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");

                String dui = rs.getString("dui");
                String nit = rs.getString("nit");
                Double sueldoDia = rs.getDouble("sueldo_dia");

                Double sueldoHora = rs.getDouble("sueldo_horaExt");
                String cargo = rs.getString("cargo");
                String plazo = rs.getString("tipoPlaza");

                String Proyecto = rs.getString("nombre_proyecto");

                TableEmpleados.getItems().add(new Empleados(id,nombre,apellido, dui, nit,sueldoDia,sueldoHora,cargo,plazo,Proyecto ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
