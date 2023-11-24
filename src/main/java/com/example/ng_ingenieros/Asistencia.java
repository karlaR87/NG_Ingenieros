package com.example.ng_ingenieros;

public class Asistencia {

    private int id;
    private int idempleado;

    private int marcarasistencia;

    private double hora_entrada;

    private double hora_salida;

    public Asistencia(int id, int idempleado, int marcarasistencia, double hora_entrada, double hora_salida) {
        this.id = id;
        this.idempleado = idempleado;
        this.marcarasistencia = marcarasistencia;
        this.hora_entrada = hora_entrada;
        this.hora_salida = hora_salida;
    }

    public Asistencia()
    {

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

    public double getHora_entrada() {
        return hora_entrada;
    }

    public void setHora_entrada(double hora_entrada) {
        this.hora_entrada = hora_entrada;
    }

    public double getHora_salida() {
        return hora_salida;
    }

    public void setHora_salida(double hora_salida) {
        this.hora_salida = hora_salida;
    }
}
