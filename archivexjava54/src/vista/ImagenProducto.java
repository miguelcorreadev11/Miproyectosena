// Paquete vista: ventanas Swing (JFrame con .form de NetBeans) y utilidades visuales.
package vista;

// Component: ventana padre del selector de archivos.
import java.awt.Component;
// Image: imagen cargada y guardada en caché.
import java.awt.Image;
// BufferedImage: resultado de ImageIO.read para validar el archivo.
import java.awt.image.BufferedImage;
// File: rutas de carpetas y archivos de imagen.
import java.io.File;
// IOException: errores de lectura o copia que se informan a Productos_Inventario_Form.
import java.io.IOException;
// URL: ubicación de una imagen dentro del classpath.
import java.net.URL;
// Files.copy copia la imagen elegida a la carpeta del proyecto.
import java.nio.file.Files;
// REPLACE_EXISTING: sobrescribe si ya existe un archivo con ese nombre.
import java.nio.file.StandardCopyOption;
// HashMap de la caché de imágenes.
import java.util.HashMap;
// Map: tipo de la caché.
import java.util.Map;
// ImageIO lee png, jpg y gif desde disco o classpath.
import javax.imageio.ImageIO;
// ImageIcon: imagen escalada que se muestra en los JLabel.
import javax.swing.ImageIcon;
// JFileChooser: diálogo para elegir la imagen del producto.
import javax.swing.JFileChooser;
// Filtro de extensiones del diálogo.
import javax.swing.filechooser.FileNameExtensionFilter;

// Utilidades de imágenes de productos: guarda solo el nombre del archivo en productos.imagen
// y lo busca en src/imagenes/productos, src/imagenes o dentro del .jar.
public final class ImagenProducto {
    // Constructor privado: la clase no se instancia.
    private ImagenProducto() {
    }

    // Carpetas del classpath donde se buscan las imágenes (primero productos, luego la general).
    private static final String[] CARPETAS_RECURSOS = {"/imagenes/productos/", "/imagenes/"};
    // Imagen de respaldo cuando el producto no tiene imagen o no se encuentra.
    private static final String IMAGEN_PREDETERMINADA = "sin_imagen.png";

    // Caché por nombre de archivo para no leer el disco cada vez que se refresca la tabla.
    private static final Map<String, Image> CACHE = new HashMap<String, Image>();

    // Carpeta donde se copian las imágenes nuevas: src/imagenes/productos si se ejecuta desde NetBeans.
    public static File carpetaImagenes() {
        // Directorio desde el que se ejecuta la aplicación (raíz del proyecto en NetBeans).
        File directorioTrabajo = new File(System.getProperty("user.dir"));
        // Carpeta src del proyecto.
        File carpetaSrc = new File(directorioTrabajo, "src");
        // Si existe src, se está ejecutando desde el proyecto.
        if (carpetaSrc.isDirectory()) {
            // Usa src/imagenes/productos.
            return new File(carpetaSrc, "imagenes" + File.separator + "productos");
        }
        // Ejecutando el .jar: usa imagenes/productos junto al directorio de trabajo.
        return new File(directorioTrabajo, "imagenes" + File.separator + "productos");
    }

    // Abre el diálogo para elegir una imagen; devuelve null si el usuario cancela.
    public static File seleccionarArchivo(Component padre) {
        // Diálogo de apertura de archivos.
        JFileChooser chooser = new JFileChooser();
        // Título del diálogo.
        chooser.setDialogTitle("Seleccionar imagen del producto");
        // Oculta la opción 'Todos los archivos'.
        chooser.setAcceptAllFileFilterUsed(false);
        // Solo muestra imágenes png, jpg, jpeg y gif.
        chooser.setFileFilter(new FileNameExtensionFilter("Imágenes (png, jpg, jpeg, gif)",
                "png", "jpg", "jpeg", "gif"));
        // Si el usuario pulsó Abrir.
        if (chooser.showOpenDialog(padre) == JFileChooser.APPROVE_OPTION) {
            // Devuelve el archivo elegido.
            return chooser.getSelectedFile();
        }
        // Cancelado: no se cambia la imagen.
        return null;
    }

    // Valida que el archivo sea una imagen y la copia a carpetaImagenes() con nombre único;
    // devuelve el nombre que Producto guarda en la columna imagen.
    public static String copiarImagen(File origen) throws IOException {
        // Imagen leída solo para comprobar que el archivo es válido.
        BufferedImage prueba;
        // ImageIO puede lanzar RuntimeException con archivos dañados.
        try {
            // Intenta decodificar el archivo.
            prueba = ImageIO.read(origen);
        // Archivo dañado.
        } catch (RuntimeException ex) {
            // Se informa como IOException para que la vista muestre un único mensaje.
            throw new IOException("el archivo seleccionado no es una imagen válida");
        }
        // ImageIO devuelve null cuando el formato no es de imagen.
        if (prueba == null) {
            // Rechaza el archivo.
            throw new IOException("el archivo seleccionado no es una imagen válida");
        }

        // Si la imagen ya está en una carpeta del proyecto, no se copia de nuevo.
        if (estaEnCarpetaDelProyecto(origen)) {
            // Se guarda su nombre tal cual.
            return origen.getName();
        }
        // Nombre original para obtener la extensión.
        String nombreOriginal = origen.getName();
        // Posición del último punto.
        int punto = nombreOriginal.lastIndexOf('.');
        // Extensión en minúsculas; .png si el archivo no tiene extensión.
        String extension = punto >= 0 ? nombreOriginal.substring(punto).toLowerCase() : ".png";
        // Nombre único con la hora en milisegundos para no pisar otras imágenes.
        String nombreDestino = "producto_" + System.currentTimeMillis() + extension;

        // Carpeta destino.
        File carpeta = carpetaImagenes();
        // Crea la carpeta si no existe.
        if (!carpeta.isDirectory() && !carpeta.mkdirs()) {
            // No se pudo crear la carpeta (permisos).
            throw new IOException("no se pudo crear la carpeta " + carpeta.getAbsolutePath());
        }
        // Copia el archivo a la carpeta del proyecto.
        Files.copy(origen.toPath(), new File(carpeta, nombreDestino).toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        // Nombre que se guardará en productos.imagen.
        return nombreDestino;
    }

    // Ícono escalado de la imagen del producto para tablas y vista previa; si no se encuentra,
    // usa sin_imagen.png y, si tampoco existe, devuelve null (la etiqueta queda sin imagen).
    public static ImageIcon obtenerIcono(String nombreArchivo, int ancho, int alto) {
        // Protege la vista ante imágenes dañadas.
        try {
            // Carga y escala la imagen del producto.
            ImageIcon icono = escalar(cargarImagen(nombreArchivo), ancho, alto);
            // Si se pudo cargar.
            if (icono != null) {
                // Devuelve la imagen del producto.
                return icono;
            }
            // Imagen de respaldo con el mismo tamaño.
            return escalar(cargarImagen(IMAGEN_PREDETERMINADA), ancho, alto);
        // Cualquier error inesperado de lectura.
        } catch (RuntimeException ex) {
            // Sin ícono.
            return null;
        }
    }

    // Escala la imagen conservando su proporción dentro del ancho y alto indicados.
    private static ImageIcon escalar(Image imagen, int ancho, int alto) {
        // Sin imagen o tamaño inválido.
        if (imagen == null || ancho <= 0 || alto <= 0) {
            // No hay ícono.
            return null;
        }
        // Ancho real de la imagen.
        int anchoOriginal = imagen.getWidth(null);
        // Alto real de la imagen.
        int altoOriginal = imagen.getHeight(null);
        // La imagen aún no tiene dimensiones válidas.
        if (anchoOriginal <= 0 || altoOriginal <= 0) {
            // No se puede escalar.
            return null;
        }
        // Factor menor entre ancho y alto para que la imagen quepa completa.
        double escala = Math.min((double) ancho / anchoOriginal, (double) alto / altoOriginal);
        // Ancho final, mínimo 1 px.
        int anchoFinal = Math.max(1, (int) Math.round(anchoOriginal * escala));
        // Alto final, mínimo 1 px.
        int altoFinal = Math.max(1, (int) Math.round(altoOriginal * escala));
        // Escala con suavizado y la envuelve en un ImageIcon.
        return new ImageIcon(imagen.getScaledInstance(anchoFinal, altoFinal, Image.SCALE_SMOOTH));
    }

    // Busca la imagen por nombre en disco y luego en el classpath; usa la caché.
    private static Image cargarImagen(String nombreArchivo) {
        // Nombre vacío o null: el producto no tiene imagen.
        if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
            // No hay imagen.
            return null;
        }

        // Normaliza el valor de la BD: acepta rutas con '\' o '/' y se queda solo con el nombre del
        // archivo, así los registros antiguos con ruta completa también funcionan.
        String nombre = new File(nombreArchivo.trim().replace('\\', '/')).getName();
        // La ruta terminaba en separador.
        if (nombre.isEmpty()) {
            // No hay nombre de archivo.
            return null;
        }
        // Si ya se cargó antes.
        if (CACHE.containsKey(nombre)) {
            // Devuelve la imagen en caché.
            return CACHE.get(nombre);
        }
        // Imagen encontrada.
        Image imagen = null;
        // Primero busca en las carpetas del proyecto (incluye imágenes copiadas durante la ejecución).
        for (File carpeta : carpetasEnDisco()) {
            // Lee el archivo si existe en esa carpeta.
            imagen = leerArchivo(buscarEnCarpeta(carpeta, nombre));
            // Encontrada.
            if (imagen != null) {
                // Deja de buscar.
                break;
            }
        }
        // Si no está en disco, busca dentro del classpath (build/classes o el .jar).
        if (imagen == null) {
            // Recorre las carpetas de recursos.
            for (String carpetaRecurso : CARPETAS_RECURSOS) {
                // Lee el recurso si existe.
                imagen = leerRecurso(ImagenProducto.class.getResource(carpetaRecurso + nombre));
                // Encontrada.
                if (imagen != null) {
                    // Deja de buscar.
                    break;
                }
            }
        }
        // Solo se guardan en caché las imágenes encontradas.
        if (imagen != null) {
            // Guarda la imagen.
            CACHE.put(nombre, imagen);
        }
        // Imagen o null.
        return imagen;
    }

    // Carpetas de disco donde se buscan imágenes, en orden de prioridad.
    private static File[] carpetasEnDisco() {
        // Directorio de ejecución.
        File directorioTrabajo = new File(System.getProperty("user.dir"));
        // Arreglo de carpetas.
        return new File[]{
            // src/imagenes/productos (imágenes de productos del proyecto).
            new File(directorioTrabajo, "src" + File.separator + "imagenes" + File.separator + "productos"),
            // src/imagenes (imágenes generales como sin_imagen.png).
            new File(directorioTrabajo, "src" + File.separator + "imagenes"),
            // imagenes/productos al ejecutar el .jar.
            new File(directorioTrabajo, "imagenes" + File.separator + "productos"),
            // imagenes al ejecutar el .jar.
            new File(directorioTrabajo, "imagenes")
        };
    }

    // Busca el archivo en la carpeta; si no coincide exacto, ignora mayúsculas (Televisor.png / televisor.png).
    private static File buscarEnCarpeta(File carpeta, String nombre) {
        // Ruta con el nombre exacto.
        File exacto = new File(carpeta, nombre);
        // Existe con el nombre exacto.
        if (exacto.isFile()) {
            // Lo devuelve.
            return exacto;
        }
        // Archivos de la carpeta; null si la carpeta no existe.
        File[] archivos = carpeta.listFiles();
        // Solo si la carpeta existe.
        if (archivos != null) {
            // Recorre los archivos.
            for (File archivo : archivos) {
                // Compara el nombre sin distinguir mayúsculas.
                if (archivo.isFile() && archivo.getName().equalsIgnoreCase(nombre)) {
                    // Archivo encontrado.
                    return archivo;
                }
            }
        }
        // No está en esa carpeta.
        return null;
    }

    // true si el archivo ya está directamente dentro de alguna carpeta de carpetasEnDisco().
    private static boolean estaEnCarpetaDelProyecto(File archivo) {
        // getCanonicalFile puede fallar con rutas inválidas.
        try {
            // Carpeta real que contiene el archivo.
            File padre = archivo.getCanonicalFile().getParentFile();
            // Compara con cada carpeta del proyecto.
            for (File carpeta : carpetasEnDisco()) {
                // Misma carpeta canónica.
                if (padre != null && padre.equals(carpeta.getCanonicalFile())) {
                    // El archivo ya pertenece al proyecto.
                    return true;
                }
            }
        // Ruta inválida.
        } catch (IOException ex) {
            // Se trata como archivo externo.
            return false;
        }
        // No está en el proyecto.
        return false;
    }

    // Lee una imagen de disco; null si no existe o no se puede decodificar.
    private static Image leerArchivo(File archivo) {
        // Archivo no encontrado.
        if (archivo == null) {
            // Sin imagen.
            return null;
        }
        // Intenta leer el archivo.
        try {
            // Decodifica la imagen.
            return ImageIO.read(archivo);
        // Archivo dañado o ilegible.
        } catch (IOException | RuntimeException ex) {
            // Sin imagen.
            return null;
        }
    }

    // Lee una imagen del classpath; null si no existe o no se puede decodificar.
    private static Image leerRecurso(URL recurso) {
        // Recurso no encontrado.
        if (recurso == null) {
            // Sin imagen.
            return null;
        }
        // Intenta leer el recurso.
        try {
            // Decodifica la imagen.
            return ImageIO.read(recurso);
        // Recurso dañado o ilegible.
        } catch (IOException | RuntimeException ex) {
            // Sin imagen.
            return null;
        }
    }
}
