package com.herencia.modelo;

import com.herencia.conexion.ConexionBD;
import com.herencia.dao.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class MainJDBC {

    static Scanner sc  = new Scanner(System.in);
    static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    // DAOs: un objeto por cada entidad
    static UsuarioDAO     usuarioDAO     = new UsuarioDAO();
    static ProductoDAO    productoDAO    = new ProductoDAO();
    static CompraDAO      compraDAO      = new CompraDAO();
    static DisposicionDAO disposicionDAO = new DisposicionDAO();

    public static void main(String[] args) {

        int opcion;
        do {
            System.out.println("\n========================================");
            System.out.println("        SISTEMA ECOGR - ODS12           ");
            System.out.println("========================================");
            System.out.println("  1. Gestionar Usuarios");
            System.out.println("  2. Gestionar Productos");
            System.out.println("  3. Gestionar Compras");
            System.out.println("  4. Gestionar Disposiciones");
            System.out.println("  5. Reportes");
            System.out.println("  0. Salir");
            System.out.println("========================================");
            System.out.print("  Selecciona una opcion: ");
            opcion = leerInt();

            switch (opcion) {
                case 1: menuUsuarios();      break;
                case 2: menuProductos();     break;
                case 3: menuCompras();       break;
                case 4: menuDisposiciones(); break;
                case 5: menuReportes();      break;
                case 0:
                    ConexionBD.cerrar();     // ← siempre cerrar la conexión
                    System.out.println("\nCerrando sistema. Hasta luego!");
                    break;
                default: System.out.println("Opcion no valida.");
            }
        } while (opcion != 0);
    }

    //  MENU USUARIOS
    static void menuUsuarios() {
        int op;
        do {
            System.out.println("\n--- USUARIOS ---");
            System.out.println("  1. Listar usuarios");
            System.out.println("  2. Registrar Reciclador");
            System.out.println("  3. Registrar Administrador");
            System.out.println("  4. Registrar Operario");
            System.out.println("  5. Buscar usuario por ID");
            System.out.println("  0. Volver");
            System.out.print("  Opcion: ");
            op = leerInt();

            switch (op) {
                case 1:
                    List<Usuario> usuarios = usuarioDAO.listarTodos();
                    if (usuarios.isEmpty()) {
                        System.out.println("No hay usuarios registrados.");
                    } else {
                        System.out.println("\nUsuarios en BD:");
                        for (Usuario u : usuarios)
                            System.out.println("  [" + u.getUsuarioId() + "] " + u.describir());
                    }
                    break;

                case 2:
                    System.out.println("\n-- Registrar Reciclador --");
                    String[] b2 = pedirDatosBase();
                    System.out.print("  Material especialidad: ");
                    String mat = sc.nextLine();
                    Reciclador r = new Reciclador(0, b2[0], b2[1], b2[2], b2[3], b2[4], mat);
                    if (usuarioDAO.insertarReciclador(r))
                        System.out.println("✅ Reciclador guardado en BD con ID: " + r.getUsuarioId());
                    break;

                case 3:
                    System.out.println("\n-- Registrar Administrador --");
                    String[] b3 = pedirDatosBase();
                    System.out.print("  Nivel de acceso (Alto/Medio/Bajo): ");
                    String nivel = sc.nextLine();
                    Administrador a = new Administrador(0, b3[0], b3[1], b3[2], b3[3], b3[4], nivel);
                    if (usuarioDAO.insertarAdministrador(a))
                        System.out.println("✅ Administrador guardado en BD con ID: " + a.getUsuarioId());
                    break;

                case 4:
                    System.out.println("\n-- Registrar Operario --");
                    String[] b4 = pedirDatosBase();
                    System.out.print("  Turno (Manana/Tarde/Noche): ");
                    String turno = sc.nextLine();
                    Operario o = new Operario(0, b4[0], b4[1], b4[2], b4[3], b4[4], turno);
                    if (usuarioDAO.insertarOperario(o))
                        System.out.println("✅ Operario guardado en BD con ID: " + o.getUsuarioId());
                    break;

                case 5:
                    System.out.print("  ID de usuario: ");
                    int id = leerInt();
                    Usuario encontrado = usuarioDAO.buscarPorId(id);
                    System.out.println(encontrado != null
                            ? "  " + encontrado.describir()
                            : "Usuario no encontrado.");
                    break;

                case 0: break;
                default: System.out.println("Opcion no valida.");
            }
        } while (op != 0);
    }


    //  Menu Productos

    static void menuProductos() {
        int op;
        do {
            System.out.println("\n--- PRODUCTOS ---");
            System.out.println("  1. Listar productos");
            System.out.println("  2. Registrar producto");
            System.out.println("  0. Volver");
            System.out.print("  Opcion: ");
            op = leerInt();

            switch (op) {
                case 1:
                    List<Producto> productos = productoDAO.listarTodos();
                    if (productos.isEmpty()) {
                        System.out.println("No hay productos registrados.");
                    } else {
                        System.out.println("\nProductos en BD:");
                        for (Producto p : productos) System.out.println("  " + p);
                    }
                    break;

                case 2:
                    System.out.println("\n-- Registrar Producto --");
                    System.out.print("  Nombre del producto: ");
                    String nombre = sc.nextLine();

                    System.out.print("  Fecha de fabricacion (dd/MM/yyyy): ");
                    Date fecha = leerFecha();

                    System.out.print("  Precio: ");
                    double precio = leerDouble();

                    List<Categoria> categorias = productoDAO.listarCategorias();
                    System.out.println("  Categorias disponibles:");
                    for (Categoria c : categorias)
                        System.out.println("    [" + c.getCategoriaId() + "] " + c.getNombreCategoria());
                    System.out.print("  Numero de categoria: ");
                    int catId = leerInt();
                    Categoria catSel = categorias.stream()
                            .filter(c -> c.getCategoriaId() == catId).findFirst().orElse(null);

                    List<Material> materiales = productoDAO.listarMateriales();
                    System.out.println("  Materiales disponibles:");
                    for (Material m : materiales)
                        System.out.println("    [" + m.getMaterialId() + "] " + m.getNombreMaterial());
                    System.out.print("  Numero de material: ");
                    int matId = leerInt();
                    Material matSel = materiales.stream()
                            .filter(m -> m.getMaterialId() == matId).findFirst().orElse(null);

                    if (catSel == null || matSel == null) {
                        System.out.println("Categoria o material no valido.");
                    } else {
                        Producto p = new Producto(0, nombre, fecha, precio, catSel, matSel);
                        if (productoDAO.insertar(p))
                            System.out.println("✅ Producto guardado en BD con ID: " + p.getProductoId());
                    }
                    break;

                case 0: break;
                default: System.out.println("Opcion no valida.");
            }
        } while (op != 0);
    }


    //  Menu compras

    static void menuCompras() {
        int op;
        do {
            System.out.println("\n--- COMPRAS ---");
            System.out.println("  1. Listar compras");
            System.out.println("  2. Registrar compra");
            System.out.println("  0. Volver");
            System.out.print("  Opcion: ");
            op = leerInt();

            switch (op) {
                case 1:
                    List<Compra> compras = compraDAO.listarTodos();
                    if (compras.isEmpty()) {
                        System.out.println("No hay compras registradas.");
                    } else {
                        System.out.println("\nCompras en BD:");
                        for (Compra c : compras) System.out.println("  " + c);
                    }
                    break;

                case 2:
                    List<Usuario> usuarios = usuarioDAO.listarTodos();
                    if (usuarios.isEmpty()) { System.out.println("No hay usuarios en BD."); break; }

                    List<Producto> productos = productoDAO.listarTodos();
                    if (productos.isEmpty()) { System.out.println("No hay productos en BD."); break; }

                    System.out.println("  Usuarios disponibles:");
                    for (Usuario u : usuarios)
                        System.out.println("    [" + u.getUsuarioId() + "] " + u.getNombre());
                    System.out.print("  ID Usuario: ");
                    int uId = leerInt();
                    Usuario uSel = usuarios.stream()
                            .filter(u -> u.getUsuarioId() == uId).findFirst().orElse(null);

                    System.out.println("  Productos disponibles:");
                    for (Producto p : productos)
                        System.out.println("    [" + p.getProductoId() + "] " + p.getNombreProducto() + " - $" + p.getPrecio());
                    System.out.print("  ID Producto: ");
                    int pId = leerInt();
                    Producto pSel = productos.stream()
                            .filter(p -> p.getProductoId() == pId).findFirst().orElse(null);

                    if (uSel == null || pSel == null) { System.out.println("Usuario o producto no valido."); break; }

                    System.out.print("  Fecha de compra (dd/MM/yyyy): ");
                    Date fechaC = leerFecha();
                    System.out.print("  Cantidad: ");
                    int cantidadC = leerInt();

                    Compra compra = new Compra(0, uSel, pSel, fechaC, cantidadC);
                    if (compraDAO.insertar(compra))
                        System.out.println("✅ Compra guardada en BD con ID: " + compra.getCompraId());
                    break;

                case 0: break;
                default: System.out.println("Opcion no valida.");
            }
        } while (op != 0);
    }


    //  Menu Disposiciones

    static void menuDisposiciones() {
        int op;
        do {
            System.out.println("\n--- DISPOSICIONES ---");
            System.out.println("  1. Listar disposiciones");
            System.out.println("  2. Registrar disposicion");
            System.out.println("  0. Volver");
            System.out.print("  Opcion: ");
            op = leerInt();

            switch (op) {
                case 1:
                    List<Disposicion> disposiciones = disposicionDAO.listarTodos();
                    if (disposiciones.isEmpty()) {
                        System.out.println("No hay disposiciones registradas.");
                    } else {
                        System.out.println("\nDisposiciones en BD:");
                        for (Disposicion d : disposiciones) System.out.println("  " + d);
                    }
                    break;

                case 2:
                    List<Compra> compras = compraDAO.listarTodos();
                    if (compras.isEmpty())   { System.out.println("No hay compras en BD.");     break; }

                    List<Usuario> usuarios = usuarioDAO.listarTodos();
                    if (usuarios.isEmpty())  { System.out.println("No hay usuarios en BD.");    break; }

                    List<Producto> productos = productoDAO.listarTodos();
                    if (productos.isEmpty()) { System.out.println("No hay productos en BD.");   break; }

                    System.out.println("  Compras disponibles:");
                    for (Compra c : compras)
                        System.out.println("    [" + c.getCompraId() + "] "
                                + c.getUsuario().getNombre() + " | "
                                + c.getProducto().getNombreProducto()
                                + " | Cant: " + c.getCantidad());
                    System.out.print("  ID Compra: ");
                    int cId = leerInt();
                    Compra cSel = compras.stream()
                            .filter(c -> c.getCompraId() == cId).findFirst().orElse(null);

                    System.out.println("  Usuarios disponibles:");
                    for (Usuario u : usuarios)
                        System.out.println("    [" + u.getUsuarioId() + "] " + u.getNombre());
                    System.out.print("  ID Usuario: ");
                    int uId = leerInt();
                    Usuario uSel = usuarios.stream()
                            .filter(u -> u.getUsuarioId() == uId).findFirst().orElse(null);

                    System.out.println("  Productos disponibles:");
                    for (Producto p : productos)
                        System.out.println("    [" + p.getProductoId() + "] " + p.getNombreProducto());
                    System.out.print("  ID Producto: ");
                    int pId = leerInt();
                    Producto pSel = productos.stream()
                            .filter(p -> p.getProductoId() == pId).findFirst().orElse(null);

                    if (cSel == null || uSel == null || pSel == null) {
                        System.out.println("Compra, usuario o producto no valido.");
                        break;
                    }

                    System.out.print("  Fecha (dd/MM/yyyy): ");
                    Date fechaD = leerFecha();
                    System.out.print("  Cantidad: ");
                    int cantD = leerInt();
                    System.out.print("  Peso en kg: ");
                    double pesoD = leerDouble();

                    Disposicion disp = new Disposicion(0, cSel, uSel, pSel, fechaD, cantD, pesoD);
                    if (disposicionDAO.insertar(disp))
                        System.out.println("✅ Disposicion guardada en BD con ID: " + disp.getDisposicionId());
                    break;

                case 0: break;
                default: System.out.println("Opcion no valida.");
            }
        } while (op != 0);
    }


    //  MENU REPORTES (consultas analíticas del script SQL)

    static void menuReportes() {
        int op;
        do {
            System.out.println("\n--- REPORTES ---");
            System.out.println("  1. Disposiciones con más de 2 unidades");
            System.out.println("  2. Total kg por centro de acopio");
            System.out.println("  3. Últimas 3 disposiciones");
            System.out.println("  0. Volver");
            System.out.print("  Opcion: ");
            op = leerInt();

            switch (op) {
                case 1: disposicionDAO.reporteDisposicionesGrandes(); break;
                case 2: disposicionDAO.reporteTotalKgPorCentro();     break;
                case 3: disposicionDAO.reporteUltimas3();             break;
                case 0: break;
                default: System.out.println("Opcion no valida.");
            }
        } while (op != 0);
    }


    //  MÉTODOS AUXILIARES
    static String[] pedirDatosBase() {
        System.out.print("  Nombre: ");    String nombre    = sc.nextLine();
        System.out.print("  Email: ");     String email     = sc.nextLine();
        System.out.print("  Telefono: ");  String telefono  = sc.nextLine();
        System.out.print("  Direccion: "); String direccion = sc.nextLine();
        System.out.print("  Ciudad: ");    String ciudad    = sc.nextLine();
        return new String[]{nombre, email, telefono, direccion, ciudad};
    }

    static int leerInt() {
        while (true) {
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.print("  Ingresa un numero entero valido: "); }
        }
    }

    static double leerDouble() {
        while (true) {
            try { return Double.parseDouble(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.print("  Ingresa un numero valido (ej: 1200.50): "); }
        }
    }

    static Date leerFecha() {
        while (true) {
            try { return sdf.parse(sc.nextLine().trim()); }
            catch (ParseException e) { System.out.print("  Formato invalido. Usa dd/MM/yyyy: "); }
        }
    }
}