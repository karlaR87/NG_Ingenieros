package com.example.ng_ingenieros;

public class Usuarios {

    private int IdUsuario;

    private String nombreUser;



    private String contraUser;

    private String NivelUser;

    private String nombreEmp;

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

    public String getNivelUser() {
        return NivelUser;
    }

    public void setNivelUser(String nivelUser) {
        NivelUser = nivelUser;
    }

    public String getNombreEmp() {
        return nombreEmp;
    }

    public void setNombreEmp(String nombreEmp) {
        this.nombreEmp = nombreEmp;
    }

    public Usuarios(int idU, String nombreU, String contraU, String nivelU, String nombreEMP) {
    this.IdUsuario = idU;
    this.nombreUser = nombreU;
    this.contraUser = contraU;
    this.NivelUser = nivelU;
    this.nombreEmp = nombreEMP;
    }
}
