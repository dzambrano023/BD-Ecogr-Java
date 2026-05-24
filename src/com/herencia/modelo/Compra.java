package com.herencia.modelo;

import java.util.Date;

public class Compra {

    private int      compraId;
    private Usuario  usuario;
    private Producto producto;
    private Date     fechaCompra;
    private int      cantidad;

    public Compra(int compraId, Usuario usuario, Producto producto,
                  Date fechaCompra, int cantidad) {
        this.compraId    = compraId;
        this.usuario     = usuario;
        this.producto    = producto;
        this.fechaCompra = fechaCompra;
        this.cantidad    = cantidad;
    }

    public double calcularTotal() {
        return producto.getPrecio() * cantidad;
    }

    public int      getCompraId()               { return compraId; }
    public void     setCompraId(int id)         { this.compraId = id; }

    public Usuario  getUsuario()                { return usuario; }
    public void     setUsuario(Usuario u)       { this.usuario = u; }

    public Producto getProducto()               { return producto; }
    public void     setProducto(Producto p)     { this.producto = p; }

    public Date     getFechaCompra()            { return fechaCompra; }
    public void     setFechaCompra(Date fecha)  { this.fechaCompra = fecha; }

    public int      getCantidad()               { return cantidad; }
    public void     setCantidad(int cantidad)   { this.cantidad = cantidad; }

    @Override
    public String toString() {
        return "Compra [" + compraId + "]"
                + " | Usuario: "  + usuario.getNombre()
                + " | Producto: " + producto.getNombreProducto()
                + " | Cantidad: " + cantidad
                + " | Total: $"   + calcularTotal();
    }
}
