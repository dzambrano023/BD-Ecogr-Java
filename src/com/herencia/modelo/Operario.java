package com.herencia.modelo;

public class Operario extends Usuario {

    private String turno;

    public Operario(int usuarioId, String nombre, String email,
                    String telefono, String direccion, String ciudad,
                    String turno) {
        super(usuarioId, nombre, email, telefono, direccion, ciudad);
        this.turno = turno;
    }

    public String getTurno()           { return turno; }
    public void   setTurno(String t)   { this.turno = t; }

    @Override
    public String describir() {
        return "Operario: " + getNombre()
                + " | Turno: " + turno
                + " | Ciudad: " + getCiudad();
    }

    @Override
    public void realizarAccion() {
        System.out.println(getNombre() + " está operando en turno: " + turno);
    }
}
