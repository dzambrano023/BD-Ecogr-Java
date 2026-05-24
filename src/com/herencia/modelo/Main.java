package com.herencia.modelo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    // Listas en memoria
    static List<Usuario> usuarios = new ArrayList<>();
    static List<Producto> productos = new ArrayList<>();
    static List<Compra> compras = new ArrayList<>();
    static List<Disposicion> disposiciones = new ArrayList<>();
    static List<Material> materiales = new ArrayList<>();
    static List<Categoria> categorias = new ArrayList<>();

    // Contadores de ID
    static int idUsuario = 1, idProducto = 1, idCompra = 1, idDisposicion = 1;

    public static void main(String[] args) {

        // Datos de ejemplo precargados
        materiales.add(new Material(1, "Plastico"));
        materiales.add(new Material(2, "Vidrio"));
        materiales.add(new Material(3, "Papel"));
        materiales.add(new Material(4, "Metal"));

        categorias.add(new Categoria(1, "Botellas"));
        categorias.add(new Categoria(2, "Empaque Alimentos"));
        categorias.add(new Categoria(3, "Electrodomesticos"));

        int opcion;
        do {
            System.out.println("\n========================================");
            System.out.println("        SISTEMA ECOGR - ODS12           ");
            System.out.println("========================================");
            System.out.println("  1. Gestionar Usuarios");
            System.out.println("  2. Gestionar Productos");
            System.out.println("  3. Gestionar Compras");
            System.out.println("  4. Gestionar Disposiciones");
            System.out.println("  0. Salir");
            System.out.println("========================================");
            System.out.print("  Selecciona una opcion: ");
            opcion = leerInt();

            switch (opcion) {
                case 1:
                    menuUsuarios();
                    break;
                case 2:
                    menuProductos();
                    break;
                case 3:
                    menuCompras();
                    break;
                case 4:
                    menuDisposiciones();
                    break;
                case 0:
                    System.out.println("\nCerrando sistema. Hasta luego!");
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        } while (opcion != 0);
    }


    //  Menu usuario

    static void menuUsuarios() {
        int opcion;
        do {
            System.out.println("\n--- USUARIOS ---");
            System.out.println("  1. Listar usuarios");
            System.out.println("  2. Registrar Reciclador");
            System.out.println("  3. Registrar Administrador");
            System.out.println("  4. Registrar Operario");
            System.out.println("  5. Buscar usuario por ID");
            System.out.println("  0. Volver");
            System.out.print("  Opcion: ");
            opcion = leerInt();

            switch (opcion) {
                case 1:
                    if (usuarios.isEmpty()) {
                        System.out.println("No hay usuarios registrados.");
                    } else {
                        System.out.println("\nUsuarios registrados:");
                        for (Usuario u : usuarios)
                            System.out.println("  [" + u.getUsuarioId() + "] " + u.describir());
                    }
                    break;

                case 2:
                    System.out.println("\n-- Registrar Reciclador --");
                    String[] b2 = pedirDatosBase();
                    System.out.print("  Material especialidad: ");
                    String mat = sc.nextLine();
                    usuarios.add(new Reciclador(idUsuario++, b2[0], b2[1], b2[2], b2[3], b2[4], mat));
                    System.out.println("Reciclador registrado correctamente.");
                    break;

                case 3:
                    System.out.println("\n-- Registrar Administrador --");
                    String[] b3 = pedirDatosBase();
                    System.out.print("  Nivel de acceso (Alto/Medio/Bajo): ");
                    String nivel = sc.nextLine();
                    usuarios.add(new Administrador(idUsuario++, b3[0], b3[1], b3[2], b3[3], b3[4], nivel));
                    System.out.println("Administrador registrado correctamente.");
                    break;

                case 4:
                    System.out.println("\n-- Registrar Operario --");
                    String[] b4 = pedirDatosBase();
                    System.out.print("  Turno (Manana/Tarde/Noche): ");
                    String turno = sc.nextLine();
                    usuarios.add(new Operario(idUsuario++, b4[0], b4[1], b4[2], b4[3], b4[4], turno));
                    System.out.println("Operario registrado correctamente.");
                    break;

                case 5:
                    System.out.print("  ID de usuario: ");
                    int id = leerInt();
                    Usuario encontrado = null;
                    for (Usuario u : usuarios) {
                        if (u.getUsuarioId() == id) {
                            encontrado = u;
                            break;
                        }
                    }
                    System.out.println(encontrado != null ? "  " + encontrado.describir() : "Usuario no encontrado.");
                    break;

                case 0:
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        } while (opcion != 0);
    }

    static String[] pedirDatosBase() {
        System.out.print("  Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("  Email: ");
        String email = sc.nextLine();
        System.out.print("  Telefono: ");
        String telefono = sc.nextLine();
        System.out.print("  Direccion: ");
        String direccion = sc.nextLine();
        System.out.print("  Ciudad: ");
        String ciudad = sc.nextLine();
        return new String[]{nombre, email, telefono, direccion, ciudad};
    }

    //  Menu productos

    static void menuProductos() {
        int opcion;
        do {
            System.out.println("\n--- PRODUCTOS ---");
            System.out.println("  1. Listar productos");
            System.out.println("  2. Registrar producto");
            System.out.println("  0. Volver");
            System.out.print("  Opcion: ");
            opcion = leerInt();

            switch (opcion) {
                case 1:
                    if (productos.isEmpty()) {
                        System.out.println("No hay productos registrados.");
                    } else {
                        System.out.println("\nProductos registrados:");
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

                    System.out.println("  Categorias disponibles:");
                    for (Categoria c : categorias)
                        System.out.println("    [" + c.getCategoriaId() + "] " + c.getNombreCategoria());
                    System.out.print("  Numero de categoria: ");
                    int catId = leerInt();
                    Categoria catSel = null;
                    for (Categoria c : categorias)
                        if (c.getCategoriaId() == catId) {
                            catSel = c;
                            break;
                        }

                    System.out.println("  Materiales disponibles:");
                    for (Material m : materiales)
                        System.out.println("    [" + m.getMaterialId() + "] " + m.getNombreMaterial());
                    System.out.print("  Numero de material: ");
                    int matId = leerInt();
                    Material matSel = null;
                    for (Material m : materiales)
                        if (m.getMaterialId() == matId) {
                            matSel = m;
                            break;
                        }

                    if (catSel == null || matSel == null) {
                        System.out.println("Categoria o material no valido.");
                    } else {
                        productos.add(new Producto(idProducto++, nombre, fecha, precio, catSel, matSel));
                        System.out.println("Producto registrado correctamente.");
                    }
                    break;

                case 0:
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        } while (opcion != 0);
    }

    // ══════════════════════════════════════════════════════════════
    //  MENU COMPRAS
    // ══════════════════════════════════════════════════════════════
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
                    if (compras.isEmpty()) {
                        System.out.println("No hay compras registradas.");
                    } else {
                        System.out.println("\nCompras registradas:");
                        for (Compra c : compras) System.out.println("  " + c);
                    }
                    break;

                case 2:
                    System.out.println("\n-- Registrar Compra --");

                    if (usuarios.isEmpty()) {
                        System.out.println("Primero registra al menos un usuario.");
                        break;
                    }
                    if (productos.isEmpty()) {
                        System.out.println("Primero registra al menos un producto.");
                        break;
                    }

                    System.out.println("  Usuarios disponibles:");
                    for (Usuario u : usuarios)
                        System.out.println("    [" + u.getUsuarioId() + "] " + u.getNombre());
                    System.out.print("  ID Usuario: ");
                    int uId = leerInt();
                    Usuario uSel = null;
                    for (Usuario u : usuarios)
                        if (u.getUsuarioId() == uId) {
                            uSel = u;
                            break;
                        }

                    System.out.println("  Productos disponibles:");
                    for (Producto p : productos)
                        System.out.println("    [" + p.getProductoId() + "] " + p.getNombreProducto() + " - $" + p.getPrecio());
                    System.out.print("  ID Producto: ");
                    int pId = leerInt();
                    Producto pSel = null;
                    for (Producto p : productos)
                        if (p.getProductoId() == pId) {
                            pSel = p;
                            break;
                        }

                    if (uSel == null || pSel == null) {
                        System.out.println("Usuario o producto no valido.");
                        break;
                    }

                    System.out.print("  Fecha de compra (dd/MM/yyyy): ");
                    Date fecha = leerFecha();
                    System.out.print("  Cantidad: ");
                    int cantidad = leerInt();

                    compras.add(new Compra(idCompra++, uSel, pSel, fecha, cantidad));
                    System.out.println("Compra registrada correctamente.");
                    break;

                case 0:
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        } while (op != 0);
    }

    // ══════════════════════════════════════════════════════════════
    //  MENU DISPOSICIONES
    // ══════════════════════════════════════════════════════════════
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
                    if (disposiciones.isEmpty()) {
                        System.out.println("No hay disposiciones registradas.");
                    } else {
                        System.out.println("\nDisposiciones registradas:");
                        for (Disposicion d : disposiciones) System.out.println("  " + d);
                    }
                    break;

                case 2:
                    System.out.println("\n-- Registrar Disposicion --");

                    if (compras.isEmpty()) {
                        System.out.println("Primero registra al menos una compra.");
                        break;
                    }
                    if (usuarios.isEmpty()) {
                        System.out.println("Primero registra al menos un usuario.");
                        break;
                    }
                    if (productos.isEmpty()) {
                        System.out.println("Primero registra al menos un producto.");
                        break;
                    }

                    System.out.println("  Compras disponibles:");
                    for (Compra c : compras)
                        System.out.println("    [" + c.getCompraId() + "] " + c.getUsuario().getNombre() + " | " + c.getProducto().getNombreProducto() + " | Cant: " + c.getCantidad());
                    System.out.print("  ID Compra: ");
                    int cId = leerInt();
                    Compra cSel = null;
                    for (Compra c : compras)
                        if (c.getCompraId() == cId) {
                            cSel = c;
                            break;
                        }

                    System.out.println("  Usuarios disponibles:");
                    for (Usuario u : usuarios)
                        System.out.println("    [" + u.getUsuarioId() + "] " + u.getNombre());
                    System.out.print("  ID Usuario: ");
                    int uId = leerInt();
                    Usuario uSel = null;
                    for (Usuario u : usuarios)
                        if (u.getUsuarioId() == uId) {
                            uSel = u;
                            break;
                        }

                    System.out.println("  Productos disponibles:");
                    for (Producto p : productos)
                        System.out.println("    [" + p.getProductoId() + "] " + p.getNombreProducto());
                    System.out.print("  ID Producto: ");
                    int pId = leerInt();
                    Producto pSel = null;
                    for (Producto p : productos)
                        if (p.getProductoId() == pId) {
                            pSel = p;
                            break;
                        }

                    if (cSel == null || uSel == null || pSel == null) {
                        System.out.println("Compra, usuario o producto no valido.");
                        break;
                    }

                    System.out.print("  Fecha (dd/MM/yyyy): ");
                    Date fecha = leerFecha();
                    System.out.print("  Cantidad: ");
                    int cantidad = leerInt();
                    System.out.print("  Peso en kg: ");
                    double peso = leerDouble();

                    disposiciones.add(new Disposicion(idDisposicion++, cSel, uSel, pSel, fecha, cantidad, peso));
                    System.out.println("Disposicion registrada correctamente.");
                    break;

                case 0:
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        } while (op != 0);
    }

    // ══════════════════════════════════════════════════════════════
    //  METODOS AUXILIARES
    // ══════════════════════════════════════════════════════════════
    static int leerInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("  Ingresa un numero entero valido: ");
            }
        }
    }

    static double leerDouble() {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("  Ingresa un numero valido (ej: 1200.50): ");
            }
        }
    }

    static Date leerFecha() {
        while (true) {
            try {
                return sdf.parse(sc.nextLine().trim());
            } catch (ParseException e) {
                System.out.print("  Formato invalido. Usa dd/MM/yyyy: ");
            }
        }
    }
}
