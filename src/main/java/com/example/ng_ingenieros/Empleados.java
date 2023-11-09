package com.example.ng_ingenieros;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Empleados {
    private int id;
    private String nombre;


    private String Dui;

    private String correo;
    private double sueldoDia;
    private double sueldoHora;

    private String cuentaBancaria;

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

    public int getCargo() {
        return cargo;
    }

    public void setCargo(int cargo) {
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

    private int cargo;
    private String plaza;
    private  String proyecto;

    public Empleados() {

    }

    public Empleados(int id, String nombre, String dui, double sueldoDia, double sueldoHora, int cargo, String plaza, String proyecto) {
        this.id = id;
        this.nombre = nombre;

        Dui = dui;

        this.sueldoDia = sueldoDia;
        this.sueldoHora = sueldoHora;
        this.cargo = cargo;
        this.plaza = plaza;
        this.proyecto = proyecto;
    }

    public ResultSet cargarCargos(){
        PreparedStatement ps;
        Connection conn;
        try{
            conn = Conexion.obtenerConexion();
            ps = conn.prepareStatement("select * from tbcargos");
            ResultSet respuesta = ps.executeQuery();
            return respuesta;
        }catch (Exception e){
            return null;
        }
    }


    public int agregarEmpleados(){
        PreparedStatement ps;
        Connection conn = null;
        try{
            conn = Conexion.obtenerConexion();
            String query="insert into tbempleados(nombre, dui, correo, sueldo_dia, sueldo_horaExt, numero_cuentabancaria, idcargo) values(?,?,?,?,?,?,?)";
            ps = conn.prepareStatement(query);
            ps.setString(1, nombre);
            ps.setString(2, Dui);
            ps.setString(3, correo);
            ps.setDouble(4, sueldoDia);
            ps.setDouble(5,sueldoHora);
            ps.setString(6,cuentaBancaria);
            ps.setInt(7, cargo);
            ps.executeUpdate();
            return 1;
        }catch (Exception e) {
            return 0;
        }finally {
            if (conn != null){
                try{
                    conn.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }

        }

    }






}




