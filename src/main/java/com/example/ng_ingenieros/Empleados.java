package com.example.ng_ingenieros;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.sql.*;

public class Empleados {
    private  int id;
    private String nombre;


    private String Dui;

    private String correo;
    private double sueldoDia;
    private double sueldoHora;

    private String cuentaBancaria;

    //checkBox para asistencia
    private final BooleanProperty checkBox1 = new SimpleBooleanProperty();
    private final BooleanProperty checkBox2 = new SimpleBooleanProperty();
    private final BooleanProperty checkBox3 = new SimpleBooleanProperty();
    private final BooleanProperty checkBox4 = new SimpleBooleanProperty();
    private final BooleanProperty checkBox5 = new SimpleBooleanProperty();

    public BooleanProperty getCheckBox1Property() {
        return checkBox1;
    }

    public BooleanProperty getCheckBox2Property() {
        return checkBox2;
    }

    public BooleanProperty getCheckBox3Property() {
        return checkBox3;
    }

    public BooleanProperty getCheckBox4Property() {
        return checkBox4;
    }

    public BooleanProperty getCheckBox5Property() {
        return checkBox5;
    }


    public Empleados(String nombre, String dui, String correo, String cargo, String plaza, Double pagoExtra, String numCuenta, Double sueldo) {
        this.nombre = nombre;
        Dui = dui;
        this.correo=correo;
        this.cargo = cargo;
        this.plaza = plaza;
        this.sueldoHora = pagoExtra;
        this.cuentaBancaria=numCuenta;
        this.sueldoDia = sueldo;
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

    private String cargo;
    private String plaza;
    private String proyecto;

    public int getIdplaza() {
        return idplaza;
    }

    public void setIdplaza(int idplaza) {
        this.idplaza = idplaza;
    }

    private int idplaza;

    public int getIdcargo() {
        return idcargo;
    }

    public void setIdcargo(int idcargo) {
        this.idcargo = idcargo;
    }

    private int idcargo;

    public Empleados() {


    }


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


    public Empleados(int id, String nombre, String dui, String correo, double sueldoDia, double sueldoHora, String cuentaBancaria, String cargo, String plaza) {
        this.id = id;
        this.nombre = nombre;
        Dui = dui;
        this.correo = correo;
        this.sueldoDia = sueldoDia;
        this.sueldoHora = sueldoHora;
        this.cuentaBancaria = cuentaBancaria;
        this.cargo = cargo;
        this.plaza = plaza;
    }

    //Constructor para la asistencia
    public Empleados(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
}