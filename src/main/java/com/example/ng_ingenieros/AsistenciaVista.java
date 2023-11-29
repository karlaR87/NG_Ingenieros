package com.example.ng_ingenieros;

public class AsistenciaVista {

    private int id;

    public AsistenciaVista(int id, int ide, String nombre, String asistencia, String fechaentrada, String fechasalida, int idproyecto, String nombrepro) {
        this.id = id;
        this.idE = ide;
        this.idempleado = nombre;
        this.marcarasistencia = asistencia;
        this.hora_entrada = fechaentrada;
        this.hora_salida = fechasalida;
        this.idproyecto = idproyecto;
        this.nombreProyecto = nombrepro;
    }


    public int getIdE() {
        return idE;
    }

    public void setIdE(int idE) {
        this.idE = idE;
    }

    private int idE;
    private String idempleado;

    private String nombreAsi;

    private String marcarasistencia;

    private String hora_entrada;

    private String hora_salida;

    private int idproyecto;

    private String nombreProyecto;



    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdempleado() {
        return idempleado;
    }

    public void setIdempleado(String idempleado) {
        this.idempleado = idempleado;
    }

    public String getMarcarasistencia() {
        return marcarasistencia;
    }

    public void setMarcarasistencia(String marcarasistencia) {
        this.marcarasistencia = marcarasistencia;
    }

    public String getHora_entrada() {
        return hora_entrada;
    }

    public void setHora_entrada(String hora_entrada) {
        this.hora_entrada = hora_entrada;
    }

    public String getHora_salida() {
        return hora_salida;
    }

    public void setHora_salida(String hora_salida) {
        this.hora_salida = hora_salida;
    }

    public int getIdproyecto() {
        return idproyecto;
    }

    public void setIdproyecto(int idproyecto) {
        this.idproyecto = idproyecto;
    }


    public String getNombreAsi() {
        return nombreAsi;
    }

    public void setNombreAsi(String nombreAsi) {
        this.nombreAsi = nombreAsi;
    }

}
