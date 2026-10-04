// Paquete modelo: clases que representan tablas de MySQL y ejecutan su SQL con JDBC.
package modelo;

// Connection: conexión JDBC compartida que entrega config.conexión_BD.
import java.sql.Connection;
// PreparedStatement: sentencias SQL parametrizadas (evitan inyección SQL).
import java.sql.PreparedStatement;
// ResultSet: filas devueltas por los SELECT.
import java.sql.ResultSet;
// SQLException: errores reportados por MySQL o por el driver JDBC.
import java.sql.SQLException;
// Timestamp: representa las columnas DATETIME de la tabla.
import java.sql.Timestamp;
// LocalDateTime lee los DATETIME sin conversión de zona horaria.
import java.time.LocalDateTime;
// DateTimeFormatter da formato 'AAAA-MM-DD HH:MM:SS' a las fechas enviadas a MySQL.
import java.time.format.DateTimeFormatter;
// ArrayList: listas de registros devueltas a los controladores.
import java.util.ArrayList;
// Level.SEVERE marca la gravedad de los errores registrados.
import java.util.logging.Level;
// Logger: escribe las excepciones en la consola de NetBeans.
import java.util.logging.Logger;
// JOptionPane: cuadros de diálogo de error, aviso o confirmación.
import javax.swing.JOptionPane;

// Modelo de la tabla movimientoinventario. Cada movimiento actualiza inventarioporalmacen y
// productos.cantidad, y deja registro en historialinventario dentro de una transacción.
public class MovimientoInventario {
    // Valor 'entrada' del ENUM tipo_movimiento: suma unidades al almacén.
    public static final String TIPO_ENTRADA = "entrada";
    // Valor 'salida': resta unidades del almacén.
    public static final String TIPO_SALIDA = "salida";
    // Valor 'ajuste': fija la existencia a la cantidad contada físicamente.
    public static final String TIPO_AJUSTE = "ajuste";
    // Valor 'devolucion': devolución al proveedor, resta unidades del almacén.
    public static final String TIPO_DEVOLUCION = "devolucion";

    // Columna id_movimiento (llave primaria AUTO_INCREMENT).
    private Integer idMovimiento;
    // Columna id_producto (llave foránea a productos).
    private Integer idProducto;
    // Columna id_almacen (llave foránea a almacenes).
    private Integer idAlmacen;
    // Columna id_usuario (llave foránea a usuarios).
    private Integer idUsuario;
    // Columna tipo_movimiento.
    private String tipoMovimiento;
    // Columna cantidad INT con signo.
    private Integer cantidad;
    // Columna id_compra (opcional).
    private Integer idCompra;
    // Columna motivo VARCHAR(230).
    private String motivo;
    // Columna fecha_movimiento.
    private Timestamp fechaMovimiento;

    // Campo auxiliar: nombre del producto para la JTable.
    private String nombreProducto;
    // Campo auxiliar: nombre del almacén para la JTable.
    private String nombreAlmacen;
    // Campo auxiliar: nombre y apellido del usuario.
    private String nombreUsuario;
    // Campo auxiliar: número de pedido de la compra relacionada.
    private Integer numeroPedido;

    // Constructor vacío.
    public MovimientoInventario() {
    }

    // Constructor completo, usado al leer movimientos en buscarMovimientos.
    public MovimientoInventario(Integer idMovimiento, Integer idProducto, Integer idAlmacen, Integer idUsuario,
            String tipoMovimiento, Integer cantidad, Integer idCompra, String motivo, Timestamp fechaMovimiento) {
        // Asigna el id del movimiento (columna id_movimiento).
        this.idMovimiento = idMovimiento;
        // Asigna el id del producto movido.
        this.idProducto = idProducto;
        // Asigna el id del almacén afectado.
        this.idAlmacen = idAlmacen;
        // Asigna el id del usuario responsable.
        this.idUsuario = idUsuario;
        // Asigna el tipo ('entrada', 'salida', 'ajuste' o 'devolucion').
        this.tipoMovimiento = tipoMovimiento;
        // Asigna la cantidad registrada (en ajustes, la diferencia con signo).
        this.cantidad = cantidad;
        // Asigna el id de la compra que originó la entrada (null en movimientos manuales).
        this.idCompra = idCompra;
        // Asigna el motivo del movimiento.
        this.motivo = motivo;
        // Asigna la fecha y hora del movimiento.
        this.fechaMovimiento = fechaMovimiento;
    }

    // Constructor para un movimiento nuevo que arma Movimiento_control.
    public MovimientoInventario(Integer idProducto, Integer idAlmacen, Integer idUsuario, String tipoMovimiento,
            Integer cantidad, Integer idCompra, String motivo) {
        // Asigna el id del producto movido.
        this.idProducto = idProducto;
        // Asigna el id del almacén afectado.
        this.idAlmacen = idAlmacen;
        // Asigna el id del usuario responsable.
        this.idUsuario = idUsuario;
        // Asigna el tipo ('entrada', 'salida', 'ajuste' o 'devolucion').
        this.tipoMovimiento = tipoMovimiento;
        // Asigna la cantidad registrada (en ajustes, la diferencia con signo).
        this.cantidad = cantidad;
        // Asigna el id de la compra que originó la entrada (null en movimientos manuales).
        this.idCompra = idCompra;
        // Asigna el motivo del movimiento.
        this.motivo = motivo;
    }

    // Devuelve el id del movimiento (columna id_movimiento).
    public Integer getIdMovimiento() {
        return idMovimiento;
    }

    // Cambia el id del movimiento (columna id_movimiento).
    public void setIdMovimiento(Integer idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    // Devuelve el id del producto movido.
    public Integer getIdProducto() {
        return idProducto;
    }

    // Cambia el id del producto movido.
    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    // Devuelve el id del almacén afectado.
    public Integer getIdAlmacen() {
        return idAlmacen;
    }

    // Cambia el id del almacén afectado.
    public void setIdAlmacen(Integer idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    // Devuelve el id del usuario responsable.
    public Integer getIdUsuario() {
        return idUsuario;
    }

    // Cambia el id del usuario responsable.
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    // Devuelve el tipo ('entrada', 'salida', 'ajuste' o 'devolucion').
    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    // Cambia el tipo ('entrada', 'salida', 'ajuste' o 'devolucion').
    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    // Devuelve la cantidad registrada (en ajustes, la diferencia con signo).
    public Integer getCantidad() {
        return cantidad;
    }

    // Cambia la cantidad registrada (en ajustes, la diferencia con signo).
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    // Devuelve el id de la compra que originó la entrada (null en movimientos manuales).
    public Integer getIdCompra() {
        return idCompra;
    }

    // Cambia el id de la compra que originó la entrada (null en movimientos manuales).
    public void setIdCompra(Integer idCompra) {
        this.idCompra = idCompra;
    }

    // Devuelve el motivo del movimiento.
    public String getMotivo() {
        return motivo;
    }

    // Cambia el motivo del movimiento.
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    // Devuelve la fecha y hora del movimiento.
    public Timestamp getFechaMovimiento() {
        return fechaMovimiento;
    }

    // Cambia la fecha y hora del movimiento.
    public void setFechaMovimiento(Timestamp fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    // Devuelve el nombre del producto (dato auxiliar del JOIN).
    public String getNombreProducto() {
        return nombreProducto;
    }

    // Cambia el nombre del producto (dato auxiliar del JOIN).
    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    // Devuelve el nombre del almacén (dato auxiliar del JOIN).
    public String getNombreAlmacen() {
        return nombreAlmacen;
    }

    // Cambia el nombre del almacén (dato auxiliar del JOIN).
    public void setNombreAlmacen(String nombreAlmacen) {
        this.nombreAlmacen = nombreAlmacen;
    }

    // Devuelve el nombre completo del usuario (dato auxiliar del JOIN).
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    // Cambia el nombre completo del usuario (dato auxiliar del JOIN).
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    // Devuelve el número de pedido de la compra relacionada (dato auxiliar).
    public Integer getNumeroPedido() {
        return numeroPedido;
    }

    // Cambia el número de pedido de la compra relacionada (dato auxiliar).
    public void setNumeroPedido(Integer numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    // Tipos de movimiento en el orden en que se muestran en los combos de Movimientos_Form.
    public static String[] getTiposMovimiento() {
        // Arreglo con los cuatro valores del ENUM.
        return new String[]{TIPO_ENTRADA, TIPO_SALIDA, TIPO_AJUSTE, TIPO_DEVOLUCION};
    }

    // Registra un movimiento manual en una transacción: inventario, producto, movimiento e historial.
    // Si algo falla hace ROLLBACK y no queda nada guardado a medias.
    public static boolean registrarMovimiento(MovimientoInventario movimiento) {
        // Obtiene la conexión compartida con MySQL desde config.conexión_BD.
        Connection con = config.conexión_BD.getConnection();
        // getConnection() devuelve null cuando MySQL no está disponible.
        if (con == null) {
            // Informa al usuario que no hay conexión con la base de datos.
            JOptionPane.showMessageDialog(null, config.conexión_BD.MENSAJE_SIN_CONEXION,
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            // Sin conexión no se registra.
            return false;
        }
        try {
            // Inicia una transacción manual: nada se confirma hasta con.commit().
            con.setAutoCommit(false);
            // Aplica el movimiento con la fecha actual del servidor.
            aplicarMovimiento(con, movimiento.getIdProducto(), movimiento.getIdAlmacen(),
                    movimiento.getIdUsuario(), movimiento.getTipoMovimiento(), movimiento.getCantidad(),
                    movimiento.getIdCompra(), movimiento.getMotivo());
            // Confirma en MySQL todas las operaciones de la transacción.
            con.commit();
            // Movimiento registrado.
            return true;
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // ROLLBACK: descarta todo lo hecho en la transacción fallida.
            deshacer(con);
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(MovimientoInventario.class.getName()).log(Level.SEVERE, null, ex);
            // Informa el motivo del fallo (por ejemplo, inventario insuficiente).
            JOptionPane.showMessageDialog(null, "No se pudo registrar el movimiento: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            // No se registró.
            return false;
        // Se ejecuta siempre, con éxito o con error.
        } finally {
            // Devuelve la conexión compartida al modo autocommit para los demás módulos.
            restaurarAutoCommit(con);
        }
    }

    // Fecha y hora actuales del servidor MySQL como texto 'AAAA-MM-DD HH:MM:SS'; al enviarla
    // como texto no la altera la zona horaria de la conexión (serverTimezone=UTC).
    static String obtenerFechaServidor(Connection con) throws SQLException {
        // DATE_FORMAT(NOW()) usa la hora del servidor, la misma de los DEFAULT CURRENT_TIMESTAMP.
        try (PreparedStatement ps = con.prepareStatement("SELECT DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s')");
                ResultSet rs = ps.executeQuery()) {
            // La consulta siempre devuelve una fila.
            if (rs.next()) {
                // Devuelve la fecha en texto.
                return rs.getString(1);
            }
        }
        // Caso imposible en la práctica; obliga a hacer ROLLBACK si ocurre.
        throw new SQLException("no se pudo obtener la fecha del servidor");
    }

    // Versión sin fecha: obtiene la hora del servidor y delega en la versión completa.
    static void aplicarMovimiento(Connection con, int idProducto, int idAlmacen, int idUsuario,
            String tipo, int cantidad, Integer idCompra, String motivo) throws SQLException {
        // Llama a la versión con fecha.
        aplicarMovimiento(con, idProducto, idAlmacen, idUsuario, tipo, cantidad, idCompra, motivo,
                obtenerFechaServidor(con));
    }

    // Aplica un movimiento dentro de la transacción del llamador (no hace commit). En 'ajuste' la cantidad
    // es la existencia contada; en los demás tipos es la cantidad movida. La usan este modelo y Compra.
    static void aplicarMovimiento(Connection con, int idProducto, int idAlmacen, int idUsuario,
            String tipo, int cantidad, Integer idCompra, String motivo, String fecha) throws SQLException {
        // Id del registro de inventario; null si el producto aún no tiene inventario en el almacén.
        Integer idInventario = null;
        // Existencia antes del movimiento.
        int cantidadAnterior = 0;
        // FOR UPDATE bloquea la fila de inventario para que otra sesión no la modifique a la vez.
        String consulta = "SELECT id_inventario, cantidad_disponible FROM inventarioporalmacen "
                + "WHERE id_producto = ? AND id_almacen = ? FOR UPDATE";
        try (PreparedStatement ps = con.prepareStatement(consulta)) {
            // Parámetro 1: id del producto.
            ps.setInt(1, idProducto);
            // Parámetro 2: id del almacén.
            ps.setInt(2, idAlmacen);
            // Ejecuta la consulta con bloqueo.
            try (ResultSet rs = ps.executeQuery()) {
                // Si ya existe inventario de ese producto en ese almacén.
                if (rs.next()) {
                    // Guarda el id del registro para actualizarlo.
                    idInventario = rs.getInt("id_inventario");
                    // Guarda la existencia actual.
                    cantidadAnterior = rs.getInt("cantidad_disponible");
                }
            }
        }

        // Existencia resultante después del movimiento.
        int cantidadNueva;
        // Calcula la nueva existencia según el tipo.
        switch (tipo) {
            // Entrada: suma.
            case TIPO_ENTRADA:
                // Existencia anterior más la cantidad.
                cantidadNueva = cantidadAnterior + cantidad;
                break;
            // Salida y devolución: restan.
            case TIPO_SALIDA:
            case TIPO_DEVOLUCION:
                // Existencia anterior menos la cantidad.
                cantidadNueva = cantidadAnterior - cantidad;
                break;
            // Ajuste: la cantidad es la nueva existencia contada.
            case TIPO_AJUSTE:
                // Reemplaza la existencia.
                cantidadNueva = cantidad;
                break;
            // Cualquier otro valor no pertenece al ENUM.
            default:
                // Provoca el ROLLBACK de la transacción.
                throw new SQLException("Tipo de movimiento no válido: " + tipo);
        }
        // El inventario de un almacén nunca puede quedar negativo (columna UNSIGNED).
        if (cantidadNueva < 0) {
            // Provoca el ROLLBACK con la existencia disponible y la solicitada.
            throw new SQLException("inventario insuficiente en el almacén (disponible: "
                    + cantidadAnterior + ", solicitado: " + cantidad + ")");
        }
        // Diferencia con signo que se aplicará a productos.cantidad y al historial.
        int diferencia = cantidadNueva - cantidadAnterior;
        // Un movimiento que no cambia nada no se registra.
        if (diferencia == 0) {
            // Provoca el ROLLBACK.
            throw new SQLException("el movimiento no cambia la existencia del almacén");
        }

        // Si ya hay registro de inventario, se actualiza.
        if (idInventario != null) {
            // UPDATE de la existencia y de la fecha de actualización.
            try (PreparedStatement ps = con.prepareStatement("UPDATE inventarioporalmacen SET "
                    + "cantidad_disponible = ?, fecha_de_actualizacion = ? WHERE id_inventario = ?")) {
                // Parámetro 1: nueva existencia.
                ps.setInt(1, cantidadNueva);
                // Parámetro 2: fecha del servidor.
                ps.setString(2, fecha);
                // Parámetro 3: id del registro.
                ps.setInt(3, idInventario);
                // Ejecuta dentro de la transacción.
                ps.executeUpdate();
            }
        // Si no había registro (primera entrada de ese producto en ese almacén), se crea.
        } else {
            // INSERT con cantidad reservada en 0.
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO inventarioporalmacen (id_producto, "
                    + "id_almacen, cantidad_disponible, cantidad_reservada, fecha_de_actualizacion) "
                    + "VALUES (?, ?, ?, 0, ?)")) {
                // Parámetro 1: id del producto.
                ps.setInt(1, idProducto);
                // Parámetro 2: id del almacén.
                ps.setInt(2, idAlmacen);
                // Parámetro 3: existencia inicial.
                ps.setInt(3, cantidadNueva);
                // Parámetro 4: fecha del servidor.
                ps.setString(4, fecha);
                // Ejecuta dentro de la transacción.
                ps.executeUpdate();
            }
        }

        // Ajusta la cantidad total del producto con la diferencia; CAST a SIGNED evita el error de resta
        // en columnas UNSIGNED y GREATEST impide que quede por debajo de 0.
        try (PreparedStatement ps = con.prepareStatement("UPDATE productos SET "
                + "cantidad = GREATEST(CAST(cantidad AS SIGNED) + ?, 0) WHERE id_producto = ?")) {
            // Parámetro 1: diferencia con signo.
            ps.setInt(1, diferencia);
            // Parámetro 2: id del producto.
            ps.setInt(2, idProducto);
            // Si no se actualizó ninguna fila, el producto fue eliminado.
            if (ps.executeUpdate() == 0) {
                // Provoca el ROLLBACK.
                throw new SQLException("el producto seleccionado ya no existe");
            }
        }

        // En ajustes se guarda la diferencia con signo; en los demás tipos, la cantidad movida.
        int cantidadRegistrada = TIPO_AJUSTE.equals(tipo) ? diferencia : cantidad;
        // INSERT del movimiento con la misma fecha del inventario y del historial.
        try (PreparedStatement ps = con.prepareStatement("INSERT INTO movimientoinventario (id_producto, "
                + "id_almacen, id_usuario, tipo_movimiento, cantidad, id_compra, motivo, fecha_movimiento) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
            // Parámetro 1: id del producto.
            ps.setInt(1, idProducto);
            // Parámetro 2: id del almacén.
            ps.setInt(2, idAlmacen);
            // Parámetro 3: id del usuario responsable.
            ps.setInt(3, idUsuario);
            // Parámetro 4: tipo de movimiento.
            ps.setString(4, tipo);
            // Parámetro 5: cantidad registrada.
            ps.setInt(5, cantidadRegistrada);
            // Los movimientos manuales no tienen compra relacionada.
            if (idCompra == null) {
                // Parámetro 6: NULL.
                ps.setNull(6, java.sql.Types.INTEGER);
            } else {
                // Parámetro 6: id de la compra que originó la entrada.
                ps.setInt(6, idCompra);
            }
            // Parámetro 7: motivo o NULL.
            ps.setString(7, motivo);
            // Parámetro 8: fecha del servidor.
            ps.setString(8, fecha);
            // Ejecuta dentro de la transacción.
            ps.executeUpdate();
        }

        // INSERT en historialinventario con la existencia anterior, la nueva y la diferencia.
        try (PreparedStatement ps = con.prepareStatement("INSERT INTO historialinventario (id_producto, "
                + "id_almacen, id_usuario, cantidad_anterior, cantidad_nueva, diferencia, fecha_cambio) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            // Parámetro 1: id del producto.
            ps.setInt(1, idProducto);
            // Parámetro 2: id del almacén.
            ps.setInt(2, idAlmacen);
            // Parámetro 3: id del usuario responsable.
            ps.setInt(3, idUsuario);
            // Parámetro 4: existencia anterior.
            ps.setInt(4, cantidadAnterior);
            // Parámetro 5: existencia nueva.
            ps.setInt(5, cantidadNueva);
            // Parámetro 6: diferencia con signo.
            ps.setInt(6, diferencia);
            // Parámetro 7: fecha del servidor.
            ps.setString(7, fecha);
            // Ejecuta dentro de la transacción.
            ps.executeUpdate();
        }
    }

    // Consulta movimientos con nombres de producto, almacén, usuario y pedido; los filtros null se omiten.
    public static ArrayList<MovimientoInventario> buscarMovimientos(Integer idProducto, Integer idAlmacen,
            String tipo) {
        // Lista resultado.
        ArrayList<MovimientoInventario> lista = new ArrayList<>();
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
        // SELECT con JOIN; LEFT JOIN a compras porque id_compra solo existe en entradas por compra.
        StringBuilder query = new StringBuilder("SELECT m.id_movimiento, m.id_producto, m.id_almacen, "
                + "m.id_usuario, m.tipo_movimiento, m.cantidad, m.id_compra, m.motivo, m.fecha_movimiento, "
                + "p.nombre AS nombre_producto, a.nombre AS nombre_almacen, "
                + "CONCAT(u.nombre, ' ', u.apellido) AS nombre_usuario, c.numero_pedido "
                + "FROM movimientoinventario m "
                + "INNER JOIN productos p ON m.id_producto = p.id_producto "
                + "INNER JOIN almacenes a ON m.id_almacen = a.id_almacen "
                + "INNER JOIN usuarios u ON m.id_usuario = u.id_usuario "
                + "LEFT JOIN compras c ON m.id_compra = c.id_compra WHERE 1 = 1");
        // Valores de los signos ? en orden.
        ArrayList<Object> parametros = new ArrayList<>();
        // Filtro por producto, solo si se eligió uno.
        if (idProducto != null) {
            // Agrega la condición por producto.
            query.append(" AND m.id_producto = ?");
            // Valor del id de producto.
            parametros.add(idProducto);
        }
        // Filtro por almacén, solo si se eligió uno.
        if (idAlmacen != null) {
            // Agrega la condición por almacén.
            query.append(" AND m.id_almacen = ?");
            // Valor del id de almacén.
            parametros.add(idAlmacen);
        }
        // Filtro por tipo, solo si no es 'Todos'.
        if (tipo != null && !tipo.trim().isEmpty()) {
            // Agrega la condición por tipo.
            query.append(" AND m.tipo_movimiento = ?");
            // Valor del tipo.
            parametros.add(tipo);
        }
        // Ordena del movimiento más reciente al más antiguo.
        query.append(" ORDER BY m.fecha_movimiento DESC, m.id_movimiento DESC");

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
                    // Crea el movimiento con las columnas de la tabla.
                    MovimientoInventario movimiento = new MovimientoInventario(
                            rs.getInt("id_movimiento"),
                            rs.getInt("id_producto"),
                            rs.getInt("id_almacen"),
                            rs.getInt("id_usuario"),
                            rs.getString("tipo_movimiento"),
                            rs.getInt("cantidad"),
                            // getObject(..., Integer.class) devuelve null cuando id_compra es NULL.
                            rs.getObject("id_compra", Integer.class),
                            rs.getString("motivo"),
                            // Fecha leída tal como está en MySQL.
                            leerFechaHora(rs, "fecha_movimiento"));
                    // Agrega el nombre del producto.
                    movimiento.setNombreProducto(rs.getString("nombre_producto"));
                    // Agrega el nombre del almacén.
                    movimiento.setNombreAlmacen(rs.getString("nombre_almacen"));
                    // Agrega el nombre del usuario.
                    movimiento.setNombreUsuario(rs.getString("nombre_usuario"));
                    // Agrega el número de pedido o null.
                    movimiento.setNumeroPedido(rs.getObject("numero_pedido", Integer.class));
                    // Agrega el movimiento a la lista.
                    lista.add(movimiento);
                }
            }
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(MovimientoInventario.class.getName()).log(Level.SEVERE, null, ex);
            // Informa el error de consulta.
            JOptionPane.showMessageDialog(null, "Error al consultar movimientos: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
        // Devuelve la lista (vacía si no hubo conexión o datos).
        return lista;
    }

    // true si la compra ya tiene movimientos de entrada; evita procesar dos veces una entrega.
    static boolean compraTieneEntradas(Connection con, int idCompra) throws SQLException {
        // Cuenta las entradas registradas con ese id_compra.
        try (PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM movimientoinventario "
                + "WHERE id_compra = ? AND tipo_movimiento = ?")) {
            // Parámetro 1: id de la compra.
            ps.setInt(1, idCompra);
            // Parámetro 2: tipo 'entrada'.
            ps.setString(2, TIPO_ENTRADA);
            // Ejecuta el conteo.
            try (ResultSet rs = ps.executeQuery()) {
                // true si el conteo es mayor que cero.
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    // Lee un DATETIME tal como está guardado (sin conversión de zona horaria); null si es NULL.
    static Timestamp leerFechaHora(ResultSet rs, String columna) throws SQLException {
        // Connector/J entrega el valor exacto de la columna como LocalDateTime.
        LocalDateTime valor = rs.getObject(columna, LocalDateTime.class);
        // Convierte a Timestamp para el resto del proyecto.
        return valor == null ? null : Timestamp.valueOf(valor);
    }

    // Convierte una fecha a texto 'AAAA-MM-DD HH:MM:SS' para guardarla sin conversión de zona horaria.
    static String textoFechaHora(Timestamp fecha) {
        // Devuelve null si no hay fecha; si la hay, la formatea.
        return fecha == null ? null
                : fecha.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // ROLLBACK de la transacción; lo usan los catch de este modelo, Compra y Usuario.
    static void deshacer(Connection con) {
        // rollback() también puede fallar si se perdió la conexión.
        try {
            // Descarta los cambios pendientes.
            con.rollback();
        // Error al deshacer.
        } catch (SQLException ex) {
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(MovimientoInventario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Vuelve la conexión compartida a autocommit después de una transacción.
    static void restaurarAutoCommit(Connection con) {
        // setAutoCommit puede fallar si la conexión se cerró.
        try {
            // Las siguientes operaciones de otros módulos se confirman automáticamente.
            con.setAutoCommit(true);
        // Error al restaurar el modo.
        } catch (SQLException ex) {
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(MovimientoInventario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
