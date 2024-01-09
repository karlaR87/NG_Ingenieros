package com.example.ng_ingenieros;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class Conexion {

    public static void main(String[] args) {

    }
// cambiar el server name por el que les aparezca en sqlserver.

   //private static final String URL = "jdbc:sqlserver://localhost\\DESKTOP-LSV5E7C:53617;databaseName=NGingenierosBD;encrypt=false;trustServerCertificate=true;";

    //private static final String URL = "jdbc:sqlserver://localhost\\DESKTOP-KD9DVMM:63886;databaseName=NGingenierosBD;encrypt=false;trustServerCertificate=true;";

    private static final String URL = "jdbc:sqlserver://localhost\\DESKTOP-6A0RB95:52575;databaseName=NGingenierosBD;encrypt=false;trustServerCertificate=true;";

   // private static final String URL = "jdbc:sqlserver://localhost\\LAPTOP-J5ID0GJ2:51695;databaseName=NGingenierosBD;encrypt=false;trustServerCertificate=true;";

    //private static final String URL = "jdbc:sqlserver://localhost\\LAPTOP-FCPSFI90:1433;databaseName=NGingenierosBD;encrypt=false;trustServerCertificate=true;";



    private static final String USUARIO = "sa";
    private static final String CONTRASENA = "itr2023";  //cambiar la contraseña por la de sus usuarios de sql

    public static Connection conexion = null;

    public static Connection obtenerConexion() {

        try {
            // Cargar el controlador JDBC de SQL Server (Asegúrate de tener el archivo .jar en tu proyecto)
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            // Obtener la conexión a la base de datos
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
        } catch (ClassNotFoundException e) {
            System.err.println("Error al cargar el controlador JDBC de SQL Server: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
        }
        return conexion;
    }

    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}
