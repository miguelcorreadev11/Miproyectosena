// Paquete de controladores del patrón MVC.
package controlador;

// BigDecimal calcula subtotales y totales sin errores de redondeo.
import java.math.BigDecimal;
// Timestamp representa las fechas de pedido y entrega que recibe el modelo Compra.
import java.sql.Timestamp;
// LocalDate interpreta las fechas escritas como AAAA-MM-DD.
import java.time.LocalDate;
// LocalTime agrega la hora actual a la fecha del pedido.
import java.time.LocalTime;
// DateTimeParseException indica que una fecha no tiene formato válido.
import java.time.format.DateTimeParseException;
// ArrayList devuelve listas a Compras_Form.
import java.util.ArrayList;
// List recibe el detalle de la compra que arma la vista.
import java.util.List;
// JOptionPane muestra validaciones, confirmaciones y resultados.
import javax.swing.JOptionPane;
// Almacen: destino de la mercancía comprada.
import modelo.Almacen;
// Modelo Compra: inserta compras, las consulta, las entrega y las cancela.
import modelo.Compra;
// Modelo DetalleCompra: línea de producto, cantidad y precio del pedido.
import modelo.DetalleCompra;
// Producto que se agrega al detalle.
import modelo.Producto;
// Proveedor al que se hace el pedido.
import modelo.Proveedor;
// Usuario de la sesión, responsable de la entrada al inventario.
import modelo.Usuario;

// Controlador de Compras_Form: valida el pedido y coordina su registro, entrega y cancelación.
public class Compra_control {
    // Texto de la opción 'sin filtro' del combo de estados.
    public static final String FILTRO_TODOS = "Todos";

    // Opciones del combo de filtro de compras: 'Todos' y los valores del ENUM compras.estado.
    public static String[] getFiltrosEstado() {
        // Arreglo con 'Todos', 'pendiente', 'entregado' y 'cancelado'.
        return new String[]{FILTRO_TODOS, Compra.ESTADO_PENDIENTE, Compra.ESTADO_ENTREGADO,
            Compra.ESTADO_CANCELADO};
    }

    // Proveedores con estado 'activo', los únicos a los que se permite hacer pedidos.
    public static ArrayList<Proveedor> listarProveedoresActivos() {
        // Lista resultado.
        ArrayList<Proveedor> activos = new ArrayList<>();
        // Recorre todos los proveedores de la tabla proveedores.
        for (Proveedor proveedor : Proveedor.getProveedores()) {
            // Descarta los proveedores inactivos o suspendidos.
            if ("activo".equals(proveedor.getEstado())) {
                // Agrega el proveedor activo.
                activos.add(proveedor);
            }
        }
        // Devuelve la lista para jComboBoxProveedor.
        return activos;
    }

    // Almacenes con estado 'activo' que pueden recibir mercancía.
    public static ArrayList<Almacen> listarAlmacenesActivos() {
        // Lista resultado.
        ArrayList<Almacen> activos = new ArrayList<>();
        // Recorre todos los almacenes de la tabla almacenes.
        for (Almacen almacen : Almacen.getAlmacenes()) {
            // Descarta los almacenes inactivos.
            if ("activo".equals(almacen.getEstado())) {
                // Agrega el almacén activo.
                activos.add(almacen);
            }
        }
        // Devuelve la lista; también la usa Movimientos_Form.
        return activos;
    }

    // Productos 'activo' con el nombre de su categoría, para el combo de productos de la compra.
    public static ArrayList<Producto> listarProductosActivos() {
        // Lista resultado.
        ArrayList<Producto> activos = new ArrayList<>();
        // Recorre los productos con su categoría (INNER JOIN en el modelo).
        for (Producto producto : Producto.getProductosConCategoria()) {
            // Descarta los productos descontinuados.
            if ("activo".equals(producto.getEstado())) {
                // Agrega el producto activo.
                activos.add(producto);
            }
        }
        // Devuelve la lista para jComboBoxProducto.
        return activos;
    }

    // Número de pedido sugerido para una compra nueva.
    public static int siguienteNumeroPedido() {
        // MAX(numero_pedido) + 1 calculado en MySQL.
        return Compra.getSiguienteNumeroPedido();
    }

    // Valida una línea del detalle; devuelve la DetalleCompra lista para agregar o null si hay errores.
    public static DetalleCompra crearLineaDetalle(Producto producto, String cantidadTexto, String precioTexto) {
        // Debe haber un producto seleccionado.
        if (producto == null) {
            // Avisa que falta el producto.
            JOptionPane.showMessageDialog(null, "Seleccione un producto", "Campo obligatorio",
                    JOptionPane.WARNING_MESSAGE);
            // Línea inválida.
            return null;
        }
        // Un producto descontinuado no se puede pedir.
        if ("descontinuado".equals(producto.getEstado())) {
            // Avisa que el producto no está disponible.
            JOptionPane.showMessageDialog(null, "El producto está descontinuado y no se puede comprar",
                    "Producto no disponible", JOptionPane.WARNING_MESSAGE);
            // Línea inválida.
            return null;
        }
        // Cantidad convertida a entero; null si el texto no es numérico.
        Integer cantidad;
        try {
            // Convierte el texto sin espacios a entero.
            cantidad = Integer.parseInt(cantidadTexto.trim());
        // Texto vacío, no numérico o null.
        } catch (NumberFormatException | NullPointerException ex) {
            // Marca la cantidad como inválida.
            cantidad = null;
        }
        // La cantidad debe ser un entero positivo (detallecompra.cantidad es UNSIGNED).
        if (cantidad == null || cantidad <= 0) {
            // Avisa que la cantidad no es válida.
            JOptionPane.showMessageDialog(null, "La cantidad debe ser un número entero mayor que cero",
                    "Dato inválido", JOptionPane.WARNING_MESSAGE);
            // Línea inválida.
            return null;
        }
        // Precio unitario convertido a BigDecimal.
        BigDecimal precio;
        try {
            // Acepta coma o punto decimal y convierte el texto a BigDecimal.
            precio = new BigDecimal(precioTexto.trim().replace(",", "."));
        // Texto vacío, no numérico o null.
        } catch (NumberFormatException | NullPointerException ex) {
            // Marca el precio como inválido.
            precio = null;
        }
        // El precio debe ser mayor que cero.
        if (precio == null || precio.compareTo(BigDecimal.ZERO) <= 0) {
            // Avisa que el precio no es válido.
            JOptionPane.showMessageDialog(null, "El precio unitario debe ser un número mayor que cero",
                    "Dato inválido", JOptionPane.WARNING_MESSAGE);
            // Línea inválida.
            return null;
        }
        // detallecompra.precio_unitario es DECIMAL(10,2): máximo 2 decimales y 8 enteros.
        if (precio.scale() > 2 || precio.compareTo(new BigDecimal("99999999.99")) > 0) {
            // Avisa que el precio excede el formato de la columna.
            JOptionPane.showMessageDialog(null, "El precio admite máximo 8 enteros y 2 decimales",
                    "Dato inválido", JOptionPane.WARNING_MESSAGE);
            // Línea inválida.
            return null;
        }
        // Crea la línea con el precio a 2 decimales; su constructor calcula el subtotal.
        return new DetalleCompra(producto.getIdProducto(), producto.getNombre(), cantidad, precio.setScale(2));
    }

    // Suma los subtotales de todas las líneas del pedido.
    public static BigDecimal calcularTotal(List<DetalleCompra> detalles) {
        // Acumulador con 2 decimales.
        BigDecimal total = BigDecimal.ZERO.setScale(2);
        // Recorre cada línea del detalle.
        for (DetalleCompra detalle : detalles) {
            // Suma el subtotal (cantidad x precio) de la línea.
            total = total.add(detalle.getSubtotal());
        }
        // Total que Compras_Form muestra en jLabelTotal.
        return total;
    }

    // Valida todos los datos del pedido y guarda compra + detalle en una transacción;
    // devuelve el id de la compra creada o null si no se guardó.
    public static Integer registrarCompra(String numeroPedidoTexto, Proveedor proveedor, Almacen almacen,
            String fechaPedidoTexto, String fechaEntregaTexto, String comentarios, List<DetalleCompra> detalles) {
        // Número de pedido convertido a entero.
        Integer numeroPedido;
        try {
            // Convierte el texto del número de pedido a entero.
            numeroPedido = Integer.parseInt(numeroPedidoTexto.trim());
        // Texto vacío, no numérico o null.
        } catch (NumberFormatException | NullPointerException ex) {
            // Marca el número como inválido.
            numeroPedido = null;
        }
        // El número de pedido debe ser un entero positivo.
        if (numeroPedido == null || numeroPedido <= 0) {
            // Avisa que el número no es válido.
            JOptionPane.showMessageDialog(null, "El número de pedido debe ser un entero mayor que cero",
                    "Dato inválido", JOptionPane.WARNING_MESSAGE);
            // No se registra la compra.
            return null;
        }
        // compras.numero_pedido es UNIQUE: se valida antes del INSERT.
        if (Compra.existeNumeroPedido(numeroPedido)) {
            // Avisa que el número ya está en uso.
            JOptionPane.showMessageDialog(null, "Ya existe una compra con el número de pedido " + numeroPedido,
                    "Pedido duplicado", JOptionPane.WARNING_MESSAGE);
            // No se registra la compra.
            return null;
        }
        // Debe haber un proveedor seleccionado.
        if (proveedor == null) {
            // Avisa que falta el proveedor.
            JOptionPane.showMessageDialog(null, "Seleccione un proveedor", "Campo obligatorio",
                    JOptionPane.WARNING_MESSAGE);
            // No se registra la compra.
            return null;
        }
        // Debe haber un almacén destino seleccionado.
        if (almacen == null) {
            // Avisa que falta el almacén.
            JOptionPane.showMessageDialog(null, "Seleccione el almacén que recibirá la compra",
                    "Campo obligatorio", JOptionPane.WARNING_MESSAGE);
            // No se registra la compra.
            return null;
        }
        // Convierte la fecha del pedido (AAAA-MM-DD) a LocalDate.
        LocalDate fechaPedido = leerFecha(fechaPedidoTexto);
        // La fecha del pedido es obligatoria y debe tener formato válido.
        if (fechaPedido == null) {
            // Avisa el formato esperado.
            JOptionPane.showMessageDialog(null, "Ingrese la fecha del pedido con el formato AAAA-MM-DD",
                    "Fecha inválida", JOptionPane.WARNING_MESSAGE);
            // No se registra la compra.
            return null;
        }
        // Fecha de entrega opcional; queda null si el campo está vacío.
        LocalDate fechaEntrega = null;
        // Solo se valida si el usuario escribió algo.
        if (fechaEntregaTexto != null && !fechaEntregaTexto.trim().isEmpty()) {
            // Convierte la fecha de entrega.
            fechaEntrega = leerFecha(fechaEntregaTexto);
            // El texto no tiene formato de fecha.
            if (fechaEntrega == null) {
                // Avisa el formato esperado.
                JOptionPane.showMessageDialog(null, "La fecha de entrega debe tener el formato AAAA-MM-DD",
                        "Fecha inválida", JOptionPane.WARNING_MESSAGE);
                // No se registra la compra.
                return null;
            }
            // La entrega no puede ser anterior al pedido.
            if (fechaEntrega.isBefore(fechaPedido)) {
                // Avisa la incoherencia de fechas.
                JOptionPane.showMessageDialog(null, "La fecha de entrega no puede ser anterior a la del pedido",
                        "Fecha inválida", JOptionPane.WARNING_MESSAGE);
                // No se registra la compra.
                return null;
            }
        }
        // El pedido debe tener al menos una línea en el detalle.
        if (detalles == null || detalles.isEmpty()) {
            // Avisa que el detalle está vacío.
            JOptionPane.showMessageDialog(null, "Agregue al menos un producto al detalle de la compra",
                    "Detalle vacío", JOptionPane.WARNING_MESSAGE);
            // No se registra la compra.
            return null;
        }
        // compras.comentarios es VARCHAR(500).
        if (comentarios != null && comentarios.trim().length() > 500) {
            // Avisa que los comentarios son demasiado largos.
            JOptionPane.showMessageDialog(null, "Los comentarios admiten máximo 500 caracteres",
                    "Dato inválido", JOptionPane.WARNING_MESSAGE);
            // No se registra la compra.
            return null;
        }

        // Objeto con los datos de la compra que se insertará.
        Compra compra = new Compra();
        // Número de pedido validado.
        compra.setNumeroPedido(numeroPedido);
        // Id del proveedor seleccionado (llave foránea a proveedores).
        compra.setIdProveedor(proveedor.getIdProveedor());
        // Id del almacén destino (llave foránea a almacenes).
        compra.setIdAlmacen(almacen.getIdAlmacen());
        // Fecha del pedido con la hora actual, sin nanosegundos.
        compra.setFechaPedido(Timestamp.valueOf(fechaPedido.atTime(LocalTime.now().withNano(0))));
        // Fecha de entrega estimada a las 00:00, o null si no se indicó.
        compra.setFechaEntrega(fechaEntrega == null ? null : Timestamp.valueOf(fechaEntrega.atStartOfDay()));
        // Comentarios sin espacios sobrantes; vacío se guarda como NULL.
        compra.setComentarios(comentarios == null || comentarios.trim().isEmpty() ? null : comentarios.trim());

        // Inserta compras y detallecompra en una transacción; devuelve el id generado o null.
        Integer idCompra = Compra.insertCompraConDetalle(compra, detalles);
        // Si se guardó, confirma al usuario.
        if (idCompra != null) {
            // Informa el número de pedido y que la compra queda pendiente.
            JOptionPane.showMessageDialog(null, "Compra registrada correctamente con el pedido N° " + numeroPedido
                    + ".\nQueda en estado pendiente hasta que se marque como entregada.", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        // Compras_Form limpia el formulario si recibe un id.
        return idCompra;
    }

    // Compras para la tabla de 'Compras registradas', filtradas por estado.
    public static ArrayList<Compra> listarCompras(String filtroEstado) {
        // 'Todos' se traduce a null para no filtrar por estado.
        String estado = FILTRO_TODOS.equals(filtroEstado) ? null : filtroEstado;
        // Consulta compras con nombres de proveedor y almacén.
        return Compra.buscarCompras(estado, null);
    }

    // Líneas del detalle de una compra para la tabla inferior de Compras_Form.
    public static ArrayList<DetalleCompra> listarDetalle(Integer idCompra) {
        // Sin compra seleccionada no hay detalle.
        if (idCompra == null) {
            // Devuelve una lista vacía.
            return new ArrayList<>();
        }
        // SELECT detallecompra + productos por id_compra.
        return DetalleCompra.getDetallesPorCompra(idCompra);
    }

    // Confirma y marca una compra pendiente como entregada, sumando el detalle al inventario.
    public static boolean marcarEntregada(Compra compra) {
        // Debe haber una compra seleccionada.
        if (compra == null) {
            // Avisa que falta la selección.
            JOptionPane.showMessageDialog(null, "Seleccione una compra de la tabla", "Compra no seleccionada",
                    JOptionPane.WARNING_MESSAGE);
            // No se procesa.
            return false;
        }
        // Solo las compras pendientes pueden entregarse.
        if (!Compra.ESTADO_PENDIENTE.equals(compra.getEstado())) {
            // Avisa que la operación no aplica al estado actual.
            JOptionPane.showMessageDialog(null, "Solo se pueden marcar como entregadas las compras pendientes",
                    "Operación no permitida", JOptionPane.WARNING_MESSAGE);
            // No se procesa.
            return false;
        }
        // Usuario autenticado que quedará como responsable de los movimientos de entrada.
        Usuario usuario = Login_control.getUsuarioSesion();
        // movimientoinventario.id_usuario es obligatorio: se exige sesión iniciada.
        if (usuario == null) {
            // Avisa que se debe iniciar sesión.
            JOptionPane.showMessageDialog(null, "Debe iniciar sesión para registrar la entrada al inventario",
                    "Sesión requerida", JOptionPane.WARNING_MESSAGE);
            // No se procesa.
            return false;
        }
        // Pide confirmación indicando el pedido y el almacén que recibirá la mercancía.
        int confirmacion = JOptionPane.showConfirmDialog(null,
                "¿Confirma que el pedido N° " + compra.getNumeroPedido() + " fue entregado?\n"
                + "Los productos se sumarán al inventario del almacén " + compra.getNombreAlmacen() + ".",
                "Confirmar entrega", JOptionPane.YES_NO_OPTION);
        // Si el usuario no eligió Sí, no se procesa.
        if (confirmacion != JOptionPane.YES_OPTION) {
            // Cancela la entrega.
            return false;
        }
        // Transacción en el modelo: suma cada línea al inventario, crea movimientos 'entrada' e
        // historial, actualiza productoproveedor y cambia la compra a 'entregado'.
        boolean entregada = Compra.marcarEntregada(compra.getIdCompra(), usuario.getIdUsuario());
        // Si se completó, confirma al usuario.
        if (entregada) {
            // Mensaje de éxito.
            JOptionPane.showMessageDialog(null, "Compra entregada. El inventario y los movimientos se "
                    + "actualizaron correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
        // Compras_Form recarga la tabla si es true.
        return entregada;
    }

    // Confirma y cancela una compra pendiente; no modifica el inventario.
    public static boolean cancelarCompra(Compra compra) {
        // Debe haber una compra seleccionada.
        if (compra == null) {
            // Avisa que falta la selección.
            JOptionPane.showMessageDialog(null, "Seleccione una compra de la tabla", "Compra no seleccionada",
                    JOptionPane.WARNING_MESSAGE);
            // No se procesa.
            return false;
        }
        // Solo las compras pendientes pueden cancelarse.
        if (!Compra.ESTADO_PENDIENTE.equals(compra.getEstado())) {
            // Avisa que la operación no aplica al estado actual.
            JOptionPane.showMessageDialog(null, "Solo se pueden cancelar compras pendientes",
                    "Operación no permitida", JOptionPane.WARNING_MESSAGE);
            // No se procesa.
            return false;
        }
        // Pide confirmación indicando el número de pedido.
        int confirmacion = JOptionPane.showConfirmDialog(null,
                "¿Está seguro de cancelar el pedido N° " + compra.getNumeroPedido() + "?",
                "Confirmar cancelación", JOptionPane.YES_NO_OPTION);
        // Si el usuario no eligió Sí, no se cancela.
        if (confirmacion != JOptionPane.YES_OPTION) {
            // Termina sin cambios.
            return false;
        }
        // UPDATE a 'cancelado' solo si la compra sigue en 'pendiente'.
        boolean cancelada = Compra.cancelarCompra(compra.getIdCompra());
        // Si se canceló, confirma al usuario.
        if (cancelada) {
            // Mensaje de éxito.
            JOptionPane.showMessageDialog(null, "Compra cancelada correctamente", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        // Otra sesión cambió el estado mientras tanto.
        // Si el false se debe a un error de MySQL, ese error ya se mostró: no se pide recargar ni redigitar.
        } else if (!config.conexión_BD.huboErrorEnUltimaOperacion()) {
            // Pide recargar la tabla.
            JOptionPane.showMessageDialog(null, "La compra ya no está pendiente; actualice la tabla",
                    "No se aplicaron cambios", JOptionPane.WARNING_MESSAGE);
        }
        // Compras_Form recarga la tabla si es true.
        return cancelada;
    }

    // Convierte texto AAAA-MM-DD a LocalDate; null si está vacío o no es una fecha válida.
    private static LocalDate leerFecha(String texto) {
        // Texto null o vacío.
        if (texto == null || texto.trim().isEmpty()) {
            // No hay fecha.
            return null;
        }
        try {
            // Interpreta el texto en formato ISO (AAAA-MM-DD).
            return LocalDate.parse(texto.trim());
        // Formato o fecha inexistente (por ejemplo 2026-02-30).
        } catch (DateTimeParseException ex) {
            // Fecha inválida.
            return null;
        }
    }
}
