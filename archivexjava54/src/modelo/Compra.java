// Paquete modelo: clases que representan tablas de MySQL y ejecutan su SQL con JDBC.
package modelo;

// BigDecimal: valores monetarios exactos (columnas DECIMAL).
import java.math.BigDecimal;
// Connection: conexión JDBC compartida que entrega config.conexión_BD.
import java.sql.Connection;
// PreparedStatement: sentencias SQL parametrizadas (evitan inyección SQL).
import java.sql.PreparedStatement;
// ResultSet: filas devueltas por los SELECT.
import java.sql.ResultSet;
// SQLException: errores reportados por MySQL o por el driver JDBC.
import java.sql.SQLException;
// Statement.RETURN_GENERATED_KEYS permite leer el id AUTO_INCREMENT insertado.
import java.sql.Statement;
// Timestamp: representa las columnas DATETIME de la tabla.
import java.sql.Timestamp;
// ArrayList: listas de registros devueltas a los controladores.
import java.util.ArrayList;
// List: tipo de las colecciones recibidas como parámetro.
import java.util.List;
// Level.SEVERE marca la gravedad de los errores registrados.
import java.util.logging.Level;
// Logger: escribe las excepciones en la consola de NetBeans.
import java.util.logging.Logger;
// JOptionPane: cuadros de diálogo de error, aviso o confirmación.
import javax.swing.JOptionPane;

// Modelo de la tabla compras (pedidos a proveedores). Guarda compra y detalle en una transacción
// y, al entregarla, genera las entradas de inventario mediante MovimientoInventario.
public class Compra {
    // Valor 'pendiente' del ENUM compras.estado: compra registrada sin recibir.
    public static final String ESTADO_PENDIENTE = "pendiente";
    // Valor 'entregado': la mercancía ya entró al inventario.
    public static final String ESTADO_ENTREGADO = "entregado";
    // Valor 'cancelado': la compra se anuló sin afectar el inventario.
    public static final String ESTADO_CANCELADO = "cancelado";

    // Columna id_compra (llave primaria AUTO_INCREMENT).
    private Integer idCompra;
    // Columna numero_pedido (índice único uk_compras_numero_pedido).
    private Integer numeroPedido;
    // Columna id_proveedor.
    private Integer idProveedor;
    // Columna id_almacen.
    private Integer idAlmacen;
    // Columna fecha_pedido DATETIME.
    private Timestamp fechaPedido;
    // Columna fecha_entrega DATETIME (opcional).
    private Timestamp fechaEntrega;
    // Columna valor_total DECIMAL(12,2).
    private BigDecimal valorTotal;
    // Columna estado.
    private String estado;
    // Columna comentarios VARCHAR(500).
    private String comentarios;

    // Campo auxiliar: nombre del proveedor para las JTable y combos.
    private String nombreProveedor;
    // Campo auxiliar: nombre del almacén para las JTable y mensajes.
    private String nombreAlmacen;

    // Constructor vacío; Compra_control llena la compra nueva con setters.
    public Compra() {
    }

    // Constructor completo, usado por mapResultSetDetallado al leer una fila.
    public Compra(Integer idCompra, Integer numeroPedido, Integer idProveedor, Integer idAlmacen,
            Timestamp fechaPedido, Timestamp fechaEntrega, BigDecimal valorTotal, String estado,
            String comentarios) {
        // Asigna el id de la compra (columna id_compra).
        this.idCompra = idCompra;
        // Asigna el número de pedido (único).
        this.numeroPedido = numeroPedido;
        // Asigna el id del proveedor (llave foránea a proveedores).
        this.idProveedor = idProveedor;
        // Asigna el id del almacén destino (llave foránea a almacenes).
        this.idAlmacen = idAlmacen;
        // Asigna la fecha y hora del pedido.
        this.fechaPedido = fechaPedido;
        // Asigna la fecha de entrega (estimada o real).
        this.fechaEntrega = fechaEntrega;
        // Asigna el valor total de la compra.
        this.valorTotal = valorTotal;
        // Asigna el estado ('pendiente', 'entregado' o 'cancelado').
        this.estado = estado;
        // Asigna los comentarios del pedido.
        this.comentarios = comentarios;
    }

    // Devuelve el id de la compra (columna id_compra).
    public Integer getIdCompra() {
        return idCompra;
    }

    // Cambia el id de la compra (columna id_compra).
    public void setIdCompra(Integer idCompra) {
        this.idCompra = idCompra;
    }

    // Devuelve el número de pedido (único).
    public Integer getNumeroPedido() {
        return numeroPedido;
    }

    // Cambia el número de pedido (único).
    public void setNumeroPedido(Integer numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    // Devuelve el id del proveedor (llave foránea a proveedores).
    public Integer getIdProveedor() {
        return idProveedor;
    }

    // Cambia el id del proveedor (llave foránea a proveedores).
    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }

    // Devuelve el id del almacén destino (llave foránea a almacenes).
    public Integer getIdAlmacen() {
        return idAlmacen;
    }

    // Cambia el id del almacén destino (llave foránea a almacenes).
    public void setIdAlmacen(Integer idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    // Devuelve la fecha y hora del pedido.
    public Timestamp getFechaPedido() {
        return fechaPedido;
    }

    // Cambia la fecha y hora del pedido.
    public void setFechaPedido(Timestamp fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    // Devuelve la fecha de entrega (estimada o real).
    public Timestamp getFechaEntrega() {
        return fechaEntrega;
    }

    // Cambia la fecha de entrega (estimada o real).
    public void setFechaEntrega(Timestamp fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    // Devuelve el valor total de la compra.
    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    // Cambia el valor total de la compra.
    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    // Devuelve el estado ('pendiente', 'entregado' o 'cancelado').
    public String getEstado() {
        return estado;
    }

    // Cambia el estado ('pendiente', 'entregado' o 'cancelado').
    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Devuelve los comentarios del pedido.
    public String getComentarios() {
        return comentarios;
    }

    // Cambia los comentarios del pedido.
    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    // Devuelve el nombre del proveedor (dato auxiliar del JOIN).
    public String getNombreProveedor() {
        return nombreProveedor;
    }

    // Cambia el nombre del proveedor (dato auxiliar del JOIN).
    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    // Devuelve el nombre del almacén (dato auxiliar del JOIN).
    public String getNombreAlmacen() {
        return nombreAlmacen;
    }

    // Cambia el nombre del almacén (dato auxiliar del JOIN).
    public void setNombreAlmacen(String nombreAlmacen) {
        this.nombreAlmacen = nombreAlmacen;
    }

    // Texto que muestra la compra en un JComboBox: número de pedido y proveedor.
    @Override
    public String toString() {
        // Ejemplo: 'Pedido N° 3 - World Phone'.
        return "Pedido N° " + numeroPedido + (nombreProveedor == null ? "" : " - " + nombreProveedor);
    }

    // Número de pedido sugerido para una compra nueva: MAX(numero_pedido) + 1.
    public static int getSiguienteNumeroPedido() {
        // Obtiene la conexión compartida con MySQL desde config.conexión_BD.
        Connection con = config.conexión_BD.getConnection();
        // Sin conexión se sugiere el número 1.
        if (con == null) {
            // Valor por defecto.
            return 1;
        }
        // COALESCE devuelve 0 cuando la tabla está vacía; try-with-resources cierra sentencia y resultado.
        try (PreparedStatement ps = con.prepareStatement("SELECT COALESCE(MAX(numero_pedido), 0) + 1 FROM compras");
                ResultSet rs = ps.executeQuery()) {
            // La consulta de agregación siempre devuelve una fila.
            if (rs.next()) {
                // Siguiente número disponible.
                return rs.getInt(1);
            }
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(Compra.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Si la consulta falla se sugiere 1.
        return 1;
    }

    // true si ya existe una compra con ese número de pedido.
    public static boolean existeNumeroPedido(int numeroPedido) {
        // Obtiene la conexión compartida con MySQL desde config.conexión_BD.
        Connection con = config.conexión_BD.getConnection();
        // Sin conexión no se puede comprobar.
        if (con == null) {
            // Asume que no existe; el INSERT posterior también fallaría sin conexión.
            return false;
        }
        // Consulta mínima: basta saber si hay alguna fila.
        try (PreparedStatement ps = con.prepareStatement("SELECT 1 FROM compras WHERE numero_pedido = ?")) {
            // Parámetro 1: número de pedido.
            ps.setInt(1, numeroPedido);
            // Ejecuta la consulta; el ResultSet se cierra automáticamente al salir del bloque.
            try (ResultSet rs = ps.executeQuery()) {
                // true si encontró una fila con ese número.
                return rs.next();
            }
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(Compra.class.getName()).log(Level.SEVERE, null, ex);
            // Ante un error se asume que no existe.
            return false;
        }
    }

    // Inserta la compra y todas sus líneas en una transacción; recalcula el total desde el detalle.
    // Devuelve el id generado o null si falló (se hace ROLLBACK).
    public static Integer insertCompraConDetalle(Compra compra, List<DetalleCompra> detalles) {
        // Obtiene la conexión compartida con MySQL desde config.conexión_BD.
        Connection con = config.conexión_BD.getConnection();
        // getConnection() devuelve null cuando MySQL no está disponible.
        if (con == null) {
            // Informa al usuario que no hay conexión con la base de datos.
            JOptionPane.showMessageDialog(null, config.conexión_BD.MENSAJE_SIN_CONEXION,
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            // Sin conexión no hay id.
            return null;
        }
        // Acumulador del total con 2 decimales.
        BigDecimal total = BigDecimal.ZERO.setScale(2);
        // Recorre las líneas del pedido.
        for (DetalleCompra detalle : detalles) {
            // Suma el subtotal de cada línea.
            total = total.add(detalle.getSubtotal());
        }
        // INSERT de la cabecera de la compra.
        String query = "INSERT INTO compras (numero_pedido, id_proveedor, id_almacen, fecha_pedido, "
                + "fecha_entrega, valor_total, estado, comentarios) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            // Inicia una transacción manual: nada se confirma hasta con.commit().
            con.setAutoCommit(false);
            // Id AUTO_INCREMENT de la compra que se va a crear.
            int idCompra;
            // RETURN_GENERATED_KEYS permite leer el id generado por MySQL.
            try (PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                // Parámetro 1: número de pedido.
                ps.setInt(1, compra.getNumeroPedido());
                // Parámetro 2: id del proveedor.
                ps.setInt(2, compra.getIdProveedor());
                // Parámetro 3: id del almacén destino.
                ps.setInt(3, compra.getIdAlmacen());

                // Parámetro 4: fecha del pedido como texto 'AAAA-MM-DD HH:MM:SS', sin conversión de zona horaria.
                ps.setString(4, MovimientoInventario.textoFechaHora(compra.getFechaPedido()));
                // Parámetro 5: fecha de entrega como texto, o NULL.
                ps.setString(5, MovimientoInventario.textoFechaHora(compra.getFechaEntrega()));
                // Parámetro 6: total calculado en Java desde el detalle.
                ps.setBigDecimal(6, total);
                // Parámetro 7: toda compra nueva empieza en 'pendiente'.
                ps.setString(7, ESTADO_PENDIENTE);
                // Parámetro 8: comentarios o NULL.
                ps.setString(8, compra.getComentarios());
                // Inserta la cabecera sin confirmar todavía.
                ps.executeUpdate();
                // Lee la llave generada; el ResultSet se cierra al terminar.
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    // Si MySQL no devolvió el id, no se pueden insertar las líneas.
                    if (!keys.next()) {
                        // Provoca el ROLLBACK de la transacción.
                        throw new SQLException("no se obtuvo el id de la compra");
                    }
                    // Id de la compra recién insertada.
                    idCompra = keys.getInt(1);
                }
            }
            // Recorre las líneas del pedido.
            for (DetalleCompra detalle : detalles) {
                // Inserta cada línea en detallecompra dentro de la misma transacción.
                DetalleCompra.insertDetalle(con, idCompra, detalle);
            }
            // Confirma en MySQL todas las operaciones de la transacción.
            con.commit();
            // Devuelve el id; Compras_Form limpia el formulario.
            return idCompra;
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // ROLLBACK: no queda ni la cabecera ni ninguna línea guardada a medias.
            MovimientoInventario.deshacer(con);
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(Compra.class.getName()).log(Level.SEVERE, null, ex);
            // Informa el error de MySQL (por ejemplo, número de pedido duplicado).
            JOptionPane.showMessageDialog(null, "Error al registrar la compra: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            // La operación falló; el controlador recibe este resultado.
            return null;
        // Se ejecuta siempre, con éxito o con error.
        } finally {
            // Devuelve la conexión compartida al modo autocommit para los demás módulos.
            MovimientoInventario.restaurarAutoCommit(con);
        }
    }

    // Compras con nombres de proveedor y almacén; filtra por estado y proveedor si no son null.
    public static ArrayList<Compra> buscarCompras(String estado, Integer idProveedor) {
        // Lista resultado.
        ArrayList<Compra> lista = new ArrayList<>();
        // Obtiene la conexión compartida con MySQL desde config.conexión_BD.
        Connection con = config.conexión_BD.getConnection();
        // getConnection() devuelve null cuando MySQL no está disponible.
        if (con == null) {
            // Informa al usuario que no hay conexión con la base de datos.
            JOptionPane.showMessageDialog(null, config.conexión_BD.MENSAJE_SIN_CONEXION,
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            // Devuelve la lista (vacía si no hubo conexión o datos).
            return lista;
        }
        // SELECT base con JOIN a proveedores y almacenes; 'WHERE 1 = 1' permite añadir filtros con AND.
        StringBuilder query = new StringBuilder("SELECT c.id_compra, c.numero_pedido, c.id_proveedor, "
                + "c.id_almacen, c.fecha_pedido, c.fecha_entrega, c.valor_total, c.estado, c.comentarios, "
                + "p.nombre AS nombre_proveedor, a.nombre AS nombre_almacen "
                + "FROM compras c "
                + "INNER JOIN proveedores p ON c.id_proveedor = p.id_proveedor "
                + "INNER JOIN almacenes a ON c.id_almacen = a.id_almacen WHERE 1 = 1");
        // Valores de los signos ? en orden.
        ArrayList<Object> parametros = new ArrayList<>();
        // Filtro por estado, solo si se indicó.
        if (estado != null && !estado.trim().isEmpty()) {
            // Agrega la condición por estado.
            query.append(" AND c.estado = ?");
            // Valor del estado.
            parametros.add(estado);
        }
        // Filtro por proveedor, solo si se indicó.
        if (idProveedor != null) {
            // Agrega la condición por proveedor.
            query.append(" AND c.id_proveedor = ?");
            // Valor del id de proveedor.
            parametros.add(idProveedor);
        }
        // Ordena de la compra más reciente a la más antigua.
        query.append(" ORDER BY c.fecha_pedido DESC, c.id_compra DESC");

        // Prepara la consulta armada; try-with-resources la cierra.
        try (PreparedStatement ps = con.prepareStatement(query.toString())) {
            // Asigna los parámetros acumulados en el mismo orden de los signos ?.
            for (int i = 0; i < parametros.size(); i++) {
                // setObject elige el tipo JDBC según el valor (Integer o String).
                ps.setObject(i + 1, parametros.get(i));
            }
            // Ejecuta la consulta; el ResultSet se cierra al salir del bloque.
            try (ResultSet rs = ps.executeQuery()) {
                // Recorre cada fila devuelta por el SELECT.
                while (rs.next()) {
                    // Convierte la fila en Compra con los nombres del JOIN.
                    lista.add(mapResultSetDetallado(rs));
                }
            }
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(Compra.class.getName()).log(Level.SEVERE, null, ex);
            // Informa el error de consulta.
            JOptionPane.showMessageDialog(null, "Error al consultar compras: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
        // Devuelve la lista (vacía si no hubo conexión o datos).
        return lista;
    }

    // Consulta una compra por id con los nombres de proveedor y almacén; null si no existe.
    public static Compra getCompraPorId(int idCompra) {
        // Obtiene la conexión compartida con MySQL desde config.conexión_BD.
        Connection con = config.conexión_BD.getConnection();
        // getConnection() devuelve null cuando MySQL no está disponible.
        if (con == null) {
            // Informa al usuario que no hay conexión con la base de datos.
            JOptionPane.showMessageDialog(null, config.conexión_BD.MENSAJE_SIN_CONEXION,
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            // Sin conexión la operación termina aquí.
            return null;
        }
        // SELECT con JOIN filtrado por id_compra.
        String query = "SELECT c.id_compra, c.numero_pedido, c.id_proveedor, c.id_almacen, c.fecha_pedido, "
                + "c.fecha_entrega, c.valor_total, c.estado, c.comentarios, p.nombre AS nombre_proveedor, "
                + "a.nombre AS nombre_almacen FROM compras c "
                + "INNER JOIN proveedores p ON c.id_proveedor = p.id_proveedor "
                + "INNER JOIN almacenes a ON c.id_almacen = a.id_almacen WHERE c.id_compra = ?";
        // Prepara la sentencia parametrizada; try-with-resources la cierra al terminar.
        try (PreparedStatement ps = con.prepareStatement(query)) {
            // Parámetro 1: id de la compra.
            ps.setInt(1, idCompra);
            // Ejecuta la consulta; el ResultSet se cierra automáticamente al salir del bloque.
            try (ResultSet rs = ps.executeQuery()) {
                // Si la consulta encontró el registro buscado.
                if (rs.next()) {
                    // Convierte la fila encontrada.
                    return mapResultSetDetallado(rs);
                }
            }
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(Compra.class.getName()).log(Level.SEVERE, null, ex);
            // Informa el error de consulta.
            JOptionPane.showMessageDialog(null, "Error al consultar la compra: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
        // No se encontró la compra.
        return null;
    }

    // Entrega la compra en una transacción: suma cada línea al inventario, crea movimientos 'entrada'
    // e historial, actualiza productoproveedor y cambia el estado. Evita procesarla dos veces.
    public static boolean marcarEntregada(int idCompra, int idUsuario) {
        // Obtiene la conexión compartida con MySQL desde config.conexión_BD.
        Connection con = config.conexión_BD.getConnection();
        // getConnection() devuelve null cuando MySQL no está disponible.
        if (con == null) {
            // Informa al usuario que no hay conexión con la base de datos.
            JOptionPane.showMessageDialog(null, config.conexión_BD.MENSAJE_SIN_CONEXION,
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            // Sin conexión la operación termina aquí.
            return false;
        }
        try {
            // Inicia una transacción manual: nada se confirma hasta con.commit().
            con.setAutoCommit(false);

            // Estado leído de la base de datos, no del objeto de la vista (puede estar desactualizado).
            String estadoActual;
            // Almacén que recibirá la mercancía.
            int idAlmacen;
            // Proveedor de la compra, para actualizar productoproveedor.
            int idProveedor;
            // Número de pedido para el motivo de los movimientos.
            int numeroPedido;
            // FOR UPDATE bloquea la fila para que dos sesiones no entreguen la misma compra al mismo tiempo.
            try (PreparedStatement ps = con.prepareStatement("SELECT estado, id_almacen, id_proveedor, "
                    + "numero_pedido FROM compras WHERE id_compra = ? FOR UPDATE")) {
                // Parámetro 1: id de la compra.
                ps.setInt(1, idCompra);
                // Ejecuta la consulta con bloqueo.
                try (ResultSet rs = ps.executeQuery()) {
                    // La compra fue eliminada por otra sesión.
                    if (!rs.next()) {
                        // Provoca el ROLLBACK.
                        throw new SQLException("la compra ya no existe");
                    }
                    // Estado actual en MySQL.
                    estadoActual = rs.getString("estado");
                    // Almacén destino.
                    idAlmacen = rs.getInt("id_almacen");
                    // Proveedor.
                    idProveedor = rs.getInt("id_proveedor");
                    // Número de pedido.
                    numeroPedido = rs.getInt("numero_pedido");
                }
            }
            // Solo las compras pendientes se pueden entregar.
            if (!ESTADO_PENDIENTE.equals(estadoActual)) {
                // Provoca el ROLLBACK con el estado actual en el mensaje.
                throw new SQLException("solo se pueden entregar compras pendientes (estado actual: "
                        + estadoActual + ")");
            }
            // Segunda protección: si ya existen entradas con este id_compra, no se suman otra vez.
            if (MovimientoInventario.compraTieneEntradas(con, idCompra)) {
                // Provoca el ROLLBACK.
                throw new SQLException("la compra ya fue procesada como entrada de inventario");
            }

            // Lee el detalle con la misma conexión y transacción.
            ArrayList<DetalleCompra> detalles = DetalleCompra.consultarDetalles(con, idCompra);
            // Una compra sin líneas no tiene nada que ingresar.
            if (detalles.isEmpty()) {
                // Provoca el ROLLBACK.
                throw new SQLException("la compra no tiene productos en el detalle");
            }
            // Motivo que quedará en cada movimiento de entrada.
            String motivo = "Entrada por compra, pedido N° " + numeroPedido;

            // Una sola fecha del servidor para la entrega, sus movimientos y su historial.
            String fecha = MovimientoInventario.obtenerFechaServidor(con);
            // Procesa cada línea del pedido.
            for (DetalleCompra detalle : detalles) {
                // Suma la cantidad al almacén y registra movimiento 'entrada' e historial con esta compra.
                MovimientoInventario.aplicarMovimiento(con, detalle.getIdProducto(), idAlmacen, idUsuario,
                        MovimientoInventario.TIPO_ENTRADA, detalle.getCantidad(), idCompra, motivo, fecha);
                // Guarda el último precio del producto con este proveedor.
                registrarPrecioProveedor(con, detalle.getIdProducto(), idProveedor, detalle.getPrecioUnitario());
            }

            // Marca la compra como entregada con la fecha real de entrega.
            try (PreparedStatement ps = con.prepareStatement("UPDATE compras SET estado = ?, "
                    + "fecha_entrega = ? WHERE id_compra = ?")) {
                // Parámetro 1: estado 'entregado'.
                ps.setString(1, ESTADO_ENTREGADO);
                // Parámetro 2: fecha del servidor usada en los movimientos.
                ps.setString(2, fecha);
                // Parámetro 3: id de la compra.
                ps.setInt(3, idCompra);
                // Actualiza la cabecera dentro de la transacción.
                ps.executeUpdate();
            }
            // Confirma inventario, movimientos, historial, precios y estado a la vez.
            con.commit();
            // Entrega completada.
            return true;
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // ROLLBACK: el inventario queda exactamente como estaba.
            MovimientoInventario.deshacer(con);
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(Compra.class.getName()).log(Level.SEVERE, null, ex);
            // Informa el motivo por el que no se entregó.
            JOptionPane.showMessageDialog(null, "No se pudo marcar la compra como entregada: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            // La operación falló; el controlador recibe este resultado.
            return false;
        // Se ejecuta siempre, con éxito o con error.
        } finally {
            // Devuelve la conexión compartida al modo autocommit para los demás módulos.
            MovimientoInventario.restaurarAutoCommit(con);
        }
    }

    // Cancela la compra solo si sigue 'pendiente'; no toca el inventario.
    public static boolean cancelarCompra(int idCompra) {
        // Obtiene la conexión compartida con MySQL desde config.conexión_BD.
        Connection con = config.conexión_BD.getConnection();
        // getConnection() devuelve null cuando MySQL no está disponible.
        if (con == null) {
            // Informa al usuario que no hay conexión con la base de datos.
            JOptionPane.showMessageDialog(null, config.conexión_BD.MENSAJE_SIN_CONEXION,
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            // Sin conexión no se cancela.
            return false;
        }
        // La condición 'AND estado = ?' impide cancelar una compra ya entregada o cancelada.
        try (PreparedStatement ps = con.prepareStatement("UPDATE compras SET estado = ? "
                + "WHERE id_compra = ? AND estado = ?")) {
            // Parámetro 1: nuevo estado 'cancelado'.
            ps.setString(1, ESTADO_CANCELADO);
            // Parámetro 2: id de la compra.
            ps.setInt(2, idCompra);
            // Parámetro 3: estado requerido 'pendiente'.
            ps.setString(3, ESTADO_PENDIENTE);
            // true si la compra seguía pendiente y se canceló.
            return ps.executeUpdate() != 0;
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(Compra.class.getName()).log(Level.SEVERE, null, ex);
            // Informa el error de MySQL.
            JOptionPane.showMessageDialog(null, "Error al cancelar la compra: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            // La operación falló; el controlador recibe este resultado.
            return false;
        }
    }

    // Inserta o actualiza en productoproveedor el último precio de compra del producto con el proveedor.
    private static void registrarPrecioProveedor(Connection con, int idProducto, int idProveedor,
            BigDecimal precio) throws SQLException {
        // ON DUPLICATE KEY UPDATE usa el índice único (id_producto, id_proveedor) para actualizar si ya existe.
        try (PreparedStatement ps = con.prepareStatement("INSERT INTO productoproveedor (id_producto, "
                + "id_proveedor, precio_compra) VALUES (?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE precio_compra = VALUES(precio_compra)")) {
            // Parámetro 1: id del producto.
            ps.setInt(1, idProducto);
            // Parámetro 2: id del proveedor.
            ps.setInt(2, idProveedor);
            // Parámetro 3: precio unitario de la compra.
            ps.setBigDecimal(3, precio);
            // Ejecuta dentro de la transacción de la entrega.
            ps.executeUpdate();
        }
    }

    // Convierte una fila con los datos del JOIN en un objeto Compra.
    private static Compra mapResultSetDetallado(ResultSet rs) throws SQLException {
        // Las fechas se leen como LocalDateTime para mostrarlas tal como están en MySQL.
        Compra compra = new Compra(
                rs.getInt("id_compra"),
                rs.getInt("numero_pedido"),
                rs.getInt("id_proveedor"),
                rs.getInt("id_almacen"),
                MovimientoInventario.leerFechaHora(rs, "fecha_pedido"),
                MovimientoInventario.leerFechaHora(rs, "fecha_entrega"),
                rs.getBigDecimal("valor_total"),
                rs.getString("estado"),
                rs.getString("comentarios"));
        // Agrega el nombre del proveedor del JOIN.
        compra.setNombreProveedor(rs.getString("nombre_proveedor"));
        // Agrega el nombre del almacén del JOIN.
        compra.setNombreAlmacen(rs.getString("nombre_almacen"));
        // Devuelve la compra construida.
        return compra;
    }
}
