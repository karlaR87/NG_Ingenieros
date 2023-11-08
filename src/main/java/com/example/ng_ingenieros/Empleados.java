package com.example.ng_ingenieros;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Empleados {
    private int id;
    private String nombre;


    private String Dui;

    private String correo;
    private double sueldoDia;
    private double sueldoHora;

    private String cuentaBancaria;

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    private String cargo;
    private String plaza;
    private  String proyecto;

    public Empleados(int id, String nombre, String dui, double sueldoDia, double sueldoHora, String cargo, String plaza, String proyecto) {
        this.id = id;
        this.nombre = nombre;

        Dui = dui;

        this.sueldoDia = sueldoDia;
        this.sueldoHora = sueldoHora;
        this.cargo = cargo;
        this.plaza = plaza;
        this.proyecto = proyecto;
    }

    public int getId() {
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
        return Dui;
    }

    public void setDui(String dui) {
        Dui = dui;
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









}




