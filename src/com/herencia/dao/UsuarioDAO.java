package com.herencia.dao;

import com.herencia.conexion.ConexionBD;
import com.herencia.modelo.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PASO 2 - UsuarioDAO
 * -------------------
 * DAO = Data Access Object.
 * Esta clase traduce entre objetos Java y filas de la tabla "usuarios".
 * <p>
 * Conceptos JDBC que usamos aquí:
 * <p>
 * Connection     → representa la conexión activa con MySQL
 * PreparedStatement → consulta SQL parametrizada con "?" (evita SQL Injection)
 * ResultSet      → tabla de resultados que devuelve un SELECT
 * <p>
 * Flujo típico de un método:
 * 1. Obtener conexión: ConexionDB.getConexion()
 * 2. Preparar SQL: con.prepareStatement("SELECT ...")
 * 3. Ejecutar:
 * - executeQuery()  → para SELECT (devuelve ResultSet)
 * - executeUpdate() → para INSERT / UPDATE / DELETE
 * 4. Leer ResultSet (si aplica): rs.next(), rs.getString("columna")
 * 5. Cerrar recursos: rs.close(), stmt.close()
 */
public class UsuarioDAO {

    // ── LISTAR TODOS ─────────────────────────────────────────────────────


    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();

        // SQL con JOIN para traer el nombre del tipo (no es obligatorio aquí,
        // pero es buena práctica para no hacer consultas adicionales)
        String sql = "SELECT u.*, t.nombre_tipo " +
                "FROM usuarios u " +
                "JOIN tipo_usuario t ON u.tipo_usuario_id = t.tipo_usuario_id";

        try {
            Connection con = ConexionBD.getConexion();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            // rs.next() avanza al siguiente registro; devuelve false al terminar
            while (rs.next()) {
                Usuario u = construirUsuario(rs);
                if (u != null) lista.add(u);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("❌ Error al listar usuarios: " + e.getMessage());
        }

        return lista;
    }

    // Buscar por ID
    public Usuario buscarPorId(int id) {
        String sql = "SELECT u.*, t.nombre_tipo " +
                "FROM usuarios u " +
                "JOIN tipo_usuario t ON u.tipo_usuario_id = t.tipo_usuario_id " +
                "WHERE u.usuario_id = ?";
        try {
            Connection con = ConexionBD.getConexion();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);          // el "?" se reemplaza con el valor id
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario u = construirUsuario(rs);
                rs.close();
                stmt.close();
                return u;
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("❌ Error al buscar usuario: " + e.getMessage());
        }
        return null;
    }

    // Insertar Reciclador
    public boolean insertarReciclador(Reciclador r) {
        // tipo_usuario_id = 1 para Reciclador (según tus datos)
        String sql = "INSERT INTO usuarios (nombre, email, telefono, direccion, ciudad, " +
                "tipo_usuario_id, material_especialidad) VALUES (?,?,?,?,?,1,?)";
        try {
            Connection con = ConexionBD.getConexion();
            PreparedStatement stmt = con.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS); // para recuperar el ID generado

            stmt.setString(1, r.getNombre());
            stmt.setString(2, r.getEmail());
            stmt.setString(3, r.getTelefono());
            stmt.setString(4, r.getDireccion());
            stmt.setString(5, r.getCiudad());
            stmt.setString(6, r.getMaterialEspecialidad());

            int filas = stmt.executeUpdate(); // devuelve cuántas filas se insertaron

            if (filas > 0) {
                // Recuperar el ID auto-generado por MySQL
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) r.setUsuarioId(keys.getInt(1));
                keys.close();
                stmt.close();
                return true;
            }
            stmt.close();

        } catch (SQLException e) {
            System.out.println("❌ Error al insertar reciclador: " + e.getMessage());
        }
        return false;
    }

    // Insertar Administrador
    public boolean insertarAdministrador(Administrador a) {
        String sql = "INSERT INTO usuarios (nombre, email, telefono, direccion, ciudad, " +
                "tipo_usuario_id, nivel_acceso) VALUES (?,?,?,?,?,2,?)";
        try {
            Connection con = ConexionBD.getConexion();
            PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, a.getNombre());
            stmt.setString(2, a.getEmail());
            stmt.setString(3, a.getTelefono());
            stmt.setString(4, a.getDireccion());
            stmt.setString(5, a.getCiudad());
            stmt.setString(6, a.getNivelAcceso());

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) a.setUsuarioId(keys.getInt(1));
                keys.close();
                stmt.close();
                return true;
            }
            stmt.close();

        } catch (SQLException e) {
            System.out.println("❌ Error al insertar administrador: " + e.getMessage());
        }
        return false;
    }

    // Insertar operario
    public boolean insertarOperario(Operario o) {
        String sql = "INSERT INTO usuarios (nombre, email, telefono, direccion, ciudad, " +
                "tipo_usuario_id, turno) VALUES (?,?,?,?,?,3,?)";
        try {
            Connection con = ConexionBD.getConexion();
            PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, o.getNombre());
            stmt.setString(2, o.getEmail());
            stmt.setString(3, o.getTelefono());
            stmt.setString(4, o.getDireccion());
            stmt.setString(5, o.getCiudad());
            stmt.setString(6, o.getTurno());

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) o.setUsuarioId(keys.getInt(1));
                keys.close();
                stmt.close();
                return true;
            }
            stmt.close();

        } catch (SQLException e) {
            System.out.println("❌ Error al insertar operario: " + e.getMessage());
        }
        return false;
    }

    // ── MÉTODO PRIVADO: construir objeto Usuario desde un ResultSet ────────

    /**
     * Lee una fila del ResultSet y devuelve la subclase correcta de Usuario.
     * Este método centraliza la lógica para no repetirla en listarTodos y buscarPorId.
     */
    private Usuario construirUsuario(ResultSet rs) throws SQLException {
        int id = rs.getInt("usuario_id");
        String nombre = rs.getString("nombre");
        String email = rs.getString("email");
        String telefono = rs.getString("telefono");
        String direccion = rs.getString("direccion");
        String ciudad = rs.getString("ciudad");
        int tipo = rs.getInt("tipo_usuario_id");

        switch (tipo) {
            case 1: // Reciclador
                return new Reciclador(id, nombre, email, telefono, direccion, ciudad,
                        rs.getString("material_especialidad"));
            case 2: // Administrador
                return new Administrador(id, nombre, email, telefono, direccion, ciudad,
                        rs.getString("nivel_acceso"));
            case 3: // Operario
                return new Operario(id, nombre, email, telefono, direccion, ciudad,
                        rs.getString("turno"));
            default:
                System.out.println("⚠️  Tipo de usuario desconocido: " + tipo);
                return null;
        }
    }
}
