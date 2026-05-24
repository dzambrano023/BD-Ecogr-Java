package com.herencia.modelo;

public class Reciclador extends Usuario {

    private String materialEspecialidad;

    public Reciclador(int usuarioId, String nombre, String email,
                      String telefono, String direccion, String ciudad,
                      String materialEspecialidad) {
        super(usuarioId, nombre, email, telefono, direccion, ciudad);
        this.materialEspecialidad = materialEspecialidad;
    }

    public String getMaterialEspecialidad()                   { return materialEspecialidad; }
    public void   setMaterialEspecialidad(String material)    { this.materialEspecialidad = material; }

    @Override
    public String describir() {
        return "Reciclador: " + getNombre()
                + " | Material Especialidad: " + materialEspecialidad
                + " | Ciudad: " + getCiudad();
    }

    @Override
    public void realizarAccion() {
        System.out.println(getNombre() + " está reciclando material de tipo: " + materialEspecialidad);
    }
}
