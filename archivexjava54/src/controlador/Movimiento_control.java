// Paquete de controladores del patrón MVC.
package controlador;

// ArrayList devuelve los movimientos a la vista.
import java.util.ArrayList;
// JOptionPane muestra validaciones y resultados.
import javax.swing.JOptionPane;
// Almacen identifica dónde ocurre el movimiento.
import modelo.Almacen;
// InventarioPorAlmacen aporta la existencia actual del producto en el almacén.
import modelo.InventarioPorAlmacen;
// Modelo MovimientoInventario: registra el movimiento en una transacción y consulta el historial.
import modelo.MovimientoInventario;
// Producto identifica qué artículo se mueve.
import modelo.Producto;
// Usuario de la sesión, responsable del movimiento.
import modelo.Usuario;

// Controlador de Movimientos_Form: valida entradas, salidas, ajustes y devoluciones.
public class Movimiento_control {
    // Texto de la opción 'sin filtro' en los combos de consulta.
    public static final String FILTRO_TODOS = "Todos";

    // Tipos de movimiento para el combo del formulario de registro.
    public static String[] getTiposMovimiento() {
        // Toma los valores del ENUM tipo_movimiento definidos en el modelo.
        return MovimientoInventario.getTiposMovimiento();
    }

    // Tipos para el combo de filtro, con 'Todos' como primera opción.
    public static String[] getFiltrosTipo() {
        // Tipos del ENUM.
        String[] tipos = MovimientoInventario.getTiposMovimiento();
        // Arreglo con una posición extra para 'Todos'.
        String[] filtros = new String[tipos.length + 1];
        // La primera opción desactiva el filtro por tipo.
        filtros[0] = FILTRO_TODOS;
        // Copia los tipos a continuación de 'Todos'.
        System.arraycopy(tipos, 0, filtros, 1, tipos.length);
        // Devuelve las opciones del filtro.
        return filtros;
    }

    // Existencia disponible del producto en el almacén; 0 si no hay registro.
    public static int existenciaActual(Producto producto, Almacen almacen) {
        // Sin producto o almacén seleccionado no hay existencia que consultar.
        if (producto == null || almacen == null) {
            // Devuelve 0.
            return 0;
        }
        // Busca el registro de inventarioporalmacen del par producto-almacén.
        InventarioPorAlmacen inventario = InventarioPorAlmacen.getInventarioPorProductoYAlmacen(
                producto.getIdProducto(), almacen.getIdAlmacen());
        // Sin registro la existencia es 0; si existe, devuelve cantidad_disponible.
        return inventario == null ? 0 : inventario.getCantidadDisponible();
    }

    // Valida el formulario y registra el movimiento; en 'ajuste' la cantidad es la nueva
    // existencia contada. La fecha y hora las asigna el modelo con la hora del servidor MySQL.
    public static boolean registrarMovimiento(Producto producto, Almacen almacen, String tipo,
            String cantidadTexto, String motivo) {
        // Usuario autenticado que quedará como responsable (id_usuario).
        Usuario usuario = Login_control.getUsuarioSesion();
        // movimientoinventario.id_usuario es obligatorio: se exige sesión iniciada.
        if (usuario == null) {
            // Avisa que se debe iniciar sesión.
            JOptionPane.showMessageDialog(null, "Debe iniciar sesión para registrar movimientos",
                    "Sesión requerida", JOptionPane.WARNING_MESSAGE);
            // Detiene el registro.
            return false;
        }
        // Debe haber un producto seleccionado.
        if (producto == null) {
            // Avisa que falta el producto.
            JOptionPane.showMessageDialog(null, "Seleccione un producto", "Campo obligatorio",
                    JOptionPane.WARNING_MESSAGE);
            // Detiene el registro.
            return false;
        }
        // Debe haber un almacén seleccionado.
        if (almacen == null) {
            // Avisa que falta el almacén.
            JOptionPane.showMessageDialog(null, "Seleccione un almacén", "Campo obligatorio",
                    JOptionPane.WARNING_MESSAGE);
            // Detiene el registro.
            return false;
        }
        // Debe haber un tipo de movimiento seleccionado.
        if (tipo == null || tipo.trim().isEmpty()) {
            // Avisa que falta el tipo.
            JOptionPane.showMessageDialog(null, "Seleccione el tipo de movimiento", "Campo obligatorio",
                    JOptionPane.WARNING_MESSAGE);
            // Detiene el registro.
            return false;
        }

        // Cantidad convertida a entero; null si el texto no es numérico.
        Integer cantidad;
        try {
            // Convierte el texto del campo cantidad (sin espacios) a entero.
            cantidad = Integer.parseInt(cantidadTexto.trim());
        // Texto vacío, no numérico o null.
        } catch (NumberFormatException | NullPointerException ex) {
            // Marca la cantidad como inválida.
            cantidad = null;
        }
        // El ajuste admite 0 porque la cantidad representa la nueva existencia.
        boolean esAjuste = MovimientoInventario.TIPO_AJUSTE.equals(tipo);
        // Rechaza cantidades inválidas o negativas, y 0 cuando no es un ajuste.
        if (cantidad == null || cantidad < 0 || (!esAjuste && cantidad == 0)) {
            // Muestra el mensaje adecuado según el tipo de movimiento.
            JOptionPane.showMessageDialog(null, esAjuste
                    ? "En un ajuste indique la nueva existencia contada (entero de 0 en adelante)"
                    : "La cantidad debe ser un número entero mayor que cero",
                    "Dato inválido", JOptionPane.WARNING_MESSAGE);
            // Detiene el registro.
            return false;
        }

        // Motivo sin espacios sobrantes; vacío si llega null.
        String motivoLimpio = motivo == null ? "" : motivo.trim();
        // El motivo es opcional solo en las entradas.
        if (!MovimientoInventario.TIPO_ENTRADA.equals(tipo) && motivoLimpio.isEmpty()) {
            // Avisa que falta el motivo.
            JOptionPane.showMessageDialog(null, "Indique el motivo del movimiento", "Campo obligatorio",
                    JOptionPane.WARNING_MESSAGE);
            // Detiene el registro.
            return false;
        }
        // movimientoinventario.motivo es VARCHAR(230).
        if (motivoLimpio.length() > 230) {
            // Avisa que el motivo es demasiado largo.
            JOptionPane.showMessageDialog(null, "El motivo admite máximo 230 caracteres", "Dato inválido",
                    JOptionPane.WARNING_MESSAGE);
            // Detiene el registro.
            return false;
        }

        // Existencia actual en el almacén, consultada antes de validar la salida.
        int existencia = existenciaActual(producto, almacen);
        // Si la existencia no se pudo consultar (error de MySQL ya informado), el 0 no es real:
        // no se muestra "Inventario insuficiente" ni se registra el movimiento.
        if (config.conexión_BD.huboErrorEnUltimaOperacion()) {
            return false;
        }
        // Las salidas y devoluciones restan unidades del almacén.
        boolean restaExistencia = MovimientoInventario.TIPO_SALIDA.equals(tipo)
                || MovimientoInventario.TIPO_DEVOLUCION.equals(tipo);
        // No se permite dejar el inventario en negativo.
        if (restaExistencia && cantidad > existencia) {
            // Informa la existencia disponible y la cantidad solicitada.
            JOptionPane.showMessageDialog(null, "Inventario insuficiente en el almacén " + almacen.getNombre()
                    + ".\nDisponible: " + existencia + ", solicitado: " + cantidad,
                    "Inventario insuficiente", JOptionPane.WARNING_MESSAGE);
            // Detiene el registro.
            return false;
        }
        // Un ajuste igual a la existencia actual no cambia nada.
        if (esAjuste && cantidad == existencia) {
            // Informa que no hay diferencia que ajustar.
            JOptionPane.showMessageDialog(null, "La existencia contada es igual a la registrada (" + existencia
                    + "); no hay nada que ajustar", "Sin cambios", JOptionPane.INFORMATION_MESSAGE);
            // Detiene el registro.
            return false;
        }

        // Crea el movimiento sin compra relacionada (null); un motivo vacío se guarda como NULL.
        MovimientoInventario movimiento = new MovimientoInventario(producto.getIdProducto(),
                almacen.getIdAlmacen(), usuario.getIdUsuario(), tipo, cantidad, null,
                motivoLimpio.isEmpty() ? null : motivoLimpio);
        // Transacción: actualiza inventario y producto, e inserta movimiento e historial.
        boolean registrado = MovimientoInventario.registrarMovimiento(movimiento);
        // Si se registró, confirma al usuario.
        if (registrado) {
            // Mensaje de éxito.
            JOptionPane.showMessageDialog(null, "Movimiento registrado correctamente", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        // Movimientos_Form refresca la existencia y la tabla si es true.
        return registrado;
    }

    // Consulta movimientos filtrando por producto, almacén y tipo ('Todos' o null no filtran).
    public static ArrayList<MovimientoInventario> buscarMovimientos(Producto producto, Almacen almacen,
            String tipo) {
        // Convierte los objetos seleccionados en ids y 'Todos' en null antes de consultar el modelo.
        return MovimientoInventario.buscarMovimientos(
                producto == null ? null : producto.getIdProducto(),
                almacen == null ? null : almacen.getIdAlmacen(),
                FILTRO_TODOS.equals(tipo) ? null : tipo);
    }
}
