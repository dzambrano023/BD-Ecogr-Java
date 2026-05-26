package com.herencia.dao;

import com.herencia.conexion.ConexionBD;
import com.herencia.modelo.Categoria;
import com.herencia.modelo.Material;
import com.herencia.modelo.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PASO 3 - ProductoDAO
 * --------------------
 * Maneja SELECT e INSERT de la tabla "productos".
 *
 * Punto importante:
 *   Producto tiene referencias a Categoria y Material (objetos Java).
 *   En SQL, solo guardamos categoria_id y material_id (enteros).
 *   Al leer, reconstruimos los objetos haciendo JOIN en la misma consulta,
 *   así evitamos hacer consultas extras a la BD.
 */
public class ProductoDAO {

    // LISTAR TODOS
    /**
     * Trae todos los productos con su categoría y material ya construidos.
     * El JOIN nos permite leer categorias y materiales en una sola consulta.
     */
    public List<Producto> listarTodos() {
        List<Producto> lista = new ArrayList<>();

        String sql = "SELECT p.producto_id, p.nombre_producto, p.fecha_fabricacion, p.precio, " +
                "       c.categoria_id, c.nombre_categoria, " +
                "       m.material_id,  m.nombre_material " +
                "FROM productos p " +
                "JOIN categorias c ON p.categoria_id = c.categoria_id " +
                "JOIN materiales  m ON p.material_id  = m.material_id";

        try {
            Connection con = ConexionBD.getConexion();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(construirProducto(rs));
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("❌ Error al listar productos: " + e.getMessage());
        }

        return lista;
    }

    // Buscar por ID
    public Producto buscarPorId(int id) {
        String sql = "SELECT p.producto_id, p.nombre_producto, p.fecha_fabricacion, p.precio, " +
                "       c.categoria_id, c.nombre_categoria, " +
                "       m.material_id,  m.nombre_material " +
                "FROM productos p " +
                "JOIN categorias c ON p.categoria_id = c.categoria_id " +
                "JOIN materiales  m ON p.material_id  = m.material_id " +
                "WHERE p.producto_id = ?";
        try {
            Connection con = ConexionBD.getConexion();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Producto p = construirProducto(rs);
                rs.close();
                stmt.close();
                return p;
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("❌ Error al buscar producto: " + e.getMessage());
        }
        return null;
    }

    // Insertar

    public boolean insertar(Producto p) {
        String sql = "INSERT INTO productos (nombre_producto, fecha_fabricacion, precio, " +
                "categoria_id, material_id) VALUES (?,?,?,?,?)";
        try {
            Connection con = ConexionBD.getConexion();
            PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, p.getNombreProducto());
            // Conversión de java.util.Date → java.sql.Date
            stmt.setDate(2, new java.sql.Date(p.getFechaFabricacion().getTime()));
            stmt.setDouble(3, p.getPrecio());
            stmt.setInt(4, p.getCategoria().getCategoriaId());
            stmt.setInt(5, p.getMaterial().getMaterialId());

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) p.setProductoId(keys.getInt(1));
                keys.close();
                stmt.close();
                return true;
            }
            stmt.close();

        } catch (SQLException e) {
            System.out.println("❌ Error al insertar producto: " + e.getMessage());
        }
        return false;
    }

    // Listar categorias
    /** Útil para mostrar el catálogo de categorías al registrar un producto. */
    public List<Categoria> listarCategorias() {
        List<Categoria> lista = new ArrayList<>();
        try {
            Connection con = ConexionBD.getConexion();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM categorias");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Categoria(rs.getInt("categoria_id"),
                        rs.getString("nombre_categoria")));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error al listar categorías: " + e.getMessage());
        }
        return lista;
    }

    // Listar Materiales
    public List<Material> listarMateriales() {
        List<Material> lista = new ArrayList<>();
        try {
            Connection con = ConexionBD.getConexion();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM materiales");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Material(rs.getInt("material_id"),
                        rs.getString("nombre_material")));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error al listar materiales: " + e.getMessage());
        }
        return lista;
    }

    //  Metodo Privado construir Producto desde ResultSet
    private Producto construirProducto(ResultSet rs) throws SQLException {
        Categoria cat = new Categoria(rs.getInt("categoria_id"),
                rs.getString("nombre_categoria"));
        Material mat  = new Material(rs.getInt("material_id"),
                rs.getString("nombre_material"));
        return new Producto(
                rs.getInt("producto_id"),
                rs.getString("nombre_producto"),
                rs.getDate("fecha_fabricacion"),   // java.sql.Date extiende java.util.Date ✓
                rs.getDouble("precio"),
                cat,
                mat
        );
    }
}
