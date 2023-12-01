package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.AsistenciaVista;
import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.SalarioEmp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class SalarioEmpleadoControlador {

    @FXML
    private TextField txtNombreEmp;

    @FXML
    private Label lblIdAsistenciaregistrar;

    @FXML
    private Label lblidEmpleado;

    @FXML
    private TextField txtDiasRemunerados;

    @FXML
    private TextField txtHorasExtras;

    @FXML
    private TextField txtTotalDev;

    @FXML
    private TextField txtAFP;

    @FXML
    private TextField txtSeguroSocial;

    @FXML
    private TextField txtRenta;

    @FXML
    private TextField txtSalarioFinal;

    private TableView<AsistenciaVista> TBMostrarAsistencia;


    public void initialize(AsistenciaVista empleadoSeleccionado)
    {
        txtNombreEmp.setText(String.valueOf(empleadoSeleccionado.getIdempleado()));

        lblIdAsistenciaregistrar.setText(String.valueOf(empleadoSeleccionado.getId()));

        lblidEmpleado.setText(String.valueOf(empleadoSeleccionado.getIdE()));

        DiasRemunerados(empleadoSeleccionado.getIdE());
    }

    //Abrir ventana del salario


    public void setTableAsistencia(TableView<AsistenciaVista> TBMostrarAsistencia) {
        this.TBMostrarAsistencia = TBMostrarAsistencia;
    }

    //aquí consulto a la base los dias que el empleado ha trabajado, por medio del id = 1 de la asistenciaMarcar, el cual es asistencia
    public void DiasRemunerados(int idempleado) {
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(idAsistencia) AS Valor FROM tbAsistencia WHERE idempleado = ? AND idAsistenciaMarcar = 1")) {

            pstmt.setInt(1, idempleado); // Establecer el valor del parámetro

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("Valor");
                    txtDiasRemunerados.setText(String.valueOf(id));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //aquí calcularé las horas extra trabajadas por el empleado seleccionado


}
