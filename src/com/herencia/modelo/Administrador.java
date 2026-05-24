package com.herencia.modelo;

public class Administrador extends Usuario {

    private String nivelAcceso;

    public Administrador(int usuarioId, String nombre, String email,
                         String telefono, String direccion, String ciudad,
                         String nivelAcceso) {
        super(usuarioId, nombre, email, telefono, direccion, ciudad);
        this.nivelAcceso = nivelAcceso;
    }

    public String getNivelAcceso()                { return nivelAcceso; }
    public void   setNivelAcceso(String nivel)    { this.nivelAcceso = nivel; }

    @Override
    public String describir() {
        return "Administrador: " + getNombre()
                + " | Nivel de acceso: " + nivelAcceso
                + " | Ciudad: " + getCiudad();
    }

    @Override
    public void realizarAccion() {
        System.out.println(getNombre() + " está gestionando el centro de acopio. Nivel: " + nivelAcceso);
    }
}
