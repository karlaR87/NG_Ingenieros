package com.example.ng_ingenieros;

public class AsistenciaVista {

    private int id;

    public AsistenciaVista(int id, int idE, String idempleado, String marcarasistencia, String hora_entrada, String hora_salida) {
        this.id = id;
        this.idE = idE;
        this.idempleado = idempleado;

        this.marcarasistencia = marcarasistencia;
        this.hora_entrada = hora_entrada;
        this.hora_salida = hora_salida;
    }

    private int idE;

    public int getIdE() {
        return idE;
    }

    public void setIdE(int idE) {
        this.idE = idE;
    }

    private String idempleado;



    private String nombreAsi;

    private String marcarasistencia;

    private String hora_entrada;

    private String hora_salida;



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
}
