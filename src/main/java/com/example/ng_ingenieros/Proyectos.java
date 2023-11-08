package com.example.ng_ingenieros;

public class Proyectos {

    private int idproyecto;
    private String nombre_proyecto;
    private String lugar_proyecto;
    private int horas_trabajo;
    private String estado;

    public Proyectos(int idproyecto, String nombre_proyecto, String lugar_proyecto, int horas_trabajo, String estado) {
        this.idproyecto = idproyecto;
        this.nombre_proyecto = nombre_proyecto;
        this.lugar_proyecto = lugar_proyecto;
        this.horas_trabajo = horas_trabajo;
        this.estado = estado;
    }

    public int getIdproyecto() {
        return idproyecto;
    }

    public void setIdproyecto(int idproyecto) {
        this.idproyecto = idproyecto;
    }

    public String getNombre_proyecto() {
        return nombre_proyecto;
    }

    public void setNombre_proyecto(String nombre_proyecto) {
        this.nombre_proyecto = nombre_proyecto;
    }

    public String getLugar_proyecto() {
        return lugar_proyecto;
    }

    public void setLugar_proyecto(String lugar_proyecto) {
        this.lugar_proyecto = lugar_proyecto;
    }

    public int getHoras_trabajo() {
        return horas_trabajo;
    }

    public void setHoras_trabajo(int horas_trabajo) {
        this.horas_trabajo = horas_trabajo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
