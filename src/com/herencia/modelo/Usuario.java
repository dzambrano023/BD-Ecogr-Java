package com.herencia.modelo;

public abstract class Usuario {

    protected int usuarioId;
    protected String nombre;
    protected String email;
    protected String telefono;
    protected String direccion;
    protected String ciudad;

    public Usuario(int usuarioId, String nombre, String email,
                   String telefono, String direccion, String ciudad) {
        this.usuarioId = usuarioId;
        this.nombre    = nombre;
        this.email     = email;
        this.telefono  = telefono;
        this.direccion = direccion;
        this.ciudad    = ciudad;
    }

    // ── Getters y Setters ──────────────────────────────────────────────────
    public int    getUsuarioId()              { return usuarioId; }
    public void   setUsuarioId(int id)        { this.usuarioId = id; }

    public String getNombre()                 { return nombre; }
    public void   setNombre(String nombre)    { this.nombre = nombre; }

    public String getEmail()                  { return email; }
    public void   setEmail(String email)      { this.email = email; }

    public String getTelefono()               { return telefono; }
    public void   setTelefono(String tel)     { this.telefono = tel; }

    public String getDireccion()              { return direccion; }
    public void   setDireccion(String dir)    { this.direccion = dir; }

    public String getCiudad()                 { return ciudad; }
    public void   setCiudad(String ciudad)    { this.ciudad = ciudad; }

    // ── Métodos abstractos (cada subclase define su propio comportamiento) ──
    public abstract String describir();
    public abstract void   realizarAccion();
}
