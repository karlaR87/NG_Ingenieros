package com.example.ng_ingenieros;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Empleados {
    private  int id;
    private String nombre;


    private String dui;

    private String correo;
    private double sueldoDia;
    private double sueldoHora;

    private String cuentaBancaria;

    private String cargo;
    private String actividad;

    private String plaza;
    private String proyecto;

    private int idplaza;
    private int idcargo;
    private boolean seleccionado;


    public static ObservableList<Empleados> empleados = FXCollections.observableArrayList();

//CONSTRUCTOR PARA EMPLEADOS DE PROYECTOS
public Empleados(String nombre, String dui, String correo, int idCargo, Double pagoExtra, String numCuenta, Double sueldo) {
    this.nombre = nombre;
    this.dui = dui;
    this.correo = correo;
    this.idcargo = idCargo;
    this.sueldoHora = pagoExtra;
    this.cuentaBancaria = numCuenta;
    this.sueldoDia = sueldo;
}

    public Empleados(int id, String nombre, String dui, Double sueldoDia, Double sueldoHora, String cargo, String plazo) {
        this.id = id;
        this.nombre = nombre;

        this.dui = dui;

        this.sueldoDia = sueldoDia;
        this.sueldoHora = sueldoHora;
        this.cargo = cargo;
        this.plaza = plazo;

    }


    public Empleados(int id, String nombre, String dui, double sueldoDia, double sueldoHora, String cargo, String plaza) {
        this.id = id;
        this.nombre = nombre;
        this.dui = dui;
        this.sueldoDia = sueldoDia;
        this.sueldoHora = sueldoHora;
        this.cargo = cargo;
        this.plaza = plaza;
    }

    public Empleados(String nombre, String dui, String corre, double sueldodia, double sueldohora, String cuenta, String cargo) {
        this.nombre = nombre;
        this.dui = dui;
        this.correo = corre;
        this.sueldoDia = sueldodia;

        this.sueldoHora = sueldohora;
        this.cuentaBancaria = cuenta;
        this.cargo = cargo;


    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public  int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public double getSueldoDia() {
        return sueldoDia;
    }

    public void setSueldoDia(double sueldoDia) {
        this.sueldoDia = sueldoDia;
    }

    public double getSueldoHora() {
        return sueldoHora;
    }

    public void setSueldoHora(double sueldoHora) {
        this.sueldoHora = sueldoHora;
    }

    public String getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(String cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getPlaza() {
        return plaza;
    }

    public void setPlaza(String plaza) {
        this.plaza = plaza;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }



    public int getIdplaza() {
        return idplaza;
    }

    public void setIdplaza(int idplaza) {
        this.idplaza = idplaza;
    }


    public int getIdcargo() {
        return idcargo;
    }

    public void setIdcargo(int idcargo) {
        this.idcargo = idcargo;
    }



    public Empleados(int id, String nombre, String dui, String correo, double sueldoDia, double sueldoHora, String cuentaBancaria, String cargo, String plaza) {
        this.id = id;
        this.nombre = nombre;
        this.dui = dui;
        this.correo = correo;
        this.sueldoDia = sueldoDia;
        this.sueldoHora = sueldoHora;
        this.cuentaBancaria = cuentaBancaria;
        this.cargo = cargo;
        this.plaza = plaza;
    }

    //constructor para rmpledos de proyectos



    // Constructor de empleados a elegir

    public Empleados(int id, String nombre, String dui, String correo, double sueldoDia, double sueldoHora, String cuentaBancaria, String cargo) {
        this.id = id;
        this.nombre = nombre;
        this.dui = dui;
        this.correo = correo;
        this.sueldoDia = sueldoDia;
        this.sueldoHora = sueldoHora;
        this.cuentaBancaria = cuentaBancaria;
        this.cargo = cargo;
    }

    // Getters y setters para el nuevo campo
    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    @Override
    public String toString() {
        return "Empleados{" +
                "nombre='" + nombre + '\'' +
                ", dui='" + dui + '\'' +
                ", correo='" + correo + '\'' +
                ", sueldoDia=" + sueldoDia +
                ", sueldoHora=" + sueldoHora +
                ", cuentaBancaria='" + cuentaBancaria + '\'' +
                ", idcargo=" + idcargo +
                '}';
    }
}