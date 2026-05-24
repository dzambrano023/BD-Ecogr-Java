package com.herencia.dao;

import com.herencia.conexion.ConexionBD;
import com.herencia.conexion.ConexionBD;
import com.herencia.modelo.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PASO 5 - DisposicionDAO
 * -----------------------
 * Maneja la tabla "disposiciones".
 * Incluye también 3 reportes analíticos del script SQL.
 */
public class DisposicionDAO {

    private final UsuarioDAO  usuarioDAO  = new UsuarioDAO();
    private final ProductoDAO productoDAO = new ProductoDAO();
    private final CompraDAO   compraDAO   = new CompraDAO();

    // ── LISTAR TODOS ──────────────────────────────────────────────────────
    public List<Disposicion> listarTodos() {
        List<Disposicion> lista = new ArrayList<>();
        String sql = "SELECT * FROM disposiciones ORDER BY fecha DESC";

        try {
            Connection con = ConexionBD.getConexion();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Disposicion d = construirDisposicion(rs);
                if (d != null) lista.add(d);
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("❌ Error al listar disposiciones: " + e.getMessage());
        }
        return lista;
    }

    // ── INSERTAR ──────────────────────────────────────────────────────────
    public boolean insertar(Disposicion d) {
        String sql = "INSERT INTO disposiciones " +
                "(compra_id, usuario_id, producto_id, fecha, cantidad, peso_kg) " +
                "VALUES (?,?,?,?,?,?)";
        try {
            Connection con = ConexionBD.getConexion();
            PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, d.getCompra().getCompraId());
            stmt.setInt(2, d.getUsuario().getUsuarioId());
            stmt.setInt(3, d.getProducto().getProductoId());
            stmt.setDate(4, new java.sql.Date(d.getFecha().getTime()));
            stmt.setInt(5, d.getCantidad());
            stmt.setDouble(6, d.getPesoKg());

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) d.setDisposicionId(keys.getInt(1));
                keys.close();
                stmt.close();
                return true;
            }
            stmt.close();

        } catch (SQLException e) {
            System.out.println("❌ Error al insertar disposición: " + e.getMessage());
        }
        return false;
    }

    // ── REPORTE 1: Disposiciones con más de 2 unidades ────────────────────
    public void reporteDisposicionesGrandes() {
        String sql = "SELECT d.disposicion_id, u.nombre, p.nombre_producto, " +
                "d.cantidad, d.peso_kg, d.fecha " +
                "FROM disposiciones d " +
                "JOIN usuarios u  ON d.usuario_id  = u.usuario_id " +
                "JOIN productos p ON d.producto_id = p.producto_id " +
                "WHERE d.cantidad > 2";
        try {
            Connection con = ConexionBD.getConexion();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            System.out.println("\n--- Disposiciones con más de 2 unidades ---");
            while (rs.next()) {
                System.out.printf("  [%d] %s | %s | Cant: %d | %.2f kg | %s%n",
                        rs.getInt("disposicion_id"),
                        rs.getString("nombre"),
                        rs.getString("nombre_producto"),
                        rs.getInt("cantidad"),
                        rs.getDouble("peso_kg"),
                        rs.getDate("fecha"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error en reporte: " + e.getMessage());
        }
    }

    // ── REPORTE 2: Total kg por producto (centro de acopio) ───────────────
    public void reporteTotalKgPorCentro() {
        String sql = "SELECT p.nombre_producto, SUM(d.peso_kg) AS total_kg " +
                "FROM disposiciones d " +
                "JOIN productos p ON d.producto_id = p.producto_id " +
                "GROUP BY p.nombre_producto " +
                "ORDER BY total_kg DESC";
        try {
            Connection con = ConexionBD.getConexion();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            System.out.println("\n--- Total kg por producto ---");
            while (rs.next()) {
                System.out.printf("  %-25s → %.2f kg%n",
                        rs.getString("nombre_producto"),
                        rs.getDouble("total_kg"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error en reporte: " + e.getMessage());
        }
    }

    // ── REPORTE 3: Últimas 3 disposiciones ───────────────────────────────
    public void reporteUltimas3() {
        String sql = "SELECT d.disposicion_id, u.nombre, p.nombre_producto, " +
                "d.fecha, d.peso_kg " +
                "FROM disposiciones d " +
                "JOIN usuarios u  ON d.usuario_id  = u.usuario_id " +
                "JOIN productos p ON d.producto_id = p.producto_id " +
                "ORDER BY d.fecha DESC LIMIT 3";
        try {
            Connection con = ConexionBD.getConexion();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            System.out.println("\n--- Últimas 3 disposiciones ---");
            while (rs.next()) {
                System.out.printf("  [%d] %s | %s | %s | %.2f kg%n",
                        rs.getInt("disposicion_id"),
                        rs.getString("nombre"),
                        rs.getString("nombre_producto"),
                        rs.getDate("fecha"),
                        rs.getDouble("peso_kg"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error en reporte: " + e.getMessage());
        }
    }

    // ── MÉTODO PRIVADO ────────────────────────────────────────────────────
    private Disposicion construirDisposicion(ResultSet rs) throws SQLException {
        Compra   compra   = compraDAO.buscarPorId(rs.getInt("compra_id"));
        Usuario  usuario  = usuarioDAO.buscarPorId(rs.getInt("usuario_id"));
        Producto producto = productoDAO.buscarPorId(rs.getInt("producto_id"));

        if (compra == null || usuario == null || producto == null) return null;

        return new Disposicion(
                rs.getInt("disposicion_id"),
                compra, usuario, producto,
                rs.getDate("fecha"),
                rs.getInt("cantidad"),
                rs.getDouble("peso_kg")
        );
    }
}