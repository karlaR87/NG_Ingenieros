package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.AsistenciaVista;
import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.Empleados;
import com.example.ng_ingenieros.SalarioEmp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalTime;

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

    @FXML
    private Label lblhorasasistidas;

    @FXML
    private Label lblhorassalidas;

    @FXML
    private Label lblIdProyecto;

    @FXML
    private Label lblhorassalida;



    @FXML
    private TextField txtSalarioEmp;
    @FXML
    private TextField txtSalarioHorasExtra;

    @FXML
    private Button btncancelar;

    @FXML
    private Button btnRegistrarSalario;

    @FXML
    private Button btnVerSalarios;
    @FXML
    private Button btnEditarHorasExtra;

    @FXML
    private Button btnEditarTotalDev;

    agregar_empleadosControlador emp = new agregar_empleadosControlador();

    public void initialize(AsistenciaVista empleadoSeleccionado) {
        txtNombreEmp.setText(String.valueOf(empleadoSeleccionado.getIdempleado()));

        lblIdAsistenciaregistrar.setText(String.valueOf(empleadoSeleccionado.getId()));

        lblidEmpleado.setText(String.valueOf(empleadoSeleccionado.getIdE()));

        lblIdProyecto.setText(String.valueOf(empleadoSeleccionado.getIdproyecto()));

        calcularHorasTrabajadas(empleadoSeleccionado.getIdE());

        DiasRemunerados(empleadoSeleccionado.getIdE());

        horasPorProyecto(empleadoSeleccionado.getIdempleado());

        CalcularHorasExtra();

        SalarioEmpleado(empleadoSeleccionado.getIdempleado());

        SalarioEmpleadoPorHoraExtra(empleadoSeleccionado.getIdempleado());

        txtSalarioHorasExtra.textProperty().addListener((observable, oldValue, newValue) -> {
            // Verificar si el nuevo valor es un número válido
            if (!newValue.matches("\\d*(\\.\\d{0,2})?")) {
                agregar_empleadosControlador.mostrarAlerta("Formato no válido", "No puede ingresar letras ni caracteres especiales que no sean el punto (.)", Alert.AlertType.WARNING);
                txtSalarioHorasExtra.setText(oldValue);

            } else {
                CalcularSalarioTotal();
                txtDescuentos();
                txtSalarioFinal();
            }
        });




        CalcularSalarioTotal();
        txtDescuentos();

        btnEditarTotalDev.setOnAction(actionEvent -> {
            // Verificar si el campo txtTotalDev está editable

            if (txtTotalDev.isEditable()) {
                // Si está editable, deshabilitar la edición
                txtTotalDev.setEditable(false);
            } else {
                // Si no está editable, habilitar la edición
                txtTotalDev.setEditable(true);
            }
        });


        txtTotalDev.textProperty().addListener((observable, oldValue, newValue) -> {
            // Verificar si el nuevo valor es un número válido
            if (!newValue.matches("\\d*(\\.\\d{0,2})?")) {
                agregar_empleadosControlador.mostrarAlerta("Formato no válido", "Ingrese un número válido. Restricciones: \n" +
                        "Máximo un punto decimal \n" +
                        "No debe contener letras ni caracteres especiales que no sean el punto \n" +
                        "No debe contener tres números después del punto decimal \n" +
                        "El número debe ser menor a 0", Alert.AlertType.WARNING);
                txtTotalDev.setText(oldValue);
            } else {
                txtDescuentos();
                txtSalarioFinal();
            }

        });

        txtSalarioFinal();



        btnRegistrarSalario.setOnAction(actionEvent -> {
            try {
                btnRegistrarSalarioOnAction(actionEvent);
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

        btnVerSalarios.setOnAction(actionEvent -> {
            try {
                mostrarVentanaSalarioOnAction(actionEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        btnEditarHorasExtra.setOnAction(actionEvent -> {
            // Verificar si el campo txtHorasExtras está editable
            if (txtSalarioHorasExtra.isEditable()) {
                // Si está editable, deshabilitar la edición
                txtSalarioHorasExtra.setEditable(false);
            } else {
                // Si no está editable, habilitar la edición
                txtSalarioHorasExtra.setEditable(true);
            }
        });


    }

    private void btnRegistrarSalarioOnAction(javafx.event.ActionEvent actionEvent) throws IOException {
        RegistrarSalario();
    }
    private void mostrarVentanaSalarioOnAction(javafx.event.ActionEvent actionEvent) throws IOException {
        mostrarVentanaSalario();
    }

    private void btnCancelarOnAction(javafx.event.ActionEvent actionEvent) throws IOException {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void mostrarVentanaSalario() throws IOException
    {


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/SalarioEmp_vista.fxml"));
        Parent root;

        root = loader.load();


        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

    }


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
    public void calcularHorasTrabajadas(int idEmpleado) {
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement("SELECT hora_entrada, hora_salida FROM tbAsistencia WHERE idempleado LIKE ? AND idAsistenciaMarcar = 1")) {

            pstmt.setInt(1, idEmpleado);

            double totalHorasTrabajadas = 0;
            double totalMinutosTrabajados = 0;

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String horaEntrada = rs.getString("hora_entrada");
                    String horaSalida = rs.getString("hora_salida");

                    double horasTrabajadas = calcularHorasEntreEntradaYSalida(horaEntrada, horaSalida);
                    double minutosTrabajados = calcularMinutosEntreEntradaYSalida(horaEntrada, horaSalida);

                    totalMinutosTrabajados += minutosTrabajados;
                    totalHorasTrabajadas += horasTrabajadas;
                }

                int horas = (int) totalHorasTrabajadas; // Tomar solo la parte entera de las horas
                int minutos = (int) totalMinutosTrabajados;

                // Si hay minutos suficientes para sumar una hora adicional, ajustar el total de horas
                if (minutos >= 60) {
                    horas += minutos / 60;
                    minutos = minutos % 60;
                }

                System.out.println("Total horas trabajadas: " + horas); // Imprimir las horas totales trabajadas

                lblhorasasistidas.setText(String.valueOf(totalHorasTrabajadas));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double calcularHorasEntreEntradaYSalida(String horaEntrada, String horaSalida) {


        String[] partesHoraEntrada = horaEntrada.split(", ");
        String tiempoEntrada = partesHoraEntrada[1];

        String[] horaMinEntrada = tiempoEntrada.split(" ");
        String horaEntradaString = horaMinEntrada[0];
        String periodoEntrada = horaMinEntrada[1]; // A.M. o P.M.

        String[] partesHoraSalida = horaSalida.split(", ");
        String tiempoSalida = partesHoraSalida[1];

        String[] horaMinSalida = tiempoSalida.split(" ");
        String horaSalidaString = horaMinSalida[0];
        String periodoSalida = horaMinSalida[1]; // A.M. o P.M.

        String[] horasYMinutosEntrada = horaEntradaString.split(":");
        int horasEntrada = Integer.parseInt(horasYMinutosEntrada[0]);
        int minutosEntrada = Integer.parseInt(horasYMinutosEntrada[1]);

        String[] horasYMinutosSalida = horaSalidaString.split(":");
        int horasSalida = Integer.parseInt(horasYMinutosSalida[0]);
        int minutosSalida = Integer.parseInt(horasYMinutosSalida[1]);


        if (periodoEntrada.equalsIgnoreCase("P.M.") && periodoSalida.equalsIgnoreCase("A.M.")) {
            horasEntrada += 12; // Sumar 12 a horasEntrada si cmbAMPM es "P.M." y cmbAMPM2 es "A.M."

            // Realizar el cálculo teniendo en cuenta la hora de salida es anterior a la hora de entrada (horario nocturno)
            double horasTrabajadas = (24 - horasEntrada) + horasSalida - 1;

            // Asegurarse de que las horas trabajadas no sean negativas
            if (horasTrabajadas < 0) {
                horasTrabajadas = 0;
            }

            int minutosTrabajados = 0;
            if (minutosSalida > minutosEntrada) {
                minutosTrabajados = minutosSalida - minutosEntrada;
            } else if (minutosSalida < minutosEntrada) {
                minutosTrabajados = minutosEntrada + minutosSalida;
            }

            // Imprimir los resultados
            System.out.println("Horas trabajadas: " + horasTrabajadas);


            return horasTrabajadas;
        }

        else {
            if (periodoSalida.equalsIgnoreCase("P.M.") && periodoEntrada.equalsIgnoreCase("A.M.")) {
                horasSalida += 12;
                System.out.println("Horas trabajadas aqui, a.m. p.m.");

            }

            if (periodoSalida.equalsIgnoreCase("P.M.") && periodoEntrada.equalsIgnoreCase("P.M.")) {
                // Si ambos son "P.M.", sumar 12 a las horas de entrada y salida
                horasEntrada += 12;
                horasSalida += 12;
                System.out.println("Horas trabajadas aqui, p.m. p.m.");
            }

            if (periodoSalida.equalsIgnoreCase("A.M.") && periodoEntrada.equalsIgnoreCase("A.M.")) {
                // Si ambos son "A.M.", sumar 0 a las horas de entrada y salida
                horasEntrada += 0;
                horasSalida += 0;
                System.out.println("Horas trabajadas aqui a.m. a.m.");
            }




            // Calcular las horas trabajadas
            double horasTrabajadas = horasSalida - horasEntrada ; // Descontando 1 hora de almuerzo

            if (horasTrabajadas > 5) {
                horasTrabajadas -= 1;
            }

            System.out.println("Horas trabajadas: " + horasTrabajadas);

            // Si las horas trabajadas son negativas, establecer en 0
            if (horasTrabajadas < 0) {
                horasTrabajadas = 0;
            }

            int minutosTrabajados = 0;
            if (minutosSalida > minutosEntrada) {
                minutosTrabajados = minutosSalida - minutosEntrada;
            } else if (minutosSalida < minutosEntrada) {
                minutosTrabajados = minutosEntrada + minutosSalida;

            }

            return horasTrabajadas;
        }

    }

    private double calcularMinutosEntreEntradaYSalida(String horaEntrada, String horaSalida) {
        String[] partesHoraEntrada = horaEntrada.split(", ");
        String tiempoEntrada = partesHoraEntrada[1];

        String[] horaMinEntrada = tiempoEntrada.split(" ");
        String horaEntradaString = horaMinEntrada[0];
        String periodoEntrada = horaMinEntrada[1]; // A.M. o P.M.

        String[] partesHoraSalida = horaSalida.split(", ");
        String tiempoSalida = partesHoraSalida[1];

        String[] horaMinSalida = tiempoSalida.split(" ");
        String horaSalidaString = horaMinSalida[0];
        String periodoSalida = horaMinSalida[1]; // A.M. o P.M.

        String[] horasYMinutosEntrada = horaEntradaString.split(":");
        int horasEntrada = Integer.parseInt(horasYMinutosEntrada[0]);
        int minutosEntrada = Integer.parseInt(horasYMinutosEntrada[1]);

        String[] horasYMinutosSalida = horaSalidaString.split(":");
        int horasSalida = Integer.parseInt(horasYMinutosSalida[0]);
        int minutosSalida = Integer.parseInt(horasYMinutosSalida[1]);


        // Calcular las horas trabajadas


        int minutosTrabajados = 0;
        if (minutosSalida > minutosEntrada) {
            minutosTrabajados = minutosSalida - minutosEntrada;
        } else if (minutosSalida < minutosEntrada) {
            minutosTrabajados = minutosEntrada + minutosSalida;

        }




        return minutosTrabajados;
    }

    //horas de trabajo por proyecto


    private void horasPorProyecto(String busqueda) {



        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement("select idemp.idEmpleado, idemp.nombreCompleto, idpro.idProyecto, idpro.horas_trabajo from tbEmpleadosProyectos id\n" +
                     "inner join tbempleados idemp on idemp.idempleado = id.idEmpleado\n" +
                     "inner join tbProyectos idpro on idpro.idproyecto = id.idProyecto  WHERE idemp.nombreCompleto LIKE ?")) {

            // Preparar el parámetro de búsqueda para la consulta SQL
            String parametroBusqueda = "%" + busqueda + "%";
            stmt.setString(1, parametroBusqueda);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Obtener los datos y agregarlos a la tabla
                int id = rs.getInt("horas_trabajo");



                lblhorassalidas.setText(String.valueOf(id));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void CalcularHorasExtra ()
    {
        double horas;
        double horaproyecto = 0.0;



        horas = Double.parseDouble(lblhorasasistidas.getText());

        horaproyecto = Double.parseDouble(lblhorassalidas.getText());


        double totalhoras = horas - horaproyecto;


        if (totalhoras <= 0) {
            txtHorasExtras.setText("0");
        } else {
            txtHorasExtras.setText(String.valueOf(totalhoras));
        }


    }

    private void SalarioEmpleado(String busqueda) {



        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement("select sueldo_dia from tbempleados  WHERE nombreCompleto LIKE ?")) {

            // Preparar el parámetro de búsqueda para la consulta SQL
            String parametroBusqueda = "%" + busqueda + "%";
            stmt.setString(1, parametroBusqueda);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Obtener los datos y agregarlos a la tabla
                Double salario = rs.getDouble("sueldo_dia");

                txtSalarioEmp.setText(String.valueOf(salario));



            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SalarioEmpleadoPorHoraExtra(String busqueda) {



        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement("select sueldo_horaExt from tbempleados where nombreCompleto LIKE ?")) {

            // Preparar el parámetro de búsqueda para la consulta SQL
            String parametroBusqueda = "%" + busqueda + "%";
            stmt.setString(1, parametroBusqueda);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Obtener los datos y agregarlos a la tabla
                Double salario = rs.getDouble("sueldo_horaExt");

                txtSalarioHorasExtra.setText(String.valueOf(salario));



            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void CalcularSalarioTotal() {
        //aqui se calculará todo el salario, incluyendo los descuentos
        float salariobruto;
        float salariohoraextra;
        int horasextra;
        int diasrem;



        salariobruto = Float.parseFloat(txtSalarioEmp.getText());
        salariohoraextra = Float.parseFloat(txtSalarioHorasExtra.getText());
        horasextra = Integer.parseInt(txtHorasExtras.getText());
        diasrem = Integer.parseInt(txtDiasRemunerados.getText());

        float primerresultado = salariobruto * (float) diasrem;
        float segundoresultado = salariohoraextra * (float) horasextra;
        float totaldevengado = primerresultado + segundoresultado;

        totaldevengado = (float) (Math.round(totaldevengado * 100.0) / 100.0);

        txtTotalDev.setText(String.valueOf(totaldevengado));

    }

    private void txtDescuentos()
    {
        //aqui se calculará todo el salario, incluyendo los descuentos
        float salariobruto;
        float salariohoraextra;
        int horasextra;
        int diasrem;
        //variables para los descuentos de renta
        float tramo2 = 0.10F;
        float tramo3 = 0.20F;
        float tramo4 = 0.30F;
        float sal1 = 0;
        float sal2;

        float totaldevengado = Float.parseFloat(txtTotalDev.getText());

        double AFP = totaldevengado * 0.0725;
        double ISSS = totaldevengado * 0.03;

        AFP = Math.round(AFP * 100.0) / 100.0;
        ISSS = Math.round(ISSS * 100.0) / 100.0;

        txtAFP.setText(String.valueOf(AFP));
        txtSeguroSocial.setText(String.valueOf(ISSS));

        if(totaldevengado < 472.00)
        {
            txtRenta.setText("0.00");
        }
        else if(totaldevengado >= 472.01 && totaldevengado < 895.25)
        {
            sal1 = totaldevengado * tramo2;
            sal2 = (float) (totaldevengado - (sal1 + 17.67));
            txtRenta.setText(String.valueOf(sal2));

        }
        else if(totaldevengado >= 895.25 && totaldevengado < 2038.11)
        {
            sal1 = totaldevengado * tramo3;
            sal2 = (float) (totaldevengado - (sal1 + 60.00));
            txtRenta.setText(String.valueOf(sal2));
        }
        else if(totaldevengado >= 2038.11)
        {
            sal1 = totaldevengado * tramo4;
            sal2 = (float) (totaldevengado - (sal1 + 288.57));
            txtRenta.setText(String.valueOf(sal2));
        }

    }


    public void txtSalarioFinal()
    {
        float dev;
        float afp;
        float seguro;
        float renta;
        float total;

        dev = Float.parseFloat(txtTotalDev.getText());
        afp = Float.parseFloat(txtAFP.getText());
        seguro = Float.parseFloat(txtSeguroSocial.getText());
        renta = Float.parseFloat(txtRenta.getText());


        total = dev - (afp + seguro + renta);

        total = (float) (Math.round(total * 100.0) / 100.0);

        txtSalarioFinal.setText(String.valueOf(total));
    }

    public void RegistrarSalario()
    {

        int diasrem = Integer.parseInt(txtDiasRemunerados.getText());
        int horasextras = Integer.parseInt(txtHorasExtras.getText());
        double totaldev = Double.parseDouble(txtTotalDev.getText());
        double afp = Double.parseDouble(txtAFP.getText());
        double isss = Double.parseDouble(txtSeguroSocial.getText());
        double renta = Double.parseDouble(txtRenta.getText());
        double salariof = Double.parseDouble(txtSalarioFinal.getText());
        int idempleado = Integer.parseInt(lblidEmpleado.getText());

        try (Connection conn = Conexion.obtenerConexion()) {
            String sql = "INSERT INTO tbplanillas (diasRemunerados, horasExtTrabajadas, totalDevengado, AFP, " +
                    "seguro_social, descuento_Renta, salarioFinal, idempleado) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps =conn.prepareStatement(sql);

            ps.setInt(1, diasrem);
            ps.setInt(2, horasextras);
            ps.setDouble(3, totaldev);
            ps.setDouble(4, afp);
            ps.setDouble(5, isss);
            ps.setDouble(6, renta);
            ps.setDouble(7, salariof);
            ps.setInt(8, idempleado);

            ps.executeUpdate();

            agregar_empleadosControlador.mostrarAlerta("Inserción de salario", "Salario registrado exitosamente", Alert.AlertType.INFORMATION);

            if (TBMostrarAsistencia != null) {
                TBMostrarAsistencia.getItems().clear();
                AsistenciaDatosControlador asistenciaDatosControlador = new AsistenciaDatosControlador();
                asistenciaDatosControlador.setTableAsistencia(TBMostrarAsistencia);
                asistenciaDatosControlador.cargarDatos();
            }



        }catch (SQLException e) {
            agregar_empleadosControlador.mostrarAlerta("Error", "Ha ocurrido un error", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
}

