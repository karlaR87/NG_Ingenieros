package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.Empleados;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

//imports del spinner
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.scene.control.SpinnerValueFactory;

public class AsistenciaEmpleadosControlador {


    @FXML
    private Spinner spHoraEn1;
    @FXML
    private Spinner spHoraEn2;
    @FXML
    private Spinner spHoraSa1;
    @FXML
    private Spinner spHoraSa2;

    @FXML
    private Label LblHoraEntrada;
    @FXML
    private Label LblHoraSalida;

    @FXML
    private ComboBox cmbAMPM;
    @FXML
    private ComboBox cmbAMPM2;
    @FXML
    private ComboBox cmbDiaAsistencia;
    @FXML
    private ComboBox cmbDiaSalida;


    @FXML
    private TableView TbAsistencia;

    @FXML
    private TextField txtBusqueda;

    @FXML
    private TextField txtEmpleadoSel;
    @FXML
    private Label LblIdEmpleado;



    public void initialize()
    {
        cargarDatos();

        configurarSpinner(spHoraEn1, 0, 11);
        configurarSpinner(spHoraEn2, 0, 59);
        configurarSpinner(spHoraSa1, 0, 11);
        configurarSpinner(spHoraSa2, 0, 59);

        ObservableList<String> opcionesAsistencia = FXCollections.observableArrayList("A.M.", "P.M.");
        cmbAMPM.setItems(opcionesAsistencia);
        cmbAMPM2.setItems(opcionesAsistencia);

        llenarCombo();

        TbAsistencia.setOnMouseClicked(this::seleccionarEmpleado);

        txtBusqueda.setOnKeyReleased(event -> {

            buscarDatos(txtBusqueda.getText());

        });

    }



    //Configurar Spinners
    private void configurarSpinner(Spinner<Integer> spinner, int min, int max) {
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max);
        spinner.setValueFactory(valueFactory);
        spinner.setEditable(true);


        spinner.getValueFactory().setConverter(new StringConverter<Integer>() {
            @Override
            public String toString(Integer value) {
                return value.toString();
            }

            @Override
            public Integer fromString(String string) {
                return Integer.valueOf(string);
            }
        });
    }

    public void llenarCombo()
    {
        cmbDiaAsistencia.getItems().addAll(
                "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"
        );

        // Agregar días de la semana a cmbDiaSalida
        cmbDiaSalida.getItems().addAll(
                "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"
        );
    }

    //Obtener valores de los Spinner (metodo directamente aplicado al boton en el fxml)

    @FXML
    private void guardarAsistencia(ActionEvent event) {
        // Obtener los valores de los Spinners
        int horaEn1 = (int) spHoraEn1.getValue();
        int horaEn2 = (int) spHoraEn2.getValue();

        String diaseleccionado = (String) cmbDiaAsistencia.getSelectionModel().getSelectedItem();
        String minutosEn2 = (horaEn2 <= 9) ? "0" + horaEn2 : String.valueOf(horaEn2); //validamos que el spinner de los minutos, al ingresar un valor menor a nueva cargue un 0 al inicio
        String seleccionComboBox1 = (String) cmbAMPM.getSelectionModel().getSelectedItem();

        // Mostrar los valores en el Label

        if (seleccionComboBox1 != null) {
            LblHoraEntrada.setText("" + diaseleccionado + ", " + horaEn1 + ":" + minutosEn2 + " " + seleccionComboBox1);
        }


        int horaSal1 = (int) spHoraSa1.getValue();
        int horaSal2 = (int) spHoraSa2.getValue();

        String diasalida = (String) cmbDiaSalida.getSelectionModel().getSelectedItem();
        String minutosEn = (horaSal2 <= 9) ? "0" + horaSal2 : String.valueOf(horaSal2);
        String seleccionComboBox2 = (String) cmbAMPM2.getSelectionModel().getSelectedItem();


        LblHoraSalida.setText("(" + horaSal1 + ":" + minutosEn + ")");
        if (seleccionComboBox2 != null) {
            LblHoraSalida.setText("" + diasalida + ", " + horaSal1 + ":" + minutosEn + " " + seleccionComboBox2);
        }

        //Aqui se puede poner directamente el codigo de agregar asistencia
    }



    private void cargarDatos() {
        try (Connection conn = Conexion.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select emp.idempleado, emp.nombreCompleto from tbempleados emp")) {

            while (rs.next()) {
                int id = rs.getInt("idempleado");
                String nombre = rs.getString("nombreCompleto");




                TbAsistencia.getItems().add(new Empleados(id, nombre));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buscarDatos(String busqueda) {
        TbAsistencia.getItems().clear(); // Limpiar los elementos actuales de la tabla

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement("SELECT emp.idempleado, emp.nombreCompleto FROM tbempleados emp  WHERE emp.nombreCompleto LIKE ?")) {

            // Preparar el parámetro de búsqueda para la consulta SQL
            String parametroBusqueda = "%" + busqueda + "%";
            stmt.setString(1, parametroBusqueda);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Obtener los datos y agregarlos a la tabla
                int id = rs.getInt("idempleado");
                String nombre = rs.getString("nombreCompleto");


                TbAsistencia.getItems().add(new Empleados(id, nombre));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void seleccionarEmpleado(MouseEvent event) {
        if (event.getClickCount() == 1) {
            // Obtener la fila seleccionada
            Empleados empleadoSeleccionado = (Empleados) TbAsistencia.getSelectionModel().getSelectedItem();

            // Verificar si se seleccionó una fila
            if (empleadoSeleccionado != null) {
                // Asignar el ID al Label
                LblIdEmpleado.setText(String.valueOf(empleadoSeleccionado.getId()));

                // Asignar el nombre al TextField
                txtEmpleadoSel.setText(empleadoSeleccionado.getNombre());
            }
        }
    }








}