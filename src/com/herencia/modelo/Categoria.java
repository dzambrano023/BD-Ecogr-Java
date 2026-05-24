package com.herencia.modelo;

public class Categoria {

    private int    categoriaId;
    private String nombreCategoria;

    public Categoria(int categoriaId, String nombreCategoria) {
        this.categoriaId     = categoriaId;
        this.nombreCategoria = nombreCategoria;
    }

    public int    getCategoriaId()                      { return categoriaId; }
    public void   setCategoriaId(int id)                { this.categoriaId = id; }

    public String getNombreCategoria()                  { return nombreCategoria; }
    public void   setNombreCategoria(String nombre)     { this.nombreCategoria = nombre; }

    @Override
    public String toString() {
        return "Categoria [" + categoriaId + "]: " + nombreCategoria;
    }
}
