package com.example.ng_ingenieros;

public class Empleados {
    private  int id;
    private String nombre;


    private String Dui;

    private String correo;
    private double sueldoDia;
    private double sueldoHora;

    private String cuentaBancaria;



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


    public Empleados(int id, String nombre, String dui, Double sueldoDia, Double sueldoHora, String cargo, String plazo) {
        this.id = id;
        this.nombre = nombre;
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

    private boolean seleccionado;

    // Constructor que incluye el nuevo campo
    public Empleados(int id, String nombre, boolean seleccionado) {
        this.id = id;
        this.nombre = nombre;
        this.seleccionado = seleccionado;
    }

    // Getters y setters para el nuevo campo
    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }
}