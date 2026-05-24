package com.herencia.modelo;

import java.util.Date;

public class Producto {

    private int      productoId;
    private String   nombreProducto;
    private Date     fechaFabricacion;
    private double   precio;
    private Categoria categoria;
    private Material  material;

    public Producto(int productoId, String nombreProducto, Date fechaFabricacion,
                    double precio, Categoria categoria, Material material) {
        this.productoId       = productoId;
        this.nombreProducto   = nombreProducto;
        this.fechaFabricacion = fechaFabricacion;
        this.precio           = precio;
        this.categoria        = categoria;
        this.material         = material;
    }

    public int       getProductoId()                  { return productoId; }
    public void      setProductoId(int id)            { this.productoId = id; }

    public String    getNombreProducto()               { return nombreProducto; }
    public void      setNombreProducto(String nom)     { this.nombreProducto = nom; }

    public Date      getFechaFabricacion()             { return fechaFabricacion; }
    public void      setFechaFabricacion(Date fecha)   { this.fechaFabricacion = fecha; }

    public double    getPrecio()                       { return precio; }
    public void      setPrecio(double precio)          { this.precio = precio; }

    public Categoria getCategoria()                    { return categoria; }
    public void      setCategoria(Categoria cat)       { this.categoria = cat; }

    public Material  getMaterial()                     { return material; }
    public void      setMaterial(Material mat)         { this.material = mat; }

    @Override
    public String toString() {
        return "Producto [" + productoId + "]: " + nombreProducto
                + " | Precio: $" + precio
                + " | Material: " + material.getNombreMaterial();
    }
}
