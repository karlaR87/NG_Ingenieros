package com.example.ng_ingenieros;

public class Cargos {

    private int idCargo;

    private String cargo;

    public int getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(int idCargo) {
        this.idCargo = idCargo;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Cargos(int idCargo, String cargo) {
        this.idCargo = idCargo;
        this.cargo = cargo;
    }
}
