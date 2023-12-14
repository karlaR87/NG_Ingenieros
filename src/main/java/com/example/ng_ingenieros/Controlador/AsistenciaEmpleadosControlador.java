package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.AsistenciaVista;
import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.Empleados;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.sql.*;

//imports del spinner
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.scene.control.SpinnerValueFactory;

import javax.swing.text.html.ImageView;

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
    @FXML
    private ComboBox cmbAsistencia;

    @FXML
    private Button btnMostrar;
    @FXML
    private Button btncancelar;

    @FXML
    private Label lblidproyecto;

    private int idProyectoSeleccionado;

    public void recibirIdProyecto(int idProyecto) {
        idProyectoSeleccionado = idProyecto;
        // Llama a un método para cargar los empleados según el ID del proyecto
        cargarDatos();
    }
    public void initialize()
    {



        recibirIdProyecto(idProyectoSeleccionado);

        cmbAsistencia.setOnAction(event -> {
            String selectedItem = (String) cmbAsistencia.getSelectionModel().getSelectedItem();
            if ("Asistencia".equals(selectedItem)) {
                //Habilitar campos
                habilitarCampos();
            } else if("Inasistencia".equals(selectedItem)){
                //deshabilitar campos
                deshabilitarCampos();
            }
        });

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

        cmbAsistencia.setPromptText("Asistencio o inasistencia del empleado");
        CargarAsistencia();

        btnMostrar.setOnAction(actionEvent -> {
            try {

                btnMostrarOnAction(actionEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        btncancelar.setOnAction(actionEvent -> {
            try {
                btnCancelarOnAction(actionEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    public void setTbAsistencia(TableView<AsistenciaVista> TbAsistencia) {
        this.TbAsistencia = TbAsistencia;
    }


    private void deshabilitarCampos() {
        configurarSpinner(spHoraEn1, 0, 0);
        configurarSpinner(spHoraEn2, 0, 0);
        configurarSpinner(spHoraSa1, 0, 0);
        configurarSpinner(spHoraSa2, 0, 0);

    }

    private void habilitarCampos() {
        configurarSpinner(spHoraEn1, 0, 11);
        configurarSpinner(spHoraEn2, 0, 59);
        configurarSpinner(spHoraSa1, 0, 11);
        configurarSpinner(spHoraSa2, 0, 59);
        cmbAMPM.setDisable(false);
        cmbAMPM2.setDisable(false);
        cmbDiaAsistencia.setDisable(false);
        cmbDiaSalida.setDisable(false);
    }


    private void btnMostrarOnAction(javafx.event.ActionEvent actionEvent) throws IOException {
        abrirVentanaMostrar();
    }

    private void btnCancelarOnAction(javafx.event.ActionEvent actionEvent) throws IOException {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void abrirVentanaMostrar() throws IOException {

        try {
            // Verifica la ruta al archivo AsistenciaEmpleados.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/Asistencia_visualizar.fxml")); // Asegúrate de la ruta correcta
            Parent root = loader.load();

            // Accede al controlador de AsistenciaEmpleados
            AsistenciaDatosControlador controller = loader.getController();

            // Establece el ID del proyecto en el controlador de AsistenciaEmpleados
            controller.recibirIdProyecto(idProyectoSeleccionado);

            // Mostrar la ventana
            Stage stage1 = new Stage();
            stage1.setScene(new Scene(root));
            stage1.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        int idAsistencia = -1; // Valor predeterminado en caso de error o no selección

        try (Connection conn = Conexion.obtenerConexion()) {
            String asistencia = (String) cmbAsistencia.getValue();

            String sql = "SELECT idAsistenciaMarcar FROM tbAsistenciaMarcar WHERE asistencia = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, asistencia);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idAsistencia = rs.getInt("idAsistenciaMarcar");
                    }
                }


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idAsistencia;
    }



    //Configurar Spinners
    private void configurarSpinner(Spinner<Integer> spinner, int min, int max) {
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max);
        spinner.setValueFactory(valueFactory);
        spinner.setEditable(false);


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

    private void cargarDatos() {
        TbAsistencia.getItems().clear();
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement statement = conn.prepareStatement("select idemp.idEmpleado, idemp.nombreCompleto, idpro.idProyecto, idpro.nombre_proyecto from tbEmpleadosProyectos id\n" +
                     "inner join tbempleados idemp on idemp.idempleado = id.idEmpleado\n" +
                     "inner join tbProyectos idpro on idpro.idproyecto = id.idProyecto WHERE idpro.idproyecto = ?")) {

            // Establece el ID del proyecto en la consulta SQL
            statement.setInt(1, idProyectoSeleccionado);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("idempleado");
                String nombre = rs.getString("nombreCompleto");
                int idproyecto = rs.getInt("idproyecto");
                String nombr = rs.getString("nombre_proyecto");


                TbAsistencia.getItems().add(new AsistenciaVista(id, nombre, idproyecto, nombr));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buscarDatos(String busqueda) {

        TbAsistencia.getItems().clear(); // Limpiar los elementos actuales de la tabla

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement("SELECT idemp.idEmpleado, idemp.nombreCompleto, idpro.idProyecto, idpro.nombre_proyecto " +
                     "FROM tbEmpleadosProyectos id " +
                     "INNER JOIN tbempleados idemp ON idemp.idempleado = id.idEmpleado " +
                     "INNER JOIN tbProyectos idpro ON idpro.idproyecto = id.idProyecto " +
                     "WHERE idpro.idproyecto = ? " +
                     "AND (idemp.nombreCompleto LIKE ? OR idpro.nombre_proyecto LIKE ?)")) {

            // Preparar el parámetro de búsqueda para la consulta SQL
            String parametroBusqueda = "%" + busqueda + "%";
            stmt.setInt(1, idProyectoSeleccionado);
            for (int i = 2; i <= 3; i++) {
                stmt.setString(i, parametroBusqueda);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Obtener los datos y agregarlos a la tabla
                int id = rs.getInt("idempleado");
                String nombre = rs.getString("nombreCompleto");
                int idproyecto = rs.getInt("idproyecto");
                String nombr = rs.getString("nombre_proyecto");


                TbAsistencia.getItems().add(new AsistenciaVista(id, nombre, idproyecto, nombr));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void seleccionarEmpleado(MouseEvent event) {
        if (event.getClickCount() == 1) {
            // Obtener la fila seleccionada
            AsistenciaVista empleadoSeleccionado = (AsistenciaVista) TbAsistencia.getSelectionModel().getSelectedItem();

            // Verificar si se seleccionó una fila
            if (empleadoSeleccionado != null) {
                // Asignar el ID al Label
                LblIdEmpleado.setText(String.valueOf(empleadoSeleccionado.getIdE()));

                // Asignar el nombre al TextField
                txtEmpleadoSel.setText(empleadoSeleccionado.getIdempleado());

                lblidproyecto.setText(String.valueOf(empleadoSeleccionado.getIdproyecto()));

            }
        }
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


        String nombre = LblIdEmpleado.getText();

        int Asistencia = obtenerIdAsistenciaSeleccionado(cmbAsistencia);

        String fechaentrada = LblHoraEntrada.getText();

        String fechasalida = LblHoraSalida.getText();




        try (Connection conn = Conexion.obtenerConexion()) {
            String sql = "INSERT INTO tbAsistencia (idempleado, idAsistenciaMarcar, hora_entrada, hora_salida) " +
                    "VALUES (?, ?, ?, ?)";
            PreparedStatement ps =conn.prepareStatement(sql);
            ps.setString(1,nombre);

            ps.setInt(2,Asistencia);
            ps.setString(3, fechaentrada);
            ps.setString(4, fechasalida);

            ps.executeUpdate();

            agregar_empleadosControlador.mostrarAlerta("Inserción de asistencia", "Asistencia del empleado agregada exitosamente", Alert.AlertType.INFORMATION);



            borrarTexto();


        }catch (SQLException e) {
            agregar_empleadosControlador.mostrarAlerta("Error", "Ha ocurrido un error", Alert.AlertType.ERROR);
            e.printStackTrace();
        }



    }

    public void borrarTexto()
    {
        txtEmpleadoSel.setText("");
        LblIdEmpleado.setText("");
        cmbAsistencia.setValue("");

        cmbDiaAsistencia.setValue("");

        cmbDiaSalida.setValue("");

        cmbAMPM.setValue("");

        cmbAMPM2.setValue("");


    }





}