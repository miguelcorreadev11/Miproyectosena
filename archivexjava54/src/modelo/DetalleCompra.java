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
// ArrayList: listas de registros devueltas a los controladores.
import java.util.ArrayList;
// Level.SEVERE marca la gravedad de los errores registrados.
import java.util.logging.Level;
// Logger: escribe las excepciones en la consola de NetBeans.
import java.util.logging.Logger;
// JOptionPane: cuadros de diálogo de error, aviso o confirmación.
import javax.swing.JOptionPane;

// Modelo de la tabla detallecompra: cada línea de producto de una compra. La columna subtotal
// es GENERATED en MySQL (cantidad * precio_unitario), por eso nunca se inserta.
public class DetalleCompra {
    // Columna id_detalle (llave primaria AUTO_INCREMENT).
    private Integer idDetalle;
    // Columna id_compra (llave foránea fk_detallecompra_compra, ON DELETE CASCADE).
    private Integer idCompra;
    // Columna id_producto (llave foránea fk_detallecompra_producto).
    private Integer idProducto;
    // Columna cantidad UNSIGNED.
    private Integer cantidad;
    // Columna precio_unitario DECIMAL(10,2).
    private BigDecimal precioUnitario;
    // Columna subtotal: calculada por MySQL al leer, o en Java para líneas aún no guardadas.
    private BigDecimal subtotal;

    // Campo auxiliar: nombre del producto para mostrar en Compras_Form.
    private String nombreProducto;

    // Constructor vacío.
    public DetalleCompra() {
    }

    // Constructor completo, usado al leer una línea guardada en MySQL.
    public DetalleCompra(Integer idDetalle, Integer idCompra, Integer idProducto, Integer cantidad,
            BigDecimal precioUnitario, BigDecimal subtotal) {
        // Asigna el id de la línea (columna id_detalle).
        this.idDetalle = idDetalle;
        // Asigna el id de la compra a la que pertenece la línea.
        this.idCompra = idCompra;
        // Asigna el id del producto comprado.
        this.idProducto = idProducto;
        // Asigna la cantidad comprada.
        this.cantidad = cantidad;
        // Asigna el precio unitario de compra.
        this.precioUnitario = precioUnitario;
        // Asigna el subtotal de la línea (cantidad x precio).
        this.subtotal = subtotal;
    }

    // Constructor para una línea nueva que arma Compras_Form antes de guardar la compra;
    // calcula el subtotal en memoria.
    public DetalleCompra(Integer idProducto, String nombreProducto, Integer cantidad, BigDecimal precioUnitario) {
        // Asigna el id del producto comprado.
        this.idProducto = idProducto;
        // Asigna el nombre del producto (dato auxiliar para las JTable).
        this.nombreProducto = nombreProducto;
        // Asigna la cantidad comprada.
        this.cantidad = cantidad;
        // Asigna el precio unitario de compra.
        this.precioUnitario = precioUnitario;
        // Calcula el subtotal para mostrarlo y sumarlo al total antes de guardar.
        this.subtotal = calcularSubtotal(cantidad, precioUnitario);
    }

    // Devuelve el id de la línea (columna id_detalle).
    public Integer getIdDetalle() {
        return idDetalle;
    }

    // Cambia el id de la línea (columna id_detalle).
    public void setIdDetalle(Integer idDetalle) {
        this.idDetalle = idDetalle;
    }

    // Devuelve el id de la compra a la que pertenece la línea.
    public Integer getIdCompra() {
        return idCompra;
    }

    // Cambia el id de la compra a la que pertenece la línea.
    public void setIdCompra(Integer idCompra) {
        this.idCompra = idCompra;
    }

    // Devuelve el id del producto comprado.
    public Integer getIdProducto() {
        return idProducto;
    }

    // Cambia el id del producto comprado.
    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    // Devuelve la cantidad comprada.
    public Integer getCantidad() {
        return cantidad;
    }

    // Cambia la cantidad comprada.
    public void setCantidad(Integer cantidad) {
        // Asigna la nueva cantidad.
        this.cantidad = cantidad;
        // Recalcula el subtotal; Compras_Form suma cantidades cuando se repite un producto.
        this.subtotal = calcularSubtotal(cantidad, precioUnitario);
    }

    // Devuelve el precio unitario de compra.
    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    // Cambia el precio unitario de compra.
    public void setPrecioUnitario(BigDecimal precioUnitario) {
        // Asigna el nuevo precio.
        this.precioUnitario = precioUnitario;
        // Recalcula el subtotal con el precio nuevo.
        this.subtotal = calcularSubtotal(cantidad, precioUnitario);
    }

    // Devuelve el subtotal de la línea.
    public BigDecimal getSubtotal() {
        return subtotal;
    }

    // Devuelve el nombre del producto (dato auxiliar para las JTable).
    public String getNombreProducto() {
        return nombreProducto;
    }

    // Cambia el nombre del producto (dato auxiliar para las JTable).
    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    // Calcula cantidad x precio con 2 decimales; lo usan esta clase y Compra_control.
    public static BigDecimal calcularSubtotal(Integer cantidad, BigDecimal precioUnitario) {
        // Si falta la cantidad o el precio, el subtotal es 0.
        if (cantidad == null || precioUnitario == null) {
            // Cero con 2 decimales.
            return BigDecimal.ZERO.setScale(2);
        }
        // Multiplica y redondea a 2 decimales (HALF_UP), igual que DECIMAL(12,2) en MySQL.
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad)).setScale(2, java.math.RoundingMode.HALF_UP);
    }

    // Líneas de una compra con nombre de producto; lista vacía si hay error (uso desde Compras_Form).
    public static ArrayList<DetalleCompra> getDetallesPorCompra(int idCompra) {
        // Obtiene la conexión compartida con MySQL desde config.conexión_BD.
        Connection con = config.conexión_BD.getConnection();
        // getConnection() devuelve null cuando MySQL no está disponible.
        if (con == null) {
            // Informa al usuario que no hay conexión con la base de datos.
            JOptionPane.showMessageDialog(null, config.conexión_BD.MENSAJE_SIN_CONEXION,
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            // Sin conexión devuelve una lista vacía.
            return new ArrayList<>();
        }
        // Reutiliza la consulta interna con la conexión obtenida.
        try {
            // Ejecuta el SELECT del detalle.
            return consultarDetalles(con, idCompra);
        // Error de consulta.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(DetalleCompra.class.getName()).log(Level.SEVERE, null, ex);
            // Informa el error de consulta.
            JOptionPane.showMessageDialog(null, "Error al consultar el detalle de la compra: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            // Devuelve una lista vacía para no romper la vista.
            return new ArrayList<>();
        }
    }

    // Consulta el detalle con la conexión recibida y propaga SQLException; Compra.marcarEntregada
    // la usa dentro de su transacción para hacer ROLLBACK si falla.
    static ArrayList<DetalleCompra> consultarDetalles(Connection con, int idCompra) throws SQLException {
        // Lista resultado.
        ArrayList<DetalleCompra> lista = new ArrayList<>();
        // Une detallecompra con productos para traer el nombre del producto; ordena por id_detalle.
        String query = "SELECT d.id_detalle, d.id_compra, d.id_producto, d.cantidad, d.precio_unitario, "
                + "d.subtotal, p.nombre AS nombre_producto "
                + "FROM detallecompra d INNER JOIN productos p ON d.id_producto = p.id_producto "
                + "WHERE d.id_compra = ? ORDER BY d.id_detalle";
        // Prepara la sentencia; try-with-resources la cierra.
        try (PreparedStatement ps = con.prepareStatement(query)) {
            // Parámetro 1: id de la compra.
            ps.setInt(1, idCompra);
            // Ejecuta la consulta; el ResultSet se cierra automáticamente al salir del bloque.
            try (ResultSet rs = ps.executeQuery()) {
                // Recorre cada fila devuelta por el SELECT.
                while (rs.next()) {
                    // Crea la línea con las columnas guardadas, incluido el subtotal generado por MySQL.
                    DetalleCompra detalle = new DetalleCompra(
                            rs.getInt("id_detalle"),
                            rs.getInt("id_compra"),
                            rs.getInt("id_producto"),
                            rs.getInt("cantidad"),
                            rs.getBigDecimal("precio_unitario"),
                            rs.getBigDecimal("subtotal"));
                    // Agrega el nombre del producto del JOIN.
                    detalle.setNombreProducto(rs.getString("nombre_producto"));
                    // Agrega la línea a la lista.
                    lista.add(detalle);
                }
            }
        }
        // Devuelve las líneas de la compra.
        return lista;
    }

    // INSERT de una línea dentro de la transacción de Compra.insertCompraConDetalle;
    // no incluye subtotal porque es una columna generada.
    static void insertDetalle(Connection con, int idCompra, DetalleCompra detalle) throws SQLException {
        // Sentencia parametrizada: compra, producto, cantidad y precio.
        String query = "INSERT INTO detallecompra (id_compra, id_producto, cantidad, precio_unitario) "
                + "VALUES (?, ?, ?, ?)";
        // Prepara la sentencia; try-with-resources la cierra.
        try (PreparedStatement ps = con.prepareStatement(query)) {
            // Parámetro 1: id de la compra recién creada.
            ps.setInt(1, idCompra);
            // Parámetro 2: id del producto.
            ps.setInt(2, detalle.getIdProducto());
            // Parámetro 3: cantidad.
            ps.setInt(3, detalle.getCantidad());
            // Parámetro 4: precio unitario.
            ps.setBigDecimal(4, detalle.getPrecioUnitario());
            // Ejecuta el INSERT sin confirmar; el commit lo hace Compra.
            ps.executeUpdate();
        }
    }
}
