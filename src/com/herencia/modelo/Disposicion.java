package com.herencia.modelo;

import java.util.Date;

public class Disposicion {

    private int      disposicionId;
    private Compra   compra;
    private Usuario  usuario;
    private Producto producto;
    private Date     fecha;
    private int      cantidad;
    private double   pesoKg;

    public Disposicion(int disposicionId, Compra compra, Usuario usuario,
                       Producto producto, Date fecha, int cantidad, double pesoKg) {
        this.disposicionId = disposicionId;
        this.compra        = compra;
        this.usuario       = usuario;
        this.producto      = producto;
        this.fecha         = fecha;
        this.cantidad      = cantidad;
        this.pesoKg        = pesoKg;
    }

    public boolean validarTrazabilidad() {
        return pesoKg > 0 && cantidad > 0;
    }

    public int      getDisposicionId()              { return disposicionId; }
    public void     setDisposicionId(int id)        { this.disposicionId = id; }

    public Compra   getCompra()                     { return compra; }
    public void     setCompra(Compra compra)        { this.compra = compra; }

    public Usuario  getUsuario()                    { return usuario; }
    public void     setUsuario(Usuario u)           { this.usuario = u; }

    public Producto getProducto()                   { return producto; }
    public void     setProducto(Producto p)         { this.producto = p; }

    public Date     getFecha()                      { return fecha; }
    public void     setFecha(Date fecha)            { this.fecha = fecha; }

    public int      getCantidad()                   { return cantidad; }
    public void     setCantidad(int cantidad)       { this.cantidad = cantidad; }

    public double   getPesoKg()                     { return pesoKg; }
    public void     setPesoKg(double pesoKg)        { this.pesoKg = pesoKg; }

    @Override
    public String toString() {
        return "Disposicion [" + disposicionId + "]"
                + " | Usuario: "  + usuario.getNombre()
                + " | Producto: " + producto.getNombreProducto()
                + " | Peso: "     + pesoKg + " kg"
                + " | Válida: "   + validarTrazabilidad();
    }
}
