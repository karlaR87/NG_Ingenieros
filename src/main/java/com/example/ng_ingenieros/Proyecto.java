package com.example.ng_ingenieros;

public class Proyecto{
    private int id;
    private String nombre;
    private String lugar;
    private int horas;
    private String inicio;

    private String Final;
    private String estado;

    private String ing;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIng() {
        return ing;
    }

    public void setIng(String ing) {
        this.ing = ing;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public int getHoras() {
        return horas;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getFinal() {
        return Final;
    }

    public void setFinal(String aFinal) {
        Final = aFinal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Proyecto(int id,String nombre, String lugar, int horas, String inicio, String fin, String estado) {
        this.id=id;
        this.nombre = nombre;
        this.lugar = lugar;
        this.horas = horas;
        this.inicio = inicio;
        this.Final = fin;
        this.estado = estado;
    }
}
