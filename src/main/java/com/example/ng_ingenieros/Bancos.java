package com.example.ng_ingenieros;

public class Bancos {

    private int idbanco;

    private String banco;

    public Bancos(int idbanco, String banco) {
        this.idbanco = idbanco;
        this.banco = banco;
    }

    public int getIdbanco() {
        return idbanco;
    }

    public void setIdbanco(int idbanco) {
        this.idbanco = idbanco;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }
}
