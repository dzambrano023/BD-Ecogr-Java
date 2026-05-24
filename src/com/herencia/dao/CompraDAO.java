package com.herencia.dao;

import com.herencia.conexion.ConexionBD;
import com.herencia.modelo.Compra;
import com.herencia.modelo.Producto;
import com.herencia.modelo.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PASO 4 - CompraDAO
 * ------------------
 * Maneja la tabla "compras".
 *
 * Una Compra referencia a Usuario y a Producto.
 * Al leer de la BD necesitamos reconstruir esos objetos.
 * Para no duplicar lógica, reutilizamos UsuarioDAO y ProductoDAO.
 *
 * Concepto importante: calcularTotal() en Compra.java ya existe como método Java.
 * No necesitamos guardarlo en la BD porque siempre se puede recalcular.
 * Esto se llama "dato derivado" → solo se persiste lo esencial.
 */
public class CompraDAO {

    // Reutilizamos los DAOs existentes para reconstruir los objetos relacionados
    private final UsuarioDAO usuarioDAO  = new UsuarioDAO();
    private final ProductoDAO productoDAO = new ProductoDAO();

    // ── LISTAR TODOS ──────────────────────────────────────────────────────
    public List<Compra> listarTodos() {
        List<Compra> lista = new ArrayList<>();

        // Solo necesitamos los IDs de usuario y producto; los objetos
        // los traemos con sus DAOs respectivos
        String sql = "SELECT compra_id, usuario_id, producto_id, fecha_compra, cantidad " +
                "FROM compras ORDER BY fecha_compra DESC";

        try {
            Connection con = ConexionBD.getConexion();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Compra c = construirCompra(rs);
                if (c != null) lista.add(c);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("❌ Error al listar compras: " + e.getMessage());
        }

        return lista;
    }

    // ── BUSCAR POR ID ─────────────────────────────────────────────────────
    public Compra buscarPorId(int id) {
        String sql = "SELECT compra_id, usuario_id, producto_id, fecha_compra, cantidad " +
                "FROM compras WHERE compra_id = ?";
        try {
            Connection con = ConexionBD.getConexion();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Compra c = construirCompra(rs);
                rs.close();
                stmt.close();
                return c;
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("❌ Error al buscar compra: " + e.getMessage());
        }
        return null;
    }

    // ── INSERTAR ──────────────────────────────────────────────────────────
    public boolean insertar(Compra c) {
        String sql = "INSERT INTO compras (usuario_id, producto_id, fecha_compra, cantidad) " +
                "VALUES (?,?,?,?)";
        try {
            Connection con = ConexionBD.getConexion();
            PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, c.getUsuario().getUsuarioId());
            stmt.setInt(2, c.getProducto().getProductoId());
            stmt.setDate(3, new java.sql.Date(c.getFechaCompra().getTime()));
            stmt.setInt(4, c.getCantidad());

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) c.setCompraId(keys.getInt(1));
                keys.close();
                stmt.close();
                return true;
            }
            stmt.close();

        } catch (SQLException e) {
            System.out.println("❌ Error al insertar compra: " + e.getMessage());
        }
        return false;
    }

    // ── MÉTODO PRIVADO ────────────────────────────────────────────────────
    /**
     * Construye un objeto Compra completo desde un ResultSet.
     * Llama a UsuarioDAO y ProductoDAO para obtener los objetos relacionados.
     */
    private Compra construirCompra(ResultSet rs) throws SQLException {
        int usuarioId  = rs.getInt("usuario_id");
        int productoId = rs.getInt("producto_id");

        Usuario  usuario  = usuarioDAO.buscarPorId(usuarioId);
        Producto producto = productoDAO.buscarPorId(productoId);

        if (usuario == null || producto == null) {
            System.out.println("⚠️  Compra con usuario o producto no encontrado en BD.");
            return null;
        }

        return new Compra(
                rs.getInt("compra_id"),
                usuario,
                producto,
                rs.getDate("fecha_compra"),
                rs.getInt("cantidad")
        );
    }
}
