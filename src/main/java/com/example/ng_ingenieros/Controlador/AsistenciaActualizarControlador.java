package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.AsistenciaVista;
import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.Empleados;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class AsistenciaActualizarControlador {

    @FXML
    private Spinner spHoraEn1;
    @FXML
    private Spinner spHoraEn2;
    @FXML
    private Spinner spHoraSa1;
    @FXML
    private Spinner spHoraSa2;

    @FXML
    private Label lblHoraEntrada;
    @FXML
    private Label lblHoraSalida;

    @FXML
    private ComboBox cmbAMPM;
    @FXML
    private ComboBox cmbAMPM2;
    @FXML
    private ComboBox cmbDiaAsistencia;
    @FXML
    private ComboBox cmbDiaSalida;

    @FXML
    private TextField txtEmpleadoSel;
    @FXML
    private Label LblIdEmpleado;
    @FXML
    private ComboBox cmbAsistencia;

    private TableView<Empleados> TBMostrarAsistencia;

    public void initialize(AsistenciaVista empleadoSeleccionado)
    {

        LblIdEmpleado.setText(String.valueOf(empleadoSeleccionado.getId()));
        txtEmpleadoSel.setText(empleadoSeleccionado.getIdempleado());
        cmbAsistencia.setValue(empleadoSeleccionado.getMarcarasistencia());
        lblHoraEntrada.setText(empleadoSeleccionado.getHora_entrada());
        lblHoraSalida.setText(empleadoSeleccionado.getHora_salida());


        // Llamar a las funciones para separar los datos


        CargarAsistencia();
        cmbAsistencia.setPromptText("Seleccione la opción de asistencia");

        configurarSpinner(spHoraEn1, 0, 11);
        configurarSpinner(spHoraEn2, 0, 59);
        configurarSpinner(spHoraSa1, 0, 11);
        configurarSpinner(spHoraSa2, 0, 59);

        ObservableList<String> opcionesAsistencia = FXCollections.observableArrayList("A.M.", "P.M.");
        cmbAMPM.setItems(opcionesAsistencia);
        cmbAMPM2.setItems(opcionesAsistencia);

        llenarCombo();
        //separar datos

        String[] partesEntrada = lblHoraEntrada.getText().split(", ");
        String[] datosEntrada = partesEntrada[1].split(" ");
        String diaEntrada = partesEntrada[0]; // Aquí tendrás el día de la entrada

// Separar los datos del Label de salida por la coma y espacio
        String[] partesSalida = lblHoraSalida.getText().split(", ");
        String[] datosSalida = partesSalida[1].split(" ");
        String diaSalida = partesSalida[0]; // Aquí tendrás el día de la salida

// Obtener la hora y los minutos de la entrada y la salida
        String[] horaMinutosEntrada = datosEntrada[0].split(":");
        int horaEntrada = Integer.parseInt(horaMinutosEntrada[0]); // Obtener la hora de la entrada como entero
        int minutosEntrada = Integer.parseInt(horaMinutosEntrada[1]); // Obtener los minutos de la entrada como entero

        String[] horaMinutosSalida = datosSalida[0].split(":");
        int horaSalida = Integer.parseInt(horaMinutosSalida[0]); // Obtener la hora de la salida como entero
        int minutosSalida = Integer.parseInt(horaMinutosSalida[1]); // Obtener los minutos de la salida como entero

// Obtener A.M. o P.M. de la entrada y la salida
        String periodoEntrada = datosEntrada[1]; // Obtener A.M. o P.M. de la entrada
        String periodoSalida = datosSalida[1]; // Obtener A.M. o P.M. de la salida

        cmbDiaAsistencia.setValue(diaEntrada); // Establecer el día de la asistencia en el ComboBox
        spHoraEn1.getValueFactory().setValue(horaEntrada); // Establecer la hora de entrada en el Spinner 1
        spHoraEn2.getValueFactory().setValue(minutosEntrada); // Establecer los minutos de entrada en el Spinner 2
        cmbAMPM.setValue(periodoEntrada); // Establecer A.M. o P.M. en el ComboBox correspondiente

// Hacer lo mismo para la salida si es necesario
        cmbDiaSalida.setValue(diaSalida); // Establecer el día de salida en el ComboBox correspondiente
        spHoraSa1.getValueFactory().setValue(horaSalida); // Establecer la hora de salida en el Spinner 1
        spHoraSa2.getValueFactory().setValue(minutosSalida); // Establecer los minutos de salida en el Spinner 2
        cmbAMPM2.setValue(periodoSalida); // Establecer A.M. o P.M. en el ComboBox correspondiente

    }

    public void setTableAsistencia(TableView<Empleados> TBMostrarAsistencia) {
        this.TBMostrarAsistencia = TBMostrarAsistencia;
    }

    private void CargarAsistencia() {
        // Crear una lista observable para almacenar los datos
        ObservableList<String> data = FXCollections.observableArrayList();

        // Conectar a la base de datos y recuperar los datos
        try (Connection conn = Conexion.obtenerConexion()) { // Reemplaza con tu propia lógica de conexión
            String query = "SELECT asistencia FROM tbAsistenciaMarcar";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Recorrer los resultados y agregarlos a la lista observable
            while (resultSet.next()) {
                String item = resultSet.getString("asistencia"); // Reemplaza con el nombre de la columna de tu tabla
                data.add(item);
            }



        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de errores
        }

        // Asignar los datos al ComboBox
        cmbAsistencia.setItems(data);
    }
    private int obtenerIdAsistenciaSeleccionado(ComboBox<String> cbCargoEmp) {
        int idCargo = -1; // Valor predeterminado en caso de error o no selección

        try (Connection conn = Conexion.obtenerConexion()) {
            String asistencia = (String) cmbAsistencia.getValue();

            String sql = "SELECT idAsistenciaMarcar FROM tbAsistenciaMarcar WHERE asistencia = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, asistencia);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idCargo = rs.getInt("idAsistenciaMarcar");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idCargo;
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




    public void actualizarAsistencia() {
        int horaEn1 = (int) spHoraEn1.getValue();
        int horaEn2 = (int) spHoraEn2.getValue();

        String diaseleccionado = (String) cmbDiaAsistencia.getSelectionModel().getSelectedItem();
        String minutosEn2 = (horaEn2 <= 9) ? "0" + horaEn2 : String.valueOf(horaEn2); //validamos que el spinner de los minutos, al ingresar un valor menor a nueva cargue un 0 al inicio
        String seleccionComboBox1 = (String) cmbAMPM.getSelectionModel().getSelectedItem();

        // Mostrar los valores en el Label

        if (seleccionComboBox1 != null) {
            lblHoraEntrada.setText("" + diaseleccionado + ", " + horaEn1 + ":" + minutosEn2 + " " + seleccionComboBox1);
        }


        int horaSal1 = (int) spHoraSa1.getValue();
        int horaSal2 = (int) spHoraSa2.getValue();

        String diasalida = (String) cmbDiaSalida.getSelectionModel().getSelectedItem();
        String minutosEn = (horaSal2 <= 9) ? "0" + horaSal2 : String.valueOf(horaSal2);
        String seleccionComboBox2 = (String) cmbAMPM2.getSelectionModel().getSelectedItem();


        lblHoraSalida.setText("(" + horaSal1 + ":" + minutosEn + ")");
        if (seleccionComboBox2 != null) {
            lblHoraSalida.setText("" + diasalida + ", " + horaSal1 + ":" + minutosEn + " " + seleccionComboBox2);
        }

        //Aqui se puede poner directamente el codigo de agregar asistencia


        String nombreN = LblIdEmpleado.getText();

        int AsistenciaN = obtenerIdAsistenciaSeleccionado(cmbAsistencia);

        String fechaentradaN = lblHoraEntrada.getText();

        String fechasalidaN = lblHoraSalida.getText();

        // Obtener los datos actualizados de los campos


        // Realizar la actualización en la base de datos
        try (Connection conn = Conexion.obtenerConexion()) {
            String sql = "UPDATE  tbAsistencia SET idempleado=?, idAsistenciaMarcar=?, hora_entrada=?, hora_salida=?) ";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1,nombreN);
                ps.setInt(2, AsistenciaN);
                ps.setString(3,fechaentradaN);
                ps.setString(4, fechasalidaN);

                agregar_empleadosControlador.mostrarAlerta("Actualización de empleados", "Se han actualizado los datos exitosamente", Alert.AlertType.INFORMATION);
            }
        } catch (SQLException e) {
            agregar_empleadosControlador.mostrarAlerta("Error", "Ha ocurrido un error", Alert.AlertType.ERROR);
            e.printStackTrace();

        }
    }









}
