package com.example.ng_ingenieros;

public class SalarioEmp {

    private int idSalario;
    private int idasistencia;

    private int idempleado;

    private String nombrempleado;

    private String DUI;
    private String numerocuenta;

    private int diasremunerados;

    private String horasextras;

    private float totaldevengado;

    private float AFP;

    private float segurosocial;

    private float descuentorenta;

    private float salariofinal;

    private int idProyecto;
    private String NombreProyecto;




    public SalarioEmp(int idSalario, String nombrempleado, String DUI, String numerocuenta, int diasremunerados, String horasextras, float totaldevengado, float AFP, float segurosocial, float descuentorenta, float salariofinal, int idProyecto, String nombreProyecto) {
        this.idSalario = idSalario;
        this.idempleado = idempleado;
        this.nombrempleado = nombrempleado;
        this.DUI = DUI;
        this.numerocuenta = numerocuenta;
        this.diasremunerados = diasremunerados;
        this.horasextras = horasextras;
        this.totaldevengado = totaldevengado;
        this.AFP = AFP;
        this.segurosocial = segurosocial;
        this.descuentorenta = descuentorenta;
        this.salariofinal = salariofinal;
        this.idProyecto = idProyecto;
        this.NombreProyecto = nombreProyecto;
    }

    public int getIdSalario() {
        return idSalario;
    }

    public void setIdSalario(int idSalario) {
        this.idSalario = idSalario;
    }

    public int getIdasistencia() {
        return idasistencia;
    }

    public void setIdasistencia(int idasistencia) {
        this.idasistencia = idasistencia;
    }

    public int getDiasremunerados() {
        return diasremunerados;
    }

    public void setDiasremunerados(int diasremunerados) {
        this.diasremunerados = diasremunerados;
    }

    public String getHorasextras() {
        return horasextras;
    }

    public void setHorasextras(String horasextras) {
        this.horasextras = horasextras;
    }

    public float getTotaldevengado() {
        return totaldevengado;
    }

    public void setTotaldevengado(float totaldevengado) {
        this.totaldevengado = totaldevengado;
    }

    public float getAFP() {
        return AFP;
    }

    public void setAFP(float AFP) {
        this.AFP = AFP;
    }

    public float getSegurosocial() {
        return segurosocial;
    }

    public void setSegurosocial(float segurosocial) {
        this.segurosocial = segurosocial;
    }

    public float getDescuentorenta() {
        return descuentorenta;
    }

    public void setDescuentorenta(float descuentorenta) {
        this.descuentorenta = descuentorenta;
    }

    public float getSalariofinal() {
        return salariofinal;
    }

    public void setSalariofinal(float salariofinal) {
        this.salariofinal = salariofinal;
    }

    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    public SalarioEmp(int idSalario, int idasistencia, int idempleado, String nombrempleado, int diasremunerados, String horasextras, float totaldevengado, float AFP, float segurosocial, float descuentorenta, float salariofinal) {
        this.idSalario = idSalario;
        this.idasistencia = idasistencia;
        this.idempleado = idempleado;
        this.nombrempleado = nombrempleado;
        this.diasremunerados = diasremunerados;
        this.horasextras = horasextras;
        this.totaldevengado = totaldevengado;
        this.AFP = AFP;
        this.segurosocial = segurosocial;
        this.descuentorenta = descuentorenta;
        this.salariofinal = salariofinal;
    }

    public SalarioEmp(int idSalario , String nombrempleado, int diasremunerados, String horasextras, float totaldevengado, float AFP, float segurosocial, float descuentorenta, float salariofinal) {
        this.idSalario = idSalario;
        this.nombrempleado = nombrempleado;
        this.diasremunerados = diasremunerados;
        this.horasextras = horasextras;
        this.totaldevengado = totaldevengado;
        this.AFP = AFP;
        this.segurosocial = segurosocial;
        this.descuentorenta = descuentorenta;
        this.salariofinal = salariofinal;

    }

    public int getIdempleado() {
        return idempleado;
    }

    public void setIdempleado(int idempleado) {
        this.idempleado = idempleado;
    }

    public String getNombrempleado() {
        return nombrempleado;
    }

    public void setNombrempleado(String nombrempleado) {
        this.nombrempleado = nombrempleado;
    }

    public String getNombreProyecto() {
        return NombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        NombreProyecto = nombreProyecto;
    }

    public String getDUI() {
        return DUI;
    }

    public void setDUI(String DUI) {
        this.DUI = DUI;
    }

    public String getNumerocuenta() {
        return numerocuenta;
    }

    public void setNumerocuenta(String numerocuenta) {
        this.numerocuenta = numerocuenta;
    }
}
