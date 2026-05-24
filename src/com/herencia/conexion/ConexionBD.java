package com.herencia.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// conexion BD

public class ConexionBD {
    private static final String URL      = "jdbc:mysql://localhost:3306/proyecto_ods12"; // url y puerto de conexion
    private static final String USER     = "root";
    private static final String PASSWORD = "";


    private static Connection conexion = null;

    // Devuelve la conexión activa si no existe o está cerrada, la crea.

    public static Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                //  metodo para establecer la conexion a mysql, se le pasan como parametros la url, usuario y contraseña

                conexion = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println(" Conexión a MySQL establecida correctamente.");
            }
        } catch (SQLException e) {
            System.out.println(" Error al conectar con MySQL: " + e.getMessage());
            System.out.println(" Verifica URL, usuario y contraseña en ConexionDB.java");
        }
        return conexion;
    }


     //Cierra la conexión cuando termina la aplicación.
     // Uso: ConexionDB.cerrar();

    public static void cerrar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
}
