package com.example.ng_ingenieros;

public class Usuarios {

    private int IdUsuario;

    private String nombreUser;

    private String contraUser;

    private int NivelUser;

    private int IdEmpleado;

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        IdUsuario = idUsuario;
    }

    public String getNombreUser() {
        return nombreUser;
    }

    public void setNombreUser(String nombreUser) {
        this.nombreUser = nombreUser;
    }

    public String getContraUser() {
        return contraUser;
    }

    public void setContraUser(String contraUser) {
        this.contraUser = contraUser;
    }

    public int getNivelUser() {
        return NivelUser;
    }

    public void setNivelUser(int nivelUser) {
        NivelUser = nivelUser;
    }

    public int getIdEmpleado() {
        return IdEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        IdEmpleado = idEmpleado;
    }


}
