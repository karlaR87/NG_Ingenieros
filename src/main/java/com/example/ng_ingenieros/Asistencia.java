package com.example.ng_ingenieros;

public class Asistencia {

    private int id;
    private int idempleado;

    private int marcarasistencia;

    private String hora_entrada;

    private String hora_salida;

    private int idproyecto;

    public Asistencia(int id, int idempleado, int marcarasistencia, String hora_entrada, String hora_salida, int idproyecto) {
        this.id = id;
        this.idempleado = idempleado;
        this.marcarasistencia = marcarasistencia;
        this.hora_entrada = hora_entrada;
        this.hora_salida = hora_salida;
        this.idproyecto = idproyecto;
    }

    public Asistencia(int id, int idempleado, int marcarasistencia, String hora_entrada, String hora_salida) {
        this.id = id;
        this.idempleado = idempleado;
        this.marcarasistencia = marcarasistencia;
        this.hora_entrada = hora_entrada;
        this.hora_salida = hora_salida;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdempleado() {
        return idempleado;
    }

    public void setIdempleado(int idempleado) {
        this.idempleado = idempleado;
    }

    public int getMarcarasistencia() {
        return marcarasistencia;
    }

    public void setMarcarasistencia(int marcarasistencia) {
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


}
